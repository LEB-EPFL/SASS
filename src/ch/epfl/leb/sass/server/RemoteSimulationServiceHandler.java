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

import ij.ImagePlus;
import ij.io.FileSaver;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;

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
    
    /**
     * The title of the image returned by the RPC server.
     */
    public static final String TITLE = "Current image";
    
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
        imp = new ImagePlus(this.TITLE, simulator.getNextImage());
        
        // Serialize the image to a byte array.
        FileSaver saver = new FileSaver(imp);
        byte[] pixels = saver.serialize();
        
        return ByteBuffer.wrap(pixels);

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
     * @param power The power of the laser.
     */
    @Override
    public void setActivationLaserPower(double power) {
        simulator.setControlSignal(power);
    }
    
    /**
     * Collects information about the simulation's current state and returns it.
     * 
     * @return Information about each active emitter.
     */
    @Override
    public List<EmitterState> getSimulationState() {
        List<FrameInfo> allInfo = this.simulator.getEmitterStatus();
        
        List<EmitterState> states = new ArrayList<>();
        for (FrameInfo info : allInfo) {
            EmitterState state = new EmitterState(
                    info.frame, info.id, info.x, info.y, info.z,
                    info.brightness, info.timeOn
            );
            
            states.add(state);
        }
        
        return states;
        
    }
}
