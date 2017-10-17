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
package ch.epfl.leb.sass.simulator.generators.realtime.psfs;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Kyle M. Douglass
 */
public class Gaussian2DTest {
    private Gaussian2D gauss2d = null;
    
    @Before
    public void setUp() {
        double fwhm = 3;
        this.gauss2d = new Gaussian2D(fwhm);
    }

    /**
     * Test of generatePixelSignature method, of class Gaussian2D.
     */
    @Test
    public void testGeneratePixelSignature() throws Exception {
        // True answer and precision
        double groundTruth = 0.0932;
        double delta = 0.0001;
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // an emitter located at the pixel's center.
        double signature = this.gauss2d.generatePixelSignature(0, 0, 0, 0, 0);
        assertEquals(signature, groundTruth, delta);
        
        // Now test the signature for an emitter not centered on the pixel
        groundTruth = 0.0849;
        delta = 0.0001;
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // an emitter located -.4 pixels from the pixel's center in x and y
        signature = this.gauss2d.generatePixelSignature(0, 0, -0.4, -0.4, 0);
        assertEquals(signature, groundTruth, delta);
        
    }

    /**
     * Test of getFWHM method, of class Gaussian2D.
     */
    @Test
    public void testGetFWHM() {
        assertEquals(this.gauss2d.getFWHM(), 3.0, 0.0001);
    }

    /**
     * Test of setFWHM method, of class Gaussian2D.
     */
    @Test
    public void testSetFWHM() {
        this.gauss2d.setFWHM(5);
        assertEquals(5, this.gauss2d.getFWHM(), 0.0001);
    }
    
}
