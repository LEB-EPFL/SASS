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
package algorithm_tester.analyzers.spotcounter;

import algorithm_tester.analyzers.AbstractAnalyzer;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper for SpotCounter implementation.
 * @author Marcel Stefko
 */
public class SpotCounter extends AbstractAnalyzer {
    private int count;
    
    private ArrayList<Double> min_dists;
    private ArrayList<Double> mean_dists;
    private ArrayList<Double> p10_dists;
    SpotCounterCore analyzer;
    
    private int noise_tolerance = 100;
    private int box_size = 5;
    
    /**
     * Initializes the algorithm.
     */
    public SpotCounter() {
        parameters = new LinkedHashMap<String, Integer>();
        parameters.put("noise-tolerance", noise_tolerance);
        parameters.put("box-size", box_size);
        init();
    }
    
    /**
     * Resets the algorithm into default state (excluding config parameters).
     */
    private void init() {
        output_history = new ArrayList<Double>();
        output_history.add(0.0);
        min_dists = new ArrayList<Double>();
        min_dists.add(0.0);
        mean_dists = new ArrayList<Double>();
        mean_dists.add(0.0);
        p10_dists = new ArrayList<Double>();
        p10_dists.add(0.0);
        analyzer = new SpotCounterCore(noise_tolerance, box_size);
        count = 0;
    } 
    
    @Override
    public void processImage(ImageProcessor ip) {
        ResultsTable result;
        result = analyzer.analyze(ip);
        output_history.add(result.getValue("n", count));
        min_dists.add(result.getValue("min-distance", count));
        mean_dists.add(result.getValue("mean-distance", count));
        p10_dists.add(result.getValue("p10-distance", count));
        count++;
    }

    @Override
    public HashMap<String, Double> getOutputValues(int image_no) {
        HashMap<String, Double> map = new LinkedHashMap<String, Double>();
        map.put("spot-count", output_history.get(image_no));
        map.put("min-dist", min_dists.get(image_no));
        map.put("mean-dist", mean_dists.get(image_no));
        map.put("p10-dist", p10_dists.get(image_no));
        return map;
    }


    @Override
    public String getName() {
        return "SpotCounter";
    }

    @Override
    public void setCustomParameters(LinkedHashMap<String, Integer> map) {
        noise_tolerance = map.get("noise-tolerance");
        box_size = map.get("box-size");
        parameters = map;
        init();
    }
    
}


