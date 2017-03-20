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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
/**
 *
 * @author stefko
 */
public class AutoLase implements EvaluationAlgorithm {
    private ImageStack stack;
    private final AutoLaseAnalyzer analyzer;
    private final ArrayList<Double> value_list;
    private final ArrayList<Double> raw_value_list;
    
    public AutoLase() {
        analyzer = new AutoLaseAnalyzer();
        value_list = new ArrayList<Double>();
        raw_value_list = new ArrayList<Double>();
    }
    
    @Override
    public String getName() {
        return "Original_AutoLase";
    }
    
    @Override
    public void setImageStack(ImageStack stack) {
        this.stack = stack;
    }
    
    @Override
    public void processStack() {
        short[] next_image;
        for (int i=1; i <= stack.getSize(); i++) {
            next_image = (short[]) stack.getPixels(i);
            analyzer.nextImage(next_image);
            value_list.add(analyzer.getCurrentValue());
            raw_value_list.add(analyzer.getRawCurrentValue());
        }
    }

    @Override
    public double getOutputValue(int image_no) {
        return value_list.get(image_no);
    }

    @Override
    public double getRawOutputValue(int image_no) {
        return raw_value_list.get(image_no);
    }


    
}

class AutoLaseAnalyzer {
    public static final int DEFAULT_THRESHOLD = 5500;
    public static final int DEFAULT_WAIT_TIME = 20;
    public static final int NUM_ELEMS = 50;
    
    boolean running = true;
    boolean stopping = false;
    
    double currentDensity = 0;
    float[] accumulator = null;
    long lastTime;
    
    int threshold = DEFAULT_THRESHOLD;
    long timeInterval = DEFAULT_WAIT_TIME;
    int fifoNumElems = NUM_ELEMS;

    ArrayDeque<Double> density_fifo;
    
    public AutoLaseAnalyzer( ) {
        this.density_fifo = new ArrayDeque<Double>(fifoNumElems);
        lastTime = System.currentTimeMillis();
    }
    
    public void nextImage(short[] image) {

        // Reset accumulator if image size has changed
        if (image!=null && accumulator!=null && (image.length != accumulator.length))
            accumulator = null;

        // Threshold the image
        boolean[] curMask = new boolean[image.length];
        boolean[] overflowMask = new boolean[image.length];
        boolean[] largeMask = new boolean[image.length];
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
                    accumulator[i] = timeInterval;
        } else {
            // A_i = (I_i > t) (1 + A_i-1)
            for(int i=0; i<accumulator.length; i++) {
                if (!curMask[i]) {
                    accumulator[i] = 0;
                } else {
                    accumulator[i]+=timeInterval;
                }
                if (overflowMask[i])  {
                    overflow_counter++;
                    accumulator[i] = -1.0f;
                }
                if (largeMask[i]){
                    accumulator[i] = -100.0f;
                }
            }
        }
        // Density measure: max(A_i)
        double curd = 0;
        for (int i=0; i<image.length; i++)
            if (accumulator[i]>curd)
                curd = accumulator[i];

        // Moving average estimate
        if (density_fifo.size() == fifoNumElems)
            density_fifo.remove();
        density_fifo.offer(curd);

        double mean_density = 0;
        for (Double d : density_fifo)
            mean_density+=d;
        mean_density /= density_fifo.size();

        currentDensity = mean_density;  
    }
    
    public double getCurrentValue() {
        return currentDensity;
    }
    
    public double getRawCurrentValue() {
        return density_fifo.getLast();
    }
}