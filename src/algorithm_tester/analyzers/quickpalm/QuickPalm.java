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
package algorithm_tester.analyzers.quickpalm;

import algorithm_tester.analyzers.AbstractAnalyzer;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * QuickPalm implementation interface.
 * 
 * @author Marcel Stefko
 */
public class QuickPalm extends AbstractAnalyzer {
    private int count;
    private final QuickPalmCore core;
    private LinkedHashMap<String, Integer> parameters;
    
    
    
    public QuickPalm() {
        core = new QuickPalmCore();
        output_history = new ArrayList<Double>();
        parameters = new LinkedHashMap<String, Integer>();
        count = 0;
    }
    
    
    @Override
    public void processImage(ImageProcessor ip) {
            count++;
            output_history.add((double) core.processImage(ip, count));
    }
    
    @Override
    public void setCustomParameters(LinkedHashMap<String, Integer> map) {
        parameters = map;
    }

    @Override
    public HashMap<String, Double> getOutputValues(int image_no) {
        image_no--;
        HashMap<String, Double> map = new LinkedHashMap<String, Double>();
        map.put("n-particles", output_history.get(image_no));
        return map;
    }

    @Override
    public String getName() {
        return "QuickPalm";
    }   
}
