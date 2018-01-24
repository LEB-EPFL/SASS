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
package ch.epfl.leb.sass.models.fluorophores.internal.dynamics;

import ch.epfl.leb.sass.models.fluorophores.internal.StateSystem;

/**
 * A dynamical system for modeling PALM-like fluorescence dynamics.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class PalmDynamics extends FluorophoreDynamics {
    
    /**
     * Fluorophores start in the dark state.
     */
    public final static int STARTINGSTATE = 1;   
    
    /**
     * Creates the PalmDynamics by calling the FlurophoreDynamics constructor.
     * 
     * @param signal The average number of photons emitted per frame.
     * @param wavelength The center wavelength of the fluorescence light.
     * @param stateSystem
     * @param startingState
     * @param Mk Array of transition rates and their dependence on laser power.
     */
    private PalmDynamics(
            double signal,
            double wavelength,
            StateSystem stateSystem,
            int startingState,
            double[][][] Mk) {
        super(signal, wavelength, stateSystem, startingState, Mk);
    }
    /**
     *  Builder for creating PALM dynamical systems.
     **/
    public static class Builder implements FluorophoreDynamicsBuilder {
        private double signal;
        private double wavelength;
        private double kA;
        private double kB;
        private double kD1;
        private double kD2;
        private double kR1;
        private double kR2;
        
        /**
         * The average number of photons per fluorophore per frame
         * @param signal
         * @return PalmDynamics builder
         */
        public Builder signal(double signal) {
            this.signal = signal;
            return this;
        }
        
        /**
         * The center wavelength of the fluorescence emission
         * @param wavelength
         * @return PalmDynamics builder
         */
        public Builder wavelength(double wavelength) {
            this.wavelength = wavelength;
            return this;
        }
                
        /**
         * The activation rate
         * @param kA
         */
        public Builder kA(double kA) { this.kA = kA; return this; }
                
        /**
         * The bleaching rate
         */
        public Builder kB(double kB) { this.kB = kB; return this; }
        
                /**
         * The rate of entering the first dark state
         */
        public Builder kD1(double kD1) { this.kD1 = kD1; return this; }
        
        /**
         * The rate of entering the second dark state
         */
        public Builder kD2(double kD2) { this.kD2 = kD2; return this; }
        
        /**
         * The return rate from the first dark state
         */
        public Builder kR1(double kR1) { this.kR1 = kR1; return this; }
        
        /**
         * The return rate from the second dark state
         */
        public Builder kR2(double kR2) { this.kR2 = kR2; return this; }
        
        /**
        * Initialize a PALM-like dynamical system for fluorescence dynamics.
        * 
        * @return The PALM dynamical system.
        */
       public PalmDynamics build() {
           if (kA<=0.0 || kB<0.0 || kD1<0.0 || kD2<0.0 || kR1<0.0 || kR2<0.0) {
               throw new IllegalArgumentException();
           }
           // 5 available states
           double [][][] Mk = new double[][][] {
               // state 0: active
               {
                   { 0.0 }, // active
                   { 0.0 }, // inactive
                   { kD1 }, // long dark
                   { kD2 }, // short dark
                   { kB }, // bleached
               },
               // state 1: inactive
               {
                   { 0.01 * kA , kA }, // active
                   { 0.0 }, // inactive
                   { 0.0 }, // long dark
                   { 0.0 }, // short dark
                   { 0.0 }, // bleached
               },
               // state 2: long dark
               {
                   { kR1 }, // active
                   { 0.0 }, // inactive
                   { 0.0 }, // long dark
                   { 0.0 }, // short dark
                   { 0.0 }, // bleached
               },
               // state 3: short dark
               {
                   { kR2 }, // active
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

           return new PalmDynamics(
                   signal,
                   wavelength,
                   stateSystem,
                   STARTINGSTATE,
                   Mk);
       }
    }
}
