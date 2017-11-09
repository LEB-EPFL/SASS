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
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.dynamics.FluorophoreDynamicsBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.dynamics.FluorophoreDynamics;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.FluorophoreCommandBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.FluorophoreCommand;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Camera;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Laser;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Objective;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Stage;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSFBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.obstructors.commands.ObstructorCommandBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.obstructors.commands.ObstructorCommand;
import ch.epfl.leb.sass.simulator.generators.realtime.backgrounds.commands.BackgroundCommandBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.backgrounds.commands.BackgroundCommand;
import ij.process.FloatProcessor;
import ij.process.ShortProcessor;
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
    private final FluorophoreDynamics fluorDynamics;
    private final List<Fluorophore> fluorophores;
    private List<Obstructor> obstructors;
    private final BackgroundCommand background;
    
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
     * @param positionBuilder Positions fluorophore's within the field of view.
     * @param fluorDynamicsBuilder
     * @param obstructorBuilder Creates the obstructors, e.g. fiducials.
     * @param backgroundBuilder Creates the background signal on the image.
     */
    public Microscope(
            Camera.Builder cameraBuilder,
            Laser.Builder laserBuilder,
            Objective.Builder objectiveBuilder,
            PSFBuilder psfBuilder,
            Stage.Builder stageBuilder,
            FluorophoreCommandBuilder positionBuilder,
            FluorophoreDynamicsBuilder fluorDynamicsBuilder,
            ObstructorCommandBuilder obstructorBuilder,
            BackgroundCommandBuilder backgroundBuilder) {
        
        // Build objects that do not require further setup
        this.camera = cameraBuilder.build();
        this.laser = laserBuilder.build();
        this.objective = objectiveBuilder.build();
        this.stage = stageBuilder.build();
        this.fluorDynamics = fluorDynamicsBuilder.build();
        
        // Extract the wavelength from the fluorophores
        double wavelength;
        wavelength = this.fluorDynamics.getWavelength();
        
        // Set the stage displacement for axially-dependent PSFs, the NA, and
        // the Gaussian FWHM for those PSFs that use a Gaussian approximation
        double fwhm = objective.airyFWHM(wavelength) / camera.getPixelSize();
        psfBuilder.stageDisplacement(stage.getZ())
                  .NA(objective.getNA())
                  .FWHM(fwhm)
                  .wavelength(wavelength)
                  .resLateral(camera.getPixelSize() / objective.getMag());
        
        // Create the set of fluorophores.
        positionBuilder.camera(camera)
                       .psfBuilder(psfBuilder)
                       .fluorDynamics(fluorDynamics);
        FluorophoreCommand fluorCommand = positionBuilder.build();
        this.fluorophores = fluorCommand.generateFluorophores();
        
        // Build the obstructors
        obstructorBuilder.camera(camera).stage(stage).psfBuilder(psfBuilder);
        ObstructorCommand obstrCommand = obstructorBuilder.build();
        this.obstructors = obstrCommand.generateObstructors();
        
        // Set the size of the background image and build it
        backgroundBuilder.nX(camera.getNX()).nY(camera.getNY());
        this.background = backgroundBuilder.build();
        
        // Determine the lifetimes for each fluorophore's current state
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
    public double getFovSize() {
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
        
        addBackground(pixels);
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
     * Adds the background signal to the image.
     * @param image The image to which a background will be added.
     */
    private void addBackground(float[][] image) {
        float[][] backgroundSignal = this.background.generateBackground();
        
        for (int row=0; row < image.length; row++) {
            for (int col=0; col < image[row].length; col++) {
                image[row][col] += backgroundSignal[row][col];
            }
        }
    }
    
    /**
     * Simulates noise sources.
     * @param image image to be noised up.
     */
    private void addNoise(float[][] image) {
        
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
