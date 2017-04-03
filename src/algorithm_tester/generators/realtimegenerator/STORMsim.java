/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 *
 * Author: Marcel Stefko
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
package algorithm_tester.generators.realtimegenerator;

import algorithm_tester.ImageGenerator;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ImageGenerator wrapper around the RealTimeGenerator implementation.
 * @author Marcel Stefko
 */
public class STORMsim implements ImageGenerator{
    Device device;
    HashMap<String,Double> parameters;
    ImageStack stack;
    ArrayList<Double> emitter_history;
            
    public STORMsim() {
        device = new Device();
        int[] res = device.getResolution();
        stack = new ImageStack(res[0],res[1]);
        parameters = new HashMap<String,Double>();
        emitter_history = new ArrayList<Double>();
        emitter_history.add(0.0);
    }
    
    @Override
    public double getTrueSignal(int image_no) {
        return emitter_history.get(image_no);
    }

    @Override
    public ImageProcessor getNextImage() {
        ImageProcessor ip = device.simulateFrame();
        stack.addSlice(ip);
        emitter_history.add(device.getOnEmitterCount());
        return ip;
    }

    @Override
    public void setCustomParameters(HashMap<String, Double> map) {
        parameters = map;
    }

    @Override
    public HashMap<String, Double> getCustomParameters() {
        parameters.put("real_laser_power", device.getLaserPower());
        return parameters;
    }
    
    public void saveStack(File file) {
        ImagePlus imp = new ImagePlus("stack", stack);
        FileSaver fs = new FileSaver(imp);
        fs.saveAsTiffStack(file.getAbsolutePath());
    }

    @Override
    public void setControlSignal(double value) {
        parameters.put("target_laser_power", value);
        device.setLaserPower(value);
    }

    @Override
    public double getControlSignal() {
        return device.getLaserPower();
    }
}
