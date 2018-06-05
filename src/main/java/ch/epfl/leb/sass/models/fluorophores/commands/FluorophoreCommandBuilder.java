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
package ch.epfl.leb.sass.models.fluorophores.commands;

import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamics;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.illuminations.Illumination;

/**
 * Interface for populating the field with fluorophores.
 * 
 * @author Kyle M. Douglass
 */
public interface FluorophoreCommandBuilder {
    
    /**
     * Creates a new Command instance for generating a fluorophore distribution.
     * 
     * @return The new instance of a FluorophoreCommand.
     */    
    public FluorophoreCommand build();
    
    /**
     * Sets the builder's Camera instance.
     * 
     * @param camera The camera used for building Fluorophore distributions.
     * @return A new copy of the builder.
     */
    public FluorophoreCommandBuilder camera(Camera camera);
    
    /**
     * Sets the FluorescenceDynamics.
     * 
     * @param fluorDynamics The fluorescence dynamical system.
     * @return A new copy of the builder.
     */
    public FluorophoreCommandBuilder fluorDynamics(
            FluorophoreDynamics fluorDynamics);
    
    /**
     * Sets the illumination profile on the sample.
     * 
     * @param illumination The illumination profile.
     * @return A new copy of the builder.
     */
    public FluorophoreCommandBuilder illumination(Illumination illumination);
    
    /**
     * Sets the PSF builder that will create the fluorophores' PSFs.
     * 
     * @param psfBuilder The PSF builder.
     * @return A new copy of the builder.
     */
    public FluorophoreCommandBuilder psfBuilder(PSFBuilder psfBuilder);
    
}
