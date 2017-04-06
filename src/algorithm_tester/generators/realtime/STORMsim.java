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
package algorithm_tester.generators.realtime;

import algorithm_tester.generators.AbstractGenerator;
import algorithm_tester.generators.realtime.obstructors.GoldBeads;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ImageGenerator wrapper around the RealTimeGenerator implementation.
 * @author Marcel Stefko
 */
public class STORMsim extends AbstractGenerator {
    Device device;

    ArrayList<Double> emitter_history;
            
    public STORMsim(boolean showDialog) {
        if (showDialog) {
            initDeviceFromDialog();
        } else {
            device = new Device();
        }
        int[] res = device.getResolution();
        stack = new ImageStack(res[0],res[1]);

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

    @Override
    public void setControlSignal(double value) {
        parameters.put("target_laser_power", value);
        device.setLaserPower(value);
    }

    @Override
    public double getControlSignal() {
        return device.getLaserPower();
    }
    
    private void initDeviceFromDialog() {
        GenericDialog gd = new GenericDialog("Camera initialization");
        gd.addMessage("Camera:");
        gd.addNumericField("Resolution X", 400, 0);
        gd.addNumericField("Resolution Y", 400, 0);
        gd.addNumericField("Framerate [fps]", 100, 0);
        gd.addNumericField("Readout noise [rms]", 1.6, 1);
        gd.addNumericField("Dark current [e/px/frame]",0.06,3);
        gd.addNumericField("Quantum efficiency", 0.8, 2);
        gd.addNumericField("Gain", 6, 1);
        gd.addNumericField("Pixel size [um]", 6.45, 3);
        gd.addNumericField("Numerical aperture", 1.3, 2);
        gd.addNumericField("Wavelength [nm]", 600, 0);
        gd.addNumericField("Magnification", 100, 0);
        gd.addNumericField("Cross-section radius [nm]", 8, 1);
        gd.showDialog();
        
        Camera camera = new Camera((int)gd.getNextNumber(), //res_x
                            (int)gd.getNextNumber(), //res_y
                            (int)gd.getNextNumber(), //acq_speed, 
                            gd.getNextNumber(), //readout_noise, 
                            gd.getNextNumber(), //dark_current, 
                            gd.getNextNumber(), //quantum_efficiency, 
                            gd.getNextNumber(), //gain, 
                            gd.getNextNumber() * 1e-6, //pixel_size, 
                            gd.getNextNumber(), //NA, 
                            gd.getNextNumber() * 1e-9, //wavelength, 
                            gd.getNextNumber(), //magnification, 
                            gd.getNextNumber() * 1e-9); //radius)
        
        gd = new GenericDialog("Device initialization");
        
        gd.addMessage("Fluorophore:");
        gd.addNumericField("Fluo signal", 2500, 0);
        gd.addNumericField("Fluo background", 50, 0);
        gd.addNumericField("Base Ton", 8, 0);
        gd.addNumericField("Base Toff", 30, 0);
        gd.addNumericField("Base Tbl", 300, 0);
        
        gd.addMessage("Laser:");
        gd.addNumericField("Laser start", 0.0, 2);
        gd.addNumericField("Laser max", 5.0, 2);
        gd.addNumericField("Laser min", 0.0, 2);
        
        gd.addMessage("Emitter:");
        gd.addNumericField("Emitter no.", 1600, 0);

        
        gd.addMessage("Obstructors:");
        gd.addNumericField("Gold bead no.", 10, 0);
        gd.addNumericField("Gold bead brightness", 4000, 0);
        gd.showDialog();
        
                
        FluorophoreProperties fluo = new FluorophoreProperties(gd.getNextNumber(), //signal_per_frame, 
                gd.getNextNumber(), //background_per_frame, 
                gd.getNextNumber(), //base_Ton_frames, 
                gd.getNextNumber(), //base_Toff_frames, 
                gd.getNextNumber()); //base_Tbl_frames)
        
        Laser laser = new Laser(gd.getNextNumber(), //start_power, 
                          gd.getNextNumber(), //max_power, 
                          gd.getNextNumber()); //min_power)
        
        ArrayList<Fluorophore> emitters = FluorophoreGenerator.generateFluorophoresRandom(
                (int)gd.getNextNumber(), //n_fluos, 
                camera,
                fluo);
        
        ArrayList<Obstructor> obstructors = new ArrayList<Obstructor>();
        obstructors.add(new GoldBeads((int)gd.getNextNumber(), //beadCount
                                      camera,
                                      (int)gd.getNextNumber())); // bead brightness
        
        
        device = new Device(camera, fluo, laser, emitters, obstructors);
    }
}
