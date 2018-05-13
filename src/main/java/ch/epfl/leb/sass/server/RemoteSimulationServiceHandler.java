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
package ch.epfl.leb.sass.server;
import ch.epfl.leb.sass.simulator.SimulationManager;
import ch.epfl.leb.sass.simulator.internal.DefaultSimulationManager;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;

import java.nio.ByteBuffer;


/**
 * Implements the remote simulation service functions.
 * 
 * @author Kyle M. Douglass
 */
public class RemoteSimulationServiceHandler implements RemoteSimulationService.Iface {
    
    /**
     * Reference to the server's SimulationManager.
     */
    private SimulationManager manager;
    
    /**
     * Initializes the remote handler.
     */
    public RemoteSimulationServiceHandler() {
        manager = new DefaultSimulationManager();
    }
    
    /**
     * Initializes the remote handler with a pre-specified SimulationManager.
     * 
     * @param inputManager SimulationManager that handles multiple simulations.
     */
    public RemoteSimulationServiceHandler(SimulationManager inputManager) {
        manager = inputManager;
    }
    
    /**
     * Advances the simulator by one time step and returns the image.
     * 
     * @param id The simulation ID.
     * @return A buffer containing the TIFF-encoded byte string of the
     *        simulator's next image.
     * @throws ch.epfl.leb.sass.server.ImageGenerationException
     */
    @Override
    public ByteBuffer getNextImage(int id) throws ImageGenerationException {
        // Advance the simulation one time step and retrieve the image.  
        try {
            ImageS is = manager.getSimulator(id).getNextImage();
            return is.serializeToBuffer();
        } catch (ImageShapeException ex) {
            ex.printStackTrace();
            throw new ImageGenerationException();
        }
    }
    
    /**
     * This method is used to determine whether the server is running.
     * 
     * @return Basic information concerning the status of the server.
     */
    @Override
    public String getServerStatus() {
        return "SASS RPC server is running.";
    }
    
    /**
     * Sets the activation laser power in the simulation.
     * 
     * @param id The simulation ID.
     * @param power The power of the laser.
     */
    @Override
    public void setActivationLaserPower(int id, double power) {
        manager.getSimulator(id).setControlSignal(power);
    }
    
    /**
     * Collects information about the simulation's current state and returns it.
     * 
     * @param id The simulation ID.
     * @return JSON string containing the current state of the simulation.
     */
    @Override
    public String getSimulationState(int id) {
        return manager.getSimulator(id).getSimulationState();
    }
}
