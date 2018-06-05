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
package ch.epfl.leb.sass.models.fluorophores.internal;

import ch.epfl.leb.sass.models.psfs.PSF;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.photophysics.StateSystem;
import ch.epfl.leb.sass.models.illuminations.Illumination;
import ch.epfl.leb.sass.logging.Listener;
import ch.epfl.leb.sass.logging.internal.FluorophoreStateTransition;


import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 *
 * @author Kyle M. Douglass
 */
public class DefaultFluorophoreTest {
    private Illumination dummyIllumination = null;
    private StateSystem dummyStateSystem = null;
    private PSFBuilder dummyPSFBuilder = null;
    private PSF dummyPSF = null;
    private FluorophoreStateTransition msg;
    
    public DefaultFluorophoreTest() {
    } 
    
    @Before
    public void setUp() {
        dummyIllumination = mock(Illumination.class);
        dummyStateSystem = mock(StateSystem.class);
        dummyPSFBuilder = mock(PSFBuilder.class);
        dummyPSF = mock(PSF.class);
        msg = new FluorophoreStateTransition(42, 42.42, 42, 43);
    }
    
     /**
     * Test of addListener and deleteListener methods,
     * of class DefaultFluorophoreTest.
     */
    @Test
    public void testAddDeleteListeners() {
        System.out.println("testAddDeleteListeners");
        TestListener testListener = new TestListener();
        testListener.isNull = false;
        TestListener testListener2 = new TestListener();
        testListener2.isNull = false;
        
        double x = 1.5; double y = -2.5; double z = 3.0;
        int startState = 0;
        double signal = 1000;
        when(dummyPSFBuilder.build()).thenReturn(dummyPSF);
        when(dummyPSFBuilder.eX(x)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eY(y)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eZ(z)).thenReturn(dummyPSFBuilder);
        when(dummyPSF.getRadius()).thenReturn(1.0);
        when(dummyStateSystem.getNStates()).thenReturn(startState + 1);
        Mockito.doNothing()
               .when(dummyPSF)
               .generateSignature(ArgumentMatchers.any(ArrayList.class));
        
        DefaultFluorophore testFluor = new DefaultFluorophore(
            dummyPSFBuilder, dummyIllumination, signal, dummyStateSystem,
            startState, x, y, z);
        
        // Add two listeners, but immediately remove the first.
        testFluor.addListener(testListener);
        testFluor.addListener(testListener2);
        testFluor.deleteListener(testListener);
        testFluor.setChanged();
        testFluor.notifyListeners();
        
        // Since the listener was removed, isNull should still be false.
        assert(!testListener.isNull);
        
        // testListener2 was not removed, so isNull should now be true.
        assert(testListener2.isNull);

    }
    
    /**
     * Test of notifyListeners method, of class DefaultFluorophoreTest.
     */
    @Test
    public void testNotifyListeners() {
        System.out.println("testNotifyListeners");
        TestListener testListener = new TestListener();
        testListener.isNull = false;
        
        double x = 1.5; double y = -2.5; double z = 3.0;
        int startState = 0;
        double signal = 1000;
        when(dummyPSFBuilder.build()).thenReturn(dummyPSF);
        when(dummyPSFBuilder.eX(x)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eY(y)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eZ(z)).thenReturn(dummyPSFBuilder);
        when(dummyPSF.getRadius()).thenReturn(1.0);
        when(dummyStateSystem.getNStates()).thenReturn(startState + 1);
        Mockito.doNothing()
               .when(dummyPSF)
               .generateSignature(ArgumentMatchers.any(ArrayList.class));
        
        DefaultFluorophore testFluor = new DefaultFluorophore(
            dummyPSFBuilder, dummyIllumination, signal, dummyStateSystem,
            startState, x, y, z);
        
        testFluor.addListener(testListener);
        testFluor.setChanged();
        testFluor.notifyListeners();
        assert(testListener.isNull);

    }
    
    /**
     * Test of notifyListeners method, of class DefaultFluorophoreTest.
     */
    @Test
    public void testNotifyListenersArg() {
        System.out.println("testNotifyListenersArg");
        TestListener testListener = new TestListener();
        testListener.isNull = false;
        
        double x = 1.5; double y = -2.5; double z = 3.0;
        int startState = 0;
        double signal = 1000;
        when(dummyPSFBuilder.build()).thenReturn(dummyPSF);
        when(dummyPSFBuilder.eX(x)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eY(y)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eZ(z)).thenReturn(dummyPSFBuilder);
        when(dummyPSF.getRadius()).thenReturn(1.0);
        when(dummyStateSystem.getNStates()).thenReturn(startState + 1);
        Mockito.doNothing()
               .when(dummyPSF)
               .generateSignature(ArgumentMatchers.any(ArrayList.class));
        
        DefaultFluorophore testFluor = new DefaultFluorophore(
            dummyPSFBuilder, dummyIllumination, signal, dummyStateSystem,
            startState, x, y, z);
        
        testFluor.addListener(testListener);
        testFluor.setChanged();
        testFluor.notifyListeners(msg);
        
        assert(!testListener.isNull);
        assertEquals(msg.ID, testListener.id);
        assertEquals(msg.TIME_ELAPSED, testListener.timeElapsed, 0.0);
        assertEquals(msg.CURRENT_STATE, testListener.currentState);
        assertEquals(msg.NEXT_STATE, testListener.nextState);

    }
    
    /**
     * Test that the fluorophore serializes itself to JSON correctly.
     */
    @Test
    public void testDefaultFluorophoreToJson() {
        
        double x = 1.5; double y = -2.5; double z = 3.0;
        int startState = 0;
        double signal = 1000;
        when(dummyPSFBuilder.build()).thenReturn(dummyPSF);
        when(dummyPSFBuilder.eX(x)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eY(y)).thenReturn(dummyPSFBuilder);
        when(dummyPSFBuilder.eZ(z)).thenReturn(dummyPSFBuilder);
        when(dummyPSF.getRadius()).thenReturn(1.0);
        when(dummyStateSystem.getNStates()).thenReturn(startState + 1);
        Mockito.doNothing()
               .when(dummyPSF)
               .generateSignature(ArgumentMatchers.any(ArrayList.class));
        
        DefaultFluorophore testFluor = new DefaultFluorophore(
            dummyPSFBuilder, dummyIllumination, signal, dummyStateSystem,
            startState, x, y, z);
        
        String result = testFluor.toJson().toString();
        
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();
        
        assertNotNull(json.get("id"));
        assertTrue(String.valueOf(x).equals(json.get("x").getAsString()));
        assertTrue(String.valueOf(y).equals(json.get("y").getAsString()));
        assertTrue(String.valueOf(z).equals(json.get("z").getAsString()));
        assertTrue(String.valueOf(signal).equals(json.get("maxPhotonsPerFrame")
                                               .getAsString()));
        assertTrue(String.valueOf(startState).equals(json.get("currentState")
                                                   .getAsString()));
        assertTrue("false".equals(json.get("bleached").getAsString()));
        assertTrue("false".equals(json.get("emitting").getAsString()));
    }
    
    /**
     * A test class that implements basic Listener capabilities.
     */
    class TestListener implements Listener {
        
        public boolean isNull;
        public int id;
        public double timeElapsed;
        public int currentState;
        public int nextState;
        
        @Override
        public void update(Object data) {
            if (data == null) {
                isNull = true;
                return;
            }
            
            FluorophoreStateTransition msg = (FluorophoreStateTransition) data;
            id = msg.ID;
            timeElapsed = msg.TIME_ELAPSED;
            currentState = msg.CURRENT_STATE;
            nextState = msg.NEXT_STATE;
        }
    }
}

