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
package tinyos.dlrc.nesc12.parser.ast.nodes.declaration;

import tinyos.dlrc.nesc12.lexer.Token;
import tinyos.dlrc.nesc12.parser.ast.ASTVisitor;
import tinyos.dlrc.nesc12.parser.ast.AnalyzeStack;
import tinyos.dlrc.nesc12.parser.ast.nodes.AbstractTokenASTNode;

public class DataObjectSpecifier extends AbstractTokenASTNode {
    public static enum Specifier{
        STRUCT( "struct" ),
        NX_STRUCT( "nx_struct" ),
        UNION( "union" ),
        NX_UNION( "nx_union" );
        
        private String name;
        
        private Specifier( String name ){
            this.name = name;
        }
        
        @Override
        public String toString(){
            return name;
        }
    }
    
    private Specifier specifier;
    
    public DataObjectSpecifier( Token token, Specifier specifier ){
        super( "DataObjectSpecifier", token );
        setSpecifier( specifier );
    }
    
    public void setSpecifier( Specifier specifier ) {
        this.specifier = specifier;
    }
    public Specifier getSpecifier() {
        return specifier;
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
        // TODO method not implemented
        super.resolve( stack );
    }
}
