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

import ch.epfl.leb.sass.models.obstructors.Obstructor;
import cern.jet.random.Gamma;
import cern.jet.random.Normal;
import ij.process.FloatProcessor;
import ij.process.ShortProcessor;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister;
import ch.epfl.leb.sass.models.fluorophores.internal.DefaultFluorophore;
import ch.epfl.leb.sass.utils.RNG;

/**
 * Encapsulator class which contains all device objects (camera, laser...)
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 * @deprecated  Use Microscope instead.
 */
@Deprecated
public class Device {
    private final Camera camera;
    private final FluorophoreProperties fluo;
    private final ArrayList<DefaultFluorophore> fluorophores;
    private final Laser laser;
    
    private final ArrayList<Obstructor> obstructors;
    
    private final Poisson poisson = RNG.getPoissonGenerator();
    private final Random random = RNG.getUniformGenerator();
    private final Gamma gamma = RNG.getGammaGenerator();
    private final Normal gaussian = RNG.getGaussianGenerator();
    
    /**
     * Initialize device with default parameters.
     */
    public Device() {
        camera = new Camera(400, //res_x
                            400, //res_y
                            100, //acq_speed, 
                            1.6, //readout_noise, 
                            0.06,//dark_current, 
                            0.9, //quantum_efficiency, 
                            45, //ADU_per_electron,
                            300, // EM_gain
                            100, // baseline
                            6.45 * 1e-6, //pixel_size, 
                            1.3, //NA, 
                            600 * 1e-9, //wavelength, 
                            100); //magnification, 
        
        fluo = new SimpleProperties(2500, //signal_per_frame, 
                               50, //background_per_frame, 
                               8, //base_Ton_frames, 
                               30, //base_Toff_frames, 
                               300); //base_Tbl_frames)
        
        laser = new Laser(0.1, //start_power, 
                          5.0, //max_power, 
                          0.1); //min_power)
        
        fluorophores = FluorophoreGenerator.generateFluorophoresRandom(
                1600, //n_fluos, 
                camera,
                fluo);
        
        obstructors = new ArrayList<Obstructor>();
        
        for (DefaultFluorophore e: fluorophores) {
            e.recalculateLifetimes(laser.getPower());
        }
    }
    
    /**
     * Initializes the device with given parameters.
     * @param cam camera properties
     * @param fluo fluorophore properties
     * @param laser laser settings
     * @param emitters list of fluorophores
     * @param obstructors list of obstructors
     */
    public Device(Camera cam, FluorophoreProperties fluo, Laser laser, ArrayList<DefaultFluorophore>emitters,
            ArrayList<Obstructor> obstructors) {
        camera = cam;
        this.fluo = fluo;
        this.laser = laser;
        this.fluorophores = emitters;
        
        if (obstructors!=null)
            this.obstructors = obstructors;
        else
            this.obstructors = new ArrayList<Obstructor>();
        
        for (DefaultFluorophore e: emitters) {
            e.recalculateLifetimes(laser.getPower());
        }
    }
    
    /**
     * Return the camera resolution
     * @return [res_x, res_y] int array
     */
    public int[] getResolution() {
        int[] result = new int[2];
        result[0] = camera.res_x; result[1] = camera.res_y;
        return result;
    }
    
    /**
     *
     * @return size of current FOV in square micrometers
     */
    public double getFOVsize_um() {
        return (getPixelSizeUm()*getPixelSizeUm())*camera.res_x*camera.res_y;
    }
    
    /**
     * 
     * @return length of one pixel side in micrometers
     */
    public double getPixelSizeUm() {
        return camera.pixel_size*1e6/camera.magnification;
    }
    
    /**
     * Modifies the laser power to desired value.
     * @param laser_power new laser power [W]
     */
    public void setLaserPower(double laser_power) {
        laser.setPower(laser_power);
        for (DefaultFluorophore e: fluorophores) {
            e.recalculateLifetimes(laser.getPower());
        }
    }
    
    /**
     * Return current power of the laser.
     * @return laser power
     */
    public double getLaserPower() {
        return laser.getPower();
    }
    
    /**
     * Returns the number of currently active emitters.
     * @return number of shining emitters
     */
    public double getOnEmitterCount() {
        int count = 0;
        for (DefaultFluorophore e: fluorophores) {
            if (e.isOn()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Generates a new frame based on the current device state, and moves
     * device state forward.
     * First the obstructions are drawn on the frame, then the fluorophores,
     * and afterwards noise is added.
     * @return simulated frame
     */
    public ShortProcessor simulateFrame() {
        float[][] pixels = new float[camera.res_x][camera.res_y];
        for (int row = 0; row < pixels.length; row++)
            Arrays.fill(pixels[row], 0.0f);
        
        // Add obstructions
        if (obstructors != null) {
            for (Obstructor o: obstructors) {
                o.applyTo(pixels);
            }
        }
        // Add emitters
        // The applyTo method also handles fluorophore state changes by calling
        // the simulateBrightness() method of an emitter.
        for (DefaultFluorophore f: fluorophores) {
            f.applyTo(pixels);
        }
        
        // Add noise
        addNoises(pixels);
        
        // Convert signal to ADU and add baseline.
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                pixels[x][y] *= camera.ADU_per_electron;
                pixels[x][y] += camera.baseline;
            }
        }
        
        // Convert to short array
        FloatProcessor fp = new FloatProcessor(pixels);
        return fp.convertToShortProcessor(false);
    }
    
    /**
     * Adds Poisson noise to the image.
     * @param image input image
     */
    private void addPoissonNoise(float[][] image) {
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                image[x][y] = (float) poisson.nextInt(image[x][y]);
            }
        }
    }
    
    /**
     * Adds Poisson,  readout,  thermal, and electron multiplication  noise to the image.
     * @param image image to be noised up
     */
    private void addNoises(float[][] image) {
        // add background
        for (int row=0; row < image.length; row++) {
            for (int col=0; col < image[row].length; col++) {
                image[row][col] += fluo.background;
            }
        }
        
        // Poisson noise
        addPoissonNoise(image);
        
        // Other noises
        for (int row=0; row < image.length; row++) {
            for (int col=0; col < image[row].length; col++) {
                if (camera.EM_gain != 0) {
                    // lambda parameter of nextDouble() is inverse of EM_gain.
                    image[row][col] = (float) gamma.nextDouble(
                            image[row][col]+0.01f,
                            1.0 / ((double) camera.EM_gain));
                }
                
                image[row][col] += camera.readout_noise*gaussian.nextDouble() +
                                   camera.thermal_noise*gaussian.nextDouble();
            }
        }
    }
    
}




