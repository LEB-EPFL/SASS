/* 
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * Author(s): Marcel Stefko, Kyle M. Douglass
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


import ch.epfl.leb.sass.models.Microscope;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The basic simulation engine from which others may be derived.
 * 
 * @author Marcel Stefko
 */
public class DefaultSimulator extends AbstractSimulator {
    /**
     * Scaling factor for the density.
     * 
     * Multiplying the estimated density by a factor of 100 means that the
     * output returns spots per 10 um x 10 um = 100 um^2 area.
     */
    private final double SCALEFACTOR = 100;
    private Microscope microscope;

    private ArrayList<Double> emitterHistory;
            
    /**
     * Initialize the generator.
     * @param microscope
     */
    public DefaultSimulator(Microscope microscope) {
        super();
        this.microscope = microscope;
        
        int[] res = this.microscope.getResolution();
        stack = new ImageStack(res[0],res[1]);

        emitterHistory = new ArrayList<Double>();
        emitterHistory.add(0.0);
    }
    
    @Override
    public double getTrueSignal(int image_no) {
        return emitterHistory.get(image_no) /
               microscope.getFovSize() * SCALEFACTOR;
    }

    @Override
    public ImageProcessor getNextImage() {
        // we calculate emitter count first so it corresponds with the beginning
        // of the frame rather than end of the frame
        emitterHistory.add(microscope.getOnEmitterCount());
        ImageProcessor ip = microscope.simulateFrame();
        stack.addSlice(ip);
        return ip;
    }
    
    /**
     * Advance the simulation by one time step (i.e. one frame).
     * 
     * Simulates a frame but does not create an image.
     */
    @Override
    public void incrementTimeStep() {
        emitterHistory.add(microscope.getOnEmitterCount());
        microscope.simulateFrame();
    }

    @Override
    public void setCustomParameters(HashMap<String, Double> map) {
        parameters = map;
    }

    @Override
    public HashMap<String, Double> getCustomParameters() {
        parameters.put("real_laser_power", microscope.getLaserPower());
        return parameters;
    }

    @Override
    public void setControlSignal(double value) {
        parameters.put("target_laser_power", value);
        microscope.setLaserPower(value);
    }

    @Override
    public double getControlSignal() {
        return microscope.getLaserPower();
    }

    /**
     *
     * @return Length of one pixel side in object-space units.
     */
    @Override
    public double getObjectSpacePixelSize() {
        return microscope.getObjectSpacePixelSize();
    }

    /**
     * 
     * @return The size of the FOV in square object-space units.
     */
    @Override
    public double getFOVSize() {
        return microscope.getFovSize();
    }
    
    @Override
    public String getShortTrueSignalDescription() {
        String descr = "counts/" + String.valueOf((int) SCALEFACTOR) + " µm^2";
        return descr;
    }
}
