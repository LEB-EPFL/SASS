/*
 * Copyright (C) 2017 stefko
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

/**
 * Encapsulator class which contains all device objects (camera, laser...)
 * @author Marcel Stefko
 */
public class Device {
    private final Camera camera;
    private final Fluorophore fluo;
    private final ArrayList<Emitter> emitters;
    private final Laser laser;
    
    private final Poisson poisson;
    private final Random random;
    private final Gamma gamma;
    private final Normal gaussian;
    
    /**
     * Initialize device with default parameters.
     */
    public Device() {
        camera = new Camera(400, //res_x
                            400, //res_y
                            100, //acq_speed, 
                            1.6, //readout_noise, 
                            0.06, //dark_current, 
                            0.8, //quantum_efficiency, 
                            6, //gain, 
                            6.45 * 1e-6, //pixel_size, 
                            1.3, //NA, 
                            600 * 1e-9, //wavelength, 
                            100, //magnification, 
                            8 * 1e-9); //radius)
        
        fluo = new Fluorophore(500, //signal_per_frame, 
                               5, //background_per_frame, 
                               8, //base_Ton_frames, 
                               30, //base_Toff_frames, 
                               600); //base_Tbl_frames)
        
        laser = new Laser(1.0, //start_power, 
                          5.0, //max_power, 
                          0.2); //min_power)
        
        emitters = EmitterGenerator.generateEmittersRandom(
                1600, //n_fluos, 
                camera,
                fluo);
        for (Emitter e: emitters) {
            e.recalculate_lifetimes(laser.getPower());
        }
        random = new Random();
        poisson = new Poisson(1.0, new MersenneTwister(random.nextInt()));
        gamma = new Gamma(1.0, 5.0, new MersenneTwister(random.nextInt()));
        gaussian = new Normal(0.0, 1.0, new MersenneTwister(random.nextInt()));
    }
    
    /**
     * Modifies the laser power to desired value.
     * @param laser_power new laser power [W]
     */
    public void changeLaserPower(double laser_power) {
        laser.setPower(laser_power);
        for (Emitter e: emitters) {
            e.recalculate_lifetimes(laser.getPower());
        }
    }
    
    /**
     * Generates a new frame based on the current device state, and moves
     * device state forward.
     * @return simulated frame
     */
    public ShortProcessor simulateFrame() {
        float[][] pixels = new float[camera.res_x][camera.res_y];
        for (int row = 0; row < pixels.length; row++)
            Arrays.fill(pixels[row], 0.0f);
        
        double s_xy = camera.fwhm_digital / 2.3548;
        double r = 3*s_xy;
        double denom = sqrt(2.0)*s_xy;
        // Diffraction
        for (Emitter e: emitters) {
            double brightness = e.simulate_brightness();
            for (Pixel p: e.getPixelList()) {
                try {
                    pixels[p.x][p.y] += 0.25* brightness * p.getSignature();
                } catch (ArrayIndexOutOfBoundsException ex) {
                    //Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        
        pixels = addNoises(pixels);
        FloatProcessor fp = new FloatProcessor(pixels);
        return fp.convertToShortProcessor(false);
    }
    
    /**
     * Adds Poisson noise to the image.
     * @param image input image
     * @return image with Poisson noise added
     */
    public float[][] addPoissonNoise(float[][] image) {
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                image[x][y] = (float) poisson.nextInt(image[x][y]);
            }
        }
        return image;
    }
    
    /**
     * Adds Poisson, readout, thermal and quantum gain noise to the image.
     * @param image image to be noised up
     * @return image with noises added
     */
    public float[][] addNoises(float[][] image) {
        // add background
        for (int row=0; row < image.length; row++) {
            for (int col=0; col < image[row].length; col++) {
                image[row][col] += fluo.background;
            }
        }
        
        // Poisson noise
        image = addPoissonNoise(image);
        
        // Other noises
        for (int row=0; row < image.length; row++) {
            for (int col=0; col < image[row].length; col++) {
                image[row][col] += camera.readout_noise*gaussian.nextDouble() +
                                   camera.thermal_noise*gaussian.nextDouble() +
                                   gamma.nextDouble(image[row][col]+0.1f,camera.quantum_gain);
            }
        }
        return image;
    }
    
}




