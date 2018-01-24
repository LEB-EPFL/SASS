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

import ch.epfl.leb.sass.models.backgrounds.internal.commands.GenerateBackgroundFromFile;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ShortProcessor;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for generating a constant background from a .tif file.
 * 
 * @author Kyle M. Douglass
 */
public class GenerateBackgroundFromFileTest {
    /**
     * The file containing the background image.
     */
    private File backgroundFile;
    
    /**
     * A copy of the builder for this class's instances.
     */
    private GenerateBackgroundFromFile.Builder builder;
    
    /**
     * The expected result.
     */
    private float[][] expResult;
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
    public GenerateBackgroundFromFileTest() {
        builder = new GenerateBackgroundFromFile.Builder();
        builder.nX(3);
        builder.nY(3);
    }
    
    /**
     * Creates a test .tif file as an example background.
     */
    @Before
    public void setUp () throws IOException {
        // Create an array of pixels increasing from 0 to 8.
        int nX = 3; int nY = 3; int counter = 0;
        int[][] pixels = new int[nY][nX];
        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                pixels[y][x] = counter++;
            }
        }
        
        // Create an image from the array of pixels
        ShortProcessor sp = new ShortProcessor(nX, nY);
        sp.setIntArray(pixels);
        expResult = sp.getFloatArray();
        
        // Save the example image
        String filename = "Example-Background.tif";
        backgroundFile = tempDir.newFile(filename);
        ImagePlus imp = new ImagePlus("Example-Background", sp);
        IJ.save(imp, backgroundFile.getAbsolutePath());
        assertTrue(backgroundFile.exists());
    }

    /**
     * Test of generateBackground method, of class GenerateBackgroundFromFile.
     */
    @Test
    public void testGenerateBackground() {
        System.out.println("generateBackground");
        
        // Create the instance
        builder.file(backgroundFile);
        GenerateBackgroundFromFile instance = builder.build();
        
        float[][] result = instance.generateBackground();
        assertArrayEquals(expResult, result);
    }
    
}
