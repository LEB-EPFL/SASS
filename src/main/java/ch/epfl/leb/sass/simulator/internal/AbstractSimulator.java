/* 
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * Author: Marcel Stefko
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
 */
package ch.epfl.leb.sass.simulator.internal;

import java.io.File;
import java.util.HashMap;
import ch.epfl.leb.sass.simulator.Simulator;
import ch.epfl.leb.sass.utils.images.ImageS;

/**
 *
 * @author Marcel Stefko
 */
public abstract class AbstractSimulator implements Simulator {

    /**
     * Map of custom parameters for the generator.
     */
    protected HashMap<String,Double> parameters;

    /**
     * Stack to which the generated images are appended.
     */
    protected ImageS stack;    
    
    /**
     * Initializes the empty parameters map.
     */
    public AbstractSimulator() {
        parameters = new HashMap<String,Double>();        
    }
    
    @Override
    public void saveStack(File file) {
        stack.saveAsTiffStack(file);
    }
    
    @Override
    public ImageS getStack() {
        return stack;
    }
    
    @Override
    public int getImageCount() {
        return stack.getSize();
    }
    
    @Override
    public String getSimulationState() {
        // This is part of RPC development at the moment, so do return nothing.
        return "";
    }
    
    
}
