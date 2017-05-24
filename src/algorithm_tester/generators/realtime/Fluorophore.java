/*
 * Copyright (C) 2017 stefko
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
package algorithm_tester.generators.realtime;

import java.util.Random;

/**
 *
 * @author stefko
 */
public abstract class Fluorophore extends Emitter {
    protected double current_laser_power;
    protected final Random random;

    public Fluorophore(Camera camera, double x, double y) {
        super(camera, x, y);
        this.current_laser_power = 0.0;
        this.random = new Random();
    }

    protected double nextExponential(double mean) {
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
    
}
