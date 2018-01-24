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

import ch.epfl.leb.sass.models.emitters.internal.Pixel;
import ch.epfl.leb.sass.models.psfs.PSF;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
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
public final class Gaussian3D implements PSF {
    
    /**
     * The FWHM of the in-focus Gaussian PSF. [pixels]
     */
    private double FWHM;
    
    /**
     * The numerical aperture of the microscope.
     * 
     * This determines the Rayleigh range of the of the Gaussian PSF.
     */
    private double numericalAperture;
    
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
     * The axial displacement of the sample stage.
     */
    private double stageDisplacement = 0;
    
    /**
     * The builder for constructing Gaussian2D instances.
     */
    public static class Builder implements PSFBuilder {
        
        // Properties of the 3D Gaussian PSF model
        private double FWHM;
        private double numericalAperture;
        private double stageDisplacement;
        private double eX;
        private double eY;
        private double eZ;
        
        public Builder FWHM(double fwhm) {this.FWHM = fwhm; return this;}
        public Builder NA(double NA) {this.numericalAperture = NA; return this;}
        
        @Override
        public Builder eX(double eX) {this.eX = eX; return this;}
        
        @Override
        public Builder eY(double eY) {this.eY = eY; return this;}
        
        @Override
        public Builder eZ(double eZ) {this.eZ = eZ; return this;}
        
        @Override
        public Builder stageDisplacement(double stageDisplacement) {
            this.stageDisplacement = stageDisplacement;
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
        public Gaussian3D build() {
            return new Gaussian3D(this);
        }
    }
    
    /**
     * Creates an instance of the two-dimensional Gaussian PSF class with a given full width half maximum.
     * 
     * Creation of the instance is possible only through the Builder.
     * 
     * @param builder A Gaussian3D.Builder for constructing the PSF.
     */
    private Gaussian3D(Builder builder) {
        this.FWHM = builder.FWHM;
        this.numericalAperture = builder.numericalAperture;
        this.stageDisplacement = builder.stageDisplacement;
        this.eX = builder.eX;
        this.eY = builder.eY;
        this.eZ = builder.eZ;
    }
    
    /**
     * Computes the relative probability of receiving a photon at the pixel. 
     * @param pixelX The pixel's x-position.
     * @param pixelY The pixel's y-position.
     * @return The probability of a photon hitting this pixel.
     * @throws org.apache.commons.math.MathException
     */
    @Override
    public double generatePixelSignature(int pixelX, int pixelY)
            throws MathException {
        final double sigma_0 = this.FWHM / 2.3548;
        final double zR = 2 * sigma_0 / this.numericalAperture; // Rayleigh range
        
        // Add the offset from the stage's position to the emitter's z-values
        double z;
        z = this.eZ + this.stageDisplacement;
        
        double sigma = sigma_0 * sqrt(1 + (z/ zR) * (z / zR));
        double denom = sqrt(2.0)*sigma;
        return 0.25 *(Erf.erf((pixelX - this.eX + 0.5)/denom) - 
                      Erf.erf((pixelX - this.eX - 0.5)/denom)) *
                     (Erf.erf((pixelY - this.eY + 0.5)/denom) -
                      Erf.erf((pixelY - this.eY - 0.5)/denom));
    }
    
    /**
     * Generates the digital signature of the emitter on its nearby pixels.
     * 
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
