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
package ch.epfl.leb.sass.simulator.internal;

import ch.epfl.leb.sass.simulator.Simulator;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the SimulationManager class.
 *
 * @author Kyle M. Douglass
 */
public class DefaultSimulationManagerTest {
    
    /**
     * A list of dummy simulations.
     */
    private ConcurrentHashMap<Integer, Simulator> sims;
    
    Simulator dummySim1;
    Simulator dummySim2;
    
    /**
     * Creates a dummy table of simulations to be managed.
     */
    @Before
    public void setUp() {
        dummySim1 = mock(Simulator.class);
        dummySim2 = mock(Simulator.class);
        
        sims = new ConcurrentHashMap<>();
        sims.put(1, dummySim1);
        sims.put(2, dummySim2);
    }

    /**
     * Test of getIds method, of class DefaultSimulationManager.
     */
    @Test
    public void testGetIds() {
        System.out.println("getIds");
        DefaultSimulationManager instance = new DefaultSimulationManager(sims);
        List<Integer> expResult = new ArrayList<>();
        expResult.add(1);
        expResult.add(2);
        
        // Ensures that the IDs from the SimulationManager's HashMap are sorted.
        List<Integer> result = instance.getIds();
        Collections.sort(result);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getSimulator method, of class DefaultSimulationManager.
     */
    @Test
    public void testGetSimulator() {
        System.out.println("getSimulator");
        int id = 1;
        DefaultSimulationManager instance = new DefaultSimulationManager(sims);
        Simulator expResult = dummySim1;
        Simulator result = instance.getSimulator(id);
        assertEquals(expResult, result);
        
        expResult = dummySim2;
        result = instance.getSimulator(2);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeSimulator method, of class DefaultSimulationManager.
     */
    @Test
    public void testRemoveSimulator() {
        System.out.println("removeSimulator");
        int id = 1;
        
        List<Integer> expResult = new ArrayList<>();
        expResult.add(2);
        
        DefaultSimulationManager instance = new DefaultSimulationManager(sims);
        instance.removeSimulator(id);
        
        List<Integer> result = instance.getIds();
        assertEquals(expResult, result);
    }
    
}
