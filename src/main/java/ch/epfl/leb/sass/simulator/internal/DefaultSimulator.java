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
package ch.epfl.leb.sass.simulator.internal;

import ch.epfl.leb.sass.utils.DeepCopy;
import ch.epfl.leb.sass.logging.Message;
import ch.epfl.leb.sass.logging.Listener;
import ch.epfl.leb.sass.models.Microscope;
import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import ch.epfl.leb.sass.utils.images.internal.DefaultImageS;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The basic simulation engine from which others may be derived.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class DefaultSimulator extends AbstractSimulator {
    
    public final static Logger LOGGER = 
            Logger.getLogger(DefaultSimulator.class.getName());
    
    /**
     * The STATE_LISTENER records changes in the fluorophore's state.
     */
    private final DefaultSimulator.StateListener STATE_LISTENER;
    
    /**
     * Scaling factor for the density.
     * 
     * Multiplying the estimated density by a factor of 100 means that the
     * output returns spots per 10 um x 10 um = 100 um^2 area.
     */
    private final double SCALEFACTOR = 100;
    
    // Member names for JSON serialization
    private final String CAMERA_MEMBER_NAME = "Camera";
    private final String FLUOR_MEMBER_NAME = "Fluorophores";
    private final String LASER_MEMBER_NAME = "Laser";
    private final String OBJECTIVE_MEMBER_NAME = "Objective";
    private final String STAGE_MEMBER_NAME = "Stage";
       
    private Microscope microscope;
    private ArrayList<Double> emitterHistory;
            
    /**
     * Initialize the generator.
     * @param microscope
     */
    public DefaultSimulator(Microscope microscope) {
        super();
        this.microscope = microscope;
        
        int[] res = this.microscope.getResolution();
        stack = new DefaultImageS(res[0], res[1]);

        emitterHistory = new ArrayList<>();
        emitterHistory.add(0.0);
        
        // Create the fluorescence state STATE_LISTENER and attach it.
        STATE_LISTENER = this.new StateListener();
        List<Fluorophore> fluorophores = this.microscope.getFluorophores();
        for (Fluorophore f: fluorophores) {
            f.addListener(STATE_LISTENER);
        }
        
    }
    
    /**
     * Returns the JSON member name assigned to the camera.
     * 
     * @return The JSON member name for the Camera field.
     */
    @Override
    public String getCameraJsonName() {
        return CAMERA_MEMBER_NAME;
    }
    
    @Override
    public double getControlSignal() {
        return microscope.getLaserPower();
    }
    
    @Override
    public HashMap<String, Double> getCustomParameters() {
        parameters.put("real_laser_power", microscope.getLaserPower());
        return parameters;
    }
    
    /**
     * 
     * @return The size of the FOV in square object-space units.
     */
    @Override
    public double getFOVSize() {
        return microscope.getFovSize();
    }
    
    /**
     * Returns the name of the JSON key for the fluorescence info.
     * 
     * @return The name of the key indicating the fluorescence information.
     */
    public String getFluorescenceJsonName() {
        return FLUOR_MEMBER_NAME;
    }
    
    /**
     * Returns the name of the JSON key for the laser info.
     * 
     * @return The name of the key indicating the laser information.
     */
    public String getLaserJsonName() {
        return LASER_MEMBER_NAME;
    }
    
    /**
     * Returns messages about changes in the simulation state.
     * 
     * Unlike {@link #getSimulationState() getSimulationState()}, which returns
     * information about the *current* state of the simulation, this method
     * returns the messages from individual components that contain information
     * about changes in their state that have occurred since the last time this
     * method was called.
     * 
     * @return A list containing the state change messages.
     */
    @Override
    public List<Message> getMessages() {
        List<Message> messages = this.STATE_LISTENER.dumpMessageCache();
        if (messages == null) {
            return new ArrayList<Message>();
        } else {
            return messages;
        }
    }
    
    /**
     * Returns a copy of the Microscope that is controlled by this simulation.
     * 
     * The copy that is returned is a deep copy of the
     * {@link ch.epfl.leb.sass.models.Microscope Microscope} that the simulation
     * was initialized with.
     * 
     * @return A copy of the Microscope object controlled by this simulation.
     */
    @Override
    public Microscope getMicroscope() {
        return microscope;
    }
    
    /**
     * Generates a new image and adds it to the internal stack.
     * @return newly generated image
     */
    @Override
    public ImageS getNextImage() throws ImageShapeException {
        // we calculate emitter count first so it corresponds with the beginning
        // of the frame rather than end of the frame
        emitterHistory.add(microscope.getOnEmitterCount());
        ImageS pixels = microscope.simulateFrame();
        stack.concatenate(pixels);
        
        return pixels;
    }
    
    /**
     * Returns the name of the JSON key for the objective state info.
     * 
     * @return The name of the key indicating the objective information.
     * @see #toJsonState()
     */
    @Override
    public String getObjectiveJsonName() {
        return OBJECTIVE_MEMBER_NAME;
    }
    
    /**
     *
     * @return Length of one pixel side in object-space units.
     */
    @Override
    public double getObjectSpacePixelSize() {
        return microscope.getObjectSpacePixelSize();
    }
    
    @Override
    public String getShortTrueSignalDescription() {
        String descr = "counts/" + String.valueOf((int) SCALEFACTOR) + " µm^2";
        return descr;
    }
    
    /**
     * Returns the name of the JSON key for the stage info.
     * 
     * @return The name of the key indicating the stage information.
     */
    public String getStageJsonName() {
        return STAGE_MEMBER_NAME;
    }
    
    /**
     * Returns this instance's StateListener.
     * 
     * This method is primarily for testing purposes and is not exposed in the
     * Simulator interface.
     * 
     * @return A reference to this instance's StateListener.
     */
    public DefaultSimulator.StateListener getStateListener() {
        return STATE_LISTENER;
    }
    
    @Override
    public double getTrueSignal(int image_no) {
        return emitterHistory.get(image_no) /
               microscope.getFovSize() * SCALEFACTOR;
    }
    
    /**
     * Advance the simulation by one time step (i.e. one frame).
     * 
     * Simulates a frame but does not create an image.
     */
    @Override
    public void incrementTimeStep() {
        emitterHistory.add(microscope.getOnEmitterCount());
        microscope.simulateFrame();
    }
    
    /**
     * Saves the messages in the cache to a select file.
     * 
     * @param file The file to save to.
     */
    public void saveMessages(File file) {
        JsonElement json = toJsonMessages();
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(json, writer);
        } catch (IOException ex) {
            String err = "Could not write simulation Messages to the file.";
            LOGGER.log(Level.WARNING, err);
            ex.printStackTrace();
        } catch (Exception ex) {
            String err = "An unknown error occurred while saving messages to " +
                         "the file.";
            LOGGER.log(Level.WARNING, err);
            ex.printStackTrace();
        }
    }
    
    /**
     * Saves the current state of the simulation.
     * 
     * @param file The file to save to.
     */
    public void saveState(File file) {
        JsonElement json = toJsonState();
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(json, writer);
        } catch (IOException ex) {
            String err = "Could not write simulation state to the file.";
            LOGGER.log(Level.WARNING, err);
            ex.printStackTrace();
        } catch (Exception ex) {
            String err = "An unknown error occurred while saving the " +
                         "simulation state to the file.";
            LOGGER.log(Level.WARNING, err);
            ex.printStackTrace();
        }
    }
    
    @Override
    public void setControlSignal(double value) {
        parameters.put("target_laser_power", value);
        microscope.setLaserPower(value);
    }

    @Override
    public void setCustomParameters(HashMap<String, Double> map) {
        parameters = map;
    }
    
    /**
     * Returns messages about changes in the simulation state as a JSON object.
     * 
     * Unlike {@link #toJsonState() toJsonState()}, which returns
     * information about the *current* state of the simulation, this method
     * returns the messages from individual simulation components that contain
     * information about changes in their state that have occurred since the
     * last time this method was called.
     * 
     * @return A JSON object containing the simulation messages.
     */
    @Override
    public JsonElement toJsonMessages() {
        JsonArray json = new JsonArray();
        for (Message msg: this.getMessages()) {
            json.add(msg.toJson().getAsJsonObject());
        }
        
        Gson gson = new Gson();
        return gson.fromJson(json, JsonArray.class);
    }
    
    /**
     * Returns information on the simulation's current state as a JSON object.
     * 
     * Unlike {@link #toJsonMessages() toJsonMessages()}, which returns
     * information about previous changes in the simulation's state, this method
     * reports on the current state of the simulation.
     * 
     * @return A JSON object containing information on the simulation state.
     */
    @Override
    public JsonElement toJsonState() {
        JsonObject json = new JsonObject();
        json.add(CAMERA_MEMBER_NAME, this.microscope.toJsonCamera());
        json.add(FLUOR_MEMBER_NAME, this.microscope.toJsonFluorescence());
        json.add(LASER_MEMBER_NAME, this.microscope.toJsonLaser());
        json.add(OBJECTIVE_MEMBER_NAME, this.microscope.toJsonObjective());
        json.add(STAGE_MEMBER_NAME, this.microscope.toJsonStage());
        return json;
    }
    
    /**
     * The StateListener listens for changes in the simulation's state.
     * 
     * These changes can occur at any time on a continuous interval between
     * the simulation time steps.
     */
    class StateListener implements Listener {
        
        /**
         * A cache containing the state transition messages.
         */
        ArrayList<Message> transitions = new ArrayList();
        
        /**
         * This method is called by an Observable when its state has changed.
         * 
         * @param data The data object that is passed from the Observable.
         */
        @Override
        public void update(Object data) {
            if (data == null) {
                // No data reported by the Observable.
                return;
            }
            
            try {
                Message msg = (Message) data;
                transitions.add(msg);
            } catch (Exception ex) {
                String err = "Could not coerce the Listener's message into a " +
                             "known message type.";
                LOGGER.log(Level.WARNING, err);
                LOGGER.log(Level.WARNING, ex.getMessage());
            }
        }
        
        /**
         * Dumps the contents of the cache to a JSON string.
         * 
         * Calling this method will irreversibly clear the cache. This method
         * will return null if the cache is empty.
         * 
         * @return The contents of the cache as JSON string or null.
         */
        public List<Message> dumpMessageCache() {
            if (transitions.isEmpty()) {
                return null;
            }
            
            // Copy the messages before clearing the cache.
            ArrayList<Message> messages = new ArrayList<>();
            for (Message msg: transitions) {
                messages.add((Message) DeepCopy.deepCopy(msg));
            }
            
            transitions.clear();
            return messages;
        }
    }
}
