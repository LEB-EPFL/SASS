/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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

import ch.epfl.leb.sass.logging.Message;
import ch.epfl.leb.sass.logging.WrongMessageTypeException;
import ch.epfl.leb.sass.logging.internal.LaserPowerChange;
import ch.epfl.leb.sass.logging.internal.FluorophoreStateTransition;
import ch.epfl.leb.sass.models.samples.RefractiveIndex;
import ch.epfl.leb.sass.models.samples.internal.UniformRefractiveIndex;
import ch.epfl.leb.sass.models.illuminations.ElectricField;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * IntegrationTests for the SquareUniformIllumination class.
 * 
 * @author Kyle M. Douglass
 */
public class SquareUniformIlluminationIT {
    
    private final double width = 32;
    private final double height = 32;
    private final double initPower = 1.0;
    private final Vector3D orientation = new Vector3D(1, 0, 0); // x-polarized
    private final RefractiveIndex refractiveIndex
            = new UniformRefractiveIndex(new Complex(1, 0.1));
    private final double wavelength = 0.532;
    private SquareUniformIllumination instance;
    
    @Before
    public void setUp() {
        instance = new SquareUniformIllumination(
                initPower, width, height, orientation, wavelength, 
                refractiveIndex);
    }

    /**
     * Test of getElectricField method, of class SquareUniformIllumination.
     */
    @Test
    public void testGetElectricField() {
        System.out.println("testGetElectricField");
        
        ElectricField result = instance.getElectricField();
        assertEquals(wavelength, result.getWavelength(), 0.0);
        assertEquals(refractiveIndex, result.getRefractiveIndex());
    }

    /**
     * Test of getIrradiance method, of class SquareUniformIllumination.
     */
    @Test
    public void testGetIrradiance() {
        System.out.println("testGetIrradiance");
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double expResult = initPower / width / height;
        double result = instance.getIrradiance(x, y, z);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getPower method, of class SquareUniformIllumination.
     */
    @Test
    public void testGetPower() {
        System.out.println("testGetPower");
        double expResult = initPower;
        double result = instance.getPower();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setPower method, of class SquareUniformIllumination.
     */
    @Test
    public void testSetPower() {
        System.out.println("testSetPower");
        double power = 10.0;
        instance.setPower(power);
        assertEquals(power, instance.getPower(), 0.0);
    }

    /**
     * Test of update method, of class SquareUniformIllumination.
     */
    @Test
    public void testUpdate() throws WrongMessageTypeException {
        System.out.println("testUpdate");
        double expResult = 15.0;
        Object data = (Message) new LaserPowerChange(expResult);
        instance.update(data);
        assertEquals(expResult, instance.getPower(), 0.0);
    }
    
    /**
     * Test of update method, of class SquareUniformIllumination.
     */
    @Test(expected = WrongMessageTypeException.class)
    public void testUpdateWrongMessageType() throws WrongMessageTypeException {
        System.out.println("testUpdateWrongMessageType");
        Object data = (Message) new FluorophoreStateTransition(1, 1.0, 1, 1);
        instance.update(data);
    }
}
