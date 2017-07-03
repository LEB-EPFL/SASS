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

import static java.lang.Math.sqrt;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;

/**
 *
 * @author stefko
 */
public class Fluorophore3D extends Fluorophore{

    protected final double z;
    
    public Fluorophore3D(Camera camera, double signal, StateSystem state_system, int start_state, double x, double y, double z) {
        super(camera, signal, state_system, start_state, x, y);
        this.z = z;
        final double sigma = camera.fwhm_digital / 2.3548;
        // radius cutoff
        final double r = 3 * sigma;
        this.pixel_list = get_pixels_within_radius(r, camera.fwhm_digital);
    }
    
    @Override
    protected double generate_signature_for_pixel(int x, int y, double camera_fwhm_digital) throws MathException {
        final double sigma = camera_fwhm_digital / 2.3548;
        final double denom = sqrt(2.0)*sigma;
        
        double z_factor = Math.exp(z);
        
        return 0.25 *(Erf.erf(z_factor*(x-this.x+0.5)/denom) - Erf.erf(z_factor*(x-this.x-0.5)/denom)) *
                     (Erf.erf((y-this.y+0.5)/denom/z_factor) - Erf.erf((y-this.y-0.5)/denom/z_factor));
    }
    
}
