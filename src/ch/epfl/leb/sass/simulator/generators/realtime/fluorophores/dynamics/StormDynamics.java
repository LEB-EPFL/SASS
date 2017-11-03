/* 
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * Author: Marcel Stefko
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
 * A dynamical system for modeling STORM-like fluorescence dynamics.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class StormDynamics extends AbstractDynamics {
    
    /**
     * Fluorophores start in the dark state.
     */
    public final static int STARTINGSTATE = 1;   
    
    /**
     * Creates the StormDynamics by calling the AbstractStateSystem constructor.
     * @param stateSystem
     * @param startingState
     * @param Mk Array of transition rates and their dependence on laser power.
     */
    private StormDynamics(
            StateSystem stateSystem,
            int startingState,
            double[][][] Mk) {
        super(stateSystem, startingState, Mk);
    }
    
    public static StormDynamics build(
            double k_bl, 
            double k_triplet,
            double k_triplet_recovery,
            double k_dark, 
            double k_dark_recovery,
            double k_dark_recovery_constant) {
        
        if (k_bl<0.0 || k_triplet<=0.0 || k_triplet_recovery<0.0 ||
            k_dark<0.0 || k_dark_recovery<0.0 || k_dark_recovery_constant<0.0) {
            throw new IllegalArgumentException("Rates must be positive.");
        }
        
        // 4 available states
        double [][][]Mk = new double[][][] {
            // state 0: active
            {
                { 0.0 }, // active
                { k_triplet }, // triplet
                { 0.0 }, // reduced
                { 0.0, k_bl }, // bleached
            },
            // state 1: triplet
            {
                { k_triplet_recovery }, // active
                { 0.0 }, // triplet
                { k_dark }, // reduced
                { 0.0 }, // bleached
            },
            // state 2: reduced
            {
                { k_dark_recovery_constant, k_dark_recovery}, // active
                { 0.0 }, // triplet
                { 0.0 }, // reduced
                { 0.0 }, // bleached
            },
            // state 4: bleached
            {
                { 0.0 }, // active
                { 0.0 }, // triplet
                { 0.0 }, // reduced
                { 0.0 }, // bleached
            }
        };
        
        StateSystem stateSystem = new StateSystem(4, Mk);
        
        return new StormDynamics(stateSystem, STARTINGSTATE, Mk);
    }

}
