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
package simulator.generators;

import simulator.ImageGenerator;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Marcel Stefko
 */
public abstract class AbstractGenerator implements ImageGenerator {

    /**
     * Map of custom parameters for the generator.
     */
    protected HashMap<String,Double> parameters;

    /**
     * Stack to which the generated images are appended.
     */
    protected ImageStack stack;    
    
    /**
     * Initializes the empty parameters map.
     */
    public AbstractGenerator() {
        parameters = new HashMap<String,Double>();        
    }
    
    @Override
    public void saveStack(File file) {
        ImagePlus imp = new ImagePlus("stack", stack);
        FileSaver fs = new FileSaver(imp);
        fs.saveAsTiffStack(file.getAbsolutePath());
    }
    
    @Override
    public ImageStack getStack() {
        return stack;
    }
}
