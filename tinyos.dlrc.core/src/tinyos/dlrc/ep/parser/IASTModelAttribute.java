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
package tinyos.dlrc.ep.parser;

import tinyos.dlrc.ep.storage.GenericArrayFactory;
import tinyos.dlrc.ep.storage.IGenericFactory;

/**
 * A wrapper around a nesc-attribute that has been attached to a 
 * connection or a node.
 * @author Benjamin Sigg
 *
 */
public interface IASTModelAttribute{
	public static IGenericFactory<IASTModelAttribute[]> ARRAY_FACTORY = new GenericArrayFactory<IASTModelAttribute>(){
		@Override
		public IASTModelAttribute[] create( int size ){
			return new IASTModelAttribute[ size ];
		}		
	};
	
	/**
	 * Gets the name of this attribute.
	 * @return the name
	 */
	public String getName();
}
