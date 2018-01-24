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
package ch.epfl.leb.sass.models.psfs.internal;

import static java.lang.Math.sqrt;
import java.util.ArrayList;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;
import ch.epfl.leb.sass.models.emitters.internal.Pixel;
import ch.epfl.leb.sass.models.psfs.PSF;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a digital representation of a two-dimensional Gaussian PSF.
 * 
 * @author Kyle M. Douglass
 */
public final class Gaussian2D implements PSF {
    /**
     * The FWHM of the in-focus Gaussian PSF. [pixels]
     */
    private double FWHM;
    
    /**
     * The emitter's x-position [microns].
     */
    private double eX = 0;
    
    /**
     * The emitter's y-position [microns].
     */
    private double eY = 0;
    
    /**
     * The emitter distance from the coverslip [microns].
     */
    private double eZ = 0;
    
    /**
     * The builder for constructing Gaussian2D instances.
     */
    public static class Builder implements PSFBuilder {
        
        // Properties of the 2D Gaussian PSF model
        private double FWHM;
        private double eX;
        private double eY;
        private double eZ;
        
        public Builder FWHM(double fwhm) {this.FWHM = fwhm; return this;}
        
        @Override
        public Builder eX(double eX) {this.eX = eX; return this;}
        
        @Override
        public Builder eY(double eY) {this.eY = eY; return this;}
        
        @Override
        public Builder eZ(double eZ) {this.eZ = eZ; return this;}
        
        @Override
        public Builder stageDisplacement(double stageDisplacement) {
            // This PSF does not depend on the stage displacement.
            return this;
        }
        
        @Override
        public Builder NA(double NA) {
            // This PSF does not depend on the objective NA.
            return this;
        }
        
        @Override
        public Builder wavelength(double wavelength) {
            // This PSF does not directly use the wavelength.
            return this;
        }
        @Override
        public Builder resLateral(double resLateral) {
            // This PSF does not directly use the object space pixel size.
            return this;
        }
        
        @Override
        public Gaussian2D build() {
            return new Gaussian2D(this);
        }
    }
    
    /**
     * Creates an instance of the two-dimensional Gaussian PSF class with a given full width half maximum.
     * 
     * Creation of the instance is possible only through the Builder.
     * 
     * @param builder A Gaussian2D.Builder for constructing the PSF.
     */
    private Gaussian2D(Builder builder) {
        this.FWHM = builder.FWHM;
        this.eX = builder.eX;
        this.eY = builder.eY;
        this.eZ = builder.eZ;
    }
    
    /**
     * Computes the relative probability of receiving a photon at the pixel.
     * (emitterX, emitterY). The z-position of the emitter is ignored.
     * @param pixelX The pixel's x-position.
     * @param pixelY The pixel's y-position.

     * @return The probability of a photon hitting this pixel.
     * @throws org.apache.commons.math.MathException
     */
    @Override
    public double generatePixelSignature(int pixelX, int pixelY)
            throws MathException {
        final double sigma = this.FWHM / 2.3548;
        final double denom = sqrt(2.0)*sigma;
        return 0.25 *(Erf.erf((pixelX - this.eX + 0.5)/denom) - 
                      Erf.erf((pixelX - this.eX - 0.5)/denom)) *
                     (Erf.erf((pixelY - this.eY + 0.5)/denom) -
                      Erf.erf((pixelY - this.eY - 0.5)/denom));
    }
    
    /**
     * Generates the digital signature of the emitter on its nearby pixels.
     * @param pixels The list of pixels spanned by the emitter's image.
     */
    public void generateSignature(ArrayList<Pixel> pixels) {
        double signature;
        for(Pixel pixel: pixels) {
            try {
                signature = this.generatePixelSignature(
                        pixel.x, pixel.y);
            } catch (MathException ex) {
                signature = 0.0;
                Logger.getLogger(
                        Gaussian2D.class.getName()).log(Level.SEVERE, null, ex);
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
