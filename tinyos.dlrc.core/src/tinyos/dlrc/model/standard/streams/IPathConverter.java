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
package tinyos.dlrc.model.standard.streams;

import org.eclipse.core.runtime.IPath;

/**
 * Converts some path to a file to another path.
 * @author Benjamin Sigg
 *
 */
public interface IPathConverter{
	public enum Namespace{
		INTERN, EXTERN
	}
	
	/**
	 * Converts the relative path <code>path</code>. If <code>path</code> is
	 * {@link ICachePath#open() open}, then this method must always return 
	 * the same result. Otherwise the result must not be a path that 
	 * is open (but may differ in every call).
	 * @param namespace the namespace in which the path is used
	 * @param path some relative path
	 * @return the converted path
	 */
	public ICachePath convert( Namespace namespace, IPath path );
}
