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
package ch.epfl.leb.sass.simulator.generators.realtime.fluorophores;

import ch.epfl.leb.sass.simulator.generators.realtime.Camera;
import ch.epfl.leb.sass.simulator.generators.realtime.Fluorophore;
import ch.epfl.leb.sass.simulator.generators.realtime.Fluorophore3D;
import ch.epfl.leb.sass.simulator.generators.realtime.FluorophoreProperties;
import ch.epfl.leb.sass.simulator.generators.realtime.StateSystem;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSFBuilder;

/**
 *
 * @author stefko
 */
public class PalmProperties extends FluorophoreProperties {

    private final double[][][] Mk;
    
    private final StateSystem state_system;
   
    /**
     * 
     * @param signal
     * @param background
     * @param k_a
     * @param k_b
     * @param k_d1
     * @param k_d2
     * @param k_r1
     * @param k_r2
     * @deprecated Use {@link #PalmProperties(double, double, double, double, double, double, double, double, double) }
     *             instead.
     */
    @Deprecated
    public PalmProperties(double signal, double background, 
            double k_a, double k_b, double k_d1, 
            double k_d2, double k_r1, double k_r2) {
        super(signal, background);
        if (k_a<=0.0 || k_b<0.0 || k_d1<0.0 || k_d2<0.0 || k_r1<0.0 || k_r2<0.0) {
            throw new IllegalArgumentException();
        }
        // 5 available states
        Mk = new double[][][] {
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
        
        state_system = new StateSystem(5, Mk);
        
    }

    public PalmProperties(
            double signal,
            double wavelength,
            double background, 
            double k_a,
            double k_b,
            double k_d1, 
            double k_d2,
            double k_r1,
            double k_r2) {
        super(signal, background, wavelength);
        if (k_a<=0.0 || k_b<0.0 || k_d1<0.0 || k_d2<0.0 || k_r1<0.0 || k_r2<0.0) {
            throw new IllegalArgumentException();
        }
        // 5 available states
        Mk = new double[][][] {
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
        
        state_system = new StateSystem(5, Mk);
        
    }
    
    @Override
    public Fluorophore createFluorophore(Camera camera, double x, double y) {
        return new Fluorophore(camera, signal, state_system, 1, x, y);
    }

    @Override
    public Fluorophore3D createFluorophore3D(Camera camera, double x, double y, double z) {
        return new Fluorophore3D(camera, signal, state_system, 1, x, y, z);
    }
    
    @Override
    public Fluorophore newFluorophore(
            PSFBuilder psfBuilder, double x, double y, double z) {
        int startingState = 1; // Fluorophore start in the non-emitting state
        return new Fluorophore(
                psfBuilder, signal, state_system, startingState, x, y, z);
    }
}
