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
 *  Tests for the GibsonLanniPSF class.
 * 
 * @author Kyle M. Douglass
 */
public class GibsonLanniPSFTest {
    private GibsonLanniPSF psf = null;
    
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
        double pZ = 2;
        double stageDisplacement = -2;
                
        builder.numBasis(numBasis).numSamples(numSamples).sizeX(sizeX)
                .sizeY(sizeY).NA(NA).wavelength(wavelength).ns(ns).ng0(ng0)
                .ng(ng).ni0(ni0).ni(ni).ti0(ti0).tg0(tg0).tg(tg)
                .resLateral(resLateral).oversampling(oversampling)
                .resPSF(resPSF).stageDisplacement(stageDisplacement).pZ(pZ);
		
        this.psf = builder.build();
    }

    /**
     * Test of generatePixelSignature method, of class GibsonLanniPSF.
     */
    @Test
    public void testGeneratePixelSignature() {
    }

    /**
     * Test of generateSignature method, of class GibsonLanniPSF.
     */
    @Test
    public void testGenerateSignature() {
    }

    /**
     * Test of getRadius method, of class GibsonLanniPSF.
     */
    @Test
    public void testGetRadius() {
    }

    /**
     * Test of computeDigitalPSF method, of class GibsonLanniPSF.
     */
    @Test
    public void testComputeDigitalPSF() {
    }
    
}
