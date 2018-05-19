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
package ch.epfl.leb.sass.models.components;

import java.io.Serializable;

/**
 * The sample stage.
 *
 * @author Kyle M. Douglass
 */
public class Stage  implements Serializable {
    /**
     * The stage x-position.
     */
    private double x;
    
    /**
     * The stage y-position.
     */
    private double y;
    
    /**
     * The stage z-position.
     * 
     * Negative values refer to moving the stage downwards on an inverted light
     * microscope. This corresponds to scanning upwards through the sample.
     */
    private double z;
    
    /**
     * Builder for creating stage instances.
     */
    public static class Builder {
        private double x;
        private double y;
        private double z;
        
        public Builder x(double x) { this.x = x; return this; }
        public Builder y(double y) { this.y = y; return this; }
        public Builder z(double z) { this.z = z; return this; }
        
        public Stage build() {
            return new Stage(this);
        }
    }
    
    private Stage(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.z = builder.z;
    }
    
    /**
     * @return The stage's x-position.
     */
    public double getX() {
        return this.x;
    }
    
    /**
     * Set the stage's z-position.
     * @param x 
     */
    public void setX(double x) {
        this.x = x;
    }
    
        /**
     * @return The stage's y-position.
     */
    public double getY() {
        return this.y;
    }
    
    /**
     * Set the stage's y-position.
     * @param y 
     */
    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * @return The stage's z-position.
     */
    public double getZ() {
        return this.z;
    }
    
    /**
     * Set the stage's z-position.
     * @param z 
     */
    public void setZ(double z) {
        this.z = z;
    }
}
