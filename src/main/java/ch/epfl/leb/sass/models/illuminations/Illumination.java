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

import ch.epfl.leb.sass.logging.Listener;
import ch.epfl.leb.sass.logging.Observable;
import ch.epfl.leb.sass.models.samples.RefractiveIndex;

/**
 * Common methods for the microscope's illumination profile.
 * 
 * @author Kyle M. Douglass
 */
public interface Illumination extends Listener, Observable {
    
    /**
     * Retrieves the complex electric field.
     * 
     * @return The complex electric field.
     */
    public ElectricField getElectricField();
    
    /**
     * Returns the illumination irradiance at the point (x, y, z).
     * 
     * @param x The x-position within the sample.
     * @param y The y-position within the sample.
     * @param z The z-position within the sample.
     * @return The irradiance at the point (x, y, z).
     */
    public double getIrradiance(double x, double y, double z);
    
    /**
     * Returns the power carried by the illumination profile.
     * 
     * This quantity is the irradiance integrated over the illuminated area and
     * is controlled by the laser power.
     * 
     * @return The power carried by the illumination profile.
     * @see #setPower(double) 
     */
    public double getPower();
    
    /**
     * Sets the power carried by the illumination.
     * 
     * @param power The power carried by the illumination.
     */
    public void setPower(double power);
    

}
