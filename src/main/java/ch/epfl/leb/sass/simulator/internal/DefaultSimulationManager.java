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

import ch.epfl.leb.sass.models.Microscope;
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
     * The microscope from the most recently-added simulation.
     */
    private Microscope microscope;
 
    /**
     * Default constructor.
     */
    public DefaultSimulationManager() {
        listOfSims = new ConcurrentHashMap<>();
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
     * Adds a simulation to the manager.
     * 
     * @param The simulation to add to the manager.
     */
    public void addSimulator(Simulator simulator) {
        listOfSims.put(simulator.getId(), simulator);
        microscope = simulator.getMicroscope();
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
     * Returns the most recent microscope that was used to create a simulation.
     * 
     * This method serves as a sort of cache for remembering the most
     * recently created {@link ch.epfl.leb.sass.models.Microscope Microscope}
     * object. Its purpose is to allow for easy generation of new Simulators.
     * 
     * This method will return null if the SimulatorManager has never managed
     * a simulation.
     * 
     * @return A copy of the Microscope object or null.
     */
    @Override
    public Microscope getMostRecentMicroscope() {
        return microscope;
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
