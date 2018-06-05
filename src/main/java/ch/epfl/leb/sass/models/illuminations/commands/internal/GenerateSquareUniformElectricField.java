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
import ch.epfl.leb.sass.models.illuminations.commands.ElectricFieldCommand;
import ch.epfl.leb.sass.models.illuminations.commands.ElectricFieldCommandBuilder;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Creates a uniform electric field of square shape.
 * 
 * This electric field is uniformly polarized and of the same magnitude
 * within the area (0, width), (0, height). It extends from z = 0 to
 * z = infinity. Obviously, it is not physical but is a good approximation
 * of a plane wave with finite energy.
 *
 * @author Kyle M. Douglass
 */
public class GenerateSquareUniformElectricField implements ElectricFieldCommand {
    
    /**
     * The extend of the illumination from y = 0 to y = height.
     */
    double height;
    
    /**
     * The orientation of the electric field vector.
     * 
     * This vector should be normalized to 1.
     */
    Vector3D orientation;
    
    /**
     * The refractive index of the medium.
     */
    RefractiveIndex refractiveIndex;
    
    /**
     * The wavelength of the radiation in free space.
     */
    double wavelength;
    
    /**
     * The extend of the illumination from x = 0 to x = width.
     */
    double width;
    
    public static class Builder implements ElectricFieldCommandBuilder {
        private double height;
        private Vector3D orientation;
        private RefractiveIndex refractiveIndex;
        private double wavelength;
        private double width;
        
        public Builder height(double height) { this.height = height; return this; }
        public Builder orientation(Vector3D orientation) {
            this.orientation = orientation;
            return this;
        }
        @Override
        public Builder refractiveIndex(RefractiveIndex refractiveIndex) {
            this.refractiveIndex = refractiveIndex;
            return this;
        }
        @Override
        public Builder wavelength(double wavelength) {
            this.wavelength = wavelength;
            return this;
        }
        public Builder width(double width) {
            this.width = width;
            return this;
        }
        
        /**
         * Creates a new command for generating a SquareUniformElectricField.
         * 
         * @return A new instance of this command.
         */
        @Override
        public ElectricFieldCommand build() {
            return new GenerateSquareUniformElectricField(
                    width, height, orientation, wavelength, refractiveIndex);
        }
    }
    
    /**
     * Creates a new instance of this command.
     * 
     * @param width The extent of the field in x from 0 to width.
     * @param height The extend of the field in y from 0 to height.
     * @param orientation The orientation of the electric field vector.
     * @param wavelength The wavelength of the radiation.
     * @param refractiveIndex The sample's refractive index distribution.
     */
    private GenerateSquareUniformElectricField(
            double width, double height, Vector3D orientation,
            double wavelength, RefractiveIndex refractiveIndex) {
        this.width = width;
        this.height = height;
        this.orientation = orientation;
        this.refractiveIndex = refractiveIndex;
        this.wavelength = wavelength;
    }
    
    /**
     * Executes the command that returns a uniform electric field of square shape.
     * 
     * This electric field is uniformly polarized and of the same magnitude
     * within the area (0, width), (0, height). It extends from z = 0 to
     * z = infinity. Obviously, it is not physical but is a good approximation
     * of a plane wave with finite energy.
     * 
     * @return 
     */
    @Override
    public ElectricField generateElectricField() {
        return ElectricFieldReceiver.generateUniformSquareElectricField(
                this.width, this.height, this.orientation, this.wavelength,
                this.refractiveIndex);
    }
    
}
