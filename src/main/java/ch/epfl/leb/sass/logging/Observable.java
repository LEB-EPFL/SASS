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
package ch.epfl.leb.sass.logging;

/**
 * Defines interface methods for logging changes in a microscope component.
 * 
 * @author Kyle M. Douglass
 */
public interface Observable {
    
    /**
     * Adds a new listener to the list of subscribed listeners.
     * 
     * @param listener The listener to add to the list of subscribed listeners.
     */
    public void addListener(Listener listener);
    
    /**
     * Deletes a listener from the list of subscribed listeners.
     * 
     * @param listener The listener to delete from the list of listeners.
     */
    public void deleteListener(Listener listener);
    
    /**
     * Notifies all subscribed listeners to a change in the Observable's state.
     * 
     * This method should only be called if setChanged() has been called.
     */
    public void notifyListeners();
    
    /**
     * Notifies all subscribed listeners of a state change and pushes the data.
     * 
     * @param data The data object to push to the listeners.
     */
    public void notifyListeners(Object data);
    
    /**
     * Indicates that the state of this Observable has been changed.
     */
    public void setChanged();
    
}
