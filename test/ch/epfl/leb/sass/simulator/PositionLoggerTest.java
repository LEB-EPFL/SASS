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
import java.io.FileReader;
import java.io.BufferedReader;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/**
 * Logs emitter positions from  a simulation.
 * @author Kyle M. Douglass
 */
public class PositionLoggerTest { 
    private PositionLogger logger = null;
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
    public PositionLoggerTest() {
    }
    
    @Before
    public void setUp () {
        logger = PositionLogger.getInstance();
    }
    
    /**
     * Test of logStateTransition method, of class StateLogger.
     */
    @Test
    public void testLogPosition() {
        System.out.println("logPosition");
        int id = 1;
        double x = 10.0;
        double y = 5.0;
        double z = 1.0;
        logger.setPerformLogging(true);
        
        // Method call to test is here.
        logger.logPosition(id, x, y, z);
        
        int actualId = logger.getIds().get(0);
        double actualX = logger.getX().get(0);
        double actualY = logger.getY().get(0);
        double actualZ = logger.getZ().get(0);
        assertEquals(id, actualId);
        assertEquals(x, actualX, 0.001);
        assertEquals(y, actualY, 0.001);
        assertEquals(z, actualZ, 0.001);
        
    }
    
    /**
     * Test for resetting the logger to its initial state.
     */
    @Test
    public void testReset() throws IOException {
        // Give the logger some initial state
        logger.setFilename("textLogFile.txt");
        logger.setPerformLogging(true);
        logger.logPosition(1, 10.0, 5.0, 1.0);
        
        assertTrue("textLogFile.txt".equals(logger.getFilename()));
        assertEquals(true, logger.getPerformLogging());
        
        int actualId = logger.getIds().get(0);
        double actualX = logger.getX().get(0);
        double actualY = logger.getY().get(0);
        double actualZ = logger.getZ().get(0);
        assertEquals(1, actualId);
        assertEquals(10.0, actualX, 0.001);
        assertEquals(5.0, actualY, 0.001);
        assertEquals(1.0, actualZ, 0.001);
        
        // Critical method test is here.
        logger.reset();
        
        assertTrue("".equals(logger.getFilename()));
        assertEquals(false, logger.getPerformLogging());
        
        assertEquals(0, logger.getIds().size());
        assertEquals(0, logger.getX().size());
        assertEquals(0, logger.getY().size());
        assertEquals(0, logger.getZ().size());
    }
    
    /**
     * Test for saving the log file.
     * @throws java.io.IOException
     */
    @Test
    public void testSaveLogFile() throws IOException {
        File logFile = tempDir.newFile("testLogFile.txt");
        logger.setFilename(logFile.toString());
        logger.setPerformLogging(true);
        
        // Add some data to the logger
        this.logger.logPosition(1, 10.0, 5.0, 1.0);
        this.logger.logPosition(2, 10.1, 5.5, 1.1);
        
        // Critical test
        logger.saveLogFile();
        
        // Open the file and verify the contents
        FileReader fileReader = new FileReader(logger.getFilename());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        try {
            String line1 = bufferedReader.readLine();
            String line2 = bufferedReader.readLine();
            String line3 = bufferedReader.readLine();
            
            assertTrue(line1.equals("id,x,y,z"));
            assertTrue(line2.equals("1,10.0000,5.0000,1.0000"));
            assertTrue(line3.equals("2,10.1000,5.5000,1.1000"));
        } catch (IOException e)
        {
            throw e;
        } finally {
            bufferedReader.close();
        }
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
