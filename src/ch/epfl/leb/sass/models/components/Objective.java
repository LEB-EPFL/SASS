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

/**
 * Properties related to the microscope objective.
 * 
 * @author Kyle M. Douglass
 */
public final class Objective {
    /**
     * Numerical aperture
     */
    private final double NA;

    /**
     * Magnification of the objective.
     */
    private final double mag;
    
    public static class Builder {
       private double NA;
       private double mag;
       
       public Builder NA(double NA) {this.NA = NA; return this;}
       public Builder mag(double mag) {this.mag = mag; return this;}
       
       public Objective build() {
           return new Objective(this);
       }
    }
    
    /**
     * Creates a new microscope objective.
     * @param builder An Objective builder.
     */
    private Objective(Builder builder) {
        this.NA = builder.NA;
        this.mag = builder.mag;
    }
    
    /**
     * Computes the full width at half maximum of the Airy disk.
     * 
     * Units are the same as those of wavelength.
     * 
     * @param wavelength
     * @return Full width at half maximum size of the Airy disk.
     */
    public double airyFWHM(double wavelength) {
        double airyDiskFWHM = 0.514*wavelength/this.NA * this.mag;

        return airyDiskFWHM;
    }
    
    /**
     * Computes the radius of the Airy disk.
     * 
     * Units are the same as those of wavelength.
     * 
     * @param wavelength
     * @return Distance from center of Airy disk to first minimum.
     */
    public double airyRadius(double wavelength) {
        double airyDiskRadius = 0.61*wavelength/this.NA * this.mag;

        return airyDiskRadius;
    }
    
    /**
     * @return The objective' numerical aperture
     */
    public double getNA() {
        return this.NA;
    }
    
    /**
     * @return The objective's magnification.
     */
    public double getMag() {
        return this.mag;
    }
}
