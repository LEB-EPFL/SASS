/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
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
package ch.epfl.leb.sass.models.illuminations.internal;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;

import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the SquareUniformElectricField.
 * 
 * @author Kyle M. Douglass
 */
public class SquareUniformElectricFieldTest {
    
    private final double width = 20;
    private final double height = 40;
    private final Vector3D orientation = new Vector3D(1, 0, 0); // x-polarized
    private final RefractiveIndex dummyRefractiveIndex 
            = mock(RefractiveIndex.class);
    private final double wavelength = 0.532;
    private SquareUniformElectricField.Builder builder;
    
    @Before
    public void setUp() {
        builder = new SquareUniformElectricField.Builder();
        builder.height(height).width(width).orientation(orientation)
               .refractiveIndex(dummyRefractiveIndex).wavelength(wavelength);
    }

    /**
     * Test of getEx method, of class SquareUniformElectricField.
     */
    @Test
    public void testGetEx() {
        System.out.println("testGetEx");
        SquareUniformElectricField instance = builder.build();
        
        // Inside the illumination area
        when(dummyRefractiveIndex.getN(10, 20, 0)).thenReturn(new Complex(1.0));
        Complex result = instance.getEx(10, 20, 0);
        assertEquals(1, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Inside the illumination area, different z-position
        Complex expResult
                = ComplexUtils.polar2Complex(orientation.getX(),
                                          MathUtils.TWO_PI / wavelength * 1.0);
        
        when(dummyRefractiveIndex.getN(10, 20, 1)).thenReturn(new Complex(1.0));
        result = instance.getEx(10, 20, 1);
        assertEquals(expResult.getReal(), result.getReal(), 0.0);
        assertEquals(expResult.getImaginary(), result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(-10, 20, 10))
           .thenReturn(new Complex(1.0));
        result = instance.getEx(-10, 20, 10);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(30, 20, -10))
           .thenReturn(new Complex(1.0));
        result = instance.getEx(30, 20, -10);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(10, -10, 100))
           .thenReturn(new Complex(1.0));
        result = instance.getEx(10, -10, 100);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(10, 50, -100))
           .thenReturn(new Complex(1.0));
        result = instance.getEx(10, 50, -100);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
    }

    /**
     * Test of getEy method, of class SquareUniformElectricField.
     */
    @Test
    public void testGetEy() {
        System.out.println("testGetEy");
        SquareUniformElectricField instance = builder.build();
        
        // Inside the illumination area
        when(dummyRefractiveIndex.getN(10, 20, 0)).thenReturn(new Complex(1.0));
        Complex result = instance.getEy(10, 20, 0);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(-10, 20, 10))
           .thenReturn(new Complex(1.0));
        result = instance.getEy(-10, 20, 10);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(30, 20, -10))
           .thenReturn(new Complex(1.0));
        result = instance.getEy(30, 20, -10);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(10, -10, 100))
           .thenReturn(new Complex(1.0));
        result = instance.getEy(10, -10, 100);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(10, 50, -100))
           .thenReturn(new Complex(1.0));
        result = instance.getEy(10, 50, -100);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
    }

    /**
     * Test of getEz method, of class SquareUniformElectricField.
     */
    @Test
    public void testGetEz() {
        System.out.println("testGetEy");
        SquareUniformElectricField instance = builder.build();

        // Inside the illumination area
        when(dummyRefractiveIndex.getN(10, 20, 0)).thenReturn(new Complex(1.0));
        Complex result = instance.getEz(10, 20, 0);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(-10, 20, 10))
           .thenReturn(new Complex(1.0));
        result = instance.getEz(-10, 20, 10);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(30, 20, -10))
           .thenReturn(new Complex(1.0));
        result = instance.getEz(30, 20, -10);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(10, -10, 100))
           .thenReturn(new Complex(1.0));
        result = instance.getEz(10, -10, 100);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
        
        // Outside the illumination area
        when(dummyRefractiveIndex.getN(10, 50, -100))
           .thenReturn(new Complex(1.0));
        result = instance.getEz(10, 50, -100);
        assertEquals(0, result.getReal(), 0.0);
        assertEquals(0, result.getImaginary(), 0.0);
    }
 
    /**
     * Test of getEx method, of class SquareUniformElectricField.
     */
    @Test
    public void testGetExAbsorption() {
        System.out.println("testGetExAbsorption");
        SquareUniformElectricField instance = builder.build();
        
        // The imaginary part of the refractive index determines the absorption.
        Complex absRefractiveIndex = new Complex(1.0, 1.0);
        double z = 1.0;
        double arg = MathUtils.TWO_PI * z / wavelength;
        double mag = orientation.getX()
                     * Math.exp(-arg * absRefractiveIndex.getImaginary());
        
        // Inside the illumination area, different z-position
        Complex expResult = ComplexUtils.polar2Complex(
                mag, arg * absRefractiveIndex.getReal());
        
        when(dummyRefractiveIndex.getN(10,20,z)).thenReturn(absRefractiveIndex);
        Complex result = instance.getEx(10, 20, z);
        assertEquals(expResult.getReal(), result.getReal(), 0.0);
        assertEquals(expResult.getImaginary(), result.getImaginary(), 0.0);
        
    }
    
    /**
     * Test of getRefractiveIndexMethod, of class SquareUniformElectricField.
     */
    @Test
    public void testGetRefractiveIndex() {
        System.out.println("testGetRefractiveIndex");
        SquareUniformElectricField instance = builder.build();

        // Inside the illumination area
        when(dummyRefractiveIndex.getN(10, 20, 0))
           .thenReturn(new Complex(1.0, 0.75));
        
        RefractiveIndex result = instance.getRefractiveIndex();
        result.getN(10, 20, 0).getReal();
        assertEquals(1.0, result.getN(10, 20, 0).getReal(), 0.0);
        assertEquals(0.75, result.getN(10, 20, 0).getImaginary(), 0.0);
    }
    
    /**
     * Test of getWavelength, of class SquareUniformElectricField.
     */
    @Test
    public void testGetWavelength() {
        System.out.println("testGetWavelength");
        SquareUniformElectricField instance = builder.build();

        // Inside the illumination area
        when(dummyRefractiveIndex.getN(10, 20, 0))
           .thenReturn(new Complex(1.0, 0.75));
        
        double result = instance.getWavelength();
        assertEquals(wavelength, result, 0.0);        
    }
}
