/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne, Switzerland
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.models.fluorophores.commands.internal;

import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.components.internal.DefaultCamera;
import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.models.illuminations.Illumination;
import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamics;
import ch.epfl.leb.sass.models.fluorophores.commands.FluorophoreCommand;
import ch.epfl.leb.sass.models.illuminations.internal.SquareUniformIllumination;
import ch.epfl.leb.sass.models.photophysics.internal.SimpleDynamics;
import ch.epfl.leb.sass.models.psfs.internal.Gaussian2D;
import ch.epfl.leb.sass.models.samples.RefractiveIndex;
import ch.epfl.leb.sass.models.samples.internal.UniformRefractiveIndex;
import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.List;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Integration tests for the FluorophoreReceiver class.
 * 
 * @author Kyle M. Douglass
 */
public class FluorophoreReceiverIT {
    
    private Camera camera;
    private FluorophoreDynamics fluorDynamics;
    private Illumination illumination;
    private PSFBuilder psfBuilder;
    
    @Before
    public void setUp() {
        // Camera
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
        camera = cameraBuilder.build();

        // Illumination profile
        RefractiveIndex n = new UniformRefractiveIndex(new Complex(1.33));
        SquareUniformIllumination.Builder illumBuilder
                = new SquareUniformIllumination.Builder();
        illumBuilder.height(32 * 6.45 / 60); // microns
        illumBuilder.orientation(new Vector3D(1.0, 0, 0)); // x-polarized
        illumBuilder.power(1.0);
        illumBuilder.refractiveIndex(n);
        illumBuilder.wavelength(0.642);
        illumBuilder.width(32 * 6.45 / 60); // microns
        illumination = illumBuilder.build();
        
        // PSF, create a 2D Gaussian point-spread function
        psfBuilder = new Gaussian2D.Builder();
        psfBuilder.stageDisplacement(0)
                  .NA(1.3)
                  .FWHM(0.3)
                  .wavelength(0.68)
                  .resLateral(0.108);
        
        // Fluorophore dynamics and properties; rates are in units of 1/frames
        SimpleDynamics.Builder fluorPropBuilder = new SimpleDynamics.Builder();

        fluorPropBuilder.signal(2500); // Photons per fluorophore per frame
        fluorPropBuilder.wavelength(0.68); // Wavelength, microns
        fluorPropBuilder.tOn(3);           // Mean on time
        fluorPropBuilder.tOff(100);        // Mean off time
        fluorPropBuilder.tBl(10000);       // Mean bleaching time
        fluorDynamics = fluorPropBuilder.build();
    }

    /**
     * Test of generateFluorophoresRandom2D method, of class FluorophoreReceiver.
     */
    @Test
    public void testGenerateFluorophoresRandom2D() {
        GenerateFluorophoresRandom2D.Builder fluorBuilder
                = new GenerateFluorophoresRandom2D.Builder();
        fluorBuilder.numFluors(25); // Number of fluorophores
        
        // Create the set of fluorophores.
        fluorBuilder.camera(camera)
                    .psfBuilder(psfBuilder)
                    .fluorDynamics(fluorDynamics)
                    .illumination(illumination);
        FluorophoreCommand fluorCommand = fluorBuilder.build();
        List<Fluorophore> fluorophores = fluorCommand.generateFluorophores();
        assertEquals(25, fluorophores.size());
        
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (Fluorophore f: fluorophores) {
            if (f.getX() < minX)
                minX = f.getX();
            
            if (f.getX() > maxX)
                maxX = f.getX();
            
            if (f.getY() < minY)
                minY = f.getY();
            
            if (f.getY() > maxY)
                maxY = f.getY();
        }
        
        assertTrue(maxX <= 32);
        assertTrue(minX >= 0.0);
        assertTrue(maxY <= 32);
        assertTrue(minY >= 0.0);
    }

    /**
     * Test of generateFluorophoresRandom3D method, of class FluorophoreReceiver.
     */
    @Test
    public void testGenerateFluorophoresRandom3D() {
        GenerateFluorophoresRandom3D.Builder fluorBuilder
                = new GenerateFluorophoresRandom3D.Builder();
        fluorBuilder.numFluors(25); // Number of fluorophores
        fluorBuilder.zLow(0);
        fluorBuilder.zHigh(5.0);
        
        // Create the set of fluorophores.
        fluorBuilder.camera(camera)
                    .psfBuilder(psfBuilder)
                    .fluorDynamics(fluorDynamics)
                    .illumination(illumination);
        FluorophoreCommand fluorCommand = fluorBuilder.build();
        List<Fluorophore> fluorophores = fluorCommand.generateFluorophores();
        assertEquals(25, fluorophores.size());
        
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        for (Fluorophore f: fluorophores) {
            if (f.getZ() < minZ)
                minZ = f.getZ();
            
            if (f.getZ() > maxZ)
                maxZ = f.getZ();
        }
        
        assertTrue(maxZ <= 5.0);
        assertTrue(minZ >= 0.0);
    }

    /**
     * Test of generateFluorophoresGrid2D method, of class FluorophoreReceiver.
     */
    @Test
    public void testGenerateFluorophoresGrid2D() {
        GenerateFluorophoresGrid2D.Builder fluorBuilder
                = new GenerateFluorophoresGrid2D.Builder();
        fluorBuilder.spacing(4); // spacing in pixels
        
        // Create the set of fluorophores.
        fluorBuilder.camera(camera)
                    .psfBuilder(psfBuilder)
                    .fluorDynamics(fluorDynamics)
                    .illumination(illumination);
        FluorophoreCommand fluorCommand = fluorBuilder.build();
        List<Fluorophore> fluorophores = fluorCommand.generateFluorophores();
        assertEquals(49, fluorophores.size());
        
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (Fluorophore f: fluorophores) {
            if (f.getX() < minX)
                minX = f.getX();
            
            if (f.getX() > maxX)
                maxX = f.getX();
            
            if (f.getY() < minY)
                minY = f.getY();
            
            if (f.getY() > maxY)
                maxY = f.getY();
        }
        
        assertEquals(28.0, maxX, 0.0);
        assertEquals(4.0, minX, 0.0);
        assertEquals(28.0, maxY, 0.0);
        assertEquals(4.0, minY, 0.0);
    }

    /**
     * Test of generateFluorophoresGrid3D method, of class FluorophoreReceiver.
     */
    @Test
    public void testGenerateFluorophoresGrid3D() {
        GenerateFluorophoresGrid3D.Builder fluorBuilder
                = new GenerateFluorophoresGrid3D.Builder();
        fluorBuilder.spacing(4); // Number of fluorophores
        fluorBuilder.zLow(0);
        fluorBuilder.zHigh(5.0);
        
        // Create the set of fluorophores.
        fluorBuilder.camera(camera)
                    .psfBuilder(psfBuilder)
                    .fluorDynamics(fluorDynamics)
                    .illumination(illumination);
        FluorophoreCommand fluorCommand = fluorBuilder.build();
        List<Fluorophore> fluorophores = fluorCommand.generateFluorophores();
        assertEquals(49, fluorophores.size());
        
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        for (Fluorophore f: fluorophores) {
            if (f.getZ() < minZ)
                minZ = f.getZ();
            
            if (f.getZ() > maxZ)
                maxZ = f.getZ();
        }
        
        assertEquals(5.0, maxZ, 0.0001);
        assertEquals(0.0, minZ, 0.0001);
    }

    /**
     * Test of generateFluorophoresFromCSV method, of class FluorophoreReceiver.
     */
    @Test
    public void testGenerateFluorophoresFromCSV() throws Exception {
        URL csv = FluorophoreReceiverIT.class.getResource("/label_pix_sass.csv");
        File csvFile = new File(csv.getFile());
        GenerateFluorophoresFromCSV.Builder fluorBuilder
                = new GenerateFluorophoresFromCSV.Builder();
        fluorBuilder.file(csvFile);  // The file containing the locations.
        fluorBuilder.rescale(true); // Rescale positions to fit image?
        
        // Create the set of fluorophores.
        fluorBuilder.camera(camera)
                    .psfBuilder(psfBuilder)
                    .fluorDynamics(fluorDynamics)
                    .illumination(illumination);
        FluorophoreCommand fluorCommand = fluorBuilder.build();
        List<Fluorophore> fluorophores = fluorCommand.generateFluorophores();
        assertEquals(69358, fluorophores.size());
        
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (Fluorophore f: fluorophores) {
            if (f.getX() < minX)
                minX = f.getX();
            
            if (f.getX() > maxX)
                maxX = f.getX();
            
            if (f.getY() < minY)
                minY = f.getY();
            
            if (f.getY() > maxY)
                maxY = f.getY();
        }
        
        assertTrue(maxX <= 32);
        assertTrue(minX >= 0.0);
        assertTrue(maxY <= 32);
        assertTrue(minY >= 0.0);
    }
    
}
