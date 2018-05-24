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

import ch.epfl.leb.sass.models.components.internal.DefaultStage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Kyle M. Douglass
 */
public class DefaultStageTest {
    private DefaultStage stage;
    
    public DefaultStageTest() {
    }
    
    // Ensure that the Stageis newly created for each test.
    @Before
    public void setUp() {
        DefaultStage.Builder builder = new DefaultStage.Builder();
        builder.x(2).y(3).z(-1);
        this.stage = builder.build();
    }

    /**
     * Test of getX method, of class DefaultStage.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        DefaultStage instance = this.stage;
        double expResult = 2;
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of setX method, of class DefaultStage.
     */
    @Test
    public void testSetX() {
        System.out.println("setX");
        DefaultStage instance = this.stage;
        double expResult = 10;
        this.stage.setX(expResult);
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getY method, of class DefaultStage.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        DefaultStage instance = this.stage;
        double expResult = 3;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }
    
        /**
     * Test of setY method, of class DefaultStage.
     */
    @Test
    public void testSetY() {
        System.out.println("setY");
        DefaultStage instance = this.stage;
        double expResult = 11;
        this.stage.setY(expResult);
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getZ method, of class DefaultStage.
     */
    @Test
    public void testGetZ() {
        System.out.println("getZ");
        DefaultStage instance = this.stage;
        double expResult = -1;
        double result = instance.getZ();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of setZ method, of class DefaultStage.
     */
    @Test
    public void testSetZ() {
        System.out.println("setZ");
        DefaultStage instance = this.stage;
        double expResult = 10;
        this.stage.setZ(expResult);
        double result = instance.getZ();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of toJson method, of class DefaultLaser.
     */
    @Test
    public void testToJson() {
        System.out.println("testToJson");
        DefaultStage instance = this.stage;
        
        String result = instance.toJson().toString();
        
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();
        
        assertTrue(String.valueOf(instance.getX())
                      .equals(json.get("x").getAsString()));
        assertTrue(String.valueOf(instance.getY())
                      .equals(json.get("y").getAsString()));
        assertTrue(String.valueOf(instance.getZ())
                      .equals(json.get("z").getAsString()));
    }
}
