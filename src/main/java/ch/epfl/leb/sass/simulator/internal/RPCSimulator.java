/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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
package ch.epfl.leb.sass.simulator.internal;

import ch.epfl.leb.sass.models.Microscope;

/**
 * A simulator that is specialized for control by remote procedure calls (RPCs).
 * 
 * @author Kyle M. Douglass
 */
public class RPCSimulator extends DefaultSimulator {
        
    /**
     * Initializes the SimpleSimulator and connects it to the simulation engine.
     * 
     * @param microscope The engine that runs the simulation.
     */
    public RPCSimulator(Microscope microscope) {
        super(microscope);
    }    
}
