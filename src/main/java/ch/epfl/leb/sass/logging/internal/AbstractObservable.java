/*
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
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.logging.internal;

import ch.epfl.leb.sass.logging.Listener;
import ch.epfl.leb.sass.logging.Observable;
import ch.epfl.leb.sass.logging.WrongMessageTypeException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides functionality common to all Observables.
 * 
 * @author Kyle M. Douglass
 */
public abstract class AbstractObservable implements Observable {
    
    protected static final Logger LOGGER
            = Logger.getLogger(AbstractObservable.class.getName());
    
    /**
     * A flag indicating whether the state of this object has changed.
     * 
     * This flag is used only when notifying listeners of a state change.
     */
    protected boolean changed;
    
    /**
     * The list of listeners that are tracking this object.
     */
    protected ArrayList<Listener> listeners = new ArrayList();
    
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
     * Indicates that the state of this Observable has been changed.
     */
    @Override
    public void setChanged() {
        changed = true;
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
}
