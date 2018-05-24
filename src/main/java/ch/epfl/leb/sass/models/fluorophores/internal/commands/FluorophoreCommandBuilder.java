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
package ch.epfl.leb.sass.models.fluorophores.internal.commands;

import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamics;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;

/**
 * Interface for populating the field with fluorophores.
 * 
 * @author Kyle M. Douglass
 */
public interface FluorophoreCommandBuilder {
    
    public FluorophoreCommand build();
    
    public FluorophoreCommandBuilder camera(Camera camera);
    public FluorophoreCommandBuilder fluorDynamics(
            FluorophoreDynamics fluorDynamics);
    public FluorophoreCommandBuilder psfBuilder(PSFBuilder psfBuilder);
    
}
