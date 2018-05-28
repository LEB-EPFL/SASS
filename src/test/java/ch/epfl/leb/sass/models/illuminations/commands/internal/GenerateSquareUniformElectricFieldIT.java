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
package ch.epfl.leb.sass.models.illuminations.commands.internal;

import ch.epfl.leb.sass.models.samples.RefractiveIndex;
import ch.epfl.leb.sass.models.illuminations.ElectricField;
import ch.epfl.leb.sass.models.illuminations.commands.ElectricFieldCommand;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexUtils;
        
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Integration tests for GenerateSquareUniformElectricField and the ElectricFieldReceiver.
 * 
 * @author Kyle M. Douglass
 */
public class GenerateSquareUniformElectricFieldIT {
    
    private double wavelength = 0.532;
    private RefractiveIndex dummyRefractiveIndex;
    private GenerateSquareUniformElectricField.Builder builder;
    private Vector3D orientation = new Vector3D(1.0, 0.0, 0.0);
    
    @Before
    public void setUp() {
        dummyRefractiveIndex = mock(RefractiveIndex.class);
        builder = new GenerateSquareUniformElectricField.Builder();
        
        builder.width(20).height(30).orientation(Vector3D.PLUS_I)
               .refractiveIndex(dummyRefractiveIndex).wavelength(0.532);
    }

    /**
     * Test of generateElectricField method, of class GenerateSquareUniformElectricField.
     */
    @Test
    public void testGenerateElectricField() {
        ElectricFieldCommand cmd = builder.build();
        ElectricField instance = cmd.generateElectricField();
        
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
    
}
