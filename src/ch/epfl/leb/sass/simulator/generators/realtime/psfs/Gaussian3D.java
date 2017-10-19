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
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;

/**
 * Generates a digital representation of a three-dimensional Gaussian PSF.
 * 
 * In this simple but unphysical model, the variance of the Gaussian PSF from
 * an emitter at a distance z from the focal plane scales linearly with the
 * amount of defocus.
 * 
 * @author Kyle M. Douglass
 */
public class Gaussian3D implements PSF {
    
    /**
     * The FWHM of the in-focus Gaussian PSF.
     */
    private double FWHM;
    
    /**
     * The numerical aperture of the microscope.
     * 
     * This determines the Rayleigh range of the of the Gaussian PSF.
     */
    private double numericalAperture;
    
    /**
     * Creates an instance of the two-dimensional Gaussian PSF class with a given full width half maximum.
     * @param fwhm The full width half maximum of the Gaussian at its waist.
     *        [pixels]
     */
    public Gaussian3D(double fwhm, double numericalAperture) {
        this.FWHM = fwhm;
        this.numericalAperture = numericalAperture;
    }
    
    /**
     * Computes the relative probability of receiving a photon at pixel (pixelX, pixelY) from an emitter at
     * (emitterX, emitterY, emitterZ).
     * @param pixelX The pixel's x-position.
     * @param pixelY The pixel's y-position.
     * @param emitterX The emitter's x-position in fractions of a pixel.
     * @param emitterY The emitter's y-position in fractions of a pixel.
     * @param emitterZ The emitter's z-position in fractions of a pixel. This is ignored.
     * @return The relative probability of a photon hitting this pixel.
     * @throws org.apache.commons.math.MathException
     */
    @Override
    public double generatePixelSignature(
            int pixelX, 
            int pixelY,
            double emitterX,
            double emitterY,
            double emitterZ
    ) throws MathException {
        final double sigma_0 = this.FWHM / 2.3548;
        final double zR = 2 * sigma_0 / this.numericalAperture; // Rayleigh range
        double sigma = sigma_0 * sqrt(1 + (emitterZ / zR) * (emitterZ / zR));
        double denom = sqrt(2.0)*sigma;
        return 0.25 *(Erf.erf((pixelX - emitterX + 0.5)/denom) - 
                      Erf.erf((pixelX - emitterX - 0.5)/denom)) *
                     (Erf.erf((pixelY - emitterY + 0.5)/denom) -
                      Erf.erf((pixelY - emitterY - 0.5)/denom));
    }
    
    /**
     * Generates the digital signature (the PSF) of the emitter on its nearby pixels.
     * 
     * @param pixels The list of pixels spanned by the emitter's image.
     * @param emitterX The emitter's x-position [pixels]
     * @param emitterY The emitter's x-position [pixels]
     * @param emitterZ The emitter's x-position [pixels]
     */
    public void generateSignature(ArrayList<Pixel> pixels, double emitterX,
                              double emitterY, double emitterZ) {
        double signature;
        for(Pixel pixel: pixels) {
            try {
                signature = this.generatePixelSignature(
                        pixel.x, pixel.y, emitterX, emitterY, emitterZ);
            } catch (MathException ex) {
                signature = 0.0;
                Logger.getLogger(Gaussian3D.class.getName()).log(Level.SEVERE, null, ex);
            }
            pixel.setSignature(signature);
        }
    }
    
    /**
     * Computes the half-width of the PSF for determining which pixels contribute to the emitter signal.
     * 
     * The effective width used here is five times the standard deviation when
     * the emitter is exactly in focus. The larger factor of five accounts for
     * the larger lateral PSF size when it is out of focus.
     * 
     * @return The width of the PSF.
     */
    @Override
    public double getRadius() {
        final double sigma = this.FWHM / 2.3548;
        // radius cutoff
        final double r = 5 * sigma;
        return r;
    }
    
    public double getFWHM() {
        return this.FWHM;
    }
    
    public void setFWHM(double fwhm) {
        this.FWHM = fwhm;
    }
    
    public double getNumericalAperture() {
        return this.numericalAperture;
    }
    
    public void setNumericalAperture(double numericalAperture) {
        this.numericalAperture = numericalAperture;
    }
}
