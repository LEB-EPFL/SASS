/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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
package ch.epfl.leb.sass.loggers;

/**
 * Stores data from the FrameLogger.
 * 
 * @author Kyle M. Douglass
 */
    public class FrameInfo {
        public int frame = 0;
        public int id = 0;
        public double x = 0.0;
        public double y = 0.0;
        public double z = 0.0;
        public double brightness = 0.0;
        public double timeOn = 0.0;
        
        /**
         * Creates a new FrameInfo object with all field values set to zero.
         */
        public FrameInfo() {
            
        }
        
        /**
         * Creates a new FrameInfo object from the desired values.
         * 
         * @param frame
         * @param id
         * @param x
         * @param y
         * @param z
         * @param brightness
         * @param timeOn 
         */
        public FrameInfo(
                int frame,
                int id,
                double x,
                double y,
                double z,
                double brightness,
                double timeOn
        ) {
            this.frame = frame;
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
            this.brightness = brightness;
            this.timeOn = timeOn;
        }
    }
