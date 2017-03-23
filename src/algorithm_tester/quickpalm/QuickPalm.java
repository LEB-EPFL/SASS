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
package algorithm_tester.quickpalm;

import algorithm_tester.EvaluationAlgorithm;
import ij.ImageStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author stefko
 */
public class QuickPalm implements EvaluationAlgorithm {
    private ImageStack stack;
    private final QuickPalmCore core;
    private HashMap<String, Integer> parameters;
    
    private ArrayList<Integer> no_particles_list;
    
    
    public QuickPalm() {
        core = new QuickPalmCore();
        no_particles_list = new ArrayList<Integer>();
        parameters = new HashMap<String, Integer>();
    }
    
    @Override
    public void setImageStack(ImageStack stack) {
        this.stack = stack;
        no_particles_list.clear();
        no_particles_list.ensureCapacity(stack.getSize()+1);
    }

    @Override
    public void processStack() {
        for (int i=1; i<= stack.getSize(); i++) {
            no_particles_list.add(core.processImage(stack.getProcessor(i), i));
        }
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
