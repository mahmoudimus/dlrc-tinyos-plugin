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

import java.util.ArrayList;
import java.util.List;

import tinyos.dlrc.ep.IEnvironment;
import tinyos.dlrc.ep.IEnvironmentListener;
import tinyos.dlrc.ep.IPlatform;
import tinyos.dlrc.ep.IPlatformListener;

/**
 * Observes an {@link IEnvironment} and adds a {@link IPlatformListener} to
 * each platform of that environment. This class is intended to be subclassed,
 * sublcasses should override {@link #makeIncludesChanged(IPlatform)}
 * and/or {@link #pathsChanged(IPlatform)}. 
 * @author Benjamin Sigg
 *
 */
public class PlatformObserver implements IEnvironmentListener, IPlatformListener{
	private List<IPlatform> platforms = new ArrayList<IPlatform>();
    
    public PlatformObserver( IEnvironment environment ){
        environment.addEnvironmentListener( this );
        reinitialized( environment );
    }
    
    public void reinitialized( IEnvironment environment ) {
        for( IPlatform platform : platforms )
            platform.removePlatformListener( this );
        
        IPlatform[] array = environment.getPlatforms();
        if( array != null ){
            for( IPlatform platform : array ){
                platforms.add( platform );
                platform.addPlatformListener( this );
            }
        }
    }
    
    public void makeIncludesChanged( IPlatform platform ) {
        // ignore
    }
    
    public void pathsChanged( IPlatform platform ) {
    	// ignore
    }
}
