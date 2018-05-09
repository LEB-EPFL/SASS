/*
 * © All rights reserved. 
 * ECOLE POLYTECHNIQUE FEDERALE DE LAUSANNE, Switzerland
 * Laboratory of Experimental Biophysics, 2017
 */
package ch.epfl.leb.sass.models;

/**
 * Defines common methods possessed by all models employed by the microscope.
 * 
 * @author Kyle M. Douglass
 */
public interface Model {
    
    /**
     * Outputs the model's properties as a JSON string.
     * 
     * @return A JSON string describing the model's properties.
     */
    public String toJson();
}
