/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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
package ch.epfl.leb.sass.server;

import ch.epfl.leb.sass.simulator.SimpleSimulator;
import ch.epfl.leb.sass.simulator.loggers.FrameInfo;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author kmdouglass
 */
public class RemoteSimulationServiceHandlerTest {
    
    private SimpleSimulator mockSimulator;
    private RemoteSimulationServiceHandler handler;
    
    // NOTE: The string argument to createImage() MUST match that used in the
    // class implementation.
    private ImagePlus imp = IJ.createImage(
            RemoteSimulationServiceHandler.TITLE, 1, 1, 1, 16
    );
    
    public RemoteSimulationServiceHandlerTest() {
        this.mockSimulator = mock(SimpleSimulator.class);
        this.handler = new RemoteSimulationServiceHandler(mockSimulator);
    }

    /**
     * Test of getNextImage method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetNextImage() {
        System.out.println("getNextImage");
                // Instructs the wrapped simulator to return the ImageJ test image.

        // Instructs the wrapped simulator to return the ImageJ test image.
        when(this.mockSimulator.getNextImage()).thenReturn(imp.getProcessor());
        ByteBuffer bufferedImage = this.handler.getNextImage();
        
        byte[] expResult = (new FileSaver(this.imp)).serialize();
        byte[] result = new byte[bufferedImage.remaining()];
        bufferedImage.get(result);
        
        assertArrayEquals(expResult, result);

    }

    /**
     * Test of getServerStatus method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetServerStatus() {
        System.out.println("getServerStatus");
        RemoteSimulationServiceHandler instance = this.handler;
        String expResult = "SASS RPC server is running.";
        String result = instance.getServerStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSimulationState method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetSimulationState() {
        System.out.println("getSimulationState");
        
        EmitterState state = new EmitterState(1, 2, 0.1, 0.1, 0.1, 0.5, 2500);
        List<EmitterState> expResult = new ArrayList<>();
        expResult.add(state);
        
        // Instructs the wrapped simulator to return the expected FrameInfo.
        List<FrameInfo> info = new ArrayList<>();
        info.add(new FrameInfo(1, 2, 0.1, 0.1, 0.1, 0.5, 2500));
        when(this.mockSimulator.getEmitterStatus()).thenReturn(info);
        
        RemoteSimulationServiceHandler instance = this.handler;

        List<EmitterState> result = instance.getSimulationState();
        
        // This should probably be changed to a loop over fields...
        assertEquals(expResult.get(0).frameNumber, result.get(0).frameNumber);
        assertEquals(expResult.get(0).fluorophoreID, result.get(0).fluorophoreID);
        assertEquals(expResult.get(0).x, result.get(0).x, 0);
        assertEquals(expResult.get(0).y, result.get(0).y, 0);
        assertEquals(expResult.get(0).z, result.get(0).z, 0);
        assertEquals(expResult.get(0).brightness, result.get(0).brightness, 0);
        assertEquals(expResult.get(0).timeOn, result.get(0).timeOn, 0);
    }
    
}
