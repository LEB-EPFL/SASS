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
public class dStormProperties extends FluorophoreProperties {
    
    private final double[][][] Mk;
    
    private final StateSystem state_system;
    
    public dStormProperties(double signal, double background, double k_bl, 
            double k_triplet, double k_triplet_recovery, double k_dark, 
            double k_dark_recovery, double k_dark_recovery_constant) {
        super(signal, background);
        
        if (k_bl<0.0 || k_triplet<=0.0 || k_triplet_recovery<0.0 ||
            k_dark<0.0 || k_dark_recovery<0.0 || k_dark_recovery_constant<0.0) {
            throw new IllegalArgumentException("Rates must be positive.");
        }
        
        // 4 available states
        Mk = new double[][][] {
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
        
        state_system = new StateSystem(4, Mk);
        }

    @Override
    public Fluorophore createFluorophore(Camera camera, double x, double y) {
        return new Fluorophore(camera, signal, state_system, 2, x, y);
    }

    @Override
    public Fluorophore3D createFluorophore3D(Camera camera, double x, double y, double z) {
                return new Fluorophore3D(camera, signal, state_system, 2, x, y, z);
    }
    
    @Override
    public Fluorophore newFluorophore(
            PSFBuilder psfBuilder,
            double x,
            double y,
            double z) {
        int startingState = 0; // Fluorophore start in the emitting state
        return new Fluorophore(
                psfBuilder, signal, state_system, startingState, x, y, z);
    }
}
