/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
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
package ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.dynamics;

import ch.epfl.leb.sass.simulator.generators.realtime.StateSystem;

/**
 * A fluorophore state system.
 */
public abstract class AbstractDynamics {
    
    protected final StateSystem stateSystem;
    
    /**
     * Fluorophores start in the dark state.
     */
    private final int startingState; 

    private final double[][][] Mk;
    
    /**
     * Initializes the state system with the transition rates and starting state.
     * 
     * @param stateSystem
     * @param startingState
     * @param Mk 
     */
    protected AbstractDynamics(
            StateSystem stateSystem, 
            int startingState,
            double[][][] Mk) {
        this.stateSystem = stateSystem;
        this.startingState = startingState;
        this.Mk = Mk;
    }
    
    public StateSystem getStateSystem() { return this.stateSystem; }
    
    public int getStartingState() { return this.startingState; }
    
    public double[][][] getMk() { return this.Mk; }
}
