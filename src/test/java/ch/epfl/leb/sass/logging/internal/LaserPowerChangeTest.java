/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne, Switzerland
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.logging.internal;

import ch.epfl.leb.sass.logging.MessageType;

import com.google.gson.JsonObject;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the LaserPowerChange Message.
 * 
 * @author Kyle M. Douglass
 */
public class LaserPowerChangeTest {
    
    public LaserPowerChangeTest() {
    }

    /**
     * Test of getType method, of class LaserPowerChange.
     */
    @Test
    public void testGetType() {
        System.out.println("testGetType");
        LaserPowerChange instance = new LaserPowerChange(5.0);
        
        assertEquals(MessageType.LASER_POWER_CHANGE, instance.getType());
    }

    /**
     * Test of toJson method, of class LaserPowerChange.
     */
    @Test
    public void testToJson() {
        System.out.println("testToJson");
        LaserPowerChange instance = new LaserPowerChange(5.0);
        
        JsonObject json = instance.toJson().getAsJsonObject();
        double result = json.get("power").getAsDouble();
        
        assertEquals(5.0, result, 0.0);
        
    }
    
}
