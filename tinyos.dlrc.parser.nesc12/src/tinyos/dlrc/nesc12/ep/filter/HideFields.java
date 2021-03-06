/*
 * Dlrc 2, NesC development in Eclipse.
 * Copyright (C) 2010 DLRC
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
package tinyos.dlrc.nesc12.ep.filter;

import tinyos.dlrc.editors.NesCEditor;
import tinyos.dlrc.editors.outline.IOutlineFilter;
import tinyos.dlrc.ep.parser.IASTModelNode;
import tinyos.dlrc.ep.parser.IASTModelNodeConnection;
import tinyos.dlrc.ep.parser.TagSet;
import tinyos.dlrc.nesc12.ep.NesC12ASTModel;

public class HideFields implements IOutlineFilter{
	public void setEditor( NesCEditor editor ){
		// ignore	
	}
	
	public boolean include( IASTModelNode node ){
		TagSet tags = node.getTags();
		return tags == null || !tags.contains( NesC12ASTModel.FIELD );
	}
	
	public boolean include( IASTModelNodeConnection connection ){
		TagSet tags = connection.getTags();
		return tags == null || !tags.contains( NesC12ASTModel.FIELD );
	}
}
