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
package tinyos.dlrc.preprocessor;

/**
 * Information about a file. That includes at least the path to the file, but
 * may include additional information that is needed by the client.
 * @author Benjamin Sigg
 */
public interface FileInfo{
    /**
     * Gets the path of the file.
     * @return the path of the file
     */
    public String getPath();
    
    /**
     * Gets the name of the file.
     * @return the name of the file
     */
    public String getName();
}
