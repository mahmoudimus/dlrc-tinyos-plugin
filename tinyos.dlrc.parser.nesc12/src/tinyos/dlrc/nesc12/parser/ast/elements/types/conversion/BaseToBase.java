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

import java.math.BigInteger;

import tinyos.dlrc.nesc12.parser.ast.elements.Type;
import tinyos.dlrc.nesc12.parser.ast.elements.Value;
import tinyos.dlrc.nesc12.parser.ast.elements.types.BaseType;
import tinyos.dlrc.nesc12.parser.ast.elements.values.IntegerValue;

public class BaseToBase implements Conversion{
    public boolean responsible( Type source, Type destination ){
        return source.asBase() != null && destination.asBase() != null;
    }

    public void check( Type source, Type destination, ConversionTable table, ConversionMap map ){
        BaseType sourceBase = source.asBase();
        BaseType destinationBase = destination.asBase();
        
        if( sourceBase != destinationBase ){
            if( map.isImplicit() ){
                if( sourceBase.typeOrdinal() > destinationBase.typeOrdinal() ){
                    boolean report = true;
                    
                    Value value = map.getConvertedValue();
                    if( value != null ){
                        if( value instanceof IntegerValue ){
                            BigInteger integer = ((IntegerValue)value).getValue();
                            switch( destinationBase ){
                                case S_CHAR:
                                case S_INT:
                                case S_LONG:
                                case S_LONG_LONG:
                                case S_SHORT:
                                    report = !IntegerValue.inBoundaries( integer, destinationBase.sizeOf(), false );
                                    break;
                                case U_CHAR:
                                case U_INT:
                                case U_LONG:
                                case U_LONG_LONG:
                                case U_SHORT:
                                    report = !IntegerValue.inBoundaries( integer, destinationBase.sizeOf(), true );
                                    break;
                            }
                        }
                    }
                    
                    if( report ){
                        map.reportWarning( "potential overflow or loss of precision because of implicit cast", source, destination  );
                    }
                }
            }
        }
    }
}
