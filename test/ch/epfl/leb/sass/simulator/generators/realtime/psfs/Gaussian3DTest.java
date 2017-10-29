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

import ch.epfl.leb.sass.simulator.generators.realtime.Pixel;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author douglass
 */
public class Gaussian3DTest {
    
   private Gaussian3D gauss3d = null;
   private final double fwhm = 3;
   private final double numericalAperture = 1.3;
    
    @Before
    public void setUp() {
        Gaussian3D.Builder builder = new Gaussian3D.Builder();
        builder.FWHM(fwhm).NA(numericalAperture);
        this.gauss3d = builder.build();
    }

    /**
     * Test of generatePixelSignature method, of class Gaussian3D.
     */
    @Test
    public void testGeneratePixelSignatureInFocus() throws Exception {
        // True answer and precision
        double groundTruth = 0.0932;
        double delta = 0.0001;
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // an emitter located at the pixel's center.
        double signature = this.gauss3d.generatePixelSignature(0, 0, 0, 0, 0);
        assertEquals(signature, groundTruth, delta);
        
        // Now test the signature for an emitter not centered on the pixel
        groundTruth = 0.0849;
        delta = 0.0001;
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // from an emitter located -.4 pixels from the pixel's center in x and y
        signature = this.gauss3d.generatePixelSignature(0, 0, -0.4, -0.4, 0);
        assertEquals(signature, groundTruth, delta);
    }
    
    /**
     * Test of generatePixelSignature method, of class Gaussian3D.
     */
    @Test
    public void testGeneratePixelSignatureOutOfFocus() throws Exception {
        // True answer and precision
        double groundTruth = 0.0748;
        double delta = 0.0001;
        double z = 1; // units are pixels
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // from an emitter located at the pixel's center.
        double signature = this.gauss3d.generatePixelSignature(0, 0, 0, 0, z);
        assertEquals(signature, groundTruth, delta);
        
        // Now test the signature for an emitter not centered on the pixel
        groundTruth = 0.0693;
        delta = 0.0001;
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // an emitter located -.4 pixels from the pixel's center in x and y
        signature = this.gauss3d.generatePixelSignature(0, 0, -0.4, -0.4, z);
        assertEquals(signature, groundTruth, delta);
    }
    
    /**
     * Test of getSignature method, of class Gaussian2D.
     */
    @Test
    public void testGetSignatureInFocus() {
        // Test point at (0,0)
        Point2D point = new Point2D.Double();
        
        // Emitter is at z = 1.0
        double z = 1.0;
        
         // Ground-truth array
        ArrayList<Pixel> groundTruth = new ArrayList();
        groundTruth.add(new Pixel(-1,  0, 0.0591)); // (-1,  0)
        groundTruth.add(new Pixel( 0, -1, 0.0591)); // ( 0, -1)
        groundTruth.add(new Pixel( 0,  0, 0.0748)); // ( 0,  0)
        groundTruth.add(new Pixel( 0,  1, 0.0591)); // ( 0,  1)
        groundTruth.add(new Pixel( 1,  0, 0.0591)); // ( 1,  0)
        
        // Generate the test array whose signature values will be computed
        ArrayList<Pixel> pixels = new ArrayList();
        pixels.add(new Pixel(-1,  0, 0.0)); // (-1,  0)
        pixels.add(new Pixel( 0, -1, 0.0)); // ( 0, -1)
        pixels.add(new Pixel( 0,  0, 0.0)); // ( 0,  0)
        pixels.add(new Pixel( 0,  1, 0.0)); // ( 0,  1)
        pixels.add(new Pixel( 1,  0, 0.0)); // ( 1,  0)
        
        // Critical test occurs here
        this.gauss3d.generateSignature(pixels, 0, 0, z);
        
        // Verify that the signatures in the pixel array match the ground truth
        for (int ctr = 0; ctr < pixels.size(); ctr++)
        {
            assertEquals(groundTruth.get(ctr).getSignature(),
                    pixels.get(ctr).getSignature(),
                    0.0001);
        }
    }

    /**
     * Test of getRadius method, of class Gaussian2D.
     */
    @Test
    public void testGetRadius() {
        double actualRadius = 5 * 1.2740;
        assertEquals(this.gauss3d.getRadius(), actualRadius, 0.0001);
    }
    
    /**
     * Test of getFWHM method, of class Gaussian2D.
     */
    @Test
    public void testGetFWHM() {
        assertEquals(this.gauss3d.getFWHM(), 3.0, 0.0001);
    }

    /**
     * Test of setFWHM method, of class Gaussian2D.
     */
    @Test
    public void testSetFWHM() {
        this.gauss3d.setFWHM(5);
        assertEquals(5, this.gauss3d.getFWHM(), 0.0001);
    }
    
        /**
     * Test of getFWHM method, of class Gaussian2D.
     */
    @Test
    public void testGetNumericalAperture() {
        assertEquals(this.gauss3d.getNumericalAperture(), numericalAperture, 0.0001);
    }

    /**
     * Test of setFWHM method, of class Gaussian2D.
     */
    @Test
    public void testSetNumericalAperture() {
        this.gauss3d.setNumericalAperture(1.2);
        assertEquals(1.2, this.gauss3d.getNumericalAperture(), 0.0001);
    }
    
}
