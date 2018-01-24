/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
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
package ch.epfl.leb.sass.simulator.generators.realtime.components;

import ch.epfl.leb.sass.models.components.Laser;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author kmdouglass
 */
public class LaserTest {
    private Laser laser;
    
    public LaserTest() {
    }

    @Before
    public void setUp() {
        Laser.Builder builder = new Laser.Builder();
        builder.currentPower(10).maxPower(100).minPower(0);
        this.laser = builder.build();
    }
    /**
     * Test of setPower method, of class Laser.
     */
    @Test
    public void testSetPower() {
        System.out.println("setPower");
        double newPower = 57.8;
        Laser instance = this.laser;
        instance.setPower(newPower);
    }

    /**
     * Test of getPower method, of class Laser.
     */
    @Test
    public void testGetPower() {
        System.out.println("getPower");
        Laser instance = this.laser;
        double expResult = 10;
        double result = instance.getPower();
        assertEquals(expResult, result, 0.0);
    }
    
}
