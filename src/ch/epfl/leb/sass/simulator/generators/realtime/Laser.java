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
package ch.epfl.leb.sass.simulator.generators.realtime;

/**
 * Class representing the laser shining on the sample.
 * @author Marcel Stefko
 */
public class Laser {
    
    private double current_power;
    private double max_power;
    private double min_power;
    
    /**
     * Initialize laser with given parameters.
     * @param start_power initial power of laser [W]
     * @param max_power maximal laser power [W]
     * @param min_power minimal laser power [W]
     */
    public Laser(double start_power, double max_power,
            double min_power) {
        current_power = start_power;
        this.max_power = max_power;
        this.min_power = min_power;
        
        
    }
    
    /**
     * Sets new laser power if it is within limits, or the closest allowed
     * value if it is outside limits.
     * @param new_power desired laser power [W]
     */
    public void setPower(double new_power) {
        if (new_power > max_power)
            new_power = max_power;
        else if (new_power < min_power)
            new_power = min_power;
        current_power = new_power;
    }
    
    /**
     * Returns current laser power.
     * @return current laser power [W]
     */
    public double getPower() {
        return current_power;
    }
}

