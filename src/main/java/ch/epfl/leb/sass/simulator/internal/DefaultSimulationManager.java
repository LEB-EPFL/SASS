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
package ch.epfl.leb.sass.simulator.internal;

import ch.epfl.leb.sass.simulator.Simulator;
import ch.epfl.leb.sass.simulator.SimulationManager;

import java.util.List;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A default implementation of the SimulationManager class.
 * 
 * @author Kyle M. Douglass
 */
public class DefaultSimulationManager implements SimulationManager {    
    /**
     * The list of Simulators currently managed by the manager.
     */
    private ConcurrentHashMap<Integer, Simulator> listOfSims;
 
    /**
     * Default constructor.
     */
    public DefaultSimulationManager() {
        
    }
    
    /**
     * Adds a table of simulations to a new SimulationManager instance.
     * 
     * @param simulations A table of simulations to add to the new instance.
     */
    public DefaultSimulationManager(ConcurrentHashMap simulations) {
        listOfSims = simulations;
    }
    
    /**
     * Returns the list of simulation IDs managed by the manager.
     * 
     * @return The list of simulation IDs managed by the manager.
     */
    @Override
    public List<Integer> getIds() {
        List<Integer> ids;
        ids = Collections.list(listOfSims.keys());
        return ids;
    }
    
    /**
     * Returns a reference to the simulator corresponding to the ID.
     * 
     * @param id The ID number of a simulation.
     * @return A reference to the Simulator.
     */
    @Override
    public Simulator getSimulator(int id) {
        return listOfSims.get(id);
    }
    
    /**
     * Removes a Simulator from the manager.
     * @param id The ID number of a simulation to remove.
     */
    @Override
    public void removeSimulator(int id) {
        listOfSims.remove(id);
    }
}
