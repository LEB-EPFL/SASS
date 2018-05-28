/*
 * © All rights reserved. 
 * ECOLE POLYTECHNIQUE FEDERALE DE LAUSANNE, Switzerland
 * Laboratory of Experimental Biophysics, 2017
 */
package ch.epfl.leb.sass.models.illuminations.internal;

import ch.epfl.leb.sass.models.illuminations.ElectricField;

import org.apache.commons.math3.complex.Complex;
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
public class SquareUniformElectricField implements ElectricField {
    
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
     * The extend of the illumination from x = 0 to x = width.
     */
    double width;
    
    /**
     * Creates a new instance of this command.
     * 
     * @param width The extent of the field in x from 0 to width.
     * @param height The extend of the field in y from 0 to height.
     * @param orientation The orientation of the electric field vector.
     */
    public SquareUniformElectricField(
            double width, double height, Vector3D orientation) {
        this.width = width;
        this.height = height;
        this.orientation = orientation.normalize();
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
        
        return new Complex(orientation.getX());
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
        
        return new Complex(orientation.getY());
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
        if (x < 0 || x > width)
            return new Complex(0);
        if (y < 0 || y > height)
            return new Complex(0);
        
        return new Complex(orientation.getZ());
    }
    
}
