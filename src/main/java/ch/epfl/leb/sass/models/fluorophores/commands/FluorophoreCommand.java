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

import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import java.util.List;

/**
 * Executes a command for generating fluorophores.
 * 
 * A fluorophore command is a tool for generating a new set of Fluorophore
 * instances. It handles the job of both creating the instances and connecting
 * the correct Listeners to their Observables.
 * 
 * @author Kyle M. Douglass
 */
public interface FluorophoreCommand {
    
    /**
     * Creates a new set of Fluorophore instances.
     * 
     * @return A new list of Fluorophore instances.
     */
    public List<Fluorophore> generateFluorophores();
    
}
