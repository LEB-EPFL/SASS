/** 
 * Copyright (C) 2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne, Switzerland
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
package ch.epfl.leb.sass.utils;

import java.io.Serializable;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the DeepCopy class.
 * 
 * @author Kyle M. Douglass
 */
public class DeepCopyTest {
    
    public DeepCopyTest() {
    }

    @Test
    public void testDeepCopy() {
        
        TestObject test = new TestObject(1);
        TestObject testRef = test;
        TestObject testCopy = (TestObject) DeepCopy.deepCopy(test);
        
        test.testField = 2;
        
        assertEquals(2, testRef.testField);
        assertEquals(1, testCopy.testField);
    }
    
}

/**
 * Test class for DeepCopy.
 */
class TestObject implements Serializable {
    public int testField;
    
    public TestObject(int number) {
        testField = number;
    }
}
