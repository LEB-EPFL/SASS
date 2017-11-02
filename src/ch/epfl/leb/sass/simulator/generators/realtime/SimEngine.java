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
package ch.epfl.leb.sass.simulator.generators.realtime;


import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.SimpleProperties;
import ch.epfl.leb.sass.simulator.generators.AbstractGenerator;
import ch.epfl.leb.sass.simulator.generators.realtime.obstructors.ConstantBackground;
import ch.epfl.leb.sass.simulator.generators.realtime.obstructors.GoldBeads;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the ImageGenerator interface with methods required by AbstractGenerator.
 * @author Marcel Stefko
 */
public class SimEngine extends AbstractGenerator {
    private Microscope microscope;

    private ArrayList<Double> emitterHistory;
            
    /**
     * Initialize the generator, either from GUI dialog or use default params.
     * @param microscope
     */
    public SimEngine(Microscope microscope) {
        super();
        this.microscope = microscope;
        
        int[] res = this.microscope.getResolution();
        stack = new ImageStack(res[0],res[1]);

        emitterHistory = new ArrayList<Double>();
        emitterHistory.add(0.0);
    }
    
    @Override
    public double getTrueSignal(int image_no) {
        return emitterHistory.get(image_no) / microscope.getFovSize() * 10000;
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
     * Advance the simulation by one time step (i.e. one frame), but do not create an image.
     */
    @Override
    public void incrementTimeStep() {
        emitterHistory.add(microscope.getOnEmitterCount());
        
        // Returned ImageProcess instance is not captured. This was easier than
        // outright avoiding any image creation that happens inside
        // simulateFrame() because the Fluorophore state transitions are handled
        // inside a long chain of method calls. -kmd
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
     * @return length of one pixel side in micrometers
     */
    @Override
    public double getPixelSizeUm() {
        return microscope.getPixelSize();
    }

    @Override
    public double getFOVSizeUm2() {
        return microscope.getFovSize();
    }
}
