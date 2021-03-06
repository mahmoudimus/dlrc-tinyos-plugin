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
package tinyos.dlrc.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.MessageConsoleStream;

import tinyos.dlrc.TinyOSConsole;

public class TinyOSConsoleStream extends OutputStream{
    private TinyOSConsole console;
	private MessageConsoleStream stream;
    
    private PrintStream print;
    
    public TinyOSConsoleStream( TinyOSConsole console, MessageConsoleStream stream ){
    	this.console = console;
        this.stream = stream;
        print = new PrintStream( this );
    }
    
    public TinyOSConsole getConsole(){
		return console;
	}
    
    /**
     * Gets a print stream that writes into this stream.
     * @return the print stream
     */
    public PrintStream print(){
        return print;
    }
    
    public void println( String message ){
    	stream.println( message );
    }
    
    public void setColor( Color color ){
        stream.setColor( color );
    }
    
    @Override
    public void write( int b ) throws IOException{
        stream.write( b );
    }
    
    @Override
    public void flush() throws IOException{
    	stream.flush();
    }
    
    @Override
    public void close() throws IOException{
        stream.close();
    }
}
