/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
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

import cern.jet.random.Gamma;
import cern.jet.random.Normal;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.FluorophoreCommandBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.FluorophoreCommand;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Camera;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Laser;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Objective;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Stage;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSFBuilder;
import ij.process.FloatProcessor;
import ij.process.ShortProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cern.jet.random.Poisson;

/**
 * Integrates all the components into one microscope.
 */
public class Microscope {
    
    // Assigned in the constructor
    private final Camera camera;
    private final Laser laser;
    private final Objective objective;
    private final Stage stage;
    private final FluorophoreProperties fluorProp;
    private final List<Fluorophore> fluorophores;
    private List<Obstructor> obstructors;
    
    // Random number generators
    private final Poisson poisson = RNG.getPoissonGenerator();
    private final Gamma gamma = RNG.getGammaGenerator();
    private final Normal gaussian = RNG.getGaussianGenerator();
    
    /** 
     * Initializes the microscope for simulations.
     * 
     * @param cameraBuilder
     * @param laserBuilder
     * @param objectiveBuilder
     * @param psfBuilder
     * @param stageBuilder
     * @param fluorBuilder
     * @param fluorProp
     * @param obstructors 
     */
    public Microscope(
            Camera.Builder cameraBuilder,
            Laser.Builder laserBuilder,
            Objective.Builder objectiveBuilder,
            PSFBuilder psfBuilder,
            Stage.Builder stageBuilder,
            FluorophoreCommandBuilder fluorBuilder,
            FluorophoreProperties fluorProp,
            List<Obstructor> obstructors) {
        
        this.camera = cameraBuilder.build();
        this.laser = laserBuilder.build();
        this.objective = objectiveBuilder.build();
        this.stage = stageBuilder.build();
        this.fluorProp = fluorProp;
        this.obstructors = obstructors;
        
        // Set the stage displacement for axially-dependent PSFs, the NA, and
        // the Gaussian FWHM for those PSFs that use a Gaussian approximation
        psfBuilder.stageDisplacement(stage.getZ()).NA(objective.getNA())
                  .FWHM(objective.airyFWHM(fluorProp.getWavelength()));
        
        // Create the set of fluorophores.
        fluorBuilder.camera(camera).psfBuilder(psfBuilder).fluorProp(fluorProp);
        FluorophoreCommand fluorCommand = fluorBuilder.build();
        this.fluorophores = fluorCommand.generateFluorophores();
        
        // Case where there are no obstructors in this simulation
        if (obstructors!=null)
            this.obstructors = obstructors;
        else
            this.obstructors = new ArrayList<>();
        
        for (Fluorophore f: fluorophores) {
            f.recalculate_lifetimes(laser.getPower());
        }
    }
    
    /**
     * Return the number of camera pixels in x and y.
     * @return 2D array with number of pixels in x and y.
     */
    public int[] getResolution() {
        int[] result = new int[2];
        result[0] = camera.getNX(); result[1] = camera.getNY();
        return result;
    }
    
    /**
     *
     * @return size of current FOV in square micrometers
     */
    public double getFovSize_() {
        return (getPixelSize() * getPixelSize()) 
               * camera.getNX() * camera.getNY();
    }
    
    /**
     * 
     * @return length of one pixel side in micrometers
     */
    public double getPixelSize() {
        return this.camera.getPixelSize() / this.objective.getMag();
    }
    
    /**
     * Modifies the laser power to desired value.
     * @param laserPower new laser power
     */
    public void setLaserPower(double laserPower) {
        laser.setPower(laserPower);
        for (Fluorophore e: fluorophores) {
            e.recalculate_lifetimes(laser.getPower());
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
        for (Fluorophore e: fluorophores) {
            if (e.isOn()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Generates a new frame and moves the device state forward.
     * 
     * First the obstructors are drawn on the frame, then the fluorophores,
     * and finally noise.
     * 
     * @return simulated frame
     */
    public ShortProcessor simulateFrame() {
        float[][] pixels = new float[this.camera.getNX()][this.camera.getNY()];
        for (int row = 0; row < pixels.length; row++)
            Arrays.fill(pixels[row], 0.0f);
        
        // Add obstructions
        if (obstructors != null) {
            for (Obstructor o: obstructors) {
                o.applyTo(pixels);
            }
        }
        // Add fluorophores
        // The applyTo method also handles fluorophore state changes by calling
        // the simulateBrightness() method of an emitter.
        for (Fluorophore f: fluorophores) {
            f.applyTo(pixels);
        }
        
        addNoise(pixels);
        
        // Convert signal to ADU and add baseline.
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                pixels[x][y] *= this.camera.getAduPerElectron();
                pixels[x][y] += this.camera.getBaseline();
            }
        }
        
        // Convert to short array
        FloatProcessor fp = new FloatProcessor(pixels);
        return fp.convertToShortProcessor(false);
    }
    
    /**
     * Simulates noise sources.
     * @param image image to be noised up.
     */
    private void addNoise(float[][] image) {
        // Add a uniform background to each pixel.
        // TODO: Move the background to a more logical location.
        for (int row=0; row < image.length; row++) {
            for (int col=0; col < image[row].length; col++) {
                image[row][col] += this.fluorProp.background;
            }
        }
        
        // Poisson noise
        addPoissonNoise(image);
        
        for (int row=0; row < image.length; row++) {
            // Multiplication noise from the EM gain register
            for (int col=0; col < image[row].length; col++) {
                if (this.camera.getEmGain() != 0) {
                    // lambda parameter of nextDouble() is inverse of EM_gain.
                    image[row][col] = (float) gamma.nextDouble(
                            image[row][col]+0.01f,
                            1.0 / ((double) this.camera.getEmGain()));
                }
                
                // Dark noises (readout and thermal)
                image[row][col] += 
                        this.camera.getReadoutNoise()*gaussian.nextDouble() +
                        this.camera.getThermalNoise()*gaussian.nextDouble();
            }
        }
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
}
