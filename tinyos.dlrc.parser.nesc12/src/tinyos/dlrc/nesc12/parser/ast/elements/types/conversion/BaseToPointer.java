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
package tinyos.dlrc.nesc12.parser.ast.elements.types.conversion;

import tinyos.dlrc.nesc12.parser.ast.elements.Type;
import tinyos.dlrc.nesc12.parser.ast.elements.types.BaseType;

public class BaseToPointer implements Conversion{
    public boolean responsible( Type source, Type destination ){
        return source.asBase() != null && destination.asPointerType() != null;
    }
    
    public void check( Type source, Type destination, ConversionTable table, ConversionMap map ){
        if( destination.asPointerType().getRawType() != BaseType.VOID ){
            if( source.asBase().isIntegerType() ){
                if( map.isImplicit() ){
                    map.reportWarning( "making pointer from integer without cast", source, destination );
                }
                if( map.isExplicit() ){
                    map.reportWarning( "implicit conversion of integer type to pointer", source, destination );
                }
            }
            else{
                if( map.isImplicit() ){
                    map.reportError( "cannot convert non-integer type to pointer", source, destination );
                }
                if( map.isExplicit() ){
                    map.reportWarning( "implicit conversion of non-integer type to pointer", source, destination );
                }
            }
        }
    }
}
