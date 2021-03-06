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
 * Defines common methods for listeners, i.e. objects that track
 * {@link ch.epfl.leb.sass.logging.Observable Observables}.
 * 
 * @author Kyle M. Douglass
 */
public interface Listener {
    
    /**
     * This method is called by an Observable when its state has changed.
     * 
     * @param data The data object that is passed from the Observable.
     */
    public void update(Object data) throws WrongMessageTypeException;
    
}
