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
package ch.epfl.leb.sass.models.samples.internal;

import org.apache.commons.math3.complex.Complex;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the UniformRefractiveIndex object.
 * 
 * @author Kyle M. Douglass
 */
public class UniformRefractiveIndexTest {
    
    private Complex expResult;
    private UniformRefractiveIndex refractiveIndex;
    
    @Before
    public void setUp() {
        expResult = new Complex(1.0, 0.5);
        refractiveIndex = new UniformRefractiveIndex(expResult);
    }

    /**
     * Test of getN method, of class UniformRefractiveIndex.
     */
    @Test
    public void testGetN() {
        Complex result = refractiveIndex.getN(0, 0, 0);
        assertTrue(expResult.equals(result));
        
        result = refractiveIndex.getN(1.0, 0, 0);
        assertTrue(expResult.equals(result));
        
        result = refractiveIndex.getN(1.0, 2.1, 0);
        assertTrue(expResult.equals(result));
        
        result = refractiveIndex.getN(1.0, 2.1, 377.42);
        assertTrue(expResult.equals(result));
    }
    
}
