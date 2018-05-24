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
package ch.epfl.leb.sass.logging.internal;

import com.google.gson.JsonElement;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the FluorophoreStateTransition class.
 * 
 * @author Kyle M. Douglass
 */
public class FluorophoreStateTransitionTest {
    
    public FluorophoreStateTransitionTest() {
    }

    /**
     * Test of toJson method, of class FluorophoreStateTransition.
     */
    @Test
    public void testToJson() {
        System.out.println("toJson");
        
        String type = "FLUOROPHORE";
        int id = 42;
        double timeElapsed = 32.5;
        int currentState = 2;
        int nextState = 3;
        
        FluorophoreStateTransition instance = new FluorophoreStateTransition(
                id, timeElapsed, currentState, nextState);
        
        JsonElement result = instance.toJson();
        
        assertEquals(type, result.getAsJsonObject().get("type").getAsString());
        assertEquals(id,
                     result.getAsJsonObject().get("fluorophore id").getAsInt());
        assertEquals(timeElapsed,
                     result.getAsJsonObject().get("time elapsed").getAsDouble(),
                     0.0);
        assertEquals(currentState,
                     result.getAsJsonObject().get("current state").getAsInt());
        assertEquals(nextState,
                     result.getAsJsonObject().get("next state").getAsInt());
    }
    
}
