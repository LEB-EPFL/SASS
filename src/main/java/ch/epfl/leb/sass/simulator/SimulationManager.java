/** 
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
package ch.epfl.leb.sass.simulator;

import java.util.List;

/**
 * Management tool for handling multiple Simulators.
 * 
 * @author Kyle M. Douglass
 */
public interface SimulationManager {
    
    /**
     * Returns a list of simulation IDs currently managed by this Manager.
     * 
     * A simulation manager is a Singleton; there is only one.
     * 
     * @return A list of simulation ID numbers.
     */
    public List<Integer> getIds();
    
    /**
     * Returns a reference to the simulator corresponding to the ID.
     * 
     * @param id The ID number of a simulation.
     * @return A reference to the Simulator.
     */
    public Simulator getSimulator(int id);
    
    /**
     * Removes a Simulator from the manager.
     * @param id The ID number of a simulation to remove.
     */
    public void removeSimulator(int id);
}
