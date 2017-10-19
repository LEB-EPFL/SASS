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

import static java.lang.Math.sqrt;
import java.util.ArrayList;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;
import ch.epfl.leb.sass.simulator.generators.realtime.Emitter;
import ch.epfl.leb.sass.simulator.generators.realtime.Pixel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a digital representation of a two-dimensional Gaussian PSF.
 * 
 * @author Kyle M. Douglass
 */
public class Gaussian2D implements PSF {
    private double FWHM;
    
    /**
     * Creates an instance of the two-dimensional Gaussian PSF class with a given full width half maximum.
     * @param fwhm The full width half maximum of the Gaussian. [pixels]
     */
    public Gaussian2D(double fwhm) {
        this.FWHM = fwhm;
    }
    
    /**
     * Computes the relative probability of receiving a photon at pixel (pixelX, pixelY) from an emitter at
     * (emitterX, emitterY). The z-position of the emitter is ignored.
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
        final double sigma = this.FWHM / 2.3548;
        final double denom = sqrt(2.0)*sigma;
        return 0.25 *(Erf.erf((pixelX - emitterX + 0.5)/denom) - 
                      Erf.erf((pixelX - emitterX - 0.5)/denom)) *
                     (Erf.erf((pixelY - emitterY + 0.5)/denom) -
                      Erf.erf((pixelY - emitterY - 0.5)/denom));
    }
    
    /**
     * Generates the digital signature (the PSF) of the emitter its nearby pixels.
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
                Logger.getLogger(Gaussian2D.class.getName()).log(Level.SEVERE, null, ex);
            }
            pixel.setSignature(signature);
        }
    }
    
    /**
     * Computes the half-width of the PSF for determining which pixels contribute to the emitter signal.
     * 
     * For a 2D Gaussian, the effective width used here is three times the
     * standard deviation.
     * 
     * @return The width of the PSF.
     */
    @Override
    public double getRadius() {
        final double sigma = this.FWHM / 2.3548;
        // radius cutoff
        final double r = 3 * sigma;
        return r;
    }
    
    public double getFWHM() {
        return this.FWHM;
    }
    
    public void setFWHM(double fwhm) {
        this.FWHM = fwhm;
    }
    
}
