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

import ij.ImagePlus;
import ij.io.FileSaver;

import java.nio.ByteBuffer;

/**
 * Implements the remote simulation service functions.
 * 
 * @author Kyle M. Douglass
 */
public class RemoteSimulationServiceHandler implements RemoteSimulationService.Iface {
    
    /**
     * Reference to the simulator that will be controlled through the server.
     */
    private SimpleSimulator simulator;
    
    public RemoteSimulationServiceHandler(SimpleSimulator simulator) {
        this.simulator = simulator;
    }
    
    /**
     * Advances the simulator by one time step and returns the image.
     * 
     * @return A buffer containing the TIFF-encoded byte string of the
     *        simulator's next image.
     */
    @Override
    public ByteBuffer getNextImage() {
        
        // Advance the simulation one time step and retrieve the image.
        ImagePlus imp;
        imp = new ImagePlus("Current image", simulator.getNextImage());
        
        // Serialize the image to a byte array.
        FileSaver saver = new FileSaver(imp);
        byte[] pixels = saver.serialize();
        
        return ByteBuffer.wrap(pixels);

    } 
    
}
