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
package algorithm_tester.spotcounter;

import ij.measure.ResultsTable;
import ij.process.ShortProcessor;

/**
 *
 * @author stefko
 */
public class SpotCounterLite {
    SpotCounterCore analyzer;
    private int noise_tolerance = 100;
    private int box_size = 5;
    private int counter = 0;
    
    public SpotCounterLite() {
        analyzer = new SpotCounterCore(noise_tolerance, box_size);
    }
    
    public void setParameters(int noise_tolerance, int box_size) {
        this.noise_tolerance = noise_tolerance;
        this.box_size = box_size;
        analyzer = new SpotCounterCore(noise_tolerance, box_size);
        counter = 0;
    }
    
    public double[] processImage(short[] image, int width, int height) {
        counter++;
        ShortProcessor ip = new ShortProcessor(width, height, image, null);
        ResultsTable result;
        result = analyzer.analyze(ip);
        double[] output = new double[4];
        
        output[0] = result.getValue("n", counter);
        output[1] = result.getValue("min-distance", counter);
        output[2] = result.getValue("mean-distance", counter);
        output[3] = result.getValue("p10-distance", counter);
        
        return output;
    }
}
