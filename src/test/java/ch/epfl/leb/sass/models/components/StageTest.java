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
package ch.epfl.leb.sass.models.components;

import ch.epfl.leb.sass.models.components.Stage;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author kmdouglass
 */
public class StageTest {
    private Stage stage;
    
    public StageTest() {
    }
    
    // Ensure that the Stageis newly created for each test.
    @Before
    public void setUp() {
        Stage.Builder builder = new Stage.Builder();
        builder.x(2).y(3).z(-1);
        this.stage = builder.build();
    }

    /**
     * Test of getX method, of class Stage.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        Stage instance = this.stage;
        double expResult = 2;
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of setX method, of class Stage.
     */
    @Test
    public void testsetX() {
        System.out.println("setX");
        Stage instance = this.stage;
        double expResult = 10;
        this.stage.setX(expResult);
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getY method, of class Stage.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        Stage instance = this.stage;
        double expResult = 3;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }
    
        /**
     * Test of setY method, of class Stage.
     */
    @Test
    public void testsetY() {
        System.out.println("setY");
        Stage instance = this.stage;
        double expResult = 11;
        this.stage.setY(expResult);
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getZ method, of class Stage.
     */
    @Test
    public void testGetZ() {
        System.out.println("getZ");
        Stage instance = this.stage;
        double expResult = -1;
        double result = instance.getZ();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of setZ method, of class Stage.
     */
    @Test
    public void testsetZ() {
        System.out.println("setZ");
        Stage instance = this.stage;
        double expResult = 10;
        this.stage.setZ(expResult);
        double result = instance.getZ();
        assertEquals(expResult, result, 0.0);
    }
}
