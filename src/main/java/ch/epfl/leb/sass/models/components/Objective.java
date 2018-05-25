/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne, Switzerland
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.models.components;

import ch.epfl.leb.sass.models.Model;

/**
 * Methods common to all microscope objectives.
 * 
 * @author Kyle M. Douglass
 */
public interface Objective extends Model {
    
    /**
     * Computes the full width at half maximum of the Airy disk.
     * 
     * Units are the same as those of wavelength.
     * 
     * @param wavelength
     * @return Full width at half maximum size of the Airy disk.
     */
    public double airyFWHM(double wavelength);
    
    /**
     * Computes the radius of the Airy disk.
     * 
     * Units are the same as those of wavelength.
     * 
     * @param wavelength
     * @return Distance from center of Airy disk to first minimum.
     */
    public double airyRadius(double wavelength);
    
    /**
     * Returns the objective's numerical aperture.
     * 
     * @return The objective's numerical aperture
     */
    public double getNA();
    
    /**
     * Returns the objective's magnification.
     * 
     * @return The objective's magnification.
     */
    public double getMag();
    
}
