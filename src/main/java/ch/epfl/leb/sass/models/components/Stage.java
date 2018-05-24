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
package ch.epfl.leb.sass.models.components;

import ch.epfl.leb.sass.models.Model;

/**
 * Defines methods common to all Stages.
 * 
 * @author Kyle M. Douglass 
 */
public interface Stage extends Model {
    
    /**
     * Returns the stage's x-position.
     * 
     * @return The stage's x-position.
     */
    public double getX();
    
    
    
    /**
     * Returns the stage's y-position.
     * 
     * @return The stage's y-position.
     */
    public double getY();
    
    /**
     * Returns the stage's z-position.
     * 
     * @return The stage's z-position.
     */
    public double getZ();
    
    /**
     * Set the stage's x-position.
     * 
     * @param x 
     */
    public void setX(double x);
    
    /**
     * Set the stage's y-position.
     * 
     * @param y 
     */
    public void setY(double y);
    
    /**
     * Set the stage's z-position.
     * 
     * @param z 
     */
    public void setZ(double z);
    
}
