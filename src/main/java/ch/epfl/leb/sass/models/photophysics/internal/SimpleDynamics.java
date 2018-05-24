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
package ch.epfl.leb.sass.models.photophysics.internal;

import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamics;
import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamicsBuilder;
import ch.epfl.leb.sass.models.photophysics.StateSystem;

/**
 * Dynamics for a simple three-state system (emitting, non-emitting, and bleached).
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class SimpleDynamics extends FluorophoreDynamics {
    
    /**
     * Fluorophores start in the dark state.
     */
    public final static int STARTINGSTATE = 1;   
    
    /**
     * Creates the SimpleDynamics by calling FlurophoreDynamics constructor.
     * 
     * @param signal The average number of photons emitted per frame.
     * @param wavelength The center wavelength of the fluorescence light.
     * @param stateSystem
     * @param startingState
     * @param Mk Array of transition rates and their dependence on laser power.
     */
    private SimpleDynamics(
            double signal,
            double wavelength,
            StateSystem stateSystem,
            int startingState,
            double[][][] Mk) {
        super(signal, wavelength, stateSystem, startingState, Mk);
    }
    
    /**
     *  Builder for creating Simple dynamical systems.
     **/
    public static class Builder implements FluorophoreDynamicsBuilder {
        private double signal;
        private double wavelength;
        private double tOn;
        private double tOff;
        private double tBl;
        
        /**
         * The average number of photons per fluorophore per frame
         * @param signal
         * @return SimpleDynamics builder
         */
        public Builder signal(double signal) {
            this.signal = signal;
            return this;
        }
        
        /**
         * The center wavelength of the fluorescence emission
         * @param wavelength
         * @return SimpleDynamics builder
         */
        public Builder wavelength(double wavelength) {
            this.wavelength = wavelength;
            return this;
        }
        
        /**
         * The average on time
         * @param tOn
         * @return SimpleDynamics builder
         */
        public Builder tOn(double tOn) {
            this.tOn = tOn;
            return this;
        }
        
        /**
         * The average off time
         * @param tOff
         * @return SimpleDynamics builder
         */
        public Builder tOff(double tOff) {
            this.tOff = tOff;
            return this;
        }
        
        /**
         * The average bleaching time
         * @param tBl
         * @return SimpleDynamics builder
         */
        public Builder tBl(double tBl) {
            this.tBl = tBl;
            return this;
        }
    
    /**
     * Creates a Simple dynamical system.
     * 
     */
        public SimpleDynamics build() {
            if ( !(tOn>=0.0 && tOff>=0.0 && tBl>=0.0) ) {
                throw new IllegalArgumentException("Lifetimes must be non-negative!");
            }
            double k_on, k_off, k_bl;
            if (tOn==0.0) {
                k_off = Double.POSITIVE_INFINITY;
            } else {
                k_off = 1 / tOn;
            }
            if (tOff==0.0) {
                k_on = Double.POSITIVE_INFINITY;
            } else {
                k_on = 1 / tOff;
            }
            if (tBl==0.0) {
                k_bl = Double.POSITIVE_INFINITY;
            } else {
                k_bl = 1 / tBl;
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

            return new SimpleDynamics(
                    signal,
                    wavelength,
                    stateSystem,
                    STARTINGSTATE,
                    Mk);
        }
    }
}
