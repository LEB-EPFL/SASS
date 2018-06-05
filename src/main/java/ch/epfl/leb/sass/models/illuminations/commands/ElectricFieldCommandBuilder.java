/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
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
package ch.epfl.leb.sass.models.illuminations.commands;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;

/**
 * Common methods for commands that build ElectricField generators.
 * 
 * A command is like a method wrapped inside an object.
 * 
 * @author Kyle M. Douglass
 */
public interface ElectricFieldCommandBuilder {
    
    /**
     * Creates a new command for generating an ElectricField instance.
     * 
     * @return A command for generating new ElectricField instances.
     */
    public ElectricFieldCommand build();
    
    /**
     * Assigns a value to the refractive index field of the command builder.
     * 
     * @param refractiveIndex
     * @return A copy of this builder with the new refractive index.
     */
    public ElectricFieldCommandBuilder refractiveIndex(RefractiveIndex refractiveIndex);
    
    /**
     * Assigns a value to the wavelength field of the command builder.
     * 
     * @param wavelength
     * @return A copy of this builder with the new wavelength.
     */
    public ElectricFieldCommandBuilder wavelength(double wavelength);
    
}
