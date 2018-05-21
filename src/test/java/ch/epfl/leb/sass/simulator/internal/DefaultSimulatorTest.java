/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne, Switzerland
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.simulator.internal;

import ch.epfl.leb.sass.logging.Message;
import ch.epfl.leb.sass.logging.MessageType;
import ch.epfl.leb.sass.logging.internal.FluorophoreStateTransition;
import ch.epfl.leb.sass.models.Microscope;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Unit tests for the DefaultSimulator class.
 * 
 * @author Kyle M. Douglass
 */
public class DefaultSimulatorTest {
    
    private Message msg1;
    private Message msg2;
    
    private Microscope dummyMicroscope;
    
    public DefaultSimulatorTest() {
    }
    
    @Before
    public void setUp() {
        int id = 1;
        double timeElapsed = 42.0;
        int currentState = 2;
        int nextState = 3;
        msg1 = new FluorophoreStateTransition(id, timeElapsed, currentState,
                                              nextState);
        
        id = 2;
        timeElapsed = 43.0;
        currentState = 4;
        nextState = 5;
        msg2 = new FluorophoreStateTransition(id, timeElapsed, currentState,
                                              nextState);
        
        dummyMicroscope = mock(Microscope.class);
    }
    
    /**
     * Test of dumpMessageCache method, of class DefaultSimulator.StateListener.
     */
    @Test
    public void testStateListenerDumpMessageCache() {
        System.out.println("testStateListenerDumpMessageCache");
        int[] res = {32, 32};
        when(dummyMicroscope.getResolution()).thenReturn(res);
        DefaultSimulator sim = new DefaultSimulator(dummyMicroscope);
        DefaultSimulator.StateListener instance = sim.new StateListener();
        
        instance.update(msg1);
        instance.update(msg2);
        List<Message> messages = instance.dumpMessageCache();
        
        JsonObject json = messages.get(0).toJson().getAsJsonObject();
        assertEquals(MessageType.FLUOROPHORE.name(),
                     json.get("type").getAsString());
        assertEquals(1, json.get("fluorophore id").getAsInt());
        assertEquals(42.0, json.get("time elapsed").getAsDouble(), 0.0);
        assertEquals(2, json.get("current state").getAsInt());
        assertEquals(3, json.get("next state").getAsInt());
        
        json = messages.get(1).toJson().getAsJsonObject();
        assertEquals(MessageType.FLUOROPHORE.name(),
                     json.get("type").getAsString());
        assertEquals(2, json.get("fluorophore id").getAsInt());
        assertEquals(43.0, json.get("time elapsed").getAsDouble(), 0.0);
        assertEquals(4, json.get("current state").getAsInt());
        assertEquals(5, json.get("next state").getAsInt());
    }

    /**
     * Test of update method, of class DefaultSimulator.StateListener.
     */
    @Test
    public void testStateListenerUpdate() {
        int[] res = {32, 32};
        when(dummyMicroscope.getResolution()).thenReturn(res);
        DefaultSimulator sim = new DefaultSimulator(dummyMicroscope);
        DefaultSimulator.StateListener instance = sim.new StateListener();
        
        instance.update(msg1);
        instance.update(msg2);
        List<Message> messages = instance.dumpMessageCache();
        assertEquals(2, messages.size());
    }
    
}
