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
    public ImageProcessor getNextImage();
    
    public void setControlSignal(double value);
    
    public double getControlSignal();
    
    public double getTrueSignal(int image_no);
    
    public void setCustomParameters(HashMap<String,Double> map);
    
    public HashMap<String,Double> getCustomParameters();

    public void saveStack(File selectedFile);
    
    public ImageStack getStack();
}
