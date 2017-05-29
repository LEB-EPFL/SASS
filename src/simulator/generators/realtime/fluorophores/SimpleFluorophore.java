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
package simulator.generators.realtime.fluorophores;

import simulator.generators.realtime.Camera;
import simulator.generators.realtime.Fluorophore;

/**
 * SimpleFluorophore defined by its coordinates and underlying fluorophore 
 state time constants.
 * @author Marcel Stefko
 */
public class SimpleFluorophore extends Fluorophore {
    
    private final SimpleProperties fluo;
    
    private boolean state;
    private boolean is_bleached;
    
    private double Ton;
    private double Toff;
    private double Tbl;
    
    
    
    /**
     * Creates new emitter and precalculates its projection on the camera.
     * @param fluorophore underlying fluorophore properties
     * @param camera camera properties (for projection calculations)
     * @param x x-position [pixels]
     * @param y y-position [pixels]
     */
    public SimpleFluorophore(SimpleProperties fluorophore, Camera camera, double x, double y) {
        super(camera, x, y);
        this.fluo = fluorophore;
        this.state = false;
        this.is_bleached = false;

        this.recalculate_lifetimes(0.0);
    }
    
    @Override
    public final void recalculate_lifetimes(double laser_power) {
        if (current_laser_power == laser_power) {
            return;
        }
        
        if (laser_power < 0.0000001) {
            laser_power = 0.0000001;
        }
        
        current_laser_power = laser_power;
        // Calculate time constants
        Ton = fluo.base_Ton;
        Toff = fluo.base_Toff / laser_power;
        Tbl = fluo.base_Tbl / laser_power;
    }
    
    
    
    /**
     * Simulates the state of the emitter for the next frame and returns its
     * integrated brightness over the duration of the frame.
     * @return emitter brightness in this frame [photons]
     */
    @Override
    protected double simulateBrightness() {
        if (is_bleached)
            return 0.0;
        double t=0.0;
        double on_time = 0.0;
        double bleach_time = nextExponential(Tbl);
        boolean does_bleach = (bleach_time < 1.0);
        double limit = does_bleach ? bleach_time : 1.0;
        while (t<limit) {
            double lifetime;
            if (state) {
                lifetime = nextExponential(Ton);
                on_time += ((lifetime < limit-t) ? lifetime : limit-t);
            } else {
                lifetime = nextExponential(Toff);
            }
            t += lifetime;
            if (t<limit)
                state = !state;
        }
        if (does_bleach) {
            is_bleached = true;
            state = false;
        }
        return flicker(on_time*fluo.signal);
    }

    @Override
    public boolean isOn() {
        return state;
    }

    @Override
    public boolean isBleached() {
        return is_bleached;
    }
    
}


