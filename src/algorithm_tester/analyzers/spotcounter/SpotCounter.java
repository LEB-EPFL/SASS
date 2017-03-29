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

import algorithm_tester.EvaluationAlgorithm;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Wrapper for SpotCounter implementation.
 * @author Marcel Stefko
 */
public class SpotCounter implements EvaluationAlgorithm {
    private int count;
    private ArrayList<Double> spot_counts;
    private ArrayList<Double> min_dists;
    private ArrayList<Double> mean_dists;
    private ArrayList<Double> p10_dists;
    SpotCounterCore analyzer;
    
    private HashMap<String, Integer> parameters;
    private int noise_tolerance = 100;
    private int box_size = 5;
    
    /**
     * Initializes the algorithm.
     */
    public SpotCounter() {
        parameters = new HashMap<String, Integer>();
        parameters.put("noise-tolerance", noise_tolerance);
        parameters.put("box-size", box_size);
        init();
    }
    
    /**
     * Resets the algorithm into default state (excluding config parameters).
     */
    private void init() {
        spot_counts = new ArrayList<Double>();
        min_dists = new ArrayList<Double>();
        mean_dists = new ArrayList<Double>();
        p10_dists = new ArrayList<Double>();
        analyzer = new SpotCounterCore(noise_tolerance, box_size);
        count = 0;
    } 
    
    @Override
    public void processImage(ImageProcessor ip) {
        ResultsTable result;
        result = analyzer.analyze(ip);
        spot_counts.add(result.getValue("n", count));
        min_dists.add(result.getValue("min-distance", count));
        mean_dists.add(result.getValue("mean-distance", count));
        p10_dists.add(result.getValue("p10-distance", count));
        count++;
    }

    @Override
    public HashMap<String, Double> getOutputValues(int image_no) {
        image_no--; //hack to get good array indexing
        HashMap<String, Double> map = new LinkedHashMap<String, Double>();
        map.put("spot-count", spot_counts.get(image_no));
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
    public void setCustomParameters(HashMap<String, Integer> map) {
        noise_tolerance = map.get("noise-tolerance");
        box_size = map.get("box-size");
        parameters = map;
        init();
    }

    @Override
    public HashMap<String, Integer> getCustomParameters() {
        return parameters;
    }
    
}


