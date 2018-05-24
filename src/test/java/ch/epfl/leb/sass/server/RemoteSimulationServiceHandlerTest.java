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

import ch.epfl.leb.sass.simulator.internal.DefaultSimulationManager;
import ch.epfl.leb.sass.simulator.internal.RPCSimulator;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import ch.epfl.leb.sass.utils.images.internal.DefaultImageS;

import java.nio.ByteBuffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author kmdouglass
 */
public class RemoteSimulationServiceHandlerTest {
    
    private final int SIM_ID = 1;
    private RPCSimulator mockSimulator;
    private DefaultSimulationManager mockManager;
    private RemoteSimulationServiceHandler handler;
    
    private ImageS is = new DefaultImageS( new int[1][1] );
    
    public RemoteSimulationServiceHandlerTest() {
        this.mockSimulator = mock(RPCSimulator.class);
        this.mockManager = mock(DefaultSimulationManager.class);
        
        when(this.mockSimulator.getId()).thenReturn(SIM_ID);
        this.mockManager.addSimulator(mockSimulator);
        this.handler = new RemoteSimulationServiceHandler(mockManager);
    }

    /**
     * Test of getNextImage method, of class RemoteSimulationServiceHandler.
     * @throws ch.epfl.leb.sass.utils.images.ImageShapeException
     * @throws ch.epfl.leb.sass.server.ImageGenerationException
     * @throws ch.epfl.leb.sass.server.UnknownSimulationIdException
     */
    @Test
    public void testGetNextImage() throws ImageShapeException,
                                          ImageGenerationException,
                                          UnknownSimulationIdException {
        System.out.println("getNextImage");

        // Instructs the wrapped simulator to return the ImageJ test image.
        when(this.mockSimulator.getNextImage()).thenReturn(is);
        when(this.mockManager.getSimulator(SIM_ID)).thenReturn(mockSimulator);
        ByteBuffer bufferedImage = this.handler.getNextImage(SIM_ID);
        
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
}
