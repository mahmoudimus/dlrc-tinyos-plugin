/*
 * Dlrc 2, NesC development in Eclipse.
 * Copyright (C) 2009 DLRC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Web:  http://tos-ide.ethz.ch
 * Mail: tos-ide@tik.ee.ethz.ch
 */
package tinyos.dlrc.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import tinyos.dlrc.ep.parser.IASTModelNode;
import tinyos.dlrc.ep.parser.IDeclaration;
import tinyos.dlrc.model.ProjectModel;

public class ResolveDeclarationJob extends FerryJob<IASTModelNode>{
    private ProjectModel model;
    private IDeclaration declaration;
    
    public ResolveDeclarationJob( ProjectModel model, IDeclaration declaration ){
        super( "resolve declaration" );
        this.model = model;
        this.declaration = declaration;
        setPriority( SHORT );
        setRule( model.getProject().getProject() );
    }
    
    @Override
    public IStatus run( IProgressMonitor monitor ){
        monitor.beginTask( "Search node for '" + declaration.getLabel() + "'", 100 );
        content = model.getNode( declaration, new SubProgressMonitor( monitor, 100 ) );
        monitor.done();
        return Status.OK_STATUS;
    }
}
