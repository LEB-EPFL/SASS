/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
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
package ch.epfl.leb.sass.models.backgrounds.commands;

import java.util.Arrays;

/**
 * Generates random background patterns from a simplex noise generator.
 * 
 * 
 * @author Kyle M. Douglass
 */
public class GenerateRandomBackground implements BackgroundCommand {
    
    /**
     * The characteristic size of a random feature in pixels.
     */
    private final double featureSize;
    
    /**
     * Seed that determines the background pattern.
     */
    private final int seed;
    
    /**
     * The minimum value for the noise;
     */
    private final float min;
    
    /**
     * The maximum value for the noise;
     */
    private final float max;
    
    
    /**
     * The number of pixels in x.
     */
    private final int nX;
    
    /**
     * The number of pixels in y.
     */
    private final int nY;
    
    public static class Builder implements BackgroundCommandBuilder {
        private double featureSize;
        private int seed;
        private float min;
        private float max;
        private int nX;
        private int nY;
        
        public Builder featureSize(double featureSize) {
            this.featureSize = featureSize;
            return this;
        }
        public Builder seed(int seed) { this.seed = seed; return this; }
        public Builder min(float min) {this.min = min; return this; }
        public Builder max(float max) {this.max = max; return this; }
        public Builder nX(int nX) { this.nX = nX; return this; }
        public Builder nY(int nY) { this.nY = nY; return this; }
        
        public GenerateRandomBackground build() {
            return new GenerateRandomBackground(this);
        }        
    }
    
    private GenerateRandomBackground(Builder builder) {
        this.featureSize = builder.featureSize;
        this.seed = builder.seed;
        this.min = builder.min;
        this.max = builder.max;
        this.nX = builder.nX;
        this.nY = builder.nY;
    }
    
    /**
     * Create the random background signal.
     * 
     * @return A 2D array of background photons for each pixel.
     */
    @Override
    public float[][] generateBackground() {
        float[][] background = new float[this.nY][this.nX];
        
        OpenSimplexNoise noise = new OpenSimplexNoise();
        float maxValue = Float.NEGATIVE_INFINITY;
        float minValue = Float.POSITIVE_INFINITY;
        
        for (int y = 0; y < this.nY; y++) {
            for (int x = 0; x < this.nX; x++) {
                // If we want to make this vary in time in the future, simply
                // increment the last argument to noise.eval() after each frame.
                background[y][x] = (float) noise.eval(
                        x / featureSize,
                        y / featureSize,
                        (double) this.seed
                );
                
                if (background[y][x] > maxValue)
                    maxValue = background[y][x];
                
                if (background[y][x] < minValue)
                    minValue = background[y][x];
            }
        }
        
        float slope = (this.max - this.min) / (maxValue - minValue);
        
        // Renormalize the noise to the range [minValue, maxValue]
        for (int y = 0; y < this.nY; y++) {
            for (int x = 0; x < this.nX; x++) {
                // If we want to make this vary in time in the future, simply
                // increment the last argument to noise.eval after each frame.
                background[y][x] = slope * background[y][x] +
                                   this.max - slope * maxValue;
            }
        }
        
        return background;
    }
    
}
