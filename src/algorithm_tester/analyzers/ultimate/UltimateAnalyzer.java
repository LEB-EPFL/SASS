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
package algorithm_tester.analyzers.ultimate;

import algorithm_tester.analyzers.AbstractAnalyzer;
import algorithm_tester.analyzers.spotcounter.SpotCounter;
import algorithm_tester.analyzers.spotcounter.SpotCounterCore;
import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author stefko
 */
public class UltimateAnalyzer extends SpotCounter {
    private final int init_phase = 20;
    private int[][] subtract_mask;
    
    private ImageStack stack;
    private ImagePlus imp;
    
    public UltimateAnalyzer(double pixel_size_um) {
        super(pixel_size_um);
        count++;
    }
    
    @Override
    public void processImage(ImageProcessor ip) {
        if (count == 1) {
            stack = ImageStack.create(ip.getWidth(), ip.getHeight(), 1, ip.getBitDepth());
            subtract_mask = new int[ip.getWidth()][ip.getHeight()];
            for (int row=0; row<ip.getHeight(); row++) {
                for (int col=0; col<ip.getWidth(); col++) {
                    subtract_mask[col][row] = ip.getPixel(col, row);
                }
            }
        } else if (count<init_phase) {
            for (int row=0; row<ip.getHeight(); row++) {
                for (int col=0; col<ip.getWidth(); col++) {
                    subtract_mask[col][row] += ip.getPixel(col, row);
                }
            }
        } else if (count == init_phase) {
            for (int row=0; row<ip.getHeight(); row++) {
                for (int col=0; col<ip.getWidth(); col++) {
                    subtract_mask[col][row] /= init_phase;
                }
            }
            imp = new ImagePlus("Ultimate view", stack);
            imp.show();
        }
        
        if (count > init_phase) {
            for (int row=0; row<ip.getHeight(); row++) {
                for (int col=0; col<ip.getWidth(); col++) {
                    ip.putPixel(col, row, ip.getPixel(col, row) - subtract_mask[col][row]);
                }
            }
            ResultsTable result;
            result = analyzer.analyze(ip);
            spot_counts.add(result.getValue("n", count-init_phase-1));
            min_dists.add(result.getValue("min-distance", count-init_phase-1));
            mean_dists.add(result.getValue("mean-distance", count-init_phase-1));
            p10_dists.add(result.getValue("p10-distance", count-init_phase-1));
            double density_per_um2 = result.getValue("n", count-init_phase-1) / 
                    (pixel_area_um2 * (ip.getWidth()*ip.getHeight()));
            output_history.add(density_per_um2);
            count++;
            imp.setSlice(imp.getNSlices());
            imp.updateAndRepaintWindow();
        }
        else {
            output_history.add(0.0);
            count++;
        }
        stack.addSlice(ip);


        
        
    }

    @Override
    public String getName() {
        return "Ultimate";
    }
    
}
