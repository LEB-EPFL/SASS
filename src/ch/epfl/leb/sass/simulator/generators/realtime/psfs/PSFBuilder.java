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
 * Defines the Builder interface for constructing PSFs.
 * 
 * Passing Builders instances, rather than PSF instances, to the simulation
 * allows the PSF to be constructed at different times during the simulation.
 * For example, one might set basic parameters like the wavelength in the
 * beginning of the simulation and set the emitter's z-position immediately
 * before a frame is computed. This means the simulation can dynamically create
 * new PSF instances in response to changing simulation parameters.
 * 
 * 
 * @author Kyle M. Douglass
 */
public interface PSFBuilder {
    /**
     * Builds a new instance of the PSF model.
     * 
     * @return The PSF model.
     */
    public PSF build();
    
    /**
     * Sets the emitter's x-position.
     * 
     * @param x The emitter's x-position. [pixels]
     */
    public PSFBuilder eX(double eX);
    
    /**
     * Sets the emitter's y-position.
     * 
     * @param y The emitter's y-position. [pixels]
     */
    public PSFBuilder eY(double eY);
    
    /**
     * Sets the emitter's z-position.
     * 
     * @param z The emitter's z-position. [pixels]
     */
    public PSFBuilder eZ(double eZ);
}