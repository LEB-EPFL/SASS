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
package simulator.generators.realtime;

import java.util.Random;

/**
 * A general fluorescent molecule which emits light.
 * @author Marcel Stefko
 */
public abstract class Fluorophore extends Emitter {

    /**
     * Laser power value for which the currently stored lifetime values are calculated.
     */
    protected double current_laser_power;
    
    private final Random random;

    /**
     * Initialize fluorophore and calculate its pattern on camera
     * @param camera Camera used for calculating diffraction pattern
     * @param x x-position in pixels
     * @param y y-position in pixels
     */
    public Fluorophore(Camera camera, double x, double y) {
        super(camera, x, y);
        this.current_laser_power = 0.0;
        this.random = RNG.getUniformGenerator();
    }

    /**
     * Sample an random number from an exponential distribution
     * @param mean mean of the distribution
     * @return random number from this distribution
     */
    protected final double nextExponential(double mean) {
        if (java.lang.Double.isInfinite(mean))
            return java.lang.Double.POSITIVE_INFINITY;
        else
            return Math.log(1 - random.nextDouble()) * (-mean);
    }

    /**
     * Returns the current state of the emitter (on or off), but does not
     * inform if this emitter is also bleached!
     * @return true-emitter is on, false-emitter is off
     */
    public abstract boolean isOn();

    /**
     * Informs if this emitter switched into the irreversible bleached state.
     * @return boolean, true if emitter is bleached
     */
    public abstract boolean isBleached();
    
    /**
     * Recalculates the lifetimes of this emitter based on current laser power.
     * @param laser_power current laser power
     */
    public abstract void recalculate_lifetimes(double laser_power);
}
