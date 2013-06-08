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
package tinyos.dlrc.nesc12.parser.ast.nodes.statement;

import tinyos.dlrc.nesc12.lexer.Token;
import tinyos.dlrc.nesc12.parser.ast.ASTVisitor;
import tinyos.dlrc.nesc12.parser.ast.AnalyzeStack;
import tinyos.dlrc.nesc12.parser.ast.elements.Type;
import tinyos.dlrc.nesc12.parser.ast.elements.types.BaseType;
import tinyos.dlrc.nesc12.parser.ast.nodes.ASTNode;
import tinyos.dlrc.nesc12.parser.ast.nodes.AbstractTokenASTNode;
import tinyos.dlrc.nesc12.parser.ast.nodes.Flag;
import tinyos.dlrc.nesc12.parser.ast.util.ControlFlow;

public class ContinueStatement extends AbstractTokenASTNode implements Statement{
    public static final Flag CONTINUABLE = new Flag( "in a continuable statement" );
    
    public ContinueStatement( Token token ){
        super( "ContinueStatement", token );
    }
    
    @Override
    protected boolean visit( ASTVisitor visitor ) {
        return visitor.visit( this );
    }

    @Override
    protected void endVisit( ASTVisitor visitor ) {
        visitor.endVisit( this );
    }
    
    @Override
    public void resolve( AnalyzeStack stack ) {
        super.resolve( stack );
        stack.checkCancellation();
        
        if( stack.isReportErrors() ){
            if( !stack.present( CONTINUABLE )){
                stack.error( "continue statement not within loop", this );
            }
        }
    }
    
    public void flow( ControlFlow flow ){
        ASTNode node = this;
        while( node != null &&
                !(node instanceof WhileStatement) &&
                !(node instanceof DoWhileStatement) && 
                !(node instanceof ForStatement)){
            
            node = node.getParent();
        }
        
        if( node != null ){
            flow.link( this, (Statement)node );
        }
        else{
            flow.follow( this );
        }
    }
    
    public Type resolveExpressionType(){
        return BaseType.VOID;
    }
    
    public boolean isFunctionEnd(){
        return false;
    }
}
