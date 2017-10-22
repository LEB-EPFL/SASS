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

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.special.BesselJ;

import ij.ImageStack;
import ij.process.FloatProcessor;
/**
 * Computes an emitter PSF based on the Gibson-Lanni model.
 * 
 * This algorithm first described in Li, J., Xue, F., & Blu, T. (2017). Fast and
 * accurate three-dimensional point spread function computation for fluorescence
 * microscopy. JOSA A, 34(6), 1029-1034.
 * 
 * The code is adapted from MicroscPSF-ImageJ by Jizhou Li:
 * https://github.com/hijizhou/MicroscPSF-ImageJ
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
     * The oversampling ratio on the image space grid.
     */
    private int oversampling = 2;
    
    /**
     * The size in x of the PSF array [pixels].
     */
    private int sizeX = 256;
    
    /**
     * The size in y of the PSF array [pixels].
     */
    private int sizeY = 256;
    
    /**
     * The size in z of the PSF array [pixels].
     */
    private int sizeZ = 128;
    
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
    private final double MINWAVELENGTH = 0.436;
    
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
        
        public GibsonLanniPSF build() {
            return new GibsonLanniPSF(this);
        }
    }
    
    /**
     * Private GibsonLanniPSF constructor forces creation through the Builder.
     * @param builder A Builder instance for constructing a Gibson-Lanni PSF.
     */
    private GibsonLanniPSF(Builder builder) {
        this.numBasis = builder.numBasis;
        this.numSamples = builder.numSamples;
        this.oversampling = builder.oversampling;
        this.sizeX = builder.sizeX;
        this.sizeY = builder.sizeY;
        this.sizeZ = builder.sizeZ;
        this.NA = builder.NA;
        this.wavelength = builder.wavelength;
        this.magnification = builder.magnification;
        this.ns = builder.ns;
        this.ng0 = builder.ng0;
        this.ng = builder.ng;
        this.ni0 = builder.ni0;
        this.ni = builder.ni;
        this.ti0 = builder.ti0;
        this.tg0 = builder.tg0;
        this.tg = builder.tg;
        this.resLateral = builder.resLateral;
        this.resAxial = builder.resAxial;
        this.pZ = builder.pZ;
    }
    
    /**
     * Computes a digital representation of the PSF.
     * @return An image stack of the PSF.
     **/
    private ImageStack computeDigitalPSF() {
        double x0 = (this.sizeX - 1) / 2.0D;
        double y0 = (this.sizeY - 1) / 2.0D;

        double xp = x0;
        double yp = y0;

        ImageStack stack = new ImageStack(this.sizeX, this.sizeY);

        int maxRadius = (int) Math.round(Math.sqrt((this.sizeX - x0)
                        * (this.sizeX - x0) + (this.sizeY - y0) * (this.sizeY - y0))) + 1;
        double[] r = new double[maxRadius * this.oversampling];
        double[][] h = new double[this.sizeZ][r.length];

        double a = 0.0D;
        double b = Math.min(1.0D, this.ns / this.NA);

        double k0 = 2 * Math.PI / this.wavelength;
        double factor1 = this.MINWAVELENGTH / this.wavelength;
        double factor = factor1 * this.NA / 1.4;
        double deltaRho = (b - a) / (this.numSamples - 1);

        // basis construction
        double rho = 0.0D;
        double am = 0.0;
        double[][] Basis = new double[this.numSamples][this.numBasis];

        BesselJ bj0 = new BesselJ(0);
        BesselJ bj1 = new BesselJ(1);

        for (int m = 0; m < this.numBasis; m++) {
//			am = (3 * m + 1) * factor;
                am = (3 * m + 1);
                for (int rhoi = 0; rhoi < this.numSamples; rhoi++) {
                        rho = rhoi * deltaRho;
                        Basis[rhoi][m] = bj0.value(am * rho);
                }
        }

        // compute the function to be approximated

        double ti = 0.0D;
        double OPD = 0;
        double W = 0;

        double[][] Coef = new double[this.sizeZ][this.numBasis * 2];
        double[][] Ffun = new double[this.numSamples][this.sizeZ * 2];

        for (int z = 0; z < this.sizeZ; z++) {
                ti = (this.ti0 + this.resAxial * (z - (this.sizeZ - 1.0D) / 2.0D));

                for (int rhoi = 0; rhoi < this.numSamples; rhoi++) {
                        rho = rhoi * deltaRho;
                        OPD = this.ns
                                        * this.pZ
                                        * Math.sqrt(1.0D - this.NA * rho / this.ns
                                                        * (this.NA * rho / this.ns));
                        OPD = OPD
                                        + this.ng
                                        * (this.tg - this.tg0)
                                        * Math.sqrt(1.0D - this.NA * rho / this.ng
                                                        * (this.NA * rho / this.ng));

                        OPD = OPD
                                        + this.ni
                                        * (ti - this.ti0)
                                        * Math.sqrt(1.0D - this.NA * rho / this.ni
                                                        * (this.NA * rho / this.ni));

                        W = k0 * OPD;

                        Ffun[rhoi][z] = Math.cos(W);
                        Ffun[rhoi][z + this.sizeZ] = Math.sin(W);
                }
        }

        // solve the linear system
        // begin....... (Using Common Math)

        RealMatrix coefficients = new Array2DRowRealMatrix(Basis, false);
        RealMatrix rhsFun = new Array2DRowRealMatrix(Ffun, false);
        DecompositionSolver solver = new SingularValueDecomposition(
                        coefficients).getSolver(); // slower but more accurate
        // DecompositionSolver solver = new
        // QRDecomposition(coefficients).getSolver(); // faster, less accurate

        RealMatrix solution = solver.solve(rhsFun);
        Coef = solution.getData();

        // end.......

        double[][] RM = new double[this.numBasis][r.length];
        double beta = 0.0D;

        double rm = 0.0D;
        for (int n = 0; n < r.length; n++) {
                r[n] = (n * 1.0 / this.oversampling);
                beta = k0 * this.NA * r[n] * this.resLateral;

                for (int m = 0; m < this.numBasis; m++) {
                        am = (3 * m + 1) * factor;
                        rm = am * bj1.value(am * b) * bj0.value(beta * b) * b;
                        rm = rm - beta * b * bj0.value(am * b) * bj1.value(beta * b);
                        RM[m][n] = rm / (am * am - beta * beta);

                }
        }

        // obtain one component
        double maxValue = 0.0D;
        for (int z = 0; z < this.sizeZ; z++) {
                for (int n = 0; n < r.length; n++) {
                        double realh = 0.0D;
                        double imgh = 0.0D;
                        for (int m = 0; m < this.numBasis; m++) {
                                realh = realh + RM[m][n] * Coef[m][z];
                                imgh = imgh + RM[m][n] * Coef[m][z + this.sizeZ];

                        }
                        h[z][n] = realh * realh + imgh * imgh;
                }
        }

        // assign

        double[][] pixel = new double[this.sizeZ][this.sizeX * this.sizeY];

        for (int z = 0; z < this.sizeZ; z++) {
                for (int x = 0; x < this.sizeX; x++) {
                        for (int y = 0; y < this.sizeY; y++) {
                                double rPixel = Math.sqrt((x - xp) * (x - xp) + (y - yp)
                                                * (y - yp));
                                int index = (int) Math.floor(rPixel * this.oversampling);

                                double value = h[z][index]
                                                + (h[z][(index + 1)] - h[z][index])
                                                * (rPixel - r[index]) * this.oversampling;
                                pixel[z][(x + this.sizeX * y)] = value;
                                if (value > maxValue) {
                                        maxValue = value;
                                }
                        }
                }
        }

        for (int z = 0; z < this.sizeZ; z++) {
                double[] slice = new double[this.sizeX * this.sizeY];

                for (int x = 0; x < this.sizeX; x++) {
                        for (int y = 0; y < this.sizeY; y++) {

                                double value = pixel[z][(x + this.sizeX * y)] / maxValue;
                                slice[(x + this.sizeX * y)] = value;
                        }
                }
                stack.addSlice(new FloatProcessor(this.sizeX, this.sizeY, slice));
        }
        
        return stack;
    }
}
