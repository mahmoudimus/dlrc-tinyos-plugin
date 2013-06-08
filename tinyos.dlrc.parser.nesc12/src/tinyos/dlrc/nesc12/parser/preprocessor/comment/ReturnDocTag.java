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
package tinyos.dlrc.nesc12.parser.preprocessor.comment;

public class ReturnDocTag extends DefaultDocTag{
	public static final IDocTagFactory FACTORY = new IDocTagFactory(){
		public IDocTag create( String name, String description, int nameOffset, int[] descriptionChars, int[] descriptionOffsets ){
			return new ReturnDocTag( name, description, nameOffset, descriptionChars, descriptionOffsets );
		}
	};

	public ReturnDocTag( String name, String description, int nameOffset, int[] descriptionChars, int[] descriptionOffsets ){
		super( name, description, nameOffset, descriptionChars, descriptionOffsets );
	}
}
