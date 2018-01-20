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

import ij.ImageStack;
import ij.ImagePlus;
import ij.io.FileSaver;

/**
 *
 * Demonstrates how to create a Gibson-Lanni PSF.
 * 
 * @author Kyle M. Douglass
 */
public class ProfileGibsonLanniPSF {
    
    public static void main(String args[]) {
        
        int numBasis = 100;
        int numSamples = 1000;
        int oversampling = 2;
        int sizeX = 1024;
        int sizeY = 1024;
        double NA = 1.4;
        double wavelength = 0.610;
        double ns = 1.33;
        double ng0 = 1.5;
        double ng = 1.5;
        double ni0 = 1.5;
        double ni = 1.5;
        double ti0 = 150;
        double tg0 = 170;
        double tg = 170;
        double resLateral = 0.1;
        double resPSF = 0.02;
        double resPSFAxial = 0.005;
        double stageDisplacement = -2;
        String solver = "qrd";
        
        GibsonLanniPSF.Builder builder = new GibsonLanniPSF.Builder();
        builder.numBasis(numBasis).numSamples(numSamples).sizeX(sizeX)
                .sizeY(sizeY).NA(NA).wavelength(wavelength).ns(ns).ng0(ng0)
                .ng(ng).ni0(ni0).ni(ni).ti0(ti0).tg0(tg0).tg(tg)
                .resLateral(resLateral).oversampling(oversampling)
                .resPSF(resPSF).stageDisplacement(stageDisplacement)
                .solver(solver).resPSFAxial(resPSFAxial);
		
        GibsonLanniPSF psf = builder.build();
        
        // Create the pixel signature
        //builder.eX(0).eY(-1).eZ(2);
        //psf = builder.build();
        //double signature = psf.generatePixelSignature(0, 0);
        
        //ImageStack stack = psf.computeDigitalPSF(-5);
        //ImagePlus imp = new ImagePlus("Gibson-Lanni PSF", stack);
        //imp.show();
        //FileSaver saver = new FileSaver(imp);
        //saver.saveAsTiff("/home/douglass/Desktop/psf.tif");
	}
    
}
