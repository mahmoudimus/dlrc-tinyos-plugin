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
package tinyos.dlrc.environment.basic.path.steps;

import tinyos.dlrc.environment.basic.path.IPathRequest;
import tinyos.dlrc.environment.basic.path.IPathSet;
import tinyos.dlrc.environment.basic.progress.ICancellation;

/**
 * A {@link ICollectStep} combining more than one steps.
 * @author Benjamin Sigg
 */
public class MultiCollectStep implements ICollectStep{
    private ICollectStep[] steps;
    
    public MultiCollectStep( ICollectStep... steps ){
        this.steps = steps;
    }
    
	public String getName(){
		return "MultiCollectStep";
	}
	
    public void collect( IPathRequest request, IPathSet paths, ICancellation cancellation ){
        for( ICollectStep step : steps ){
            step.collect( request, paths, cancellation );
        }
    }
}
