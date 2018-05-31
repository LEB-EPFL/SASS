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
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.models.illuminations.Illumination;
import ch.epfl.leb.sass.logging.Listener;
import ch.epfl.leb.sass.logging.WrongMessageTypeException;
import ch.epfl.leb.sass.logging.internal.FluorophoreStateTransition;
import ch.epfl.leb.sass.utils.Constants;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A general fluorescent molecule which emits light.
 * 
 * This class directly implements the methods of Observables, rather than
 * extending AbstractObservable, because Java does not support multiple
 * inheritance.
 * 
 * TODO: IMPLEMENT TESTS FOR THIS CLASS.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class PhysicalFluorophore extends AbstractEmitter implements Fluorophore {
    
    private final static Logger LOGGER
            = Logger.getLogger(PhysicalFluorophore.class.getName());
    
    /**
     * A flag indicating whether the state of this object has changed.
     * 
     * This flag is used only when notifying listeners of a state change.
     */
    private boolean changed;
    
    /**
     * State that this fluorophore is currently in
     */
    private int currentState;
    
    /**
     * The extinction coefficient of the fluorophore.
     * 
     * Typically, this is in units of 1 / Mol / cm.
     */
    private double extinctionCoefficient;
    
    /**
     * The illumination profile on this fluorophore.
     */
    private Illumination illumination;
    
    /**
     * Used to monitor changes in the irradiance at this fluorophore.
     */
    private IlluminationListener illuminationListener;
    
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
     * RNG
     */
    private final Random random;
    
    /**
     * The fluorophore's quantum yield.
     */
    private final double quantumYield;
    
    /**
     * The exposure time of a single camera frame, in seconds.
     * 
     * This is used to compute the photons per frame.
     */
    private double secondsPerFrame;
    
    /**
     * Number of photons per frame.
     */
    private double signal;
    
    /**
     * Internal state system for this fluorophore
     */
    private final StateSystem stateSystem;
    
    /**
     * internal emitter clock for tracking total time elapsed
     */
    private double timeElapsed = 0.0;

    /**
     * Initialize fluorophore and calculate its pattern on camera
     * @param psfBuilder The Builder for calculating microscope PSFs.
     * @param illumination The illumination at the fluorophore.
     * @param quantumYield The fluorophore's quantumYield.
     * @param extinctionCoefficient The fluorophore's extinction coefficient.
     * @param secondsPerFrame The length of a frame's exposure time in seconds.
     * @param stateSystem Internal state system for this fluorophore
     * @param startState Initial state number
     * @param x x-position in pixels
     * @param y y-position in pixels
     * @param z z-position in pixels
     */
    public PhysicalFluorophore(
            PSFBuilder psfBuilder,
            Illumination illumination,
            double quantumYield,
            double extinctionCoefficient,
            double secondsPerFrame,
            StateSystem stateSystem,
            int startState,
            double x,
            double y,
            double z) {
        super(x, y, z, psfBuilder);
        this.illumination = illumination;
        
        if (extinctionCoefficient < 0) {
            throw new IllegalArgumentException("The extinction coefficient " +
                                               "not be less than 0.");
        }
        this.extinctionCoefficient = extinctionCoefficient;
        this.stateSystem = stateSystem;
        this.currentState = startState;
        this.changed = false;
        if (startState >= stateSystem.getNStates()) {
            throw new IllegalArgumentException("Starting state no. is out of " +
                                               "bounds.");
        }
        if (quantumYield < 0 | quantumYield > 1) {
            throw new IllegalArgumentException("Quantum yield must between 0 " +
                                               "and 1.");
        }
        this.quantumYield = quantumYield;
        this.random = RNG.getUniformGenerator();
        this.illuminationListener = new IlluminationListener();
        
        // Sets the signal field.
        try {
            illuminationListener.update(null);
        } catch (WrongMessageTypeException ex) {
            String err = "Could not determine the photon flux for this " +
                         "fluorophore due to a malformed message from the " +
                         "Illumination. Setting the initial signal to zero.";
            LOGGER.log(Level.SEVERE, err);
            signal = 0;
        }
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
        return currentState;
    }
    
    /**
     * Returns the fluorophore extinction coefficient.
     * 
     * @return The fluorophore's extinction coefficient.
     */
    public double getExtinctionCoefficient() {
        return extinctionCoefficient;
    }
    
    /**
     * Returns the fluorophore's illumination profile.
     * 
     * @return The fluorophore's illumination profile.
     */
    public Illumination getIllumination() {
        return illumination;
    }
    
    /**
     * Returns the Listener that is attached to the illumination profile.
     * 
     * @return The illumination Listener.
     */
    @Override
    public Listener getIlluminationListener() {
        return this.illuminationListener;
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
     * Returns the fluorophore's quantum yield.
     * 
     * @return The fluorophore's quantum yield.
     */
    public double getQuantumYield() {
        return quantumYield;
    }
    
    /**
     * Returns the length of a camera exposure in seconds.
     * 
     * @return The length of a camera exposure in seconds.
     */
    public double getSecondsPerFrame() {
        return secondsPerFrame;
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
        return stateSystem.isBleachedState(currentState);
    }
    
    /**
     * Returns the current state of the emitter (on or off), but does not
     * inform if this emitter is also bleached!
     * @return true-emitter is on, false-emitter is off
     */
    public boolean isOn() {
        return stateSystem.isOnState(currentState);
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
                try {
                    l.update(data);
                } catch (WrongMessageTypeException ex) {
                    String err = "Could not notify the Listner "
                                 + l.getClass().getName() + "of the message " +
                                 "sent from the Observable "
                                 + this.getClass().getName() + "because the " +
                                 "wrong type of message was sent.";
                    LOGGER.log(Level.SEVERE, err);
                }
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
        this.stateSystem.recalculate_lifetimes(laserPower);
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
        
        double remainingTime = 1.0;
        double onTime = 0.0;
        while (remainingTime > 0.0) {
            // initialize time of next transition and next state id variables
            double transition_time = java.lang.Double.POSITIVE_INFINITY; 
            int nextState = currentState;
            // for each state transition, draw lifetime of this transition,
            // and keep track which one is the minimal one
            for (int state=0; state<stateSystem.getNStates(); state++) {
                double state_time = nextExponential(stateSystem.getMeanTransitionLifetime(currentState, state));
                if (state_time < transition_time) {
                    nextState = state;
                    transition_time = state_time;
                }
            }
            // transition happens sooner than end of frame
            if (transition_time <= remainingTime) {
                if (this.isOn()) {
                    onTime += transition_time;
                }
                remainingTime -= transition_time;
                timeElapsed += transition_time;
                
                // Notify all listeners of this transition.
                setChanged();
                notifyListeners(new FluorophoreStateTransition(
                                        this.getId(),
                                        timeElapsed,
                                        currentState,
                                        nextState)
                );
                
                currentState = nextState;
            // no transition happens till end of frame
            } else {
                if (this.isOn()) {
                    onTime += remainingTime;
                }
                timeElapsed += remainingTime;
                remainingTime = 0.0;
            }
        }
        // The brightness of the fluorophore
        double brightness = flicker(onTime * signal);
        
        // These are only recorded if the fluorophore was on during the frame.
        if (onTime > 0.0) {
            onTimeThisFrame = onTime;
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
                        .registerTypeAdapter(PhysicalFluorophore.class,
                                             new PhysicalFluorophoreSerializer()
                        )
                        .create();
        return gson.toJsonTree(this);
    }
    
    class IlluminationListener implements Listener {

        /**
         * This method is called by an Illumination profile when its state has changed.
         * 
         * @param data The data object that is passed from the Observable, or
         *             null.
         */
        public void update(Object data) throws WrongMessageTypeException {
            if (isBleached()) {
                return;
            }
            
            double irrad = illumination.getIrradiance(x, y, z);
            double wavelength = illumination.getElectricField().getWavelength();
            // 3.8235e-21 * extinctionCoefficient is the absorption x-section.
            signal = quantumYield * irrad * 3.8235e-21 * extinctionCoefficient
                     * wavelength / Constants.HC * secondsPerFrame;
        }
    }
}

class PhysicalFluorophoreSerializer implements JsonSerializer<PhysicalFluorophore> {
    @Override
    public JsonElement serialize(PhysicalFluorophore src, Type typeOfSrc, JsonSerializationContext context) {
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


