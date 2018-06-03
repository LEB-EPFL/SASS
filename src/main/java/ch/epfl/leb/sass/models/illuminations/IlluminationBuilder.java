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
package ch.epfl.leb.sass.models.illuminations;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;

/**
 * Builds a new Illumination instance.
 * 
 * @author Kyle M. Douglass
 */
public interface IlluminationBuilder {
    
    /**
     * Creates a new Illumination instance from the builder's properties.
     * 
     * @return A new Illumination instance. 
     */
    public Illumination build();
    
    /**
     * Sets the power delivered by the illumination.
     * 
     * @param power The illumination's power (energy per time).
     * @return A new IlluminationBuilder with the power set.
     */
    public IlluminationBuilder power(double power);
    
    /**
     * Sets the refractive index of the sample.
     * 
     * @param refractiveIndex The sample's refractive index.
     * @return A new IlluminationBuilder with the refractive index set.
     */
    public IlluminationBuilder refractiveIndex(RefractiveIndex refractiveIndex);
    
    /**
     * Sets the wavelength of the illumination.
     * 
     * @param wavelength The illumination's wavelength.
     * @return A new IlluminationBuilder with the refractive wavelength set.
     */
    public IlluminationBuilder wavelength(double wavelength);
    
}
