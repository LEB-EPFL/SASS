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
package ch.epfl.leb.sass.models.legacy;

import ch.epfl.leb.sass.logging.Message;
import ch.epfl.leb.sass.models.Microscope;
import ch.epfl.leb.sass.models.obstructors.Obstructor;
import ch.epfl.leb.sass.models.fluorophores.internal.DefaultFluorophore;
import ch.epfl.leb.sass.utils.RNG;
import ch.epfl.leb.sass.simulator.internal.AbstractSimulator;
import ch.epfl.leb.sass.models.obstructors.internal.ConstantBackground;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import ch.epfl.leb.sass.utils.images.internal.DefaultImageS;
import com.google.gson.JsonObject;
import ij.gui.GenericDialog;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the ImageGenerator interface with methods required by AbstractSimulator.
 * @author Marcel Stefko
 */
@Deprecated
public class STORMsim extends AbstractSimulator {
    private Device device;

    private ArrayList<Double> emitter_history;
            
    /**
     * Initialize the generator, either from GUI dialog or use default params.
     * @param device
     */
    public STORMsim(Device device) {
        super();
        
        if (device == null) {
            RNG.setSeed(1);
            initDeviceFromDialog();
        } else {
            this.device = device;
        }
        int[] res = this.device.getResolution();
        stack = new DefaultImageS(res[0], res[1]);

        emitter_history = new ArrayList<Double>();
        emitter_history.add(0.0);
    }
    
    @Override
    public String getObjectiveJsonName() {
        return null;
    }
    
    @Override
    public String getStageJsonName() {
        return null;
    }
    
    @Override
    public String getLaserJsonName() {
        return null;
    }
    
    @Override
    public String getCameraJsonName() {
        return null;
    }
    
    @Override
    public void saveState(File file) {
    }
    
    @Override
    public void saveMessages(File file) {
    }
    
    @Override
    public JsonObject toJsonMessages() {
        return null;
    }
    
    @Override
    public List<Message> getMessages() {
        return null;
    }
    /**
     * Returns information about the state of the sample fluorescence.
     * 
     * @return A JSON object containing information on the sample fluorescence.
     */
    @Override
    public JsonObject toJsonState() {
        return null;
    }
    
    @Override
    public String getFluorescenceJsonName() {
        return "";
    }
    
    @Override
    public double getTrueSignal(int image_no) {
        return emitter_history.get(image_no) / device.getFOVsize_um() * 10000;
    }
    
    @Override
    public Microscope getMicroscope() {
        return null;
    }

    @Override
    public ImageS getNextImage() throws ImageShapeException {
        // we calculate emitter count first so it corresponds with the beginning
        // of the frame rather than end of the frame
        emitter_history.add(device.getOnEmitterCount());
        ImageS is = device.simulateFrame();
              
        stack.concatenate(is);
        return is;
    }
    
    /**
     * Advance the simulation by one time step (i.e. one frame), but do not create an image.
     */
    @Override
    public void incrementTimeStep() {
        emitter_history.add(device.getOnEmitterCount());
        
        // Returned ImageProcess instance is not captured. This was easier than
        // outright avoiding any image creation that happens inside
        // simulateFrame() because the DefaultFluorophore state transitions are handled
        // inside a long chain of method calls. -kmd
        device.simulateFrame();
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
        gd.addNumericField("ADU per electron", 0.0200, 4);
        gd.addNumericField("EM gain", 100, 0);
        gd.addNumericField("Baseline", 100, 0);
        gd.addNumericField("Pixel size [um]", 6.45, 3);
        gd.addNumericField("Numerical aperture", 1.3, 2);
        gd.addNumericField("Wavelength [nm]", 600, 0);
        gd.addNumericField("Magnification", 100, 0);
        gd.showDialog();
        
        Camera camera = new Camera((int)gd.getNextNumber(), //res_x
                            (int)gd.getNextNumber(), //res_y
                            (int)gd.getNextNumber(), //acq_speed, 
                            gd.getNextNumber(), //readout_noise, 
                            gd.getNextNumber(), //dark_current, 
                            gd.getNextNumber(), //quantum_efficiency, 
                            gd.getNextNumber(), //ADU_per_electron,
                            (int)gd.getNextNumber(), // EM_gain,
                            (int)gd.getNextNumber(), // baseline,
                            gd.getNextNumber() * 1e-6, //pixel_size, 
                            gd.getNextNumber(), //NA, 
                            gd.getNextNumber() * 1e-9, //wavelength, 
                            gd.getNextNumber()); //magnification, 
        
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
        
                
        SimpleProperties fluo = new SimpleProperties(gd.getNextNumber(), //signal_per_frame, 
                gd.getNextNumber(), //background_per_frame, 
                gd.getNextNumber(), //base_Ton_frames, 
                gd.getNextNumber(), //base_Toff_frames, 
                gd.getNextNumber()); //base_Tbl_frames)
        
        Laser laser = new Laser(gd.getNextNumber(), //start_power, 
                          gd.getNextNumber(), //max_power, 
                          gd.getNextNumber()); //min_power)
        
        
        ArrayList<DefaultFluorophore> emitters;
        int emitter_no = (int) gd.getNextNumber();
        if (emitter_no == 0) {
            try {
                emitters = FluorophoreGenerator.parseFluorophoresFromCsv(null, camera, fluo, true);
            } catch (IOException ex) {
                Logger.getLogger(STORMsim.class.getName()).log(Level.SEVERE, null, ex);
                emitters = FluorophoreGenerator.generateFluorophoresRandom(
                    (int)100, //n_fluos, 
                    camera,
                    fluo);
            }
        } else {
            emitters = FluorophoreGenerator.generateFluorophoresRandom(
                emitter_no, //n_fluos, 
                camera,
                fluo);
        }
        
        ArrayList<Obstructor> obstructors = new ArrayList<Obstructor>();
        obstructors.add(new GoldBeads((int)gd.getNextNumber(), //beadCount
                                      camera,
                                      (int)gd.getNextNumber())); // bead brightness
        try {
            obstructors.add(new ConstantBackground(camera));
        } catch (RuntimeException ex) {
            Logger.getLogger(STORMsim.class.getName()).log(Level.FINER, null, ex);
        }
            
        
        device = new Device(camera, fluo, laser, emitters, obstructors);
    }

    /**
     *
     * @return length of one pixel side in micrometers
     */
    @Override
    public double getObjectSpacePixelSize() {
        return device.getPixelSizeUm();
    }

    @Override
    public double getFOVSize() {
        return device.getFOVsize_um();
    }
    
    @Override
    public String getShortTrueSignalDescription() {
        return "";
    }
}
