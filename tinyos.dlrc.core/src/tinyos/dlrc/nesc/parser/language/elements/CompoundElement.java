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
package tinyos.dlrc.nesc.parser.language.elements;

import java.util.ArrayList;

import org.eclipse.jface.text.reconciler.DirtyRegion;

import tinyos.dlrc.nesc.scanner.Token;

public class CompoundElement extends StatementElement {
	
    DeclarationElement[] declarations;
    
    @Override
    public void updatePosition(DirtyRegion region) {
    	super.updatePosition(region);
    	if (declarations!= null) {
    		for (int i = 0; i < declarations.length; i++) {
    			declarations[i].updatePosition(region);
			}
    	}
    }
    
    // Statements are added as childs..

	public CompoundElement(Token token, Token token2) {
		super(token, token2);
	}

	@SuppressWarnings("unchecked")
	public void setDeclarations(ArrayList list) {
		this.declarations = (DeclarationElement[]) list.toArray(new DeclarationElement[list.size()]);		
	}




}
