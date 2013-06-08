package tinyos.dlrc.refactoring.abstractrefactoring.rename;

import tinyos.dlrc.nesc12.parser.ast.nodes.general.Identifier;
import tinyos.dlrc.refactoring.ast.AstAnalyzerFactory;
import tinyos.dlrc.refactoring.ast.CAstAnalyzer;
import tinyos.dlrc.refactoring.ast.ComponentAstAnalyzer;
import tinyos.dlrc.refactoring.ast.ConfigurationAstAnalyzer;
import tinyos.dlrc.refactoring.ast.InterfaceAstAnalyzer;
import tinyos.dlrc.refactoring.ast.ModuleAstAnalyzer;
import tinyos.dlrc.refactoring.utilities.ASTUtil;

public class SelectionIdentifier {

	protected Identifier identifier;
	protected AstAnalyzerFactory factory4Selection;
	protected CAstAnalyzer cAnalyzer;
	protected ComponentAstAnalyzer componentAnalyzer;
	protected ConfigurationAstAnalyzer configurationAnalyzer;
	protected ModuleAstAnalyzer moduleAnalyzer;
	protected InterfaceAstAnalyzer interfaceAnalyzer;
	protected ASTUtil astUtil=new ASTUtil();
	
	/**
	 * Creates A Selection Identifier for the given identifier.
	 * Is convenience constructor for the call new SelectionIdentifier(identifier,new AstAnalyzerFactory(identifier) )
	 * @param identifier
	 */
	protected SelectionIdentifier(Identifier identifier){
		this(identifier,new AstAnalyzerFactory(identifier));
	}
	
	/**
	 * Creates A Selection Identifier for the given AstAnalyzerFactory.
	 * The factory has to be initialized with the given identifier for this to be of use.
	 * This constructor can be used to avoid multiple analyzing of the same Ast. 
	 * @param identifier
	 */
	protected SelectionIdentifier(Identifier identifier,AstAnalyzerFactory analyzerFactory){
		this.identifier=identifier;
		this.factory4Selection=analyzerFactory;
		if(analyzerFactory.hasCAnalyzerCreated()){
			cAnalyzer=analyzerFactory.getCAnalyzer();
		}
		if(analyzerFactory.hasComponentAnalyzerCreated()){
			componentAnalyzer=analyzerFactory.getComponentAnalyzer();
		}
		if(analyzerFactory.hasConfigurationAnalyzerCreated()){
			configurationAnalyzer=analyzerFactory.getConfigurationAnalyzer();
		}
		if(analyzerFactory.hasModuleAnalyzerCreated()){
			moduleAnalyzer=analyzerFactory.getModuleAnalyzer();
		}
		if(analyzerFactory.hasInterfaceAnalyzerCreated()){
			interfaceAnalyzer=analyzerFactory.getInterfaceAnalyzer();
		}
	}
	
	/**
	 * Returns the identifier, for which this class checks the selection type.
	 * @return
	 */
	public Identifier getSelection(){
		return identifier;
	}
	
}
