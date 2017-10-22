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

/**
 * Computes an emitter PSF based on the Gibson-Lanni model.
 * 
 * @author Kyle M. Douglass
 */
public class GibsonLanniPSF { //TODO: impelements PSF
    
    /**
     * The number of rescaled Bessel functions to approximate the pupil.
     */
    private int numBasis = 100;
    
    /**
     * Number of samples along the pupil in the radial direction.
     */
    private int numSamples = 1000;
    
    /**
     * The upsampling ratio on the image space grid.
     */
    private int upsampling = 2;
    
    /**
     * Numerical aperture of the microscope.
     */
    private double NA = 1.4;
    
    /**
     * The wavelength of light [microns].
     */
    private double wavelength = 0.610;
    
    /**
     * The microscope magnification.
     */
    private double magnification = 100;
    
    /**
     * The refractive index of the sample.
     */
    private double ns = 1.33;
    
    /**
     * The design value for the coverslip refractive index.
     */
    private double ng0 = 1.5;
    
    /**
     * The actual value for the coverslip refractive index.
     */
    private double ng = 1.5;
    
    /**
     * The design value for the immersion medium refractive index.
     */
    private double ni0 = 1.5;
    
    /**
     * The actual value for the immersion medium refractive index.
     */
    private double ni = 1.5;
    
    /**
     * The design value for the immersion medium thickness [microns].
     */
    private double ti0 = 150;
    
    /**
     * The design value for the coverslip thickness [microns].
     */
    private double tg0 = 170;
    
    /**
     * The actual value for the coverslip thickness [microns].
     */
    private double tg = 170;
    
    /**
     * The pixel size in the lateral direction [microns].
     */
    private double resLateral = 0.1;
    
    /**
     * The pixel size in the axial direction [microns].
     */
    private double resAxial = 0.25;
    
    /**
     * The emitter distance from the coverslip [microns].
     */
    private double pZ = 0;
    
    /**
     * Determines the scaling factor for the basis Bessel functions [microns].
     * See Li, J., Xue, F., & Blu, T. (2017). JOSA A, 34(6), 1029-1034 for more
     * information.
     */
    public final double MINWAVELENGTH = 0.436;
    
    /**
     * The scaling factor for the basis Bessel functions.
     */
    private double scalingFactor;
    
    public static class Builder {
        
        // Properties of the Gibson-Lanni PSF model
        private int numBasis;
        private int numSamples;
        private int oversampling;
        private int sizeX;
        private int sizeY;
        private int sizeZ;
        private double NA;
        private double wavelength;
        private double magnification;
        private double ns;
        private double ng0;
        private double ng;
        private double ni0;
        private double ni;
        private double ti0;
        private double tg0;
        private double tg;
        private double resLateral;
        private double resAxial;
        private double pZ;
        
        public Builder numBasis(int numBasis) {
            this.numBasis = numBasis;
            return this;
        }
        public Builder numSamples(int numSamples) {
            this.numSamples = numSamples;
            return this;
        }
        public Builder sizeX(int sizeX) {this.sizeX = sizeX; return this;}
        public Builder sizeY(int sizeY) {this.sizeY = sizeY; return this;}
        public Builder sizeZ(int sizeZ) {this.sizeZ = sizeZ; return this;}
        public Builder NA(double NA) {this.NA = NA; return this;}
        public Builder wavelength(double wavelength) {
            this.wavelength = wavelength; return this;
        }
        public Builder magnification(double magnification) {
            this.magnification = magnification;
            return this;
        }
        public Builder ns(double ns) {this.ns = ns; return this;}
        public Builder ng0(double ng0) {this.ng0 = ng0; return this;}
        public Builder ng(double ng) {this.ng = ng; return this;}
        public Builder ni0(double ni0) {this.ni0 = ni0; return this;}
        public Builder ni(double ni) {this.ni = ni; return this;}
        public Builder ti0(double ti0) {this.ti0 = ti0; return this;}
        public Builder tg0(double tg0) {this.tg0 = tg0; return this;}
        public Builder tg(double tg) {this.tg = tg; return this;}
        public Builder resLateral(double resLateral) {
            this.resLateral = resLateral; return this;
        }
        public Builder resAxial(double resAxial) {
            this.resAxial = resAxial;
            return this;
        }
        public Builder pZ(double pZ) {this.pZ = pZ; return this;}
    }
}
