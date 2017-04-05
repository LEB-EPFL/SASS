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
package algorithm_tester;

import ij.ImageStack;
import ij.process.ImageProcessor;
import java.io.File;
import java.util.HashMap;

/**
 * Interface through which AlgorithmTester generates new images to be
 * analyzed by EvaluationAlgorithms.
 * @author Marcel Stefko
 */
public interface ImageGenerator {

    /**
     * Generates a new image and adds it to the internal stack.
     * @return newly generated image
     */
    public ImageProcessor getNextImage();
    
    /**
     * Sets control signal of the generator (e.g. laser power). This should be
     * used by the controller.
     * @param value new value of the control signal
     */
    public void setControlSignal(double value);
    
    /**
     * Returns currently set control signal of the generator (e.g. laser power 
     * settings).
     * @return control signal value
     */
    public double getControlSignal();
    
    /**
     * Returns the actual value of signal (if applicable) for given image.
     * @param image_no image number in history
     * @return value of signal (e.g. no. of active emitters)
     */
    public double getTrueSignal(int image_no);
    
    /**
     * Sets custom parameters of the generator.
     * @param map map of custom parameters
     */
    public void setCustomParameters(HashMap<String,Double> map);
    
    /**
     * Returns custom parameters of the generator.
     * @return map of custom parameters
     */
    public HashMap<String,Double> getCustomParameters();

    /**
     * Saves .tif stack to selected file.
     * @param selectedFile file to save to
     */
    public void saveStack(File selectedFile);
    
    /**
     * Returns internal stack with all generated images.
     * @return internal stack
     */
    public ImageStack getStack();
}
