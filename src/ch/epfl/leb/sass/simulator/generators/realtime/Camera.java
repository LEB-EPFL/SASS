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
package ch.epfl.leb.sass.simulator.generators.realtime;

import static java.lang.Math.exp;

/**
 * Class containing camera configuration info and optics calculation functions.
 * @author Marcel Stefko
 */
public class Camera {

    /**
     * framerate [frames/second]
     */
    public final int acq_speed;

    /**
     * readout noise of camera [RMS]
     */
    public final double readout_noise;

    /**
     * dark current [electrons/second/pixel]
     */
    public final double dark_current;

    /**
     * quantum efficiency [0.0-1.0]
     */
    public final double quantum_efficiency;

    /**
     * gain [-]
     */
    public final double gain;

    /**
     * physical size of pixel [m]
     */
    public final double pixel_size;

    /**
     * numerical aperture [-]
     */
    public final double NA;

    /**
     * light wavelength [m]
     */
    public final double wavelength;

    /**
     * magnification of camera [-]
     */
    public final double magnification;

    /**
     * square root of absorption cross-section [m]
     */
    public final double radius;
    
    /**
     * noise in frame caused by dark current [electrons/frame/pixel]
     */
    public final double thermal_noise;

    /**
     * gain multiplied by quantum efficiency
     */
    public final double quantum_gain;

    /**
     * digital representation of the FWHM?
     */
    public final double fwhm_digital;
    
    /**
     * horizontal resolution [pixels]
     */
    public final int res_x;

    /**
     * vertical resolution [pixels]
     */
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

        fwhm_digital = airy_psf_radius_digital / pixel_size;
    }
}
