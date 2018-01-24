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

import ch.epfl.leb.sass.models.emitters.internal.Pixel;
import ch.epfl.leb.sass.models.emitters.internal.AbstractEmitter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author douglass
 */
public class EmitterTest {
    
    public EmitterTest() {
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
        pixels = AbstractEmitter.getPixelsWithinRadius(point, radius);
        assertEquals(5, pixels.size());
        
        // Verify that the positions in the pixel array match the ground truth
        for (int ctr = 0; ctr < pixels.size(); ctr++)
        {
            assertEquals(groundTruth.get(ctr).x, pixels.get(ctr).x);
            assertEquals(groundTruth.get(ctr).y, pixels.get(ctr).y);
        }
        
        // Verify that the method works with an even radius
        radius = 2.0;
        pixels = AbstractEmitter.getPixelsWithinRadius(point, radius);
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
        pixels = AbstractEmitter.getPixelsWithinRadius(point, radius);
        assertEquals(1, pixels.size());
        assertEquals(1, pixels.get(0).x);
        assertEquals(2, pixels.get(0).y);
    }
    
}
