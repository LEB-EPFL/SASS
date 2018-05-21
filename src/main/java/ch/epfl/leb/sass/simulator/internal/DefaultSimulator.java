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
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import ch.epfl.leb.sass.utils.images.internal.DefaultImageS;

import com.google.gson.JsonObject;

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
     * Scaling factor for the density.
     * 
     * Multiplying the estimated density by a factor of 100 means that the
     * output returns spots per 10 um x 10 um = 100 um^2 area.
     */
    private final double SCALEFACTOR = 100;
       
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
        return this.microscope.getFluorescenceJsonName();
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
     * Returns information about the sample's fluorophores as a JSON object.
     * 
     * @return A JSON object containing information on the sample fluorescence.
     */
    @Override
    public JsonObject toJsonFluorescence() {
        return this.microscope.toJsonFluorescence();
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
