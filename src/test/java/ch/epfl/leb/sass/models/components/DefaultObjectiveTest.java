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

import ch.epfl.leb.sass.models.components.internal.DefaultLaser;
import ch.epfl.leb.sass.models.components.internal.DefaultObjective;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kyle M. Douglass
 */
public class DefaultObjectiveTest {
    
    public DefaultObjectiveTest() {
    }

    /**
     * Test of psfFWHM method, of class DefaultObjective.
     */
    @Test
    public void testAiryFWHM() {
        System.out.println("airyFWHM");
        double wavelength = 0.65;
        DefaultObjective.Builder builder = new DefaultObjective.Builder();
        DefaultObjective instance = builder.NA(1.4).mag(60).build();
        double expResult = 14.3185;
        double result = instance.airyFWHM(wavelength);
        assertEquals(expResult, result, 0.0001);
    }
    
    /**
     * Test of psfFWHM method, of class DefaultObjective.
     */
    @Test
    public void testAiryRadius() {
        System.out.println("airyRadius");
        double wavelength = 0.65;
        DefaultObjective.Builder builder = new DefaultObjective.Builder();
        DefaultObjective instance = builder.NA(1.4).mag(60).build();
        double expResult = 16.9929;
        double result = instance.airyRadius(wavelength);
        assertEquals(expResult, result, 0.0001);
    }
    
    /**
     * Test of getMag method, of class DefaultObjective.
     */
    @Test
    public void testGetMag() {
        System.out.println("testGetMag");
        DefaultObjective.Builder builder = new DefaultObjective.Builder();
        DefaultObjective instance = builder.NA(1.4).mag(60).build();
        double expResult = 60;
        double result = instance.getMag();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getNA method, of class DefaultObjective.
     */
    @Test
    public void testGetNA() {
        System.out.println("testGetNA");
        DefaultObjective.Builder builder = new DefaultObjective.Builder();
        DefaultObjective instance = builder.NA(1.4).mag(60).build();
        double expResult = 1.4;
        double result = instance.getNA();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of toJson method, of class DefaultLaser.
     */
    @Test
    public void testToJson() {
        System.out.println("testToJson");
        DefaultObjective.Builder builder = new DefaultObjective.Builder();
        DefaultObjective instance = builder.NA(1.4).mag(60).build();
        
        String result = instance.toJson().toString();
        
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();
        
        assertTrue(String.valueOf(instance.getMag())
                      .equals(json.get("magnification").getAsString()));
        assertTrue(String.valueOf(instance.getNA())
                      .equals(json.get("numerical aperture").getAsString()));
    }
}
