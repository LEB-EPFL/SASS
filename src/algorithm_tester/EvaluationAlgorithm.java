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

import ij.process.ImageProcessor;
import java.util.HashMap;


/**
 * Interface through which the AlgorithmTester extracts analysis results from
 * and feeds images to individual algorithms.
 * @author Marcel Stefko
 */
public interface EvaluationAlgorithm {

    /**
     * Carry out analysis on the received image and modify your internal state
     * accordingly.
     * @param ip image to be processed
     */
    public void processImage(ImageProcessor ip);
    
    /**
     * Set your internal configuration parameters to new values.
     * @param map new configuration parameters
     */
    public void setCustomParameters(HashMap<String, Integer> map);
    
    /**
     * Returns the current internal configuration parameters.
     * @return current configuration parameters
     */
    public HashMap<String, Integer> getCustomParameters();
    
    /**
     * Returns the output (various error signal candidate values) of the 
     * algorithm for the specific image number.
     * 
     * @param image_no 1-based sequential number of the frame
     * @return error signal values
     */
    public HashMap<String, Double> getOutputValues(int image_no);
    
    /**
     * Returns the current value of error signal for given analyzer.
     * @return error signal value
     */
    public double getCurrentErrorSignal();
    
    /**
     * Returns custom name of the analyzer.
     * @return name of the analyzer
     */
    public String getName();
    
}
