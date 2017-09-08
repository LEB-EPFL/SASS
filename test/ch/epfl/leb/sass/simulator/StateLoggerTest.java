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
package ch.epfl.leb.sass.simulator;

import java.io.File;
import java.io.IOException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/**
 * Logs all state transitions from a simulation.
 * @author Kyle M. Douglass
 */
public class StateLoggerTest {
    private StateLogger logger = StateLogger.getInstance();
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
    public StateLoggerTest() {
    }

    /**
     * Test of logStateTransition method, of class StateLogger.
     */
    @Test
    public void testLogStateTransition_4args() {
        System.out.println("logStateTransition");
        int id = 1;
        double timeElapsed = 10.0;
        int initialState = 0;
        int nextState = 1;
        StateLogger logger = StateLogger.getInstance();
        logger.setPerformLogging(true);
        
        // Method call to test is here.
        logger.logStateTransition(id, timeElapsed, initialState, nextState);
        
        int actualId = logger.getIds().get(0);
        double actualTimeElapsed = logger.getElapsedTimes().get(0);
        int actualInitialState = logger.getInitialStates().get(0);
        int actualNextState = logger.getNextStates().get(0);
        assertEquals(id, actualId);
        assertEquals(timeElapsed, actualTimeElapsed, 0.001);
        assertEquals(initialState, actualInitialState);
        assertEquals(nextState, actualNextState);
        
    }
    
    /**
     * Test for resetting the logger to its initial state.
     */
    @Test
    public void testReset() throws IOException {
        StateLogger logger = StateLogger.getInstance();
        
        // Give the logger some initial state
        logger.setFilename("textLogFile.txt");
        logger.setPerformLogging(true);
        logger.logStateTransition(1, 10.0, 0, 1);
        
        assertTrue("textLogFile.txt".equals(logger.getFilename()));
        assertEquals(true, logger.getPerformLogging());
        
        int actualId = logger.getIds().get(0);
        double actualTimeElapsed = logger.getElapsedTimes().get(0);
        int actualInitialState = logger.getInitialStates().get(0);
        int actualNextState = logger.getNextStates().get(0);
        assertEquals(1, actualId);
        assertEquals(10.0, actualTimeElapsed, 0.001);
        assertEquals(0, actualInitialState);
        assertEquals(1, actualNextState);
        
        // Critical method test is here.
        logger.reset();
        
        assertTrue("".equals(logger.getFilename()));
        assertEquals(false, logger.getPerformLogging());
        
        assertEquals(0, logger.getIds().size());
        assertEquals(0, logger.getElapsedTimes().size());
        assertEquals(0, logger.getInitialStates().size());
        assertEquals(0, logger.getNextStates().size());
    }
    
    /**
     * Test of setFilename method and unique filename generation.
     * @throws IOException 
     */
    @Test
    public void testSetFilename() throws IOException {
        String filename = "testLogFile.txt";
        File existingFile = tempDir.newFile(filename);
        assertTrue(existingFile.exists());
        
        // Tell logger to create a file whose filename already exists
        logger.setFilename(existingFile.toString());
        File logFile = new File(logger.getFilename());
        
        // Critical check of setFilename() is here.
        String newName = logFile.getName();
        assertTrue(
            "logger file name is: " + newName,
            newName.equals("testLogFile_1.txt")
        );
        
        // Create the actual file
        logFile.createNewFile();
        
        // Another critical check: is integer id is incremented another time?
        logger.setFilename(existingFile.toString());
        logFile = new File(logger.getFilename());
        String newName2 = logFile.getName();
        assertTrue(
            "logger file name is: " + newName2,
            newName2.equals("testLogFile_2.txt")
        );
    }
}
