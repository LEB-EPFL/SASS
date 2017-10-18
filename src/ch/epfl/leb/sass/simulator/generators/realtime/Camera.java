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
package ch.epfl.leb.sass.simulator.generators.realtime;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Class containing camera configuration info and optics calculation functions.
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class Camera {

    /**
     * frame rate [frames/second]
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
     * Conversion factor between camera's analog-to-digital units (ADU) and electrons. [-]
     */
    public final double ADU_per_electron;
    
    /**
     * Electron multiplication (EM) gain of the camera.
     * This may be set to zero for sensors without EM gain, such as CMOS
     * sensors.
     */
    public final int EM_gain;

    /**
     * Camera pixel baseline (zero signal mean) [ADU]
     */
    public final int baseline;
    
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
     * noise in frame caused by dark current [electrons/frame/pixel]
     */
    public final double thermal_noise;

    /**
     * digital representation of the FWHM
     */
    public final double fwhm_digital;
    
    /**
     * horizontal image size [pixels]
     */
    public final int res_x;

    /**
     * vertical image size [pixels]
     */
    public final int res_y;
    
    /**
     * Initialize camera with parameters.
     * @param res_x horizontal resolution [pixels]
     * @param res_y vertical resolution [pixels]
     * @param acq_speed frame rate [frames/second]
     * @param readout_noise readout noise of camera [RMS]
     * @param dark_current dark current [electrons/second/pixel]
     * @param quantum_efficiency quantum efficiency [0.0-1.0]
     * @param ADU_per_electron conversion between camera units and electrons [-]
     * @param EM_gain electron multiplication gain [-]
     * @param baseline zero-signal average of pixel values [ADU]
     * @param pixel_size physical size of pixel [m]
     * @param NA numerical aperture [-]
     * @param wavelength light wavelength [m]
     * @param magnification magnification of camera [-]
     */
    public Camera(int res_x, int res_y, int acq_speed, double readout_noise,
            double dark_current, double quantum_efficiency,
            double ADU_per_electron, int EM_gain, int baseline,
            double pixel_size, double NA, double wavelength,
            double magnification) {
        this.res_x = res_x;
        this.res_y = res_y;
        this.acq_speed = acq_speed;
        this.readout_noise = readout_noise;
        this.dark_current = dark_current;
        this.quantum_efficiency = quantum_efficiency;
        this.ADU_per_electron = ADU_per_electron;
        this.EM_gain = EM_gain;
        this.baseline = baseline;
        this.pixel_size = pixel_size;
        this.NA = NA;
        this.wavelength = wavelength;
        this.magnification = magnification;
        
        this.thermal_noise = this.dark_current / this.acq_speed;
        
        // calculate Gaussian PSF
        double airy_psf_radius = 0.61*wavelength/NA;
        double airy_psf_radius_digital = airy_psf_radius * magnification;

        fwhm_digital = airy_psf_radius_digital / pixel_size;
    }
}
