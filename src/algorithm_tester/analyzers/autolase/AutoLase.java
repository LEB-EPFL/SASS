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
package algorithm_tester.analyzers.autolase;

import algorithm_tester.analyzers.AbstractAnalyzer;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper for Thomas Pengo's implementation of AutoLase algorithm.
 * @author Marcel Stefko
 */
public class AutoLase extends AbstractAnalyzer {
    private ImageStack stack;
    private AutoLaseAnalyzer analyzer;
    private ArrayList<Double> raw_value_history;

    
    private int threshold = 120;
    private int averaging = 30;
    
    /**
     * Initializes AutoLase with default threshold (120) and averaging (30) 
     * values.
     */
    public AutoLase() {
        parameters = new LinkedHashMap<String, Integer>();
        parameters.put("threshold", threshold);
        parameters.put("averaging", averaging);
        init();
    }
    
    /**
     * Resets internal state.
     */
    private void init() {
        analyzer = new AutoLaseAnalyzer(threshold, averaging);
        output_history = new ArrayList<Double>();
        raw_value_history = new ArrayList<Double>();
    }
    
    @Override
    public String getName() {
        return "AutoLase";
    }

    @Override
    public void processImage(ImageProcessor ip) {
        short[] next_image;
        next_image = (short[]) ip.getPixels();
        analyzer.nextImage(next_image);
        output_history.add(analyzer.getCurrentValue());
        raw_value_history.add(analyzer.getRawCurrentValue());
    }
    
    @Override
    public HashMap<String, Double> getOutputValues(int image_no) {
        image_no--;
        HashMap<String, Double> map = new LinkedHashMap<String, Double>();
        map.put("on-time", output_history.get(image_no));
        map.put("raw-on-time", raw_value_history.get(image_no));
        return map;
    }

    @Override
    public void setCustomParameters(LinkedHashMap<String, Integer> map) {
        threshold = map.get("threshold");
        averaging = map.get("averaging");
        parameters = map;
        init();
    }
}


/**
 * This class estimates the density of activations. The density at a particular 
 * point relates 
 * to the maximum time a certain pixel is "on", or above a certain threshold. 
 * The density is calculated as a moving average 30 frames.
 * 
 * The code only works for 2 bytes per pixel cameras for now. 
 * 
 * @author Thomas Pengo
 */
class AutoLaseAnalyzer {
    public final int threshold;
    public final int averaging;
    
    boolean running = true;
    boolean stopping = false;
    
    double currentDensity = 0;
    float[] accumulator = null;
    long lastTime;

    ArrayDeque<Double> density_fifo;
    
    public AutoLaseAnalyzer(int threshold, int averaging ) {
        this.density_fifo = new ArrayDeque<Double>(averaging);
        this.threshold = threshold;
        this.averaging = averaging;
        lastTime = System.currentTimeMillis();
    }
    
    /**
     * Analyzes next image and adjusts internal state.
     * @param image image to be analyzed
     */
    public void nextImage(short[] image) {

        // Reset accumulator if image size has changed
        if (image!=null && accumulator!=null && (image.length != accumulator.length))
            accumulator = null;

        // Threshold the image
        boolean[] curMask = new boolean[image.length];
        // Mask which warns about short overflow
        boolean[] overflowMask = new boolean[image.length];
        for (int i=0; i<curMask.length; i++) {
            curMask[i]=image[i]>threshold;
            overflowMask[i]=image[i]<0;
        }
        int overflow_counter = 0;
        // Calculate accumulator
        if (accumulator == null) {
            // A_0 = I_0 > t; 
            accumulator = new float[image.length];
            for(int i=0; i<accumulator.length; i++)
                if (curMask[i])
                    accumulator[i] = 1;
        } else {
            // A_i = (I_i > t) (1 + A_i-1)
            for(int i=0; i<accumulator.length; i++) {
                if (!curMask[i]) {
                    accumulator[i] = 0;
                } else {
                    accumulator[i]++;
                }
                // If we have overflow, assign a special value
                if (overflowMask[i])  {
                    overflow_counter++;
                    accumulator[i] = -1.0f;
                }
            }
        }
        // Density measure: max(A_i)
        double curd = 0;
        for (int i=0; i<image.length; i++)
            if (accumulator[i]>curd)
                curd = accumulator[i];

        // Moving average estimate
        if (density_fifo.size() == averaging)
            density_fifo.remove();
        density_fifo.offer(curd);

        double mean_density = 0;
        for (Double d : density_fifo)
            mean_density+=d;
        mean_density /= density_fifo.size();

        currentDensity = mean_density;  
    }
    
    /**
     * Returns error signal value from AutoLase
     * @return estimated averaged max emitter density
     */
    public double getCurrentValue() {
        return currentDensity;
    }
    
    /**
     * Returns raw error signal value from AutoLase
     * @return estimated max emitter density for most recent frame
     */
    public double getRawCurrentValue() {
        return density_fifo.getLast();
    }
}