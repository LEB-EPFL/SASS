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
package ch.epfl.leb.sass.models.samples;

import org.apache.commons.math3.complex.Complex;

/**
 * Common methods for the sample's refractive index distribution.
 * 
 * @author Kyle M. Douglass
 */
public interface RefractiveIndex {
    
    /**
     * Returns the (complex) refractive index at the position (x, y, z).
     * 
     * z = 0 corresponds to the plane of the coverslip.
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The complex refractive index at the position (x, y, z).
     */
    public Complex getN(double x, double y, double z);
    
}
