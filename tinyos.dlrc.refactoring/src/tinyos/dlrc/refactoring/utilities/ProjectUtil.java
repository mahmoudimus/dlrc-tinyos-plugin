package tinyos.dlrc.refactoring.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import tinyos.dlrc.TinyOSPlugin;
import tinyos.dlrc.builder.ProjectResourceCollector;
import tinyos.dlrc.editors.NesCEditor;
import tinyos.dlrc.ep.IParseFile;
import tinyos.dlrc.ep.parser.IDeclaration;
import tinyos.dlrc.ep.parser.IDeclaration.Kind;
import tinyos.dlrc.model.ProjectModel;
import tinyos.dlrc.nature.MissingNatureException;
import tinyos.dlrc.nesc.FileMultiReader;
import tinyos.dlrc.nesc12.Parser;
import tinyos.dlrc.nesc12.ep.NesC12AST;
import tinyos.dlrc.refactoring.RefactoringPlugin;
import tinyos.dlrc.refactoring.RefactoringPlugin.LogLevel;

public class ProjectUtil {

	private NesCEditor editor;
	private IProject project;
	
	private static ParserCache parserChache = new ParserCache();
	
	public ProjectUtil(NesCEditor editor){
		this.editor=editor;
		this.project=editor.getProject();
	}
	
	/**
	 * Returns the Ast for the given IFile.
	 * @param iFile
	 * @param monitor
	 * @return
	 * @throws IOException
	 * @throws MissingNatureException
	 */
	public NesC12AST getAst(IFile iFile, IProgressMonitor monitor)
			throws IOException, MissingNatureException {
		// Create Parser for File to construct an AST
		return (NesC12AST) getParser(iFile, monitor).getAST();
	}
	
	/**
	 * Parses a given File and returns the Parser.
	 */
	public Parser getParser(IFile iFile, IProgressMonitor monitor) throws IOException, MissingNatureException{
		// check if parser is already in cache
		Parser parser = parserChache.get(iFile);
		if(parser != null){
			return parser;
		}
		
		ProjectModel projectModel = TinyOSPlugin.getDefault().getProjectTOS(project).getModel();

		File file = iFile.getLocation().toFile();
		IParseFile parseFile = projectModel.parseFile(file);
		
		iFile.getModificationStamp();
		
		parser = (Parser) projectModel.newParser(parseFile, null, monitor);
		parser.setCreateAST(true);
		parser.setFollowIncludes(true);
		parser.setGatherGlobalFieldInformation(true);
		parser.setResolveFullModel(true);
		parser.setASTModel(editor.getASTModel());
		parser.parse(new FileMultiReader(file), monitor);
		
		// Add parser to cache for future use
		parserChache.add(iFile, parser);
		
		return parser;
	}

	/**
	 * Returns all files of this project.
	 * @return
	 * @throws CoreException
	 */
	public Collection<IFile> getAllFiles() throws CoreException{
		ProjectResourceCollector collector = new ProjectResourceCollector();
		try {
			TinyOSPlugin.getDefault().getProjectTOS(project).acceptSourceFiles(collector);
		} catch (MissingNatureException e) {
			RefactoringPlugin.getDefault().log(
					LogLevel.WARNING,
					"Refactroing was called while Plugin was not ready: "
							+ e.getMessage());
			throw new CoreException(new Status(IStatus.ERROR,
					RefactoringPlugin.PLUGIN_ID,
					"Plugin wasn't ready while calling Rename global Variable Refactoring: "
							+ e.getMessage()));
		}
		Collection<IFile> files=new LinkedList<IFile>();
		for(IResource resource:collector.resources){
			if(resource.getType() == IResource.FILE) {
				IFile file = (IFile) resource;
				files.add(file);
			}
		}
		return files;
	}
	
	/**
	 * Returns the projectModel of this project.
	 * @return
	 * @throws MissingNatureException
	 */
	public ProjectModel getModel() throws MissingNatureException{
		return TinyOSPlugin.getDefault().getProjectTOS(project).getModel();
	}
	
	/**
	 * Searchs all project files to find the matching IFile to the given IParseFile
	 * @param parseFile
	 * @return
	 * @throws CoreException
	 * @throws MissingNatureException
	 */
	public IFile getIFile4ParseFile(IParseFile parseFile) throws CoreException, MissingNatureException{
		Collection<IFile> files = getAllFiles();
		for(IFile file:files){
			IParseFile otherPF = getIParseFile4IFile(file);
			if(parseFile.equals(otherPF)){
				return file;
			}
		}
		return null;
	}
	
	/**
	 * Returns the IParseFile for the given IFile
	 * @param file
	 * @return
	 * @throws MissingNatureException
	 */
	public IParseFile getIParseFile4IFile(IFile file) throws MissingNatureException{
		IPath path = file.getLocation();
		if(path==null){
			return null;
		}
		File f=path.toFile();
		IParseFile pF = getModel().parseFile(f);
		return pF;
	}
	
	/**
	 * Checks if the given file is a project file, which means if it is a file defined in this project.
	 * @param file
	 * @return	true if it is a project file, false if it is not or an exception occured.
	 */
	public boolean isProjectFile(IFile file){
		try {
			return getIParseFile4IFile(file).isProjectFile();
		} catch (MissingNatureException e){
			return false;
		}
	}
	
	/**
	 * Checks if the given file is a project file, which means if it is a file defined in this project.
	 * @param file
	 * @return
	 * @throws MissingNatureException
	 */
	public boolean isProjectFile(IParseFile file){
		return file.isProjectFile();
	}
	
	/**
	 * Looks for a component definition with the given name.
	 * @param identifier
	 * @param editor
	 * @return
	 * @throws CoreException
	 * @throws MissingNatureException
	 */
	public IDeclaration getComponentDefinition(String componentName) throws CoreException, MissingNatureException{
		ProjectModel model=getModel();
		List<IDeclaration> declarations=new LinkedList<IDeclaration>();
		declarations.addAll(model.getDeclarations(Kind.MODULE));
		declarations.addAll(model.getDeclarations(Kind.CONFIGURATION));
		for(IDeclaration declaration:declarations){
			if(componentName.equals(declaration.getName())){
				return declaration;
			}
		}
		
		return null;
	}
	
	/**
	 * Looks for a interface definition with the given name.
	 * @param identifier
	 * @param editor
	 * @return
	 * @throws CoreException
	 * @throws MissingNatureException
	 */
	public IDeclaration getInterfaceDefinition(String interfaceName) throws CoreException, MissingNatureException{
		ProjectModel model=getModel();
		List<IDeclaration> declarations=model.getDeclarations(Kind.INTERFACE);
		for(IDeclaration declaration:declarations){
			if(interfaceName.equals(declaration.getName())){
				return declaration;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the IFile which contains the given declaration.
	 * @param declaration
	 * @return
	 * @throws MissingNatureException 
	 * @throws CoreException 
	 */
	public IFile getDeclaringFile(IDeclaration declaration) throws CoreException, MissingNatureException{
		return getIFile4ParseFile(declaration.getParseFile());
	}
	
	/**
	 * Writes parameters to the project log.
	 * @return
	 */
	public void log(String msg){
		TinyOSPlugin.getDefault().log(msg);
	}
	
	/**
	 * Writes parameters to the project log.
	 * @return
	 */
	public void log(String msg,Exception e){
		TinyOSPlugin.getDefault().log(msg, e);
	}
	
	/**
	 * Appends the appropriate file extension string for nesc files to the given string.
	 * @param fileNamePrefix
	 * @return
	 */
	public String appendFileExtension(String fileNamePrefix){
		return fileNamePrefix+".nc";
	}
}
