/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
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
package ch.epfl.leb.sass.models;

import ch.epfl.leb.sass.models.components.internal.DefaultLaser;
import ch.epfl.leb.sass.models.components.internal.DefaultCamera;
import ch.epfl.leb.sass.IntegrationTest;
import ch.epfl.leb.sass.models.components.*;
import ch.epfl.leb.sass.models.psfs.internal.Gaussian2D;
import ch.epfl.leb.sass.models.photophysics.internal.PalmDynamics;
import ch.epfl.leb.sass.models.fluorophores.internal.commands
                                                    .GenerateFluorophoresGrid2D;
import ch.epfl.leb.sass.models.obstructors.internal.commands
                                                   .GenerateFiducialsRandom2D;
import ch.epfl.leb.sass.models.backgrounds.internal.commands
                                                   .GenerateUniformBackground;
import ch.epfl.leb.sass.utils.RNG;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import org.junit.Test;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.*;


/**
 * Integration tests for the Microscope class.
 * @author Kyle M. Douglass
 * @see https://stackoverflow.com/questions/2606572/junit-splitting-integration-test-and-unit-tests
 */
@Category(IntegrationTest.class)
public class MicroscopeIT {
    
    /**
     * The microscope instance to simulate.
     */
    private static Microscope microscope;
    
    /**
     * Flag indicating whether the microscope has been setup.
     */
    private static boolean setupIsDone = false;
    
    /**
     * Sets up a basic Microscope for an acquisition simulation.
     */
    @Before
    public void setUp() {
        if (setupIsDone) {
            // Skip setup if it has already run.
            return;
        }
        
        // The seed determines the outputs of the random number generator.
        RNG.setSeed(42);
        
        DefaultCamera.Builder cameraBuilder = new DefaultCamera.Builder();

        cameraBuilder.nX(32); // Number of pixels in x
        cameraBuilder.nY(32); // Number of pixels in y
        cameraBuilder.readoutNoise(1.6); // Standard deviation, electrons
        cameraBuilder.darkCurrent(0.06); 
        cameraBuilder.quantumEfficiency(0.8);
        cameraBuilder.aduPerElectron(2.2);
        cameraBuilder.emGain(0);       // Set to zero for CMOS cameras
        cameraBuilder.baseline(100);   // ADU
        cameraBuilder.pixelSize(6.45); // microns
        cameraBuilder.thermalNoise(0.05); // electrons/frame/pixel

        // Objective
        Objective.Builder objectiveBuilder = new Objective.Builder();

        objectiveBuilder.NA(1.3); // Numerical aperture
        objectiveBuilder.mag(60); // Magnification

        // DefaultLaser
        DefaultLaser.Builder laserBuilder = new DefaultLaser.Builder();

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

        // Assemble the microscope.
        microscope = new Microscope(
            cameraBuilder,
            laserBuilder,
            objectiveBuilder,
            psfBuilder,
            stageBuilder,
            fluorPosBuilder,
            fluorPropBuilder,
            fidBuilder,
            backgroundBuilder);
        
        setupIsDone = true;
    }
    
    /**
     * Test of getResolution method, of class Microscope.
     */
    @Test
    public void testGetResolution() {
        System.out.println("getResolution");
        int[] expResult = {32, 32};
        int[] result = microscope.getResolution();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getFluorophores method, of class Microscope.
     */
    @Test
    public void testGetFluorophores() {
        System.out.println("getFluorophores");
        int expResult = 49;
        int result = microscope.getFluorophores().size();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getFovSize method, of class Microscope.
     */
    @Test
    public void testGetFovSize() {
        System.out.println("getFovSize");
        double expResult = 6.45 * 6.45 * 32 * 32 / 60 / 60;
        double result = microscope.getFovSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getObjectSpacePixelSize method, of class Microscope.
     */
    @Test
    public void testGetObjectSpacePixelSize() {
        System.out.println("getObjectSpacePixelSize");
        double expResult = 6.45 / 60;
        double result = microscope.getObjectSpacePixelSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setLaserPower and getLaserPower methods, of class Microscope.
     */
    @Test
    public void testGetSetLaserPower() {
        System.out.println("setLaserPower");
        double laserPower = 0.42;
        microscope.setLaserPower(laserPower);
        assertEquals(0.42, microscope.getLaserPower(), 0.0);
    }

    /**
     * Test of getOnEmitterCount method, of class Microscope.
     */
    @Test
    public void testGetOnEmitterCount() {
        System.out.println("getOnEmitterCount");
        double result = microscope.getOnEmitterCount();
        assert(result >= 0);
    }

    /**
     * Test of simulateFrame method, of class Microscope.
     */
    @Test
    public void testSimulateFrame() {
        System.out.println("simulateFrame");
        microscope.simulateFrame();
    }
    
     /**
     * Test of toJsonCamera method, of class Microscope.
     */
    @Test
    public void testToJsonCamera() {
        System.out.println("toJsonCamera");
        
        JsonObject json = microscope.toJsonCamera().getAsJsonObject();
        assertEquals(2.2, json.get("aduPerElectron").getAsDouble(), 0.0);
        assertEquals(100, json.get("baseline").getAsInt());
        assertEquals(0.06, json.get("darkCurrent").getAsDouble(), 0.0);
        assertEquals(0, json.get("emGain").getAsDouble(), 0.0);
        assertEquals(32, json.get("nPixelsX").getAsInt());
        assertEquals(32, json.get("nPixelsY").getAsInt());
        assertEquals(6.45, json.get("pixelSize").getAsDouble(), 0.0);
        assertEquals(0.8, json.get("quantumEfficiency").getAsDouble(), 0.0);
        assertEquals(1.6, json.get("readoutNoise").getAsDouble(), 0.0);
        assertEquals(0.05, json.get("thermalNoise").getAsDouble(), 0.0);

    }
    
    /**
     * Test of toJsonFluorescence method, of class Microscope.
     */
    @Test
    public void testToJsonFluorescence() {
        System.out.println("getToJsonFluorescence");
        
        int expResult = 49; // Fluorophores are placed on a grid every 4 pixels.
        
        JsonArray fluorArray = microscope.toJsonFluorescence().getAsJsonArray();      
        assertEquals(expResult, fluorArray.size());
    }
    
    /**
     * Test of toJsonLaser method, of class Microscope.
     */
    @Test
    public void testToJsonLaser() {
        System.out.println("toJsonLaser");
        
        double laserPower = 1.84;
        microscope.setLaserPower(laserPower);
        
        JsonObject json = microscope.toJsonLaser().getAsJsonObject();
        assertEquals(1.84, json.get("currentPower").getAsDouble(), 0.0);

    }
}
