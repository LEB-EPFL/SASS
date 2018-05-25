/**
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne, Switzerland
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

import ch.epfl.leb.sass.logging.Message;
import ch.epfl.leb.sass.models.Microscope;
import ch.epfl.leb.sass.simulator.Simulator;
import ch.epfl.leb.sass.simulator.SimulationManager;
import ch.epfl.leb.sass.simulator.internal.RPCSimulator;
import ch.epfl.leb.sass.simulator.internal.DefaultSimulationManager;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.List;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Implements the remote simulation service functions.
 * 
 * @author Kyle M. Douglass
 */
public class RemoteSimulationServiceHandler implements RemoteSimulationService.Iface {
    
    private final static Logger LOGGER = 
            Logger.getLogger(RemoteSimulationServiceHandler.class.getName());
    
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
     * Creates a new simulation and returns its ID.
     * 
     * This creates a copy of one of the already created simulations in the
     * SimulationManager. If you wish to create a simulation with all new
     * parameters, then you will need to create a new SimulationManager.
     * 
     * @return The new simulation's ID.
     */
    @Override
    public int createSimulation() {
        Microscope microscope = manager.getMostRecentMicroscope();
        if (microscope == null) {
            String msg = "Cannot not create a new simulation without a " +
                         "a previous one to use as a model.";
            LOGGER.log(Level.WARNING, msg);
            
            throw new java.lang.NullPointerException();
        }
        
        Simulator simulator = new RPCSimulator(microscope);
        manager.addSimulator(simulator);
        return simulator.getId();
    }
    
    /**
     * Deletes the simulation with the given ID.
     * 
     * @param id The ID of the simulation to delete.
     */
    @Override
    public void deleteSimulation(int id) throws UnknownSimulationIdException {
        List ids = manager.getIds();
        
        if (ids.contains(id)) {
            manager.removeSimulator(id);
        } else {
            String msg = "Cannot delete simulation with ID " + 
                          String.valueOf(id) + " because it does not exist.";
            LOGGER.log(Level.WARNING, msg);
            throw new UnknownSimulationIdException();
        }
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
            String msg = "Cannot get simulation with ID " + String.valueOf(id) +
                         " because it does not exist.";
            LOGGER.log(Level.WARNING, msg);
            throw new UnknownSimulationIdException();
        }
        
        return sim.getControlSignal();
    }
    
    /**
     * Returns the name of the JSON key for the camera info.
     * 
     * @param id The simulation ID.
     * @return The name of the key indicating the camera information.
     * @throws UnknownSimulationIdException
     */
    @Override
    public String getCameraJsonName(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getCameraJsonName();
    }
    
    /**
     * Returns the name of the JSON key for the fluorescence info.
     * 
     * @param id The simulation ID.
     * @return The name of the key indicating the fluorescence information.
     * @throws UnknownSimulationIdException
     */
    @Override
    public String getFluorescenceJsonName(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getFluorescenceJsonName();
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
     * Returns the name of the JSON key for the laser info.
     * 
     * @param id The simulation ID.
     * @return The name of the key indicating the laser information.
     * @throws UnknownSimulationIdException
     */
    @Override
    public String getLaserJsonName(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getLaserJsonName();
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
     * Returns the name of the JSON key for the objective info.
     * 
     * @param id The simulation ID.
     * @return The name of the key indicating the laser information.
     * @throws UnknownSimulationIdException
     */
    @Override
    public String getObjectiveJsonName(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getObjectiveJsonName();
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
     * Returns the name of the JSON key for the stage info.
     * 
     * @param id The simulation ID.
     * @return The name of the key indicating the stage information.
     * @throws UnknownSimulationIdException
     */
    @Override
    public String getStageJsonName(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getStageJsonName();
    }
    
    /**
     * Returns the ground-truth signal of the image at the given index.
     * 
     * @param id The simulation ID.
     * @param imageNum The index of the image to get the true signal for.
     * @return The ground truth signal.
     * @throws UnknownSimulationIdException 
     */
    @Override
    public double getTrueSignal(int id, int imageNum) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        return sim.getTrueSignal(imageNum);
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
    
    /**
     * Returns messages about changes in the simulation state as a JSON string.
     * 
     * Unlike {@link #toJsonState(int id) toJsonState()}, which returns
     * information about the *current* state of just the fluorophores, this
     * method returns the messages from individual simulation components that
     * contain information about changes in their state that have occurred since
     * the last time this method was called.
     * 
     * @param id The simulation ID.
     * @return A JSON string containing the messages that were recorded.
     * @throws UnknownSimulationIdException
     */
    @Override
    public String toJsonMessages(int id) 
           throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        List<Message> messages = sim.getMessages();
        JsonArray json = new JsonArray();
        for (Message msg: messages) {
            json.add(msg.toJson());
        }

        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(json);
            return jsonString;
        } catch (Exception ex) {
            String err = "Unable to convert JSON object to string.";
            LOGGER.log(Level.SEVERE, err);
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Returns information on the simulation's current state as a JSON object.
     * 
     * Unlike {@link #toJsonMessages(int id) toJsonMessages()}, which returns
     * information about previous changes in the simulation's state, this method
     * reports on the current state of the simulation.
     * 
     * @param id The simulation ID.
     * @return The state of the sample fluorescence as a JSON string.
     * @throws UnknownSimulationIdException 
     */
    @Override
    public String toJsonState(int id) throws UnknownSimulationIdException {
        Simulator sim = manager.getSimulator(id);
        if (sim == null) {
            throw new UnknownSimulationIdException();
        }
        
        Gson gson = new Gson();
        return gson.toJson(sim.toJsonState());
    }
    
}
