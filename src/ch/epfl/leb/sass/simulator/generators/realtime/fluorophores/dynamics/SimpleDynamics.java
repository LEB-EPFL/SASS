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
 * Dynamics for a simple three-state system (emitting, non-emitting, and bleached).
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class SimpleDynamics extends AbstractDynamics {
    
    /**
     * Fluorophores start in the dark state.
     */
    public final static int STARTINGSTATE = 1;   
    
    /**
     * Creates the SimpleDynamics by calling the AbstractStateSystem constructor.
     * @param stateSystem
     * @param startingState
     * @param Mk Array of transition rates and their dependence on laser power.
     */
    private SimpleDynamics(
            StateSystem stateSystem,
            int startingState,
            double[][][] Mk) {
        super(stateSystem, startingState, Mk);
    }
    
    /**
     * Initialize fluorophore with given properties.
     * 
     * @param Ton mean on-time with unit laser power [frames]
     * @param Toff mean off-time with unit laser power [frames]
     * @param Tbl mean bleaching time with unit laser power [frames]
     */
    public static SimpleDynamics build(
            double Ton, 
            double Toff,
            double Tbl) {
        if ( !(Ton>=0.0 && Toff>=0.0 && Tbl>=0.0) ) {
            throw new IllegalArgumentException("Lifetimes must be non-negative!");
        }
        double k_on, k_off, k_bl;
        if (Ton==0.0) {
            k_off = Double.POSITIVE_INFINITY;
        } else {
            k_off = 1 / Ton;
        }
        if (Toff==0.0) {
            k_on = Double.POSITIVE_INFINITY;
        } else {
            k_on = 1 / Toff;
        }
        if (Tbl==0.0) {
            k_bl = Double.POSITIVE_INFINITY;
        } else {
            k_bl = 1 / Tbl;
        }
        
        
        // 3 states
        double[][][] Mk = new double[][][] {
            // state 0: active
            {
                { 0.0 },       // active
                { k_off },     // dark
                { 0.0, k_bl }, // bleached
            },
            // state 1: dark
            {
                { 0.0, k_on }, // active
                { 0.0 },       // dark
                { 0.0, k_bl }, // bleached
            },
            // state 2: long dark
            {
                { 0.0 }, // active
                { 0.0 }, // dark
                { 0.0 }, // bleached
            }
        };
        
        StateSystem stateSystem = new StateSystem(3, Mk);
        
        return new SimpleDynamics(stateSystem, STARTINGSTATE, Mk);
    }

}
