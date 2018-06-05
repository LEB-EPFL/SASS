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
package ch.epfl.leb.sass.models.illuminations.internal;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;
import ch.epfl.leb.sass.models.illuminations.ElectricField;
import ch.epfl.leb.sass.models.illuminations.ElectricFieldBuilder;

import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Creates a uniform electric field of square shape propagating in the +z-direction.
 * 
 * This electric field is uniformly polarized and of the same magnitude
 * within the area (0, width), (0, height). It extends from z = 0 to
 * z = infinity. Obviously, it is not physical but is a good approximation
 * of a plane wave with finite energy.
 * 
 * The field propagates in the z-direction.
 *
 * @author Kyle M. Douglass
 */
public class SquareUniformElectricField implements ElectricField {
    
    /**
     * The extent of the illumination from y = 0 to y = height.
     */
    private double height;
    
    /**
     * The orientation of the electric field vector.
     * 
     * This vector should be normalized to 1.
     * 
     * Note: Because Apache Commons's Vector3D class only supports doubles,
     * this class cannot handle elliptical polarizations. A 3D vector that can
     * hold Complex values is required for elliptical polarization.
     */
    private Vector3D orientation;
    
    /**
     * The refractive index of the medium.
     */
    private RefractiveIndex refractiveIndex;
    
    /**
     * The wavelength of the radiation in free space.
     */
    private double wavelength;
    
    /**
     * The extent of the illumination from x = 0 to x = width.
     */
    private double width;
    
    /**
     * The builder for constructing Gaussian2D instances.
     */
    public static class Builder implements ElectricFieldBuilder {
        private double height;
        private Vector3D orientation;
        private RefractiveIndex refractiveIndex;
        private double wavelength;
        private double width;
        
        public Builder height(double height) {
            this.height = height;
            return this;
        }
        
        public Builder orientation(Vector3D orientation) {
            this.orientation = orientation;
            return this;
        }
        
        @Override
        public Builder refractiveIndex(RefractiveIndex n) {
            this.refractiveIndex = n;
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
        
        @Override
        public SquareUniformElectricField build() {
            return new SquareUniformElectricField(
                    width, height, orientation, wavelength, refractiveIndex);
        }
    }
    
    /**
     * Creates a new instance of this command.
     * 
     * Any z-component of the E-field orientation will be ignored since this
     * class propagates along the +z direction by default.
     * 
     * @param width The extent of the field in x from 0 to width.
     * @param height The extend of the field in y from 0 to height.
     * @param orientation The orientation of the electric field vector.
     * @param wavelength The wavelength of the radiation.
     * @param refractiveIndex The sample's refractive index distribution.
     */
    private SquareUniformElectricField(
            double width, double height, Vector3D orientation,
            double wavelength, RefractiveIndex refractiveIndex) {
        this.width = width;
        this.height = height;
        this.wavelength = wavelength;
        this.refractiveIndex = refractiveIndex;
        
        // Ignore any z-component of the field.
        this.orientation = (new Vector3D(orientation.getX(),
                                         orientation.getY(), 
                                         0))
                           .normalize();
    }
    
    /**
     * Returns the argument of the field's propagation phasor exp(j (k dot r)).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The argument of the phasor.
     */
    private Complex getArgument(double x, double y, double z) {
        // Compute the argument of the phasor.
        return refractiveIndex.getN(x, y, z)
                              .multiply(MathUtils.TWO_PI)
                              .divide(wavelength)
                              .multiply(z);
    }
    
     /**
     * Returns the x-component of the time-independent electric field at the position (x, y, z).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The x-component of the electric field at the position (x, y, z).
     */
    @Override
    public Complex getEx(double x, double y, double z) {
        if (x < 0 || x > width)
            return new Complex(0);
        if (y < 0 || y > height)
            return new Complex(0);
        
        Complex arg = getArgument(x, y, z);
        double magnitude = orientation.getX()
                           * Math.exp(-arg.getImaginary());
        return ComplexUtils.polar2Complex(magnitude, arg.getReal());
    }
    
     /**
     * Returns the y-component of the time-independent electric field at the position (x, y, z).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The y-component of the electric field at the position (x, y, z).
     */
    @Override
    public Complex getEy(double x, double y, double z) {
        if (x < 0 || x > width)
            return new Complex(0);
        if (y < 0 || y > height)
            return new Complex(0);
        
        Complex arg = getArgument(x, y, z);
        double magnitude = orientation.getY()
                           * Math.exp(-arg.getImaginary());
        return ComplexUtils.polar2Complex(magnitude, arg.getReal());
    }
    
     /**
     * Returns the z-component of the time-independent electric field at the position (x, y, z).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The z-component of the electric field at the position (x, y, z).
     */
    @Override
    public Complex getEz(double x, double y, double z) {        
        // E is in the x-y plane by definition of this class.
        return new Complex(0);
    }
    
    /**
     * Returns the sample's refractive index that produced this field.
     * 
     * @return The refractive index distribution of the sample.
     */
    public RefractiveIndex getRefractiveIndex() {
        return refractiveIndex;
    }
    
    /**
     * Returns the radiation's wavelength.
     * 
     * @return The wavelength of the radiation.
     */
    @Override
    public double getWavelength() {
        return this.wavelength;
    }
    
}
