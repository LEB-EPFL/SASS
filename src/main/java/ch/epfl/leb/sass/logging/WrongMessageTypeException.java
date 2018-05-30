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
package ch.epfl.leb.sass.logging;

/**
 * Raised when a Listener receives an unexpected message type.
 * 
 * @author Kyle M. Douglass
 */
public class WrongMessageTypeException extends Exception {
    
    public WrongMessageTypeException() {}
    
    /**
     * 
     * @param msg An error message describing what raised this exception.
     */
    public WrongMessageTypeException(String msg) {
        super(msg);
    }
    
}
