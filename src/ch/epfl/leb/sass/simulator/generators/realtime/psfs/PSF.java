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

import org.apache.commons.math.MathException;

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
     * @param emitterX The emitter's x-position.
     * @param emitterY The emitter's y-position.
     * @param emitterZ The emitter's z-position. This is ignored for 2D PSFs.
     * @return The relative probability of a photon hitting this pixel.
     * @throws org.apache.commons.math.MathException
     */
    public double generatePixelSignature(int pixelX, int pixelY, double emitterX,
                                    double emitterY, double emitterZ)
                  throws MathException;
    
}
