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
package ch.epfl.leb.sass.simulator.generators.realtime.backgrounds.commands;

import ch.epfl.leb.sass.models.backgrounds.commands.GenerateRandomBackground;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kyle M. Douglass
 */
public class GenerateRandomBackgroundTest {
    
    /**
     * A copy of the builder for this class's instances.
     */
    private GenerateRandomBackground.Builder builder;
    
    public GenerateRandomBackgroundTest() {
        builder = new GenerateRandomBackground.Builder();
        builder.featureSize(10);
        builder.seed(0);
    }
    
    /**
     * Test of generateBackground method, of class GenerateRandomBackground.
     */
    @Test
    public void testGenerateBackground() {
        builder.nX(64);
        builder.nY(64);
        builder.max(250);
        builder.min(100);
        float delta = 0.001f;
        
        GenerateRandomBackground bg = builder.build();
        float[][] pixels = new float[64][64];
        pixels = bg.generateBackground();
        
        // Find max and min values in the background
        float maxValue = Float.NEGATIVE_INFINITY;
        float minValue = Float.POSITIVE_INFINITY;
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                if (maxValue < pixels[y][x])
                    maxValue = pixels[y][x];
                
                if (minValue > pixels[y][x])
                    minValue = pixels[y][x];
            }
        }
        
        assertTrue("Hello", minValue >= 100 - delta);
        assertTrue("Goodbye", maxValue <= 250 + delta);
    }    
}
