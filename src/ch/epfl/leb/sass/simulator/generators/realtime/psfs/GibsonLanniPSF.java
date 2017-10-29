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
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.special.BesselJ;
import org.apache.commons.math3.analysis.interpolation.PiecewiseBicubicSplineInterpolatingFunction;
import org.apache.commons.math3.exception.OutOfRangeException;

import ij.ImageStack;
import ij.process.FloatProcessor;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Computes an emitter PSF based on the Gibson-Lanni model.
 * 
 * This algorithm was first described in Li, J., Xue, F., and Blu, T. (2017).
 * Fast and accurate three-dimensional point spread function computation for
 * fluorescence microscopy. JOSA A, 34(6), 1029-1034.
 * 
 * The code is adapted from MicroscPSF-ImageJ by Jizhou Li:
 * https://github.com/hijizhou/MicroscPSF-ImageJ
 * 
 * @author Kyle M. Douglass
 */
public final class GibsonLanniPSF implements PSF {
    
    /**
     * The number of rescaled Bessel functions to approximate the pupil.
     */
    private int numBasis = 100;
    
    /**
     * Number of samples along the pupil in the radial direction.
     */
    private int numSamples = 1000;
    
    /**
     * The oversampling ratio on the image space grid when computing the radial PSF component.
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
     * Numerical aperture of the microscope.
     */
    private double NA = 1.4;
    
    /**
     * The wavelength of light [microns].
     */
    private double wavelength = 0.610;
    
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
     * The lateral size of a pixel on the discrete PSF grid [microns].
     * 
     * This should be smaller than resLateral so that computation of the PSF
     * is performed on a finer scale than the camera pixels.
     */
    private double resPSF = 0.02;
    
    /**
     * The emitter's x-position [pixels].
     */
    private double eX = 0;
    
    /**
     * The emitter's y-position [pixels].
     */
    private double eY = 0;
    
    /**
     * The emitter distance from the coverslip [microns].
     */
    private double eZ = 0;
    
    /**
     * The displacement of the stage away from the surface of the coverslip.
     * 
     * Negative numbers correspond to moving the stage downwards, which, for an
     * inverted microscope, moves the focal plane upwards through the sample.
     */
    private double stageDisplacement = 0;
    
    /**
     * Determines the scaling factor for the basis Bessel functions [microns].
     * See Li, J., Xue, F., & Blu, T. (2017). JOSA A, 34(6), 1029-1034 for more
     * information.
     */
    private final double MINWAVELENGTH = 0.436;
    
    /**
     * The spline representation of the PSF.
     */
    private PiecewiseBicubicSplineInterpolatingFunction interpCDF;
    
    public static class Builder implements PSFBuilder {
        
        // Properties of the Gibson-Lanni PSF model
        private int numBasis;
        private int numSamples;
        private int oversampling;
        private int sizeX;
        private int sizeY;
        private double NA;
        private double wavelength;
        private double ns;
        private double ng0;
        private double ng;
        private double ni0;
        private double ni;
        private double ti0;
        private double tg0;
        private double tg;
        private double resLateral;
        private double resPSF;
        private double eX;
        private double eY;
        private double eZ;
        private double stageDisplacement;
        
        public Builder numBasis(int numBasis) {
            this.numBasis = numBasis;
            return this;
        }
        public Builder numSamples(int numSamples) {
            this.numSamples = numSamples;
            return this;
        }
        public Builder oversampling(int oversampling) {
            this.oversampling = oversampling;
            return this;
        }
        public Builder sizeX(int sizeX) {this.sizeX = sizeX; return this;}
        public Builder sizeY(int sizeY) {this.sizeY = sizeY; return this;}
        public Builder NA(double NA) {this.NA = NA; return this;}
        public Builder wavelength(double wavelength) {
            this.wavelength = wavelength; return this;
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
        public Builder resPSF(double resPSF) { 
            this.resPSF = resPSF;
            return this;
        }
        public Builder stageDisplacement(double stageDisplacement) {
            this.stageDisplacement = stageDisplacement; return this;
        }
        
        @Override
        public Builder eX(double eX) {this.eX = eX; return this;}
        
        @Override
        public Builder eY(double eY) {this.eY = eY; return this;}
        
        @Override
        public Builder eZ(double eZ) {this.eZ = eZ; return this;}
        
        @Override
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
        this.NA = builder.NA;
        this.wavelength = builder.wavelength;
        this.ns = builder.ns;
        this.ng0 = builder.ng0;
        this.ng = builder.ng;
        this.ni0 = builder.ni0;
        this.ni = builder.ni;
        this.ti0 = builder.ti0;
        this.tg0 = builder.tg0;
        this.tg = builder.tg;
        this.resLateral = builder.resLateral;
        this.resPSF = builder.resPSF;
        this.eX = builder.eX;
        this.eY = builder.eY;
        this.eZ = builder.eZ;
        this.stageDisplacement = builder.stageDisplacement;
        
        // Compute the signature for this PSF.
        this.computeDigitalPSF(this.stageDisplacement);
    }
    
    /**
     * Computes the relative probability of receiving a photon at pixel (pixelX, pixelY) from an emitter at
     * (emitterX, emitterY, emitterZ).
     * @param pixelX The pixel's x-position.
     * @param pixelY The pixel's y-position.
     * @return The probability of a photon hitting this pixel.
     */
    @Override
    public double generatePixelSignature(int pixelX, int pixelY) {
        double scalingFactor = this.resLateral;
        return this.interpCDF.value((pixelX - this.eX + 0.5) * scalingFactor,
                                    (pixelY - this.eY + 0.5) * scalingFactor) +
               this.interpCDF.value((pixelX - this.eX - 0.5) * scalingFactor,
                                    (pixelY - this.eY - 0.5) * scalingFactor) -
               this.interpCDF.value((pixelX - this.eX + 0.5) * scalingFactor,
                                    (pixelY - this.eY - 0.5) * scalingFactor) -
               this.interpCDF.value((pixelX - this.eX - 0.5) * scalingFactor,
                                    (pixelY - this.eY + 0.5) * scalingFactor);
    }
    
        /**
     * Generates the digital signature (the PSF) of the emitter on its nearby pixels.
     * 
     * @param pixels The list of pixels spanned by the emitter's image.
     */
    @Override
    public void generateSignature(ArrayList<Pixel> pixels) {
        double signature;
        
        this.computeDigitalPSF(this.stageDisplacement); // Compute the PSF
        for(Pixel pixel: pixels) {
            try {
                signature = this.generatePixelSignature(pixel.x, pixel.y);
            } catch (org.apache.commons.math3.exception.OutOfRangeException ex) {
                signature = 0.0;
                Logger.getLogger(GibsonLanniPSF.class.getName())
                      .log(Level.SEVERE, null, ex);
            }

            pixel.setSignature(signature);
        }
    }
    
    /**
     * Computes the half-width of the PSF for determining which pixels contribute to the emitter signal.
     * 
     * This number is based on the greatest horizontal or vertical extent of the
     * grid that the PSF is computed on.
     * 
     * @return The width of the PSF.
     */
    @Override
    public double getRadius() {
        double minSize = (double) Math.min(this.sizeX, this.sizeY) / 2;
        return this.resPSF / this.resLateral * minSize - 1;
    }
    
    /**
     * Computes a digital representation of the PSF.
     * 
     * @return An image stack of the PSF.
     **/
    private ImageStack computeDigitalPSF(double z) {
        double x0 = (this.sizeX - 1) / 2.0D;
        double y0 = (this.sizeY - 1) / 2.0D;

        double xp = x0;
        double yp = y0;

        ImageStack stack = new ImageStack(this.sizeX, this.sizeY);

        int maxRadius = (int) Math.round(Math.sqrt((this.sizeX - x0)
                        * (this.sizeX - x0) + (this.sizeY - y0) * (this.sizeY - y0))) + 1;
        double[] r = new double[maxRadius * this.oversampling];
        double[] h = new double[r.length];

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
		am = (3 * m + 1) * factor;
//                am = (3 * m + 1);
                for (int rhoi = 0; rhoi < this.numSamples; rhoi++) {
                        rho = rhoi * deltaRho;
                        Basis[rhoi][m] = bj0.value(am * rho);
                }
        }

        // compute the function to be approximated

        double ti = 0.0D;
        double OPD = 0;
        double W = 0;

        double[][] Coef = new double[1][this.numBasis * 2];
        double[][] Ffun = new double[this.numSamples][2];

        // Oil thickness.
        ti = (this.ti0 + z);
        double sqNA = this.NA * this.NA;
        double rhoNA2;
        for (int rhoi = 0; rhoi < this.numSamples; rhoi++) {
                rho = rhoi * deltaRho;
                rhoNA2 = rho * rho * sqNA;
                
                // OPD in the sample
                OPD = this.eZ * Math.sqrt(this.ns * this.ns - rhoNA2);
                
                // OPD in the immersion medium
                OPD += ti * Math.sqrt(this. ni * this.ni - rhoNA2) -
                       this.ti0 * Math.sqrt(this.ni0 * this.ni0 - rhoNA2);

                // OPD in the coverslip
                OPD += this.tg * Math.sqrt(this.ng * this.ng - rhoNA2) -
                       this.tg0 * Math.sqrt(this.ng0 * this.ng0 - rhoNA2);

                W = k0 * OPD;

                Ffun[rhoi][0] = Math.cos(W);
                Ffun[rhoi][1] = Math.sin(W);
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
                beta = k0 * this.NA * r[n] * this.resPSF;

                for (int m = 0; m < this.numBasis; m++) {
                        am = (3 * m + 1) * factor;
                        rm = am * bj1.value(am * b) * bj0.value(beta * b) * b;
                        rm = rm - beta * b * bj0.value(am * b) * bj1.value(beta * b);
                        RM[m][n] = rm / (am * am - beta * beta);

                }
        }

        // obtain one component
        double maxValue = 0.0D;

        for (int n = 0; n < r.length; n++) {
                double realh = 0.0D;
                double imgh = 0.0D;
                for (int m = 0; m < this.numBasis; m++) {
                        realh = realh + RM[m][n] * Coef[m][0];
                        imgh = imgh + RM[m][n] * Coef[m][1];

                }
                h[n] = realh * realh + imgh * imgh;
        }

        // Interpolate the PSF onto a 2D grid of physical coordinates
        double[] pixel = new double[this.sizeX * this.sizeY];

        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                // Distance in pixels from the center of the array
                double rPixel = Math.sqrt((x - xp) * (x - xp) + (y - yp)
                                * (y - yp));

                // Find the index of the PSF h array that matches this distance
                int index = (int) Math.floor(rPixel * this.oversampling);
                
                // Perform a linear interpolation from h onto the current point.
                // (1 / this.oversampling) is the distance between samples in
                // the h array in units of pixels in the final output array.
                double value = h[index]
                                + (h[(index + 1)] - h[index])
                                * (rPixel - r[index]) * this.oversampling;
                pixel[(x + this.sizeX * y)] = value;
            }
        }

        
        // Compute the constant that normalizes the PSF to its area
        double normConst = 0;
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                normConst += pixel[x + this.sizeX * y] * this.resPSF * this.resPSF;
            }
        }
        
        // Compute the (discrete) cumulative distribution function.
        // First, compute the sums in the y-direction.
        double[] CDF = new double[this.sizeX * this.sizeY];
        double sum = 0;
        for (int x = 0; x < this.sizeX; x++) {
            sum = 0;
            for (int y = 0; y < this.sizeY; y++) {
                // Normalize to the integrated area
                pixel[x + this.sizeX * y] /= normConst;
                
                // Increment the sum in the y-direction and the CDF
                sum += pixel[x + this.sizeX * y] * this.resPSF;
                CDF[x + this.sizeX * y] = sum;
            }
        }
        
        // Next, compute the sums in the x-direction.
        for (int y = 0; y < this.sizeY; y++) {
            sum = 0;
            for (int x = 0; x < this.sizeX; x++) {              
                // Increment the sum in the x-direction
                sum += CDF[x + y * this.sizeX] * this.resPSF;
                CDF[x + y * this.sizeX] = sum;
            }
        }        
        
        // Construct the x-coordinates
        double[] mgridX = new double[this.sizeX];
        for (int x = 0; x < this.sizeX; x++) {
            mgridX[x] = (x - 0.5 * (this.sizeX - 1)) * this.resPSF;
        }
        
        // Construct the y-coordinates
        double[] mgridY = new double[this.sizeY];
        for (int y = 0; y < this.sizeY; y++) {
            mgridY[y] = (y - 0.5 * (this.sizeY - 1)) * this.resPSF;
        }
        
        stack.addSlice(new FloatProcessor(this.sizeX, this.sizeY, pixel));
        stack.addSlice(new FloatProcessor(this.sizeX, this.sizeY, CDF));
        
        // Reshape CDF for interpolation
        double[][] rCDF = new double[this.sizeY][this.sizeX];
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                rCDF[y][x] = CDF[x + y * this.sizeX];
            }
        }
        
        // Compute the interpolating spline for this PSF.
        this.interpCDF = new PiecewiseBicubicSplineInterpolatingFunction(
                mgridX, mgridY, rCDF);
        
        return stack;
    }
}
