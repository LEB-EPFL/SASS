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
package ch.epfl.leb.sass.models.illuminations.commands.internal;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;
import ch.epfl.leb.sass.models.illuminations.ElectricField;
import ch.epfl.leb.sass.models.illuminations.internal.SquareUniformElectricField;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Methods for creating new and different types of ElectricField instances.
 * 
 * @author Kyle M. Douglass
 */
public class ElectricFieldReceiver {
    
    /**
     * Creates a uniform electric field of square shape.
     * 
     * This electric field is uniformly polarized and of the same magnitude
     * within the area (x, x+width), (y, y+height). It extends from z = 0 to
     * z = infinity. Obviously, it is not physical but is a good approximation
     * of a plane wave with finite energy.
     * 
     * @param width The extent of the field in x from 0 to width.
     * @param height The extend of the field in y from 0 to height.
     * @param orientation The orientation of the electric field vector.
     * @param wavelength The wavelength of the radiation.
     * @param refractiveIndex The sample's refractive index distribution.
     * @return A new instance of an ElectricField that is uniform and square.
     */
    public static ElectricField generateUniformSquareElectricField(
            double width,
            double height,
            Vector3D orientation,
            double wavelength,
            RefractiveIndex refractiveIndex) {
        
        SquareUniformElectricField.Builder builder 
                = new SquareUniformElectricField.Builder();
        
        builder.width(width).height(height).orientation(orientation)
               .wavelength(wavelength).refractiveIndex(refractiveIndex);
        
        return builder.build();
        
    }
    
}
