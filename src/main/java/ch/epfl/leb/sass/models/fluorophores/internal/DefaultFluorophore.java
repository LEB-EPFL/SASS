/* 
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne, Switzerland
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
package ch.epfl.leb.sass.models.fluorophores.internal;

import ch.epfl.leb.sass.models.photophysics.StateSystem;
import ch.epfl.leb.sass.models.emitters.internal.AbstractEmitter;
import ch.epfl.leb.sass.utils.RNG;
import ch.epfl.leb.sass.models.legacy.Camera;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.logging.Listener;
import ch.epfl.leb.sass.logging.internal.FluorophoreStateTransition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Random;
import java.util.ArrayList;

/**
 * A general fluorescent molecule which emits light.
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class DefaultFluorophore extends AbstractEmitter implements Fluorophore {
    
    /**
     * A flag indicating whether the state of this object has changed.
     * 
     * This flag is used only when notifying listeners of a state change.
     */
    private boolean changed;
    
    /**
     * The list of listeners that are tracking this object.
     */
    private ArrayList<Listener> listeners = new ArrayList();
    
    /**
     * The amount of time that the fluorophore spent in the emitting state during the frame.
     */
    private double onTimeThisFrame = 0.0;
    
    /**
     * How many photons the fluorophore emitted during the most recent frame.
     */
    private double photonsThisFrame = 0.0;
    /**
     * internal emitter clock for tracking total time elapsed
     */
    private double time_elapsed = 0.0;
    
    /**
     * RNG
     */
    private final Random random;
    
    /**
     * Internal state system for this fluorophore
     */
    private final StateSystem state_system;
    
    /**
     * State that this fluorophore is currently in
     */
    private int current_state;
    
    /**
     * No of photons per frame.
     */
    private final double signal;
    
    /**
     * Initialize fluorophore and calculate its pattern on camera
     * @param camera Camera used for calculating diffraction pattern
     * @param signal No of photons per frame.
     * @param state_system Internal state system for this fluorophore
     * @param start_state Initial state number
     * @param x x-position in pixels
     * @param y y-position in pixels
     * @deprecated 
     */
    @Deprecated
    public DefaultFluorophore(Camera camera, double signal, StateSystem state_system, int start_state, double x, double y) {
        super(camera, x, y);
        this.state_system = state_system;
        this.signal = signal;
        this.current_state = start_state;
        if (start_state >= state_system.getNStates()) {
            throw new IllegalArgumentException("Starting state no. is out of bounds.");
        }
        this.random = RNG.getUniformGenerator();
    }
    
     /**
     * Initialize fluorophore and calculate its pattern on camera
     * @param psfBuilder The Builder for calculating microscope PSFs.
     * @param signal Number of photons per frame.
     * @param state_system Internal state system for this fluorophore
     * @param start_state Initial state number
     * @param x x-position in pixels
     * @param y y-position in pixels
     * @param z z-position in pixels
     */
    public DefaultFluorophore(
            PSFBuilder psfBuilder,
            double signal,
            StateSystem state_system,
            int start_state,
            double x,
            double y,
            double z) {
        super(x, y, z, psfBuilder);
        this.state_system = state_system;
        this.signal = signal;
        this.current_state = start_state;
        this.changed = false;
        if (start_state >= state_system.getNStates()) {
            throw new IllegalArgumentException("Starting state no. is out of " +
                                               "bounds.");
        }
        this.random = RNG.getUniformGenerator();
    }

    /**
     * Adds a new listener to the list of subscribed listeners.
     */
    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    /**
     * Deletes a listener from the list of subscribed listeners.
     */
    @Override
    public void deleteListener(Listener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Returns the id of the fluorophore state system's current state.
     * 
     * @return The id of the current state of the fluorophore's state system.
     */
    public int getCurrentState() {
        return current_state;
    }
    
    /**
     * Returns the time spent in the emitting state during the previous frame.
     * 
     * This time is the proportion of the frame's duration; 1 corresponds to
     * having spent the entirety of the frame in the emitting state.
     * 
     * @return The time spent in the emitting state.
     */
    public double getOnTimeThisFrame() {
        return onTimeThisFrame;
    }
    
    /**
     * Returns the number of photons emitted during the previous frame.
     *
     * @return The number of photons emitted during the previous frame.
     */
    public double getPhotonsThisFrame() {
        return photonsThisFrame;
    }
    
    /**
     * Returns the fluorophore's number of photons per frame.
     * 
     * @return The number of photons per frame emitted by the fluorophore.
     */
    public double getSignal() {
        return signal;
    }
    
    /**
     * Return the x-position of the fluorophore.
     * 
     * @return The fluorophore's x-position.
     */
    public double getX() { return this.x; }
    
    /**
     * Return the y-position of the fluorophore.
     * 
     * @return The fluorophore's y-position.
     */
    public double getY() { return this.y; }
    
    /**
     * Return the z-position of the fluorophore.
     * 
     * @return The fluorophore's z-position.
     */
    public double getZ() { return this.z; }

    /**
     * Informs if this emitter switched into the irreversible bleached state.
     * @return boolean, true if emitter is bleached
     */
    public boolean isBleached() {
        return state_system.isBleachedState(current_state);
    }
    
    /**
     * Returns the current state of the emitter (on or off), but does not
     * inform if this emitter is also bleached!
     * @return true-emitter is on, false-emitter is off
     */
    public boolean isOn() {
        return state_system.isOnState(current_state);
    }
    
    /**
     * Sample an random number from an exponential distribution
     * @param mean mean of the distribution
     * @return random number from this distribution
     */
    protected final double nextExponential(double mean) {
        if (java.lang.Double.isInfinite(mean))
            return java.lang.Double.POSITIVE_INFINITY;
        else
            return Math.log(1 - random.nextDouble()) * (-mean);
    }
    
    /**
     * Notifies all subscribed listeners to a change in the Observable's state.
     * 
     * This method should only be called if setChanged() has been called.
     */
    @Override
    public void notifyListeners() {
        notifyListeners(null);
    }
    
    /**
     * Notifies all subscribed listeners of a state change and pushes the data.
     * 
     * @param data The data object to push to the listeners.
     */
    @Override
    public void notifyListeners(Object data) {
        if (changed) {
            for (Listener l: listeners) {
                l.update(data);
            }
            changed = false;
        }
    }
    
    /**
     * Recalculates the lifetimes of this emitter based on current laser power.
     * @param laserPower current laser power
     */
    @Override
    public void recalculateLifetimes(double laserPower) {
        this.state_system.recalculate_lifetimes(laserPower);
    }
    
    /**
     * Indicates that the state of this Observable has been changed.
     */
    @Override
    public void setChanged() {
        changed = true;
    }

    @Override
    protected double simulateBrightness() {
        // Reset the frame tracking variables
        onTimeThisFrame = 0.0;
        photonsThisFrame = 0.0;
        
        if (isBleached()) {
            return 0.0;
        }
        
        double remaining_time = 1.0;
        double on_time = 0.0;
        while (remaining_time > 0.0) {
            // initialize time of next transition and next state id variables
            double transition_time = java.lang.Double.POSITIVE_INFINITY; 
            int next_state = current_state;
            // for each state transition, draw lifetime of this transition,
            // and keep track which one is the minimal one
            for (int state=0; state<state_system.getNStates(); state++) {
                double state_time = nextExponential(state_system.getMeanTransitionLifetime(current_state, state));
                if (state_time < transition_time) {
                    next_state = state;
                    transition_time = state_time;
                }
            }
            // transition happens sooner than end of frame
            if (transition_time <= remaining_time) {
                if (this.isOn()) {
                    on_time += transition_time;
                }
                remaining_time -= transition_time;
                time_elapsed += transition_time;
                
                // Notify all listeners of this transition.
                setChanged();
                notifyListeners(new FluorophoreStateTransition(
                                        this.getId(),
                                        time_elapsed,
                                        current_state,
                                        next_state)
                );
                
                current_state = next_state;
            // no transition happens till end of frame
            } else {
                if (this.isOn()) {
                    on_time += remaining_time;
                }
                time_elapsed += remaining_time;
                remaining_time = 0.0;
            }
        }
        // The brightness of the fluorophore
        double brightness = flicker(on_time*signal);
        
        // These are only recorded if the fluorophore was on during the frame.
        if (on_time > 0.0) {
            onTimeThisFrame = on_time;
            photonsThisFrame = brightness;
        }
        return brightness;
    }
    
    /**
     * Returns the fluorophore's properties as a JSON string.
     * @return The properties of the fluorophore as a JSON string.
     */
    @Override
    public JsonElement toJson() {
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(DefaultFluorophore.class,
                                             new DefaultFluorophoreSerializer())
                        .create();
        return gson.toJsonTree(this);
    }
}

class DefaultFluorophoreSerializer implements JsonSerializer<DefaultFluorophore> {
    @Override
    public JsonElement serialize(DefaultFluorophore src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("id", new JsonPrimitive(src.getId()));
        result.add("x", new JsonPrimitive(src.x));
        result.add("y", new JsonPrimitive(src.y));
        result.add("z", new JsonPrimitive(src.z));
        result.add("currentState", new JsonPrimitive(src.getCurrentState()));
        result.add("maxPhotonsPerFrame", new JsonPrimitive(src.getSignal()));
        result.add("bleached", new JsonPrimitive(src.isBleached()));
        result.add("emitting", new JsonPrimitive(src.isOn()));
        result.add("onTime", new JsonPrimitive(src.getOnTimeThisFrame()));
        result.add("photonsEmittedLastFrame", 
                   new JsonPrimitive(src.getPhotonsThisFrame()));
        return result;
    }
}


