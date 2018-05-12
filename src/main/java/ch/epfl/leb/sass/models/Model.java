/*
 * © All rights reserved. 
 * ECOLE POLYTECHNIQUE FEDERALE DE LAUSANNE, Switzerland
 * Laboratory of Experimental Biophysics, 2017
 */
package ch.epfl.leb.sass.models;

import com.google.gson.JsonElement;

/**
 * Defines common methods possessed by all models employed by the microscope.
 * 
 * @author Kyle M. Douglass
 */
public interface Model {
    
    /**
     * Outputs the model's properties as a JSON element.
     * 
     * @return A JSON tree describing the model's properties.
     */
    public JsonElement toJson();
}
