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
import ch.epfl.leb.sass.simulator.Simulator;
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
     * Returns the control signal for the current simulation.
     * 
     * @param id The simulation ID.
     * @return The value of the simulation's current control signal.
     * @throws ch.epfl.leb.sass.server.UnknownSimulationIdException
     */
    @Override
    public double getControlSignal(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getControlSignal();
    }
    
    /**
     * Returns the field-of-view size in object space units.
     * 
     * @param id The simulation ID.
     * @return The size of the simulation's FOV.
     * @throws ch.epfl.leb.sass.server.UnknownSimulationIdException
     */
    @Override
    public double getFovSize(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getFOVSize();
    }
    
    /**
     * Returns the number of images already simulated.
     * 
     * @param id The simulation ID.
     * @return The number of images already simulated.
     * @throws UnknownSimulationIdException 
     */
    @Override
    public int getImageCount(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getImageCount();
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
    public ByteBuffer getNextImage(int id)
           throws ImageGenerationException, UnknownSimulationIdException {
        
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        // Advance the simulation one time step and retrieve the image.  
        try {
            ImageS is = sim.getNextImage();
            return is.serializeToBuffer();
        } catch (ImageShapeException ex) {
            ex.printStackTrace();
            throw new ImageGenerationException();
        }
    }
    
    /**
     * Returns the object space pixel size.
     * 
     * Units are the same as those of the camera pixel size.
     * 
     * @param id The simulation ID.
     * @return The object space pixel size.
     * @throws UnknownSimulationIdException 
     */
    @Override
    public double getObjectSpacePixelSize(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getObjectSpacePixelSize();
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
     * Returns a brief description of the ground-truth signal.
     * 
     * @param id The simulation ID.
     * @return A brief description of the ground truth signal.
     * @throws UnknownSimulationIdException 
     */
    @Override
    public String getShortTrueSignalDescription(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getShortTrueSignalDescription();
    }
    
    /**
     * Collects information about the simulation's current state and returns it.
     * 
     * @param id The simulation ID.
     * @return JSON string containing the current state of the simulation.
     */
    @Override
    public String getSimulationState(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return manager.getSimulator(id).getSimulationState();
    }
    
    /**
     * Advances the simulation without creating an image.
     * 
     * @param id The simulation ID.
     * @throws UnknownSimulationIdException 
     */
    @Override
    public void incrementTimeStep(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        sim.incrementTimeStep();
    }
    
    /**
     * Sets the activation laser power in the simulation.
     * 
     * @param id The simulation ID.
     * @param power The power of the laser.
     * @throws UnknownSimulationIdException
     */
    @Override
    public void setControlSignal(int id, double power)
                throws UnknownSimulationIdException {
        manager.getSimulator(id).setControlSignal(power);
    }
}
