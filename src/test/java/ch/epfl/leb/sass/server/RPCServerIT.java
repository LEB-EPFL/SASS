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
package ch.epfl.leb.sass.server;

import ch.epfl.leb.sass.IntegrationTest;
import ch.epfl.leb.sass.models.Microscope;
import ch.epfl.leb.sass.models.backgrounds.internal.commands.GenerateUniformBackground;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.components.Laser;
import ch.epfl.leb.sass.models.components.Objective;
import ch.epfl.leb.sass.models.components.Stage;
import ch.epfl.leb.sass.models.fluorophores.internal.commands.GenerateFluorophoresGrid2D;
import ch.epfl.leb.sass.models.fluorophores.internal.dynamics.PalmDynamics;
import ch.epfl.leb.sass.models.obstructors.internal.commands.GenerateFiducialsRandom2D;
import ch.epfl.leb.sass.models.psfs.internal.Gaussian2D;
import ch.epfl.leb.sass.utils.RNG;
import ch.epfl.leb.sass.simulator.Simulator;
import ch.epfl.leb.sass.simulator.SimulationManager;
import ch.epfl.leb.sass.simulator.internal.RPCSimulator;
import ch.epfl.leb.sass.simulator.internal.DefaultSimulationManager;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.experimental.categories.Category;

/**
 * Integration tests for the RPCServer.
 * 
 * @author Kyle M. Douglass
 */
@Category(IntegrationTest.class)
public class RPCServerIT {
    
    /**
     * The number of simulations to run.
     */
    private final int NUM_SIMS = 2;
    
    /**
     * The port for RPC server communications.
     */
    private final int PORT = 9090;
    
    /**
     * The simulation manager that the server will interact with.
     */
    private SimulationManager manager;
    
    /**
     * The ground truth simulation objects.
     */
    private Simulator[] sims = new Simulator[NUM_SIMS];
    
    /**
     * Flag indicating whether the microscope has been setup.
     */
    private static boolean setupIsDone = false;
    
    /**
     * Sets up two different Microscopes for acquisition simulations.
     */
    @Before
    public void setUp() {
        if (setupIsDone) {
            // Skip setup if it has already run.
            return;
        }
        
        // The seed determines the outputs of the random number generator.
        RNG.setSeed(42);
        
        Camera.Builder cameraBuilder = new Camera.Builder();

        cameraBuilder.nX(32); // Number of pixels in x
        cameraBuilder.nY(32); // Number of pixels in y
        cameraBuilder.readoutNoise(1.6); // Standard deviation, electrons
        cameraBuilder.darkCurrent(0.06); 
        cameraBuilder.quantumEfficiency(0.8);
        cameraBuilder.aduPerElectron(2.2);
        cameraBuilder.emGain(0);       // Set to zero for CMOS cameras
        cameraBuilder.baseline(100);   // ADU
        cameraBuilder.pixelSize(6.45); // microns

        // Objective
        Objective.Builder objectiveBuilder = new Objective.Builder();

        objectiveBuilder.NA(1.3); // Numerical aperture
        objectiveBuilder.mag(60); // Magnification

        // Laser
        Laser.Builder laserBuilder = new Laser.Builder();

        laserBuilder.currentPower(0.0);
        laserBuilder.minPower(0.0);
        laserBuilder.maxPower(500.0);

        // Stage
        Stage.Builder stageBuilder = new Stage.Builder();

        stageBuilder.x(0);
        stageBuilder.y(0);
        stageBuilder.z(0); // Coverslip surface is at z = 0

        // PSF, create a 2D Gaussian point-spread function
        Gaussian2D.Builder psfBuilder = new Gaussian2D.Builder();

        // Fluorophore dynamics and properties; rates are in units of 1/frames
        PalmDynamics.Builder fluorPropBuilder = new PalmDynamics.Builder();

        fluorPropBuilder.signal(2500); // Photons per fluorophore per frame
        fluorPropBuilder.wavelength(0.6); // Wavelength, microns
        fluorPropBuilder.kA(100);      // Activation rate
        fluorPropBuilder.kB(0);        // Bleaching rate
        fluorPropBuilder.kD1(0.065);   // Transition rate to first dark state
        fluorPropBuilder.kD2(0.013);   // Transition rate to second dark state
        fluorPropBuilder.kR1(0.004);   // Return rate from first dark state
        fluorPropBuilder.kR2(0.157);   // Return rate from second dark state

        // Fluorophore positions on a square grid
        GenerateFluorophoresGrid2D.Builder fluorPosBuilder = 
                new GenerateFluorophoresGrid2D.Builder();
        fluorPosBuilder.spacing(4); // pixels
		
        // Add fiducials to the field of view at a random location
        GenerateFiducialsRandom2D.Builder fidBuilder = 
                new GenerateFiducialsRandom2D.Builder();
        fidBuilder.numFiducials(2);
        fidBuilder.brightness(3000); // photons per frame

        // Add a constant background
        GenerateUniformBackground.Builder backgroundBuilder = 
                new GenerateUniformBackground.Builder();
        backgroundBuilder.backgroundSignal(10); // photons

        // Assemble the microscope and the simulator.
        Microscope microscope1 = new Microscope(
            cameraBuilder,
            laserBuilder,
            objectiveBuilder,
            psfBuilder,
            stageBuilder,
            fluorPosBuilder,
            fluorPropBuilder,
            fidBuilder,
            backgroundBuilder);
        RPCSimulator sim0 = new RPCSimulator(microscope1);
        sims[0] = sim0;
        
        // Change the number of pixels for the second microscopy
        cameraBuilder.nX(64);
        cameraBuilder.nY(64);
        Microscope microscope2 = new Microscope(
            cameraBuilder,
            laserBuilder,
            objectiveBuilder,
            psfBuilder,
            stageBuilder,
            fluorPosBuilder,
            fluorPropBuilder,
            fidBuilder,
            backgroundBuilder);
        RPCSimulator sim1 = new RPCSimulator(microscope2);
        sims[1] = sim1;
        
        // Add the simulations to the manager.
        manager = new DefaultSimulationManager();
        manager.addSimulator(sims[0]);
        manager.addSimulator(sims[1]);
        
        setupIsDone = true;
    }

    /**
     * Test of serve, stop, and isServing methods, of class RPCServer.
     */
    @Test
    public void testServeStopIsServing() throws InterruptedException {
        System.out.println("isServing");
        RPCServer instance = new RPCServer(manager, PORT);
        
        Runnable serverRunnable = new Runnable() {
            public void run() {
                instance.serve();
            }
        };
        new Thread(serverRunnable).start();
        Thread.sleep(1000); // Give the server time to start
        System.out.println("Server started!");     
        
        boolean expResult = true;
        boolean result = instance.isServing();
        assertEquals(expResult, result);
        
        instance.stop();
        Thread.sleep(1000); // Give the server time to stop
        expResult = false;
        result = instance.isServing();
        assertEquals(expResult, result);
    }
    
}
