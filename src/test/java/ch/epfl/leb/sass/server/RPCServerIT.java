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
import ch.epfl.leb.sass.models.components.internal.DefaultCamera;
import ch.epfl.leb.sass.models.components.internal.DefaultLaser;
import ch.epfl.leb.sass.models.components.internal.DefaultObjective;
import ch.epfl.leb.sass.models.components.internal.DefaultStage;
import ch.epfl.leb.sass.models.fluorophores.internal.commands.GenerateFluorophoresGrid2D;
import ch.epfl.leb.sass.models.photophysics.internal.PalmDynamics;
import ch.epfl.leb.sass.models.obstructors.internal.commands.GenerateFiducialsRandom2D;
import ch.epfl.leb.sass.models.psfs.internal.Gaussian2D;
import ch.epfl.leb.sass.utils.RNG;
import ch.epfl.leb.sass.simulator.Simulator;
import ch.epfl.leb.sass.simulator.SimulationManager;
import ch.epfl.leb.sass.simulator.internal.RPCSimulator;
import ch.epfl.leb.sass.simulator.internal.DefaultSimulationManager;
import ch.epfl.leb.sass.client.RPCClient;
import ch.epfl.leb.sass.logging.MessageType;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.experimental.categories.Category;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.apache.thrift.TException;

/**
 * Integration tests for the RPCServer.
 * 
 * @author Kyle M. Douglass
 */
@Category(IntegrationTest.class)
public class RPCServerIT {
    
    private final static Logger LOGGER = Logger.getLogger(
            RPCServerIT.class.getName());
    
    /**
     * The number of simulations to run.
     */
    private final int NUM_SIMS = 2;
    
    /**
     * The URL to the server.
     */
    private final String HOST_URL = "localhost";
    
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
     * A RPCServer that will be used to test client/server communications.
     */
    RPCServer rpcServer;
    
    /**
     * A RPCClient that will be used to test server communications.
     */
    RPCClient rpcClient;
    
    /**
     * Sets up two different Microscopes for acquisition simulations.
     */
    @Before
    public void setUp() throws InterruptedException {        
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

        // DefaultObjective
        DefaultObjective.Builder objectiveBuilder = new DefaultObjective.Builder();

        objectiveBuilder.NA(1.3); // Numerical aperture
        objectiveBuilder.mag(60); // Magnification

        // DefaultLaser
        DefaultLaser.Builder laserBuilder = new DefaultLaser.Builder();

        laserBuilder.currentPower(0.0);
        laserBuilder.minPower(0.0);
        laserBuilder.maxPower(500.0);

        // DefaultStage
        DefaultStage.Builder stageBuilder = new DefaultStage.Builder();

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
        
        // Adds the simulations to the manager.
        manager = new DefaultSimulationManager();
        manager.addSimulator(sims[0]);
        manager.addSimulator(sims[1]);
        
        // Starts the server.
        rpcServer = new RPCServer(manager, PORT);
        
        Runnable serverRunnable = new Runnable() {
            public void run() {
                rpcServer.serve();
            }
        };
        new Thread(serverRunnable).start();
        Thread.sleep(500); // Give the server time to start
        System.out.println("Server started!");     
        
       // Creates the client.
       rpcClient = new RPCClient(HOST_URL, PORT);
       RemoteSimulationService.Client client = rpcClient.getClient();
    }

    /**
     * Closes the server communications.
     * @throws java.lang.InterruptedException
     */
    @After
    public void tearDown() throws InterruptedException {
        // Close the client connection.
        try {
            rpcClient.close();
            Thread.sleep(500);
        } catch (java.lang.NullPointerException ex) {
            LOGGER.log(Level.INFO, "RPCClient connection failed to close. This "
                                 + "is likely because a RPCClient instance was "
                                 + "not created during this test.");
        }
        
        // Shutdown the server.
        try {
            rpcServer.stop();
            Thread.sleep(500);
        } catch (java.lang.NullPointerException ex) {
            LOGGER.log(Level.INFO, "RPCServer failed to shutdown. This may be "
                                 + "because no server was started in this "
                                 + "test.");
        }
    }
    
    /**
     * Test of isServing method, of class RPCServer.
     */
    @Test
    public void testIsServing() throws InterruptedException {
        System.out.println("isServing");   
        
        boolean expResult = true;
        boolean result = rpcServer.isServing();
        assertEquals(expResult, result);

    }
    
    /**
     * Test of createSimulation and deleteSimulation methods, of class
     * RemoteSimulationServiceHandler.
     */
    @Test
    public void testCreateAndDeleteSimulation() throws TException {
        System.out.println("testCreateSimulation");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        
        int origNumSims;
        origNumSims = manager.getIds().size();
        
        int newSimId;
        newSimId = client.createSimulation();
        
        // There should now be one additional simulation in the manager..
        assertEquals(origNumSims + 1, manager.getIds().size());
        
        // Increment the new simulation by one time step and verify that only
        // the new simulation state has changed.
        int newImageCount;
        int oldImageCount;
        client.getNextImage(newSimId);
        oldImageCount = client.getImageCount(sims[0].getId());
        newImageCount = client.getImageCount(newSimId);
        assertEquals(0, oldImageCount);
        assertEquals(1, newImageCount);
        
        // Delete the new simulation.
        client.deleteSimulation(newSimId);
        List<Integer> listOfSims = manager.getIds();
        assertEquals(origNumSims, listOfSims.size());
        assert(!listOfSims.contains(newSimId));
        
    }
    
    /**
     * Test of getControlSignal method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetControlSignal() throws InterruptedException,
                                              UnknownSimulationIdException,
                                              TException {
        System.out.println("testGetControlSignal");
        
       RemoteSimulationService.Client client = rpcClient.getClient();
       
       double expResult = sims[0].getControlSignal();
       double result = client.getControlSignal(sims[0].getId());
       assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of toJsonMessages method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testToJsonMessages() throws UnknownSimulationIdException,
                                            TException {
        System.out.println("testToJsonMessages");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        JsonParser parser = new JsonParser();
        
        // Run the simulation for a few steps to generate some messages.
        int simId = sims[0].getId();
        for (int i = 0; i < 10; i++) {
            client.incrementTimeStep(simId);
        }
        
         // Extract the messages from the first simulation.
        String info = client.toJsonMessages(simId);
        System.out.println(info);
        
        // Ensure that all messages have a TYPE field that is registered in
        // MessageType.
        EnumSet<MessageType> set = EnumSet.allOf(MessageType.class);
        List<String> typeStrings = new ArrayList<>();
        for (MessageType type: set) {
            typeStrings.add(type.name());
        }
        
        String typeString;
        JsonArray json = parser.parse(info).getAsJsonArray();
        for (JsonElement e: json) {
            typeString = e.getAsJsonObject().get("type").getAsString();
            assert(typeStrings.contains(typeString));
        }
        
    }
    
    /**
     * Test of toJsonState and getCameraJsonName methods,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testToJsonStateCamera() throws UnknownSimulationIdException,
                                           TException {
        System.out.println("testToJsonStateCamera");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        JsonParser parser = new JsonParser();
        
        // Extract the fluorescence info from the first simulation.
        String info = client.toJsonState(sims[0].getId());
        String cameraName = client.getCameraJsonName(sims[0].getId());
        JsonObject json = parser.parse(info).getAsJsonObject();
        JsonObject jsonCamera;
        jsonCamera = json.get(cameraName).getAsJsonObject();
        assertEquals(2.2, jsonCamera.get("aduPerElectron").getAsDouble(), 0.0);
        assertEquals(100, jsonCamera.get("baseline").getAsInt());
        assertEquals(0.06, jsonCamera.get("darkCurrent").getAsDouble(), 0.0);
        assertEquals(0, jsonCamera.get("emGain").getAsDouble(), 0.0);
        assertEquals(32, jsonCamera.get("nPixelsX").getAsInt());
        assertEquals(32, jsonCamera.get("nPixelsY").getAsInt());
        assertEquals(6.45, jsonCamera.get("pixelSize").getAsDouble(), 0.0);
        assertEquals(0.8, jsonCamera.get("quantumEfficiency").getAsDouble(), 0.0);
        assertEquals(1.6, jsonCamera.get("readoutNoise").getAsDouble(), 0.0);
        assertEquals(0.05, jsonCamera.get("thermalNoise").getAsDouble(), 0.0);
        
        // Extract the fluorescence info from the second simulation.
        info = client.toJsonState(sims[1].getId());
        cameraName = client.getCameraJsonName(sims[1].getId());
        json = parser.parse(info).getAsJsonObject();
        jsonCamera = json.get(cameraName).getAsJsonObject();
        assertEquals(2.2, jsonCamera.get("aduPerElectron").getAsDouble(), 0.0);
        assertEquals(100, jsonCamera.get("baseline").getAsInt());
        assertEquals(0.06, jsonCamera.get("darkCurrent").getAsDouble(), 0.0);
        assertEquals(0, jsonCamera.get("emGain").getAsDouble(), 0.0);
        assertEquals(64, jsonCamera.get("nPixelsX").getAsInt());
        assertEquals(64, jsonCamera.get("nPixelsY").getAsInt());
        assertEquals(6.45, jsonCamera.get("pixelSize").getAsDouble(), 0.0);
        assertEquals(0.8, jsonCamera.get("quantumEfficiency").getAsDouble(), 0.0);
        assertEquals(1.6, jsonCamera.get("readoutNoise").getAsDouble(), 0.0);
        assertEquals(0.05, jsonCamera.get("thermalNoise").getAsDouble(), 0.0);
    }
    
    /**
     * Test of toJsonState and getFluorescenceJsonName methods,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testToJsonStateFluorescence() throws UnknownSimulationIdException,
                                              TException {
        System.out.println("testToJsonStateFluorescence");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        JsonParser parser = new JsonParser();
        
        // Extract the fluorescence info from the first simulation.
        String info = client.toJsonState(sims[0].getId());
        String fluorName = client.getFluorescenceJsonName(sims[0].getId());
        JsonObject json = parser.parse(info).getAsJsonObject();
        JsonArray fluorArray;
        fluorArray = json.get(fluorName).getAsJsonArray();
        int expResult = 49; // (num_pixels / grid_spacing - 1)^2
        assertEquals(expResult, fluorArray.size());
        
        // Extract the fluorescence info from the second simulation.
        info = client.toJsonState(sims[1].getId());
        fluorName = client.getFluorescenceJsonName(sims[1].getId());
        json = parser.parse(info).getAsJsonObject();
        fluorArray = json.get(fluorName).getAsJsonArray();
        
        expResult = 225; // (num_pixels / grid_spacing - 1)^2
        assertEquals(expResult, fluorArray.size());
    }
    
    /**
     * Test of toJsonState and getLaserJsonName methods,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testToJsonStateLaser() throws UnknownSimulationIdException,
                                           TException {
        System.out.println("testToJsonStateLaser");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        JsonParser parser = new JsonParser();
        
        // Extract the fluorescence info from the first simulation.
        String info = client.toJsonState(sims[0].getId());
        String laserName = client.getLaserJsonName(sims[0].getId());
        JsonObject json = parser.parse(info).getAsJsonObject();
        JsonObject jsonLaser;
        jsonLaser = json.get(laserName).getAsJsonObject();
        assertEquals(0.0, jsonLaser.get("currentPower").getAsDouble(), 0.0);
        
        // Extract the fluorescence info from the second simulation.
        info = client.toJsonState(sims[1].getId());
        laserName = client.getLaserJsonName(sims[1].getId());
        json = parser.parse(info).getAsJsonObject();
        jsonLaser = json.get(laserName).getAsJsonObject();
        assertEquals(0.0, jsonLaser.get("currentPower").getAsDouble(), 0.0);
    }
    
     /**
     * Test of toJsonState and getLaserJsonName methods,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testToJsonStateStage() throws UnknownSimulationIdException,
                                              TException {
        System.out.println("testToJsonStateStage");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        JsonParser parser = new JsonParser();
        
        // Extract the fluorescence info from the first simulation.
        String info = client.toJsonState(sims[0].getId());
        String stageName = client.getStageJsonName(sims[0].getId());
        JsonObject json = parser.parse(info).getAsJsonObject();
        JsonObject jsonStage;
        jsonStage = json.get(stageName).getAsJsonObject();
        assertEquals(0.0, jsonStage.get("x").getAsDouble(), 0.0);
        assertEquals(0.0, jsonStage.get("y").getAsDouble(), 0.0);
        assertEquals(0.0, jsonStage.get("z").getAsDouble(), 0.0);
        
        // Extract the fluorescence info from the second simulation.
        info = client.toJsonState(sims[1].getId());
        stageName = client.getStageJsonName(sims[1].getId());
        json = parser.parse(info).getAsJsonObject();
        jsonStage = json.get(stageName).getAsJsonObject();
        assertEquals(0.0, jsonStage.get("x").getAsDouble(), 0.0);
        assertEquals(0.0, jsonStage.get("y").getAsDouble(), 0.0);
        assertEquals(0.0, jsonStage.get("z").getAsDouble(), 0.0);
    }
    
    /**
     * Test of getFovSize method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetFovSize() throws UnknownSimulationIdException,
                                                 TException {
        System.out.println("testGetFovSize");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        // pixel size * num pixels / mag
        double expResult = 6.45 * 6.45 * 32 * 32 / 60 / 60;
        double result = client.getFovSize(sims[0].getId());
        assertEquals(expResult, result, 0.0);
        
        // pixel size * num pixels / mag
        expResult = 6.45 * 6.45 * 64 * 64 / 60 / 60; // pixel size * num pixels / mag
        result = client.getFovSize(sims[1].getId());
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getNextImage and getImageCount methods,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetNextImageAndImageCount() throws UnknownSimulationIdException,
                                                       TException {
        System.out.println("testGetNextImageAndImageCount");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        int expResult = 0;
        int result = client.getImageCount(sims[0].getId());
        assertEquals(expResult, result);
        
        result = client.getImageCount(sims[1].getId());
        assertEquals(expResult, result);
        
        // Generate an image in each simulation
        client.getNextImage(sims[0].getId());
        client.getNextImage(sims[1].getId());
        
        expResult = 1;
        result = client.getImageCount(sims[0].getId());
        assertEquals(expResult, result);
        
        result = client.getImageCount(sims[1].getId());
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getObjectSpacePixelSize method,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetObjectSpacePixelSize() throws UnknownSimulationIdException,
                                                     TException {
        System.out.println("testGetObjectSpacePixelSize");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        // pixel size / mag
        double expResult = 6.45 / 60;
        double result = client.getObjectSpacePixelSize(sims[0].getId());
        assertEquals(expResult, result, 0.0);
        
        result = client.getObjectSpacePixelSize(sims[1].getId());
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getGetServerStatus method,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testGetServerStatus() throws UnknownSimulationIdException,
                                             TException {
        System.out.println("testGetServerStatus");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        String expResult = "SASS RPC server is running.";
        String result = client.getServerStatus();
        
        assert(expResult.equals(result));
    }
    
    /**
     * Test of getShortTrueSignalDescription and getTrueSignal methods,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testTrueSignal() throws UnknownSimulationIdException,
                                                           TException {
        System.out.println("testTrueSignal");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        String result = client.getShortTrueSignalDescription(sims[0].getId());
        
        // The result can vary, so just make sure that it's not empty.
        assert(!result.equals(""));
        
        double result2 = client.getTrueSignal(sims[0].getId(), 0);
        assert(result2 >= 0);
        
        result2 = client.getTrueSignal(sims[1].getId(), 0);
        assert(result2 >= 0);
    }
    
    /**
     * Test of incrementTimeStep method,
     * of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testIncrementTimeStep() throws UnknownSimulationIdException,
                                               TException {
        System.out.println("testIncrementTimeStep");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
        
        //Sim 0
        String before = client.toJsonState(sims[0].getId());
        client.incrementTimeStep(sims[0].getId());
        String after = client.toJsonState(sims[0].getId());
        assert(!after.equals(before));
        
        // Sim 1
        before = client.toJsonState(sims[1].getId());
        client.incrementTimeStep(sims[1].getId());
        after = client.toJsonState(sims[1].getId());
        assert(!after.equals(before));
    }
    
    /**
     * Test of setControlSignal method, of class RemoteSimulationServiceHandler.
     */
    @Test
    public void testSetControlSignal() throws InterruptedException,
                                              UnknownSimulationIdException,
                                              TException {
        System.out.println("testSetControlSignal");
        
        RemoteSimulationService.Client client = rpcClient.getClient();
       
        // Test sim[0]
        double expResult = 1.42;
        client.setControlSignal(sims[0].getId(), expResult);
        double result = sims[0].getControlSignal();
        assertEquals(expResult, result, 0.0);

        // Test the other simulation
        expResult = 0.0;
        result = client.getControlSignal(sims[1].getId());
        assertEquals(expResult, result, 0.0);
       
    }
}
