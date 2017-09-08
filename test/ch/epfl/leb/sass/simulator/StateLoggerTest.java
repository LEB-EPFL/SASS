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
 *
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
    public void testLogStateTransition_3args() {
        System.out.println("logStateTransition");
        double time_elapsed = 0.0;
        int current_state = 0;
        int next_state = 0;
        StateLogger logger = StateLogger.getInstance();
        logger.logStateTransition(time_elapsed, current_state, next_state);
        // TODO: ADD ASSERTION
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
        boolean created = logFile.createNewFile();
        assertTrue(created);
        
        // Critical check of setFilename() is here.
        String newName = logFile.getName();
        assertTrue(
            "logger file name is: " + newName,
            newName.equals("testLogFile_1.txt")
        );
        
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
