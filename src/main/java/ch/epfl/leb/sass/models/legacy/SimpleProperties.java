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
package ch.epfl.leb.sass.models.legacy;

import ch.epfl.leb.sass.models.legacy.Camera;
import ch.epfl.leb.sass.models.fluorophores.internal.DefaultFluorophore;
import ch.epfl.leb.sass.models.photophysics.StateSystem;
import ch.epfl.leb.sass.models.legacy.Fluorophore3D;
import ch.epfl.leb.sass.models.legacy.FluorophoreProperties;
import ch.epfl.leb.sass.models.legacy.MovingFluorophore;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * SimpleProperties properties (signal, background values and time constants).
 * @author Marcel Stefko
 * @deprecated Use the SimpleStateSystem instead.
 */
@Deprecated
public class SimpleProperties extends FluorophoreProperties {
    
    /**
     * Fluorophores start in the dark state.
     */
    private final int startingState = 1; 

    private final double[][][] Mk;
    
    private final StateSystem state_system;
    
    /**
     * Initialize fluorophore with given properties
     * @param signal_per_frame photons emitted if fluorophore is fully on
     * @param background_per_frame constant background of the fluorophore
     * @param Ton mean on-time with unit laser power [frames]
     * @param Toff mean off-time with unit laser power [frames]
     * @param Tbl mean bleaching time with unit laser power [frames]
     * @deprecated Use {@link #SimpleProperties(double, double, double, double, double, double) }
     *             instead.
     */
    @Deprecated
    public SimpleProperties(double signal_per_frame, double background_per_frame, 
            double Ton, 
            double Toff, double Tbl) {
        super(signal_per_frame, background_per_frame);
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
        Mk = new double[][][] {
            // state 0: active
            {
                { 0.0 }, // active
                { k_off }, // dark
                { 0.0, k_bl }, // bleached
            },
            // state 1: dark
            {
                { 0.0, k_on }, // active
                { 0.0 }, // dark
                { 0.0, k_bl }, // bleached
            },
            // state 2: long dark
            {
                { 0.0 }, // active
                { 0.0 }, // dark
                { 0.0 }, // bleached
            }
        };
                
        state_system = new StateSystem(3, Mk);
    }
    
    /**
     * Initialize fluorophore with given properties
     * @param signal_per_frame photons emitted if fluorophore is fully on
     * @param wavelength The emission wavelength of the fluorophore
     * @param background_per_frame constant background of the fluorophore
     * @param Ton mean on-time with unit laser power [frames]
     * @param Toff mean off-time with unit laser power [frames]
     * @param Tbl mean bleaching time with unit laser power [frames]
     */
    public SimpleProperties(
            double signal_per_frame,
            double wavelength,
            double background_per_frame, 
            double Ton, 
            double Toff, double Tbl) {
        super(signal_per_frame, background_per_frame, wavelength);
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
        Mk = new double[][][] {
            // state 0: active
            {
                { 0.0 }, // active
                { k_off }, // dark
                { 0.0, k_bl }, // bleached
            },
            // state 1: dark
            {
                { 0.0, k_on }, // active
                { 0.0 }, // dark
                { 0.0, k_bl }, // bleached
            },
            // state 2: long dark
            {
                { 0.0 }, // active
                { 0.0 }, // dark
                { 0.0 }, // bleached
            }
        };
                
        state_system = new StateSystem(3, Mk);
    }

    @Override
    public DefaultFluorophore createFluorophore(Camera camera, double x, double y) {
        return new DefaultFluorophore(camera, signal, state_system, 1, x, y);
    }
    
    /**
     * Creates a moving variant of simple fluorophore
     * @param camera
     * @param x
     * @param y
     * @param trajectory
     * @return
     */
    public MovingFluorophore createMovingFluorophore(Camera camera, double x, double y, ArrayList<Point2D.Double> trajectory) {
        return new MovingFluorophore(camera, signal, state_system, 1, x, y, trajectory);
    }

    @Override
    public Fluorophore3D createFluorophore3D(Camera camera, double x, double y, double z) {
                return new Fluorophore3D(camera, signal, state_system, 1, x, y, z);
    }
    
    @Override
    public DefaultFluorophore newFluorophore(
            PSFBuilder psfBuilder,
            double x,
            double y,
            double z) {
        return new DefaultFluorophore(
                psfBuilder, signal, state_system, startingState, x, y, z);
    }
}
