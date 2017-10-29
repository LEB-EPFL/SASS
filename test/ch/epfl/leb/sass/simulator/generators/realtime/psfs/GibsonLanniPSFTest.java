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
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

/**
 *  Tests for the GibsonLanniPSF class.
 * 
 * @author Kyle M. Douglass
 */
public class GibsonLanniPSFTest {
    private GibsonLanniPSF.Builder builder;
    
    public GibsonLanniPSFTest() {
    }
    
    @Before
    public void setUp() {
        GibsonLanniPSF.Builder builder = new GibsonLanniPSF.Builder();
        
        // Build the PSF
        int numBasis = 100;
        int numSamples = 1000;
        int oversampling = 2;
        int sizeX = 256;
        int sizeY = 256;
        double NA = 1.4;
        double wavelength = 0.610;
        double ns  = 1.33;
        double ng0 = 1.5;
        double ng  = 1.5;
        double ni0 = 1.5;
        double ni  = 1.5;
        double ti0 = 150;
        double tg0 = 170;
        double tg  = 170;
        double resLateral = 0.1;
        double resPSF = 0.02;
        double stageDisplacement = -2;
                
        builder.numBasis(numBasis).numSamples(numSamples).sizeX(sizeX)
                .sizeY(sizeY).NA(NA).wavelength(wavelength).ns(ns).ng0(ng0)
                .ng(ng).ni0(ni0).ni(ni).ti0(ti0).tg0(tg0).tg(tg)
                .resLateral(resLateral).oversampling(oversampling)
                .resPSF(resPSF).stageDisplacement(stageDisplacement);
	
        this.builder = builder;
    }

    /**
     * Test of generatePixelSignature method, of class GibsonLanniPSF.
     */
    @Test
    public void testGeneratePixelSignature() {
        GibsonLanniPSF psf;

        // True answers and precision
        double groundTruth = 0.02800;
        double delta = 0.0001;

        // Find the relative probability of a photon hitting a pixel at (0,0)
        // an emitter located one pixel away in y.=.
        this.builder.eX(0).eY(-1).eZ(2);
        psf = this.builder.build();
        double signature = psf.generatePixelSignature(0, 0);
        assertEquals(groundTruth, signature, delta);
        
        groundTruth = 0.03726;
        this.builder.eX(1).eY(1).eZ(2);
        psf = this.builder.build();
        signature = psf.generatePixelSignature(1, 1);
        assertEquals(groundTruth, signature, delta);
    }

    /**
     * Test of generateSignature method, of class GibsonLanniPSF.
     */
    @Test
    public void testGenerateSignature() {
        PSF psf;

        // Precision
        double delta = 0.0001;

        // Test point at (0,0)
        Point2D point = new Point2D.Double();
        
         // Ground-truth array
         // Note that the ground truth is not symmetric about the center pixel
         // because of the finite sampling of the CDF; it becomes more symmetric
         // the smaller resPSF is and the larger sizeX/Y are.
        ArrayList<Pixel> groundTruth = new ArrayList();
        groundTruth.add(new Pixel( 1,  1, 0.03726)); // ( 1,  1)
        groundTruth.add(new Pixel( 2,  1, 0.02800)); // ( 2,  1)
        groundTruth.add(new Pixel( 0,  1, 0.03078)); // ( 0,  1)
        groundTruth.add(new Pixel( 1,  2, 0.02800)); // ( 1,  2)
        groundTruth.add(new Pixel( 1,  0, 0.03078)); // ( 1,  0)
        
        // Generate the test array whose signature values will be computed
        ArrayList<Pixel> pixels = new ArrayList();
        pixels.add(new Pixel( 1,  1, 0.0)); // ( 1,  1)
        pixels.add(new Pixel( 2,  1, 0.0)); // ( 2,  1)
        pixels.add(new Pixel( 0,  1, 0.0)); // ( 0,  1)
        pixels.add(new Pixel( 1,  2, 0.0)); // ( 1,  2)
        pixels.add(new Pixel( 1,  0, 0.0)); // ( 1,  0)
        
        this.builder.eX(1).eY(1).eZ(2);
        psf = builder.build();
        
        // Critical test occurs here
        psf.generateSignature(pixels);
        
        // Verify that the signatures in the pixel array match the ground truth
        for (int ctr = 0; ctr < pixels.size(); ctr++)
        {
            assertEquals(groundTruth.get(ctr).getSignature(),
                    pixels.get(ctr).getSignature(),
                    delta);
        }
    }

    /**
     * Test of getRadius method, of class GibsonLanniPSF.
     */
    @Test
    public void testGetRadius() {
        PSF psf;
        this.builder.eZ(2);
        psf = builder.build();
        assertEquals(psf.getRadius(), 24.6, 0.1);
    }
    
}
