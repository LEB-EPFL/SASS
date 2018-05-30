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
package ch.epfl.leb.sass.models.illuminations.internal;

import ch.epfl.leb.sass.logging.Message;
import ch.epfl.leb.sass.logging.MessageType;
import ch.epfl.leb.sass.logging.WrongMessageTypeException;
import ch.epfl.leb.sass.logging.internal.AbstractObservable;
import ch.epfl.leb.sass.models.illuminations.Illumination;
import ch.epfl.leb.sass.models.illuminations.ElectricField;
import ch.epfl.leb.sass.models.samples.RefractiveIndex;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implements a linearly polarized uniform illumination profile of square shape.
 * 
 * @author Kyle M. Douglass
 */
public class SquareUniformIllumination extends AbstractObservable 
                                  implements Illumination {
    
    private final static Logger LOGGER
            = Logger.getLogger(SquareUniformIllumination.class.getName());
       
    /**
     * The electric field that propagates through the sample.
     */
    private ElectricField electricField;
    
    /**
     * The extent of the illumination from y = 0 to y = height.
     */
    private double height;
    
    /**
     * The current illumination power.
     */
    private double power;
    
    /**
     * The extent of the illumination from x = 0 to x = width.
     */
    private double width;
    
    /**
     * Creates a new illumination profile that is square and uniform.
     * 
     * @param power The power carried by the radiation.
     * @param width The extent of the field in x from 0 to width.
     * @param height The extend of the field in y from 0 to height.
     * @param orientation The orientation of the electric field vector.
     * @param wavelength The wavelength of the radiation.
     * @param refractiveIndex The sample's refractive index distribution.
     */
    public SquareUniformIllumination(double power, double width, double height,
            Vector3D orientation, double wavelength,
            RefractiveIndex refractiveIndex) {
        
        // Create The electric field for this illumination.
        SquareUniformElectricField.Builder builder = 
                new SquareUniformElectricField.Builder();
        builder.width(width).height(height).orientation(orientation)
               .refractiveIndex(refractiveIndex).wavelength(wavelength);
        electricField = builder.build();
        
        this.power = power;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Retrieves the complex electric field.
     * 
     * @return The complex electric field.
     */
    @Override
    public ElectricField getElectricField() {
        return electricField;
    }
    
    /**
     * Returns the irradiance in the sample at the point (x, y, z).
     * 
     * @param x The x-position in the sample.
     * @param y The y-position in the sample.
     * @param z The z-position in the sample.
     */
    @Override
    public double getIrradiance(double x, double y, double z) {      
        // Compute the absorption, if any.
        double abs = Math.exp(
                -4 * Math.PI 
                   * electricField.getRefractiveIndex()
                                  .getN(x, y, z)
                                  .getImaginary()
                   * z
                   / electricField.getWavelength());

        double irrad = this.power * abs / width / height;
        return irrad;
    }
    
    /**
     * Returns the power carried by the illumination profile.
     * 
     * This quantity is the irradiance integrated over the illuminated area and
     * is controlled by the laser power.
     * 
     * @return The power carried by the illumination profile.
     * @see #setPower(double) 
     */
    @Override
    public double getPower() {
        return power;
    }
    
    /**
     * Sets the power carried by the illumination.
     * 
     * @param power The power carried by the illumination.
     */
    @Override
    public void setPower(double power) {
        this.power = power;
        
        setChanged();
        this.notifyListeners();
    }
    
    /**
     * This method is called by an illumination source when its state has changed.
     * 
     * @param data The message that is passed from the illumination source.
     */
    @Override
    public void update(Object data) throws WrongMessageTypeException {
        if (data == null) {
            // No data reported by the Observable.
            return;
        }
        
        try {
            Message msg = (Message) data;
            assert(msg.getType() == MessageType.LASER_POWER_CHANGE);
            double power = msg.toJson()
                              .getAsJsonObject()
                              .get("power")
                              .getAsDouble();
            this.power = power;
        } catch (AssertionError ex) {
            String err = "The message from the illumination source was not " + 
                         "the expected type.";
            LOGGER.log(Level.WARNING, err);
            throw new WrongMessageTypeException(err);
        } catch (Exception ex) {
            String err = "Could not coerce the illumination source message " +
                         "into a known message type.";
            LOGGER.log(Level.WARNING, err);
            ex.printStackTrace();
       }
   }
    
}
