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

import ch.epfl.leb.sass.models.components.internal.DefaultCamera;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the DefaultCamera class.
 * 
 * @author Kyle M. Douglass
 */
public class DefaultCameraTest {
    private DefaultCamera camera;
    
    public DefaultCameraTest() {
        DefaultCamera.Builder builder = new DefaultCamera.Builder();
        
        builder.aduPerElectron(2.2).baseline(100).darkCurrent(1)
               .emGain(100).pixelSize(6.5).quantumEfficiency(0.7)
               .readoutNoise(1.6).thermalNoise(0.2).nX(256).nY(256);
        
        this.camera = builder.build();
    }

    /**
     * Test of getAduPerElectron method, of class DefaultCamera.
     */
    @Test
    public void testGetAduPerElectron() {
        System.out.println("getAduPerElectron");
        DefaultCamera instance = this.camera;
        double expResult = 2.2;
        double result = instance.getAduPerElectron();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBaseline method, of class DefaultCamera.
     */
    @Test
    public void testGetBaseline() {
        System.out.println("getBaseline");
        DefaultCamera instance = this.camera;
        int expResult = 100;
        int result = instance.getBaseline();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDarkCurrent method, of class DefaultCamera.
     */
    @Test
    public void testGetDarkCurrent() {
        System.out.println("getDarkCurrent");
        DefaultCamera instance = this.camera;
        double expResult = 1;
        double result = instance.getDarkCurrent();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getEmGain method, of class DefaultCamera.
     */
    @Test
    public void testGetEmGain() {
        System.out.println("getEmGain");
        DefaultCamera instance = this.camera;
        double expResult = 100;
        double result = instance.getEmGain();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getPixelSize method, of class DefaultCamera.
     */
    @Test
    public void testGetPixelSize() {
        System.out.println("getPixelSize");
        DefaultCamera instance = this.camera;
        double expResult = 6.5;
        double result = instance.getPixelSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getQuantumEfficiency method, of class DefaultCamera.
     */
    @Test
    public void testGetQuantumEfficiency() {
        System.out.println("getQuantumEfficiency");
        DefaultCamera instance = this.camera;
        double expResult = 0.7;
        double result = instance.getQuantumEfficiency();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getReadoutNoise method, of class DefaultCamera.
     */
    @Test
    public void testGetReadoutNoise() {
        System.out.println("getReadoutNoise");
        DefaultCamera instance = this.camera;
        double expResult = 1.6;
        double result = instance.getReadoutNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getThermalNoise method, of class DefaultCamera.
     */
    @Test
    public void testGetThermalNoise() {
        System.out.println("getThermalNoise");
        DefaultCamera instance = this.camera;
        double expResult = 0.2;
        double result = instance.getThermalNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getNX method, of class DefaultCamera.
     */
    @Test
    public void testGetNX() {
        System.out.println("getNX");
        DefaultCamera instance = this.camera;
        int expResult = 256;
        int result = instance.getNX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNY method, of class DefaultCamera.
     */
    @Test
    public void testGetNY() {
        System.out.println("getNY");
        DefaultCamera instance = this.camera;
        int expResult = 256;
        int result = instance.getNY();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toJson method, of class DefaultCamera.
     */
    @Test
    public void testToJson() {
        System.out.println("testToJson");
        DefaultCamera instance = this.camera;
        
        String result = instance.toJson().toString();
        
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();
        
        assertTrue(String.valueOf(instance.getAduPerElectron()).equals(json.get("aduPerElectron").getAsString()));
        assertTrue(String.valueOf(instance.getBaseline()).equals(json.get("baseline").getAsString()));
        assertTrue(String.valueOf(instance.getDarkCurrent()).equals(json.get("darkCurrent").getAsString()));
        assertTrue(String.valueOf(instance.getEmGain()).equals(json.get("emGain")
                                               .getAsString()));
        assertTrue(String.valueOf(instance.getNX()).equals(json.get("nPixelsX")
                                                   .getAsString()));
        assertTrue(String.valueOf(instance.getNY()).equals(json.get("nPixelsY")
                                                   .getAsString()));
        assertTrue(String.valueOf(instance.getPixelSize()).equals(json.get("pixelSize").getAsString()));
        assertTrue(String.valueOf(instance.getQuantumEfficiency()).equals(json.get("quantumEfficiency").getAsString()));
        assertTrue(String.valueOf(instance.getReadoutNoise()).equals(json.get("readoutNoise").getAsString()));
        assertTrue(String.valueOf(instance.getThermalNoise()).equals(json.get("thermalNoise").getAsString()));
    }
    
}
