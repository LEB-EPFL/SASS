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
package ch.epfl.leb.sass.models.components;

import ch.epfl.leb.sass.models.Model;
import ch.epfl.leb.sass.logging.Observable;

/**
 * Defines methods common to Lasers.
 * 
 * @author Kyle M. Douglass
 */
public interface Laser extends Model, Observable {
    
    /**
     * Returns the current power if the laser.
     * 
     * @return The current laser power.
     */
    public double getPower();
    
    /**
     * Returns the wavelength of the laser.
     * 
     * @return The laser's wavelength.
     */
    public double getWavelength();
    
    /**
     * Sets the light source's power.
     * 
     * @param newPower The power of the light source.
     */
    public void setPower(double newPower);
    
}
