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

import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.psfs.PSF;
import ch.epfl.leb.sass.models.psfs.internal.Gaussian2D;
import ch.epfl.leb.sass.models.Pixel;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Kyle M. Douglass
 */
public class Gaussian2DTest {
    private PSFBuilder builder;
    
    @Before
    public void setUp() {
        double fwhm = 3;
        Gaussian2D.Builder builder = new Gaussian2D.Builder();
        builder.FWHM(fwhm);
        this.builder = builder;
    }

    /**
     * Test of generatePixelSignature method, of class Gaussian2D.
     */
    @Test
    public void testGeneratePixelSignature() throws Exception {
        PSF psf;
        
        // True answer and precision
        double groundTruth = 0.0932;
        double delta = 0.0001;
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // an emitter located at the pixel's center.
        builder.eX(0).eY(0).eZ(0);
        psf = builder.build();
        double signature = psf.generatePixelSignature(0, 0);
        assertEquals(signature, groundTruth, delta);
        
        // Now test the signature for an emitter not centered on the pixel
        groundTruth = 0.0849;
        delta = 0.0001;
        
        // Find the relative probability of a photon hitting a pixel at (0,0)
        // an emitter located -.4 pixels from the pixel's center in x and y
        builder.eX(-0.4).eY(-0.4).eZ(0);
        psf = builder.build();
        signature = psf.generatePixelSignature(0, 0);
        assertEquals(signature, groundTruth, delta);
    }
    
    /**
     * Test of getSignature method, of class Gaussian2D.
     */
    @Test
    public void testGetSignature() {
        PSF psf;
        
        // Test point at (0,0)
        Point2D point = new Point2D.Double();
        
         // Ground-truth array
        ArrayList<Pixel> groundTruth = new ArrayList();
        groundTruth.add(new Pixel(-1,  0, 0.0696)); // (-1,  0)
        groundTruth.add(new Pixel( 0, -1, 0.0696)); // ( 0, -1)
        groundTruth.add(new Pixel( 0,  0, 0.0932)); // ( 0,  0)
        groundTruth.add(new Pixel( 0,  1, 0.0696)); // ( 0,  1)
        groundTruth.add(new Pixel( 1,  0, 0.0696)); // ( 1,  0)
        
        // Generate the test array whose signature values will be computed
        ArrayList<Pixel> pixels = new ArrayList();
        pixels.add(new Pixel(-1,  0, 0.0)); // (-1,  0)
        pixels.add(new Pixel( 0, -1, 0.0)); // ( 0, -1)
        pixels.add(new Pixel( 0,  0, 0.0)); // ( 0,  0)
        pixels.add(new Pixel( 0,  1, 0.0)); // ( 0,  1)
        pixels.add(new Pixel( 1,  0, 0.0)); // ( 1,  0)
        
        builder.eX(0).eY(0).eZ(0);
        psf = builder.build();
        
        // Critical test occurs here
        psf.generateSignature(pixels);
        
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
        PSF psf;
        
        builder.eX(0).eY(0).eZ(0);
        psf = builder.build();
        
        assertEquals(psf.getRadius(), 1.2740 * 3, 0.0001);
    }
}
