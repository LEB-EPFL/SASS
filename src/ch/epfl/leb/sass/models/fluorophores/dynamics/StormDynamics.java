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
package ch.epfl.leb.sass.models.fluorophores.dynamics;

import ch.epfl.leb.sass.models.fluorophores.StateSystem;

/**
 * A dynamical system for modeling STORM-like fluorescence dynamics.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class StormDynamics extends FluorophoreDynamics {
    
    /**
     * Fluorophores start in the dark state.
     */
    public final static int STARTINGSTATE = 1;   
    
    /**
     * Creates the StormDynamics by calling the FluorophoreDynamics constructor.
     * 
     * @param signal The average number of photons emitted per frame.
     * @param wavelength The center wavelength of the fluorescence light.
     * @param stateSystem
     * @param startingState
     * @param Mk Array of transition rates and their dependence on laser power.
     */
    private StormDynamics(
            double signal,
            double wavelength,
            StateSystem stateSystem,
            int startingState,
            double[][][] Mk) {
        super(signal, wavelength, stateSystem, startingState, Mk);
    }
    
    public static class Builder implements FluorophoreDynamicsBuilder {
        
        private double signal;
        private double wavelength;
        private double kBl;
        private double kTriplet;
        private double kTripletRecovery;
        private double kDark;
        private double kDarkRecovery;
        private double kDarkRecoveryConstant;
        
        /**
         * The average number of photons per fluorophore per frame
         * @param signal
         * @return StormDynamics builder
         */
        public Builder signal(double signal) {
            this.signal = signal;
            return this;
        }
        
        /**
         * The center wavelength of the fluorescence emission
         * @param wavelength
         * @return StormDynamics builder
         */
        public Builder wavelength(double wavelength) {
            this.wavelength = wavelength;
            return this;
        }
        
        /**
         * The bleaching rate
         * @return StormDynamics Builder
         */
        public Builder kBl(double kBl) {
            this.kBl = kBl;
            return this;
        }
        
        /**
         * The transition to the triplet state
         * @param kTriplet
         * @return StormDynamics builder
         */
        public Builder kTriplet(double kTriplet) {
            this.kTriplet = kTriplet;
            return this;
        }
        
        /**
         * The recovery rate from the triplet state
         * @param kTripletRecovery
         * @return StormDynamics builder
         */
        public Builder kTripletRecovery(double kTripletRecovery) {
            this.kTripletRecovery = kTripletRecovery;
            return this;
        }
        
        /**
         * The transition to the dark state
         * @param kDark
         * @return StormDynamics builder
         */
        public Builder kDark(double kDark) {
            this.kDark = kDark;
            return this;
        }
        
        /**
         * The recovery from the dark state
         * @param kDarkRecovery
         * @return StormDynamics builder
         */
        public Builder kDarkRecovery(double kDarkRecovery) {
            this.kDarkRecovery = kDarkRecovery;
            return this;
        }
        
        /**
         * The constant recovery rate from the dark state
         * @param kDarkRecoveryConstant
         * @return StormDynamics builder
         */
        public Builder kDarkRecoveryConstant(double kDarkRecoveryConstant) {
            this.kDarkRecoveryConstant = kDarkRecoveryConstant;
            return this;
        }
        
        public StormDynamics build() {

            if (kBl<0.0 || kTriplet<=0.0 || kTripletRecovery<0.0 ||
                kDark<0.0 || kDarkRecovery<0.0 || kDarkRecoveryConstant<0.0) {
                throw new IllegalArgumentException("Rates must be positive.");
            }

            // 4 available states
            double [][][]Mk = new double[][][] {
                // state 0: active
                {
                    { 0.0 }, // active
                    { kTriplet }, // triplet
                    { 0.0 }, // reduced
                    { 0.0, kBl }, // bleached
                },
                // state 1: triplet
                {
                    { kTripletRecovery }, // active
                    { 0.0 }, // triplet
                    { kDark }, // reduced
                    { 0.0 }, // bleached
                },
                // state 2: reduced
                {
                    { kDarkRecoveryConstant, kDarkRecovery}, // active
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

            return new StormDynamics(
                    signal,
                    wavelength,
                    stateSystem,
                    STARTINGSTATE,
                    Mk);
        }
    }
}
