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

import org.apache.commons.math3.complex.Complex;

/**
 * Common methods for ElectricField instances of an illumination profile. 
 * 
 * @author Kyle M. Douglass
 */
public interface ElectricField {
    
    /**
     * Returns the x-component of the time-independent electric field at the position (x, y, z).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The x-component of the electric field at the position (x, y, z).
     */
    public Complex getEx(double x, double y, double z);
    
    /**
     * Returns the y-component of the time-independent electric field at the position (x, y, z).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The y-component of the electric field at the position (x, y, z).
     */
    public Complex getEy(double x, double y, double z);
    
    /**
     * Returns the z-component of the time-independent electric field at the position (x, y, z).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The z-component of the electric field at the position (x, y, z).
     */
    public Complex getEz(double x, double y, double z);
    
    /**
     * Returns the sample's refractive index that produced this field.
     * 
     * @return The refractive index distribution of the sample.
     */
    public RefractiveIndex getRefractiveIndex();
    
    /**
     * Returns the radiation's wavelength.
     * 
     * @return The wavelength of the radiation.
     */
    public double getWavelength();
    
}
