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

import ch.epfl.leb.sass.simulator.internal.RPCSimulator;
import ch.epfl.leb.sass.loggers.FrameInfo;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import ch.epfl.leb.sass.utils.images.internal.DefaultImageS;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author kmdouglass
 */
public class RemoteSimulationServiceHandlerTest {
    
    private RPCSimulator mockSimulator;
    private RemoteSimulationServiceHandler handler;
    
    private ImageS is = new DefaultImageS( new int[1][1] );
    
    public RemoteSimulationServiceHandlerTest() {
        this.mockSimulator = mock(RPCSimulator.class);
        this.handler = new RemoteSimulationServiceHandler(mockSimulator);
    }

    /**
     * Test of getNextImage method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetNextImage() throws ImageShapeException, ImageGenerationException {
        System.out.println("getNextImage");

        // Instructs the wrapped simulator to return the ImageJ test image.
        when(this.mockSimulator.getNextImage()).thenReturn(is);
        ByteBuffer bufferedImage = this.handler.getNextImage();
        
        byte[] expResult = is.serializeToArray();
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
        RemoteSimulationServiceHandler instance = this.handler;
        String expResult = "[{\"frame\":1,\"id\":2,\"x\":0.1,\"y\":0.1,\"z\":0.1,\"brightness\":0.5,\"timeOn\":2500.0}]";
        
        // Instructs the wrapped simulator to return the expected FrameInfo.
        Gson gson = new Gson();
        List<FrameInfo> info = new ArrayList<>();
        info.add(new FrameInfo(1, 2, 0.1, 0.1, 0.1, 0.5, 2500));
        when(this.mockSimulator.getSimulationState()).thenReturn(gson.toJson(info));

        String result = instance.getSimulationState();
        
        // This should probably be changed to a loop over fields...
        assertEquals(expResult, result);
    }
}
