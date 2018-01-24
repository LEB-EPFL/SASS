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
package ch.epfl.leb.sass.simulator.internal;

import ch.epfl.leb.sass.loggers.FrameLogger;
import ch.epfl.leb.sass.loggers.FrameInfo;
import ij.process.ImageProcessor;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import ch.epfl.leb.sass.simulator.Simulator;

/**
 * A simulator that is specialized for control by remote procedure calls (RPCs).
 * 
 * @author Kyle M. Douglass
 */
public class RPCSimulator {
    
    private final Simulator engine;
    private final FrameLogger frameLogger = FrameLogger.getInstance();
    private int currFrame;
    
    /**
     * Initializes the RPC server and connects it to the simulation engine.
     * 
     * @param engine The engine that runs the simulation.
     */
    public RPCSimulator(Simulator engine) {
        this.currFrame = 0;
        this.engine = engine;
        frameLogger.reset();
        frameLogger.setPerformLogging(true);
        frameLogger.setLogCurrentFrameOnly(true);
    }
    
    /**
     * Advances the simulation one time step.
     * 
     * @return The next image generated from the simulation.
     */
    public ImageProcessor getNextImage() {
        ImageProcessor img = null;
        try {
            this.currFrame++;
            img = engine.getNextImage();
        } catch (Exception ex) {
            Logger.getLogger(RPCSimulator.class.getName())
                  .log(Level.SEVERE, null, ex);
        }
        
        return img;
    }
    
    /**
     * Sets the activation laser power.
     * 
     * @param power The new value for the activation laser power.
     */
    public void setControlSignal(double power) {
        engine.setControlSignal(power);
    }
    
    /**
     * Returns the info from the frame's currently active emitters.
     * 
     * @return info The emitter information from the current frame.
     */
    public List<FrameInfo> getEmitterStatus() {        
        return frameLogger.getFrameInfo();
    }
    
}
