/*
 * Copyright (C) 2017 stefko
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

import algorithm_tester.EvaluationAlgorithm;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author stefko
 */
public class QuickPalm implements EvaluationAlgorithm {
    private int count;
    private final QuickPalmCore core;
    private HashMap<String, Integer> parameters;
    
    private ArrayList<Integer> no_particles_list;
    
    
    public QuickPalm() {
        core = new QuickPalmCore();
        no_particles_list = new ArrayList<Integer>();
        parameters = new HashMap<String, Integer>();
        count = 0;
    }
    
    @Override
    public void processImage(ImageProcessor ip) {
            count++;
            no_particles_list.add(core.processImage(ip, count));
    }
    
    @Override
    public void setCustomParameters(HashMap<String, Integer> map) {
        parameters = map;
    }

    @Override
    public HashMap<String, Integer> getCustomParameters() {
        return parameters;
    }

    @Override
    public HashMap<String, Double> getOutputValues(int image_no) {
        image_no--;
        HashMap<String, Double> map = new LinkedHashMap<String, Double>();
        map.put("n-particles", (double) no_particles_list.get(image_no));
        return map;
    }

    @Override
    public String getName() {
        return "QuickPalm";
    }
    
}
