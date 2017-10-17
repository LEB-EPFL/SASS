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
package ch.epfl.leb.sass.simulator.generators.realtime;

import ch.epfl.leb.sass.simulator.generators.realtime.Pixel;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.geom.Point2D;

/**
 *
 * @author Kyle M. Douglass
 */
public class CameraTest {
    
    private Camera camera;
    
    public CameraTest() {
    }
    
    /**
     * Creates a test instance of the Camera class.
     */
    @Before
    public void setUp() {
        this.camera = new Camera(
            32,          //res_x
            32,          //res_y
            100,         //acq_speed, 
            1.6,         //readout_noise, 
            0.06,        //dark_current, 
            0.8,         //quantum_efficiency, 
            2.2,         // ADU_per_electron
            0,           // EM_gain
            100,         // baseline, ADU 
            6.45 * 1e-6, //pixel_size, 
            1.4,         //NA, 
            680 * 1e-9,  //wavelength, 
            60);         //magnification));
    }

    /**
     * Test of getPixelsWithinRadius method, of class Camera.
     * 
     * Tests that all pixels within a certain radius of the origin are correctly
     * returned.
     */
    @Test
    public void testGetPixelsWithinRadiusOfOrigin() {
        // Test point at (0,0)
        Point2D point = new Point2D.Double();
        
        // Ground-truth array
        ArrayList<Pixel> groundTruth = new ArrayList();
        groundTruth.add(new Pixel(-1,  0, 0.0));
        groundTruth.add(new Pixel( 0, -1, 0.0));
        groundTruth.add(new Pixel( 0,  0, 0.0));
        groundTruth.add(new Pixel( 0,  1, 0.0));
        groundTruth.add(new Pixel( 1,  0, 0.0));
        
        double radius = 1.0;
        ArrayList<Pixel> pixels;
        pixels = Camera.getPixelsWithinRadius(point, radius);
        assertEquals(5, pixels.size());
        
        // Verify that the positions in the pixel array match the ground truth
        for (int ctr = 0; ctr < pixels.size(); ctr++)
        {
            assertEquals(groundTruth.get(ctr).x, pixels.get(ctr).x);
            assertEquals(groundTruth.get(ctr).y, pixels.get(ctr).y);
        }
        
        // Verify that the method works with an even radius
        radius = 2.0;
        pixels = Camera.getPixelsWithinRadius(point, radius);
        assertEquals(13, pixels.size());
        
        
    }
    
    /**
     * Test of getPixelsWithinRadius method, of class Camera.
     * 
     * Tests that only the pixel containing the point is returned if the
     * radius is less than one.
     */
    @Test
    public void testGetPixelsWithinRadiusLessThanOne() {
        // Test point at (1.5,2)
        Point2D point = new Point2D.Double(1.5, 2);
        
        double radius = 0.95;
        ArrayList<Pixel> pixels;
        pixels = Camera.getPixelsWithinRadius(point, radius);
        assertEquals(1, pixels.size());
        assertEquals(1, pixels.get(0).x);
        assertEquals(2, pixels.get(0).y);
    }
    
}
