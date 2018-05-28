/*
 * © All rights reserved. 
 * ECOLE POLYTECHNIQUE FEDERALE DE LAUSANNE, Switzerland
 * Laboratory of Experimental Biophysics, 2017
 */
package ch.epfl.leb.sass.models.illuminations;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;

/**
 * Builds a new ElectricField instance.
 * 
 * @author Kyle M. Douglass
 */
public interface ElectricFieldBuilder {
    
    /**
     * Builds a new ElectricField instance.
     * 
     * @return A new instance of an electric field.
     */
    public ElectricField build();
    
    /**
     * Sets the refractive index of the sample.
     * 
     * @param n The sample's refractive index.
     * @return A new ElectricFieldBuilder with the refractive index set.
     */
    public ElectricFieldBuilder refractiveIndex(RefractiveIndex n);
    
    /**
     * Sets the free space wavelength of the radiation.
     * @param wavelength The free space wavelength.
     * @return A new ElectricFieldBuilder with the wavelength set.
     */
    public ElectricFieldBuilder wavelength(double wavelength);
    
}
