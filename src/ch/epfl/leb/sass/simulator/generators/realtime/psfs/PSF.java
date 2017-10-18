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

import java.util.ArrayList;
import org.apache.commons.math.MathException;
import ch.epfl.leb.sass.simulator.generators.realtime.Pixel;

/**
 * Interface that defines the behavior of a microscope point spread function.
 * 
 * @author Kyle M. Douglass
 */
public interface PSF {
    
    /**
     * Computes  the expected value for the PSF integrated over a pixel.
     * 
     * @param pixelX The pixel's x-position.
     * @param pixelY The pixel's y-position.
     * @param emitterX The emitter's x-position in fractions of a pixel.
     * @param emitterY The emitter's y-position in fractions of a pixel.
     * @param emitterZ The emitter's z-position in fractions of a pixel. This is ignored for 2D PSFs.
     * @return The relative probability of a photon hitting this pixel.
     * @throws org.apache.commons.math.MathException
     */
    public double generatePixelSignature(int pixelX, int pixelY, double emitterX,
                                    double emitterY, double emitterZ)
                  throws MathException;
    
    /**
     * Computes the digitized PSF across all pixels within the emitter's vicinity.
     * @param pixels The list of pixels spanned by the emitter's image.
     * @param emitterX The emitter's x-position [pixels]
     * @param emitterY The emitter's x-position [pixels]
     * @param emitterZ The emitter's x-position [pixels]
     */
    public void generateSignature(ArrayList<Pixel> pixels, double emitterX,
                              double emitterY, double emitterZ);
    
    /**
     * Returns the radius of the circle that fully encloses the PSF.
     * 
     * This value is used to determine how many pixels within the vicinity of
     * the emitter contribute to the PSF. It is necessary because many PSF
     * models extend to infinity in one or more directions.
     * 
     * @return The radius of the PSF in pixels.
     */
    public double getRadius();
    
}
