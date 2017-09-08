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
package ch.epfl.leb.sass.simulator.generators.realtime;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import ch.epfl.leb.sass.simulator.generators.realtime.StateSystem;
import ch.epfl.leb.sass.simulator.generators.realtime.Fluorophore;
import ch.epfl.leb.sass.simulator.generators.realtime.Camera;

/**
 *
 * @author Kyle M. Douglass
 */
public class FluorophoreTest {
    private Camera dummyCamera = null;
    private StateSystem dummyStateSystem = null;
    
    public FluorophoreTest() {
    } 
    
    @Before
    public void setUp() {
        dummyCamera = mock(Camera.class);
        dummyStateSystem = mock(StateSystem.class);
    }
    
    /**
     * Test that fluorophores are assigned their proper IDs
     */
    @Test
    public void testFluorophoreIdAssignment() {
        Fluorophore testFluor= new Fluorophore(
            dummyCamera,
            1000,
            dummyStateSystem,
            -1,
            0,
            0
        );
        assertEquals(1, testFluor.getId());
        
        // Create a new Fluorophore and test whether its id is incremented
        Fluorophore testFluorNext = new Fluorophore(
            dummyCamera,
            1000,
            dummyStateSystem,
            -1,
            0,
            0
        );
        assertEquals(2, testFluorNext.getId());
    }
}

