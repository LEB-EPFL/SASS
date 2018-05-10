/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
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
package ch.epfl.leb.sass.models.fluorophores;

/**
 * A single fluorophore including its position and photophysical properties.
 * 
 * @author Kyle M. Douglass
 */
public interface Fluorophore {
    
    /**
     * Has the fluorophore been bleached? If so, it can never return to a fluorescence-emitting state.
     * 
     * @return A true/false value describing whether the fluorophore is bleached.
     */
    public boolean isBleached();
    
    /**
     * Describes whether the fluorophore is emitting light or is in a dark state.
     * 
     * @return A true/false value describing whether the fluorophore is emitting.
     */
    public boolean isOn();
    
    /**
     * This method recalculates the lifetimes of the fluorophore's state system based on the laser power.
     * 
     * @param laserPower The new power of the laser.
     */
    public void recalculateLifetimes(double laserPower);
    
    /**
     * Renders the fluorophore onto an array of pixels.
     * 
     * @param pixels Image on which the fluorophore's signature will be drawn.
     */
    public void applyTo(float[][] pixels);
    
    /**
     * Return the x-position of the fluorophore.
     * 
     * @return The fluorophore's x-position.
     */
    public double getX();
    
    /**
     * Return the y-position of the fluorophore.
     * 
     * @return The fluorophore's y-position.
     */
    public double getY();
    
    /**
     * Return the z-position of the fluorophore.
     * 
     * @return The fluorophore's z-position.
     */
    public double getZ();
    
}
