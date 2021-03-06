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
package tinyos.dlrc.nesc12.ep.rules.proposals;

import static tinyos.dlrc.nesc12.parser.ast.elements.types.TypeUtility.object;
import static tinyos.dlrc.nesc12.parser.ast.elements.types.TypeUtility.pointer;
import static tinyos.dlrc.nesc12.parser.ast.elements.types.TypeUtility.raw;
import tinyos.dlrc.nesc12.ep.NesC12AST;
import tinyos.dlrc.nesc12.ep.rules.ProposalUtility;
import tinyos.dlrc.nesc12.ep.rules.RuleUtility;
import tinyos.dlrc.nesc12.parser.CompletionProposalCollector;
import tinyos.dlrc.nesc12.parser.ast.elements.Field;
import tinyos.dlrc.nesc12.parser.ast.elements.types.DataObjectType;
import tinyos.dlrc.nesc12.parser.ast.nodes.ASTNode;
import tinyos.dlrc.nesc12.parser.ast.nodes.expression.Expression;

public class AccessDataObjectPointerRule extends AccessRule{
    public AccessDataObjectPointerRule(){
        super( "->" );
    }
    
    @Override
    protected void propose( ASTNode node, NesC12AST ast, CompletionProposalCollector collector ){
        if( node == null )
            return;
        
        if( !(node instanceof Expression)){
        	node = RuleUtility.expressionsBefore( ast.getOffsetInput( collector.getOffset() ), node );
        	if( node == null )
        		return;
        }
        
        DataObjectType data = object( raw( pointer( ((Expression)node).resolveType() )));
        if( data == null )
            return;
     
        for( Field field : data.getAllFields() ){
            collector.add( ProposalUtility.createProposal( field, collector.getLocation(), ast ) );
        }
    }
}
