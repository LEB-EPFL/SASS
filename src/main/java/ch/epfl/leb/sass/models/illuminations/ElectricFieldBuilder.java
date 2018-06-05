/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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
package ch.epfl.leb.sass.models.illuminations;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;

/**
 * Builds a new ElectricField instance.
 * 
 * @author Kyle M. Douglass
 */
public interface ElectricFieldBuilder {
    
    /**
     * Builds a new ElectricField instance.
     * 
     * @return A new instance of an electric field.
     */
    public ElectricField build();
    
    /**
     * Sets the refractive index of the sample.
     * 
     * @param n The sample's refractive index.
     * @return A new ElectricFieldBuilder with the refractive index set.
     */
    public ElectricFieldBuilder refractiveIndex(RefractiveIndex n);
    
    /**
     * Sets the free space wavelength of the radiation.
     * @param wavelength The free space wavelength.
     * @return A new ElectricFieldBuilder with the wavelength set.
     */
    public ElectricFieldBuilder wavelength(double wavelength);
    
}
