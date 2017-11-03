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
 * A dynamical system for modeling PALM-like fluorescence dynamics.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class PalmDynamics extends AbstractDynamics {
    
    /**
     * Fluorophores start in the dark state.
     */
    public final static int STARTINGSTATE = 1;   
    
    /**
     * Creates the PalmDynamics by calling the AbstractStateSystem constructor.
     * 
     * @param stateSystem
     * @param startingState
     * @param Mk Array of transition rates and their dependence on laser power.
     */
    private PalmDynamics(
            StateSystem stateSystem,
            int startingState,
            double[][][] Mk) {
        super(stateSystem, startingState, Mk);
    }
    
    /**
     * Initialize a PALM-like dynamical system for fluorescence dynamics.
     * 
     * @param k_a The activate rate
     * @param k_b The bleaching rate
     * @param k_d1 The rate of entering the first dark state
     * @param k_d2 The rate of entering the second dark state
     * @param k_r1 The return rate from the first dark state
     * @param k_r2 The return rate from the second dark state
     * @return The PALM dynamical system.
     */
    public static PalmDynamics build(
            double k_a,
            double k_b,
            double k_d1, 
            double k_d2,
            double k_r1,
            double k_r2) {
        if (k_a<=0.0 || k_b<0.0 || k_d1<0.0 || k_d2<0.0 || k_r1<0.0 || k_r2<0.0) {
            throw new IllegalArgumentException();
        }
        // 5 available states
        double [][][] Mk = new double[][][] {
            // state 0: active
            {
                { 0.0 }, // active
                { 0.0 }, // inactive
                { k_d1 }, // long dark
                { k_d2 }, // short dark
                { k_b }, // bleached
            },
            // state 1: inactive
            {
                { 0.01 * k_a , k_a }, // active
                { 0.0 }, // inactive
                { 0.0 }, // long dark
                { 0.0 }, // short dark
                { 0.0 }, // bleached
            },
            // state 2: long dark
            {
                { k_r1 }, // active
                { 0.0 }, // inactive
                { 0.0 }, // long dark
                { 0.0 }, // short dark
                { 0.0 }, // bleached
            },
            // state 3: short dark
            {
                { k_r2 }, // active
                { 0.0 }, // inactive
                { 0.0 }, // long dark
                { 0.0 }, // short dark
                { 0.0 }, // bleached
            },
            // state 4: bleached
            {
                { 0.0 }, // active
                { 0.0 }, // inactive
                { 0.0 }, // long dark
                { 0.0 }, // short dark
                { 0.0 }, // bleached
            }
        };
        
        StateSystem stateSystem = new StateSystem(5, Mk);
        
        return new PalmDynamics(stateSystem, STARTINGSTATE, Mk);
    }

}
