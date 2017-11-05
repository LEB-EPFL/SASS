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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Camera class.
 * 
 * @author Kyle M. Douglass
 */
public class CameraTest {
    private Camera camera;
    
    public CameraTest() {
        Camera.Builder builder = new Camera.Builder();
        
        builder.aduPerElectron(2.2).baseline(100).darkCurrent(1)
               .emGain(100).pixelSize(6.5).quantumEfficiency(0.7)
               .readoutNoise(1.6).thermalNoise(0.2).nX(256).nY(256);
        
        this.camera = builder.build();
    }

    /**
     * Test of getAduPerElectron method, of class Camera.
     */
    @Test
    public void testGetAduPerElectron() {
        System.out.println("getAduPerElectron");
        Camera instance = this.camera;
        double expResult = 2.2;
        double result = instance.getAduPerElectron();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBaseline method, of class Camera.
     */
    @Test
    public void testGetBaseline() {
        System.out.println("getBaseline");
        Camera instance = this.camera;
        int expResult = 100;
        int result = instance.getBaseline();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDarkCurrent method, of class Camera.
     */
    @Test
    public void testGetDarkCurrent() {
        System.out.println("getDarkCurrent");
        Camera instance = this.camera;
        double expResult = 1;
        double result = instance.getDarkCurrent();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getEmGain method, of class Camera.
     */
    @Test
    public void testGetEmGain() {
        System.out.println("getEmGain");
        Camera instance = this.camera;
        double expResult = 100;
        double result = instance.getEmGain();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getPixelSize method, of class Camera.
     */
    @Test
    public void testGetPixelSize() {
        System.out.println("getPixelSize");
        Camera instance = this.camera;
        double expResult = 6.5;
        double result = instance.getPixelSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getQuantumEfficiency method, of class Camera.
     */
    @Test
    public void testGetQuantumEfficiency() {
        System.out.println("getQuantumEfficiency");
        Camera instance = this.camera;
        double expResult = 0.7;
        double result = instance.getQuantumEfficiency();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getReadoutNoise method, of class Camera.
     */
    @Test
    public void testGetReadoutNoise() {
        System.out.println("getReadoutNoise");
        Camera instance = this.camera;
        double expResult = 1.6;
        double result = instance.getReadoutNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getThermalNoise method, of class Camera.
     */
    @Test
    public void testGetThermalNoise() {
        System.out.println("getThermalNoise");
        Camera instance = this.camera;
        double expResult = 0.2;
        double result = instance.getThermalNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getNX method, of class Camera.
     */
    @Test
    public void testGetNX() {
        System.out.println("getNX");
        Camera instance = this.camera;
        int expResult = 256;
        int result = instance.getNX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNY method, of class Camera.
     */
    @Test
    public void testGetNY() {
        System.out.println("getNY");
        Camera instance = this.camera;
        int expResult = 256;
        int result = instance.getNY();
        assertEquals(expResult, result);
    }
    
}
