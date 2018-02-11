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

import ch.epfl.leb.sass.simulator.Simulator;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import ch.epfl.leb.sass.utils.images.internal.DefaultImageS;

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
    private Simulator simulator;
    
    public RemoteSimulationServiceHandler(Simulator simulator) {
        this.simulator = simulator;
    }
    
    /**
     * Advances the simulator by one time step and returns the image.
     * 
     * @return A buffer containing the TIFF-encoded byte string of the
     *        simulator's next image.
     * @throws ch.epfl.leb.sass.server.ImageGenerationException
     */
    @Override
    public ByteBuffer getNextImage() throws ImageGenerationException {
        // Advance the simulation one time step and retrieve the image.  
        try {
            ImageS is = simulator.getNextImage();
            return is.serializeToBuffer();
        } catch (ImageShapeException ex) {
            ex.printStackTrace();
            throw new ImageGenerationException();
        }
    }
    
    /**
     * This method is used to determine whether the server is running.
     * 
     * @return Basic information concerning the status of the server.
     */
    @Override
    public String getServerStatus() {
        return "SASS RPC server is running.";
    }
    
    /**
     * Sets the activation laser power in the simulation.
     * 
     * @param power The power of the laser.
     */
    @Override
    public void setActivationLaserPower(double power) {
        simulator.setControlSignal(power);
    }
    
    /**
     * Collects information about the simulation's current state and returns it.
     * 
     * @return JSON string containing the current state of the simulation.
     */
    @Override
    public String getSimulationState() {
        return this.simulator.getSimulationState();
    }
}
