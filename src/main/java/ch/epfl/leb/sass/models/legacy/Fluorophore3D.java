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
package ch.epfl.leb.sass.models.legacy;

import ch.epfl.leb.sass.models.fluorophores.internal.DefaultFluorophore;
import ch.epfl.leb.sass.models.photophysics.StateSystem;
import ch.epfl.leb.sass.models.legacy.Camera;
import static java.lang.Math.sqrt;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;

/**
 * 3D version of the fluorophore. It adds a third coordinate,
 * and the fluorophore's PSF changes depending on its z-position.
 * @author Marcel Stefko
 */
public class Fluorophore3D extends DefaultFluorophore{

    protected final double z;
    
    public Fluorophore3D(Camera camera, double signal, StateSystem state_system, int start_state, double x, double y, double z) {
        super(camera, signal, state_system, start_state, x, y);
        // in the super() method the 2D PSF is calculated
        this.z = z;
        
        // we need to force recalculation of the PSF
        // here we estimate the radius cutoff around the fluorophore
        // (pixels beyond this radius will not be updated)
        final double sigma = camera.fwhm_digital / 2.3548;
        // radius cutoff, you can change the hardcoded constant if you want
        // a wider shape
        final double r = 3 * sigma;
        
        // this recalculates the PSF shape according to the overriden
        // generate_signature_for_pixel() method below
        this.pixel_list = get_pixels_within_radius(r, camera.fwhm_digital);
    }
    
    /**
     * This function should implement the new PSF shape for the 3D fluorophore
     * @param x x-position of camera pixel
     * @param y y-position of camera pixel
     * @param camera_fwhm_digital fwhm of gaussian PSF
     * @return normalized value of pixel brightness (ie how bright is this particular
     * pixel due to emission from the relevant fluorophore)
     * @throws MathException 
     */
    @Override
    protected double generate_signature_for_pixel(int x, int y, double camera_fwhm_digital) throws MathException {
        final double sigma = camera_fwhm_digital / 2.3548;
        final double denom = sqrt(2.0)*sigma;
        
        // this is just an example of a z-dependent PSF, it is not the physically
        // accurate normalized PSF, I just made one up so it could be demonstrated
        double z_factor = Math.exp(z);
        
        return 0.25 *(Erf.erf(z_factor*(x-this.x+0.5)/denom) - Erf.erf(z_factor*(x-this.x-0.5)/denom)) *
                     (Erf.erf((y-this.y+0.5)/denom/z_factor) - Erf.erf((y-this.y-0.5)/denom/z_factor));
    }
    
}
