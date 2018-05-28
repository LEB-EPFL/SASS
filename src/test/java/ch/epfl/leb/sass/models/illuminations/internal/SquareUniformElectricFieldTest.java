/*
 * © All rights reserved. 
 * ECOLE POLYTECHNIQUE FEDERALE DE LAUSANNE, Switzerland
 * Laboratory of Experimental Biophysics, 2017
 */
package ch.epfl.leb.sass.models.illuminations.internal;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the SquareUniformElectricField.
 * 
 * @author Kyle M. Douglass
 */
public class SquareUniformElectricFieldTest {
    
    private double width = 20;
    private double height = 40;
    private Vector3D orientation = new Vector3D(1, 0, 0); // x-polarized
    
    public SquareUniformElectricFieldTest() {
    }

    /**
     * Test of getEx method, of class SquareUniformElectricField.
     */
    @Test
    public void testGetEx() {
        System.out.println("testGetEx");
        SquareUniformElectricField instance = new SquareUniformElectricField(
                width, height, orientation);
        
        // Inside the illumination area
        Complex expResult = instance.getEx(10, 20, 0);
        assertEquals(expResult.getReal(), 1, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEx(-10, 20, 10);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEx(30, 20, -10);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEx(10, -10, 100);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEx(10, 50, -100);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
    }

    /**
     * Test of getEy method, of class SquareUniformElectricField.
     */
    @Test
    public void testGetEy() {
        System.out.println("testGetEy");
        SquareUniformElectricField instance = new SquareUniformElectricField(
                width, height, orientation);
        
        // Inside the illumination area
        Complex expResult = instance.getEy(10, 20, 0);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEy(-10, 20, 10);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEy(30, 20, -10);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEy(10, -10, 100);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEy(10, 50, -100);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
    }

    /**
     * Test of getEz method, of class SquareUniformElectricField.
     */
    @Test
    public void testGetEz() {
        System.out.println("testGetEy");
        SquareUniformElectricField instance = new SquareUniformElectricField(
                width, height, orientation);
        
        // Inside the illumination area
        Complex expResult = instance.getEz(10, 20, 0);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEz(-10, 20, 10);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEz(30, 20, -10);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEz(10, -10, 100);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
        
        // Outside the illumination area
        expResult = instance.getEz(10, 50, -100);
        assertEquals(expResult.getReal(), 0, 0.0);
        assertEquals(expResult.getImaginary(), 0, 0.0);
    }
    
}
