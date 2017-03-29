/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 *
 * Author: Marcel Stefko
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
package algorithm_tester.generators.realtimegenerator;

import static java.lang.Math.exp;

/**
 * Class containing camera configuration info and optics calculation functions.
 * @author Marcel Stefko
 */
public class Camera {
    public final int acq_speed;
    public final double readout_noise;
    public final double dark_current;
    public final double quantum_efficiency;
    public final double gain;
    public final double pixel_size;
    public final double NA;
    public final double wavelength;
    public final double magnification;
    public final double radius;
    
    public final double thermal_noise;
    public final double quantum_gain;
    
    public final double[][] psf;
    public final double[][] psf_digital;
    public final double fwhm;
    public final double fwhm_digital;
    
    public final int res_x;
    public final int res_y;
    
    /**
     * Initialize camera with parameters.
     * @param res_x horizontal resolution [pixels]
     * @param res_y vertical resolution [pixels]
     * @param acq_speed framerate [frames/second]
     * @param readout_noise readout noise of camera [RMS]
     * @param dark_current dark current [electrons/second/pixel]
     * @param quantum_efficiency quantum efficiency [0.0-1.0]
     * @param gain gain [-]
     * @param pixel_size physical size of pixel [m]
     * @param NA numerical aperture [-]
     * @param wavelength light wavelength [m]
     * @param magnification magnification of camera [-]
     * @param radius square root of absorption cross-section [m]
     */
    public Camera(int res_x, int res_y, int acq_speed, double readout_noise, double dark_current,
            double quantum_efficiency, double gain, double pixel_size, double NA,
            double wavelength, double magnification, double radius) {
        this.res_x = res_x;
        this.res_y = res_y;
        this.acq_speed = acq_speed;
        this.readout_noise = readout_noise;
        this.dark_current = dark_current;
        this.quantum_efficiency = quantum_efficiency;
        this.gain = gain;
        this.pixel_size = pixel_size;
        this.NA = NA;
        this.wavelength = wavelength;
        this.magnification = magnification;
        this.radius = radius;
        
        this.thermal_noise = this.dark_current / this.acq_speed;
        this.quantum_gain = this.quantum_efficiency * this.gain;
        
        // calculate Gaussian PSF
        double airy_psf_radius = 0.61*wavelength/NA;
        double airy_psf_radius_digital = airy_psf_radius * magnification;
        
        fwhm = airy_psf_radius / radius;
        fwhm_digital = airy_psf_radius_digital / pixel_size;
        
        int psfSize = (2 * (int)fwhm_digital) + 1;
        int psfSize_digital = psfSize;
        
        psf_digital = generateGaussianMatrix(psfSize_digital, fwhm_digital/2.3548);
        double[][] psf_temp = generateGaussianMatrix(psfSize, fwhm/2.3548);
        psf = multiplyBy(psf_temp, getMaximum(psf_digital)/getMaximum(psf_temp));
    }
    
    /**
     * Generate a Gaussian low-pass filter matrix (psf simulation)
     * @param size size of the matrix (odd integer)
     * @param sigma sigma of the 2D Gaussian curve
     */
    private double[][] generateGaussianMatrix(int size, double sigma) {
        double[][] result = new double[size][size];
        if (size%2 != 1)
            throw new IllegalArgumentException("Gaussian matrix size must be an odd integer!");
        int mid = (size - 1)/2;
        for (int step_x = 0; step_x <= mid; step_x++) {
            for (int step_y = 0; step_y <= mid; step_y++) {
                double val = exp( -(step_x*step_x + step_y*step_y)/(2*sigma*sigma));
                
                result[mid + step_x][mid + step_y] = val;
                result[mid + step_x][mid - step_y] = val;
                result[mid - step_x][mid + step_y] = val;
                result[mid - step_x][mid - step_y] = val;
            }
        }
        return result;
    }
    
    /**
     * Return maximal value from a 2D array
     * @param arr array to be analyzed
     */
    private double getMaximum(double[][] arr) {
        double max = 0.0;
        for (double[] row: arr) {
            for (double val: row) {
                if (val > max)
                    max = val;
            }
        }
        return max;
    }
    
    /**
     * Multiply an array by a constant
     * @param arr array to be multiplied
     * @return resulting array
     */
    private double[][] multiplyBy(double[][] grid, double factor) {
        double[][] result = new double[grid.length][grid[0].length];
        for (int row=0; row < grid.length; row++) {
            for (int col=0; col < grid[row].length; col++) {
                double val = grid[row][col] * factor;
                result[row][col] = val;
            }
        }
        return result;
    }
}
