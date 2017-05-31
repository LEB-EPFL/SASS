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
package ch.epfl.leb.sass.simulator.generators.tiff;

import ch.epfl.leb.sass.simulator.ImageGenerator;
import ch.epfl.leb.sass.simulator.generators.AbstractGenerator;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import java.io.File;
import java.util.HashMap;

/**
 * Generates images from a .tiff stack image file.
 * @author Marcel Stefko
 */
public class TiffGenerator extends AbstractGenerator {
    private int count;
    
    /**
     * Initializes the TiffParser and loads up the image stack from a file.
     * @param file tiff file to be loaded
     */
    public TiffGenerator(File file) {
        TiffParser parser = new TiffParser();
        System.out.print("Loading selected .tif file.");
        stack = parser.loadGeneralTiff(file);
        System.out.format("Tif file loaded: %s\n",file.getAbsolutePath());
        count = 0;
    }

    @Override
    public ImageProcessor getNextImage() {
        count++;
        if (count > stack.getSize())
            return null;
        return stack.getProcessor(count);
    }

    @Override
    public void setCustomParameters(HashMap<String, Double> map) {
        return;
    }

    @Override
    public HashMap<String, Double> getCustomParameters() {
        return new HashMap<String, Double>();
    }

    @Override
    public void setControlSignal(double value) {
        return;
    }

    @Override
    public double getControlSignal() {
        return 0.0;
    }
    
    @Override
    public double getTrueSignal(int image_no) {
        return 0.0;
    }

    @Override
    public double getPixelSizeUm() {
        return 1.0;
    }
    
}
