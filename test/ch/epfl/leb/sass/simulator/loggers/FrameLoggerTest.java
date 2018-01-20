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
package ch.epfl.leb.sass.simulator.loggers;

import ch.epfl.leb.sass.simulator.loggers.FrameLogger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Logs all per-frame positions
 * @author Baptiste Ottino
 */
public class FrameLoggerTest {
    private FrameLogger logger = null;

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();

    public FrameLoggerTest() {
    }

    @Before
    public void setUp () {
        logger = FrameLogger.getInstance();
        logger.reset();
    }

    /**
     * Test of logFrame method, of class FrameLogger.
     */
    @Test
    public void testLogFrame() {
        System.out.println("logFrame");
        int frame = 4;
        int id = 1;
        double x = 2.3;
        double y = 2.5;
        double z = 0.7;
        double brightness = 0.8;
        double timeOn = 0.8;
        logger.setPerformLogging(true);

        // Method call to test is here.
        logger.logFrame(frame, id, x, y, z, brightness, timeOn);

        int actualFrame = logger.getFrame().get(0);
        int actualId = logger.getId().get(0);
        double actualX = logger.getX().get(0);
        double actualY = logger.getY().get(0);
        double actualZ = logger.getZ().get(0);
        double actualBrightness = logger.getBrightness().get(0);
        double actualTimeOn = logger.getTimeOn().get(0);

        assertEquals(frame, actualFrame);
        assertEquals(id, actualId);
        assertEquals(x, actualX, 0.001);
        assertEquals(y, actualY, 0.001);
        assertEquals(z, actualZ, 0.001);
        assertEquals(brightness, actualBrightness, 0.001);
        assertEquals(timeOn, actualTimeOn, 0.001);
    }
    
    /**
     * Test of getFrameInfo method, of class FrameLogger.
     */
    @Test
    public void testGetFrameInfo() {
        System.out.println("getCurrentFrameInfo");
        logger.setPerformLogging(true);
        logger.setLogCurrentFrameOnly(true);
        
        int frame = 1;
        int id = 1;
        double x = 2.3;
        double y = 2.5;
        double z = 0.7;
        double brightness = 0.8;
        double timeOn = 0.8;
        
        logger.logFrame(frame, id, x, y, z, brightness, timeOn);
        // PICK UP HERE
        List<FrameInfo> info = logger.getFrameInfo();
        
        assertEquals(1, info.size());
        assertEquals(frame, info.get(0).frame);
        assertEquals(id, info.get(0).id);
        assertEquals(x, info.get(0).x, 0.0);
        assertEquals(y, info.get(0).y, 0.0);
        assertEquals(z, info.get(0).z, 0.0);
        assertEquals(brightness, info.get(0).brightness, 0.0);
        assertEquals(timeOn, info.get(0).timeOn, 0.0);
        
        // Should make the current frame equal to 2, not 1, so that the size of
        // the returned array is now equal to the number of times that logFrame
        // is called with the new frame number.
        int newFrame = 2;
        logger.logFrame(newFrame, id, x, y, z, brightness, timeOn);
        
        double newX = 3.4;
        logger.logFrame(newFrame, id, newX, y, z, brightness, timeOn);
        info = logger.getFrameInfo();
        
        assertEquals(logger.getFrameInfo().size(), 2);
        assertEquals(info.get(0).frame, newFrame);
        assertEquals(info.get(0).x, x, 0.0);
        assertEquals(info.get(1).frame, newFrame);
        assertEquals(info.get(1).x, newX, 0.0);
    }

    /**
     * Test for resetting the logger to its initial state.
     */
    @Test
    public void testReset() throws IOException {
        // Give the logger some initial state
        logger.setFilename("textLogFile.txt");
        logger.setPerformLogging(true);
        logger.logFrame(1, 1, 10.0, 0.4, 0.8, 0.9, 0.2);

        assertTrue("textLogFile.txt".equals(logger.getFilename()));
        assertEquals(true, logger.getPerformLogging());

        // Critical method test is here.
        logger.reset();

        assertTrue("".equals(logger.getFilename()));
        assertEquals(false, logger.getPerformLogging());

        assertEquals(0, logger.getFrame().size());
        assertEquals(0, logger.getId().size());
        assertEquals(0.0, logger.getX().size(), 0.001);
        assertEquals(0.0, logger.getY().size(), 0.001);
        assertEquals(0.0, logger.getZ().size(), 0.001);
        assertEquals(0.0, logger.getBrightness().size(), 0.001);
        assertEquals(0.0, logger.getTimeOn().size(), 0.001);
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
        this.logger.logFrame(1, 1, 10.0, 5.0, 1.0, 0.8, 0.2);
        this.logger.logFrame(2, 5, 20.0, 5.0, 1.1, 0.6, 0.3);
        
        // Critical test
        logger.saveLogFile();
        
        // Open the file and verify the contents
        FileReader fileReader = new FileReader(logger.getFilename());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        try {
            String line1 = bufferedReader.readLine();
            String line2 = bufferedReader.readLine();
            String line3 = bufferedReader.readLine();
            
            assertTrue(line1.equals("frame,id,x,y,z,brightness,time_on"));
            assertTrue(line2.equals("1,1,10.0000,5.0000,1.0000,0.8000,0.2000"));
            assertTrue(line3.equals("2,5,20.0000,5.0000,1.1000,0.6000,0.3000"));
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
