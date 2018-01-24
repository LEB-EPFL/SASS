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
 *
 * @author Kyle M. Douglass
 */
public final class GenerateUniformBackground implements BackgroundCommand {
    
    /**
     * The number of background photons per pixel.
     */
    private final float backgroundSignal;
    
    /**
     * The number of pixels in x.
     */
    private final int nX;
    
    /**
     * The number of pixels in y.
     */
    private final int nY;
    
    /**
     * Creates the command to generate a uniform background.
     */
    public static class Builder implements BackgroundCommandBuilder {
        private float backgroundSignal;
        private int nX;
        private int nY;
        
        public Builder backgroundSignal(float backgroundSignal) {
            this.backgroundSignal = backgroundSignal;
            return this;
        }
        public Builder nX(int nX) { this.nX = nX; return this; }
        public Builder nY(int nY) { this.nY = nY; return this; }
        
        /**
         * Builds the command.
         * @return The command to build a uniform background.
         */
        public GenerateUniformBackground build() {
            return new GenerateUniformBackground(this);
        }
    }
    
    private GenerateUniformBackground(Builder builder) {
        this.backgroundSignal = builder.backgroundSignal;
        this.nX = builder.nX;
        this.nY = builder.nY;
    }
    
    /**
     * Create the background signal.
     * @return A 2D array of background photons for each pixel.
     */
    @Override
    public float[][] generateBackground() {
        float[][] background = new float[this.nY][this.nX];
        
        // Fill background array with the background value.
        for (float[] row: background) {
            Arrays.fill(row, this.backgroundSignal);
        }
        
        return background;
    }
}
