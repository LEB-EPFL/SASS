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
package ch.epfl.leb.sass.ijplugin;

import ch.epfl.leb.sass.simulator.generators.realtime.Microscope;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kyle M. Douglass
 */
public class ModelTest {
    
    public ModelTest() {
    }

    /**
     * Test of getCameraNX method, of class Model.
     */
    @Test
    public void testGetCameraNX() {
        System.out.println("getCameraNX");
        Model instance = new Model();
        int expResult = 32;
        instance.setCameraNX(expResult);
        int result = instance.getCameraNX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraNY method, of class Model.
     */
    @Test
    public void testGetCameraNY() {
        System.out.println("getCameraNY");
        Model instance = new Model();
        int expResult = 64;
        instance.setCameraNY(expResult);
        int result = instance.getCameraNY();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraReadoutNoise method, of class Model.
     */
    @Test
    public void testGetCameraReadoutNoise() {
        System.out.println("getCameraReadoutNoise");
        Model instance = new Model();
        double expResult = 2.5;
        instance.setCameraReadoutNoise(expResult);
        double result = instance.getCameraReadoutNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraDarkCurrent method, of class Model.
     */
    @Test
    public void testGetCameraDarkCurrent() {
        System.out.println("getCameraDarkCurrent");
        Model instance = new Model();
        double expResult = 0.5;
        instance.setCameraDarkCurrent(expResult);
        double result = instance.getCameraDarkCurrent();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraQuantumEfficiency method, of class Model.
     */
    @Test
    public void testGetCameraQuantumEfficiency() {
        System.out.println("getCameraQuantumEfficiency");
        Model instance = new Model();
        double expResult = 0.8;
        instance.setCameraQuantumEfficiency(expResult);
        double result = instance.getCameraQuantumEfficiency();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraAduPerElectron method, of class Model.
     */
    @Test
    public void testGetCameraAduPerElectron() {
        System.out.println("getCameraAduPerElectron");
        Model instance = new Model();
        double expResult = 2.1;
        instance.setCameraAduPerElectron(expResult);
        double result = instance.getCameraAduPerElectron();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraEmGain method, of class Model.
     */
    @Test
    public void testGetCameraEmGain() {
        System.out.println("getCameraEmGain");
        Model instance = new Model();
        int expResult = 300;
        instance.setCameraEmGain(expResult);
        int result = instance.getCameraEmGain();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraBaseline method, of class Model.
     */
    @Test
    public void testGetCameraBaseline() {
        System.out.println("getCameraBaseline");
        Model instance = new Model();
        int expResult = 100;
        instance.setCameraBaseline(100);
        int result = instance.getCameraBaseline();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraPixelSize method, of class Model.
     */
    @Test
    public void testGetCameraPixelSize() {
        System.out.println("getCameraPixelSize");
        Model instance = new Model();
        double expResult = 16.0;
        instance.setCameraPixelSize(expResult);
        double result = instance.getCameraPixelSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraThermalNoise method, of class Model.
     */
    @Test
    public void testGetCameraThermalNoise() {
        System.out.println("getCameraThermalNoise");
        Model instance = new Model();
        double expResult = 0.4;
        instance.setCameraThermalNoise(expResult);
        double result = instance.getCameraThermalNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getObjectiveNa method, of class Model.
     */
    @Test
    public void testGetObjectiveNa() {
        System.out.println("getObjectiveNa");
        Model instance = new Model();
        double expResult = 1.2;
        instance.setObjectiveNa(expResult);
        double result = instance.getObjectiveNa();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getObjectiveMag method, of class Model.
     */
    @Test
    public void testGetObjectiveMag() {
        System.out.println("getObjectiveMag");
        Model instance = new Model();
        double expResult = 63;
        instance.setObjectiveMag(expResult);
        double result = instance.getObjectiveMag();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getFluorophoreCurrentSelection method, of class Model.
     */
    @Test
    public void testGetFluorophoreCurrentSelection() {
        System.out.println("getFluorophoreCurrentSelection");
        Model instance = new Model();
        String expResult = "Simple";
        instance.setFluorophoreCurrentSelection(expResult);
        String result = instance.getFluorophoreCurrentSelection();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getFluorophoreSimpleText method, of class Model.
     */
    @Test
    public void testGetFluorophoreSimpleText() {
        System.out.println("getFluorophoreSimpleText");
        Model instance = new Model();
        String expResult = "Simple";
        instance.setFluorophoreSimpleText(expResult);
        String result = instance.getFluorophoreSimpleText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFluorophoreSignal method, of class Model.
     */
    @Test
    public void testGetFluorophoreSignal() {
        System.out.println("getFluorophoreSignal");
        Model instance = new Model();
        double expResult = 1500;
        instance.setFluorophoreSignal(expResult);
        double result = instance.getFluorophoreSignal();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreWavelength method, of class Model.
     */
    @Test
    public void testGetFluorophoreWavelength() {
        System.out.println("getFluorophoreWavelength");
        Model instance = new Model();
        double expResult = 0.532;
        instance.setFluorophoreWavelength(expResult);
        double result = instance.getFluorophoreWavelength();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreTOn method, of class Model.
     */
    @Test
    public void testGetFluorophoreTOn() {
        System.out.println("getFluorophoreTOn");
        Model instance = new Model();
        double expResult = 3;
        instance.setFluorophoreTOn(expResult);
        double result = instance.getFluorophoreTOn();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreTOff method, of class Model.
     */
    @Test
    public void testGetFluorophoreTOff() {
        System.out.println("getFluorophoreTOff");
        Model instance = new Model();
        double expResult = 100;
        instance.setFluorophoreTOff(expResult);
        double result = instance.getFluorophoreTOff();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreTBl method, of class Model.
     */
    @Test
    public void testGetFluorophoreTBl() {
        System.out.println("getFluorophoreTBl");
        Model instance = new Model();
        double expResult = 1000;
        instance.setFluorophoreTBl(expResult);
        double result = instance.getFluorophoreTBl();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLaserMinPower method, of class Model.
     */
    @Test
    public void testGetLaserMinPower() {
        System.out.println("getLaserMinPower");
        Model instance = new Model();
        double expResult = 0.0;
        instance.setLaserMinPower(expResult);
        double result = instance.getLaserMinPower();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLaserMaxPower method, of class Model.
     */
    @Test
    public void testGetLaserMaxPower() {
        System.out.println("getLaserMaxPower");
        Model instance = new Model();
        double expResult = 150.0;
        instance.setLaserMaxPower(expResult);
        double result = instance.getLaserMaxPower();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLaserCurrentPower method, of class Model.
     */
    @Test
    public void testGetLaserCurrentPower() {
        System.out.println("getLaserCurrentPower");
        Model instance = new Model();
        double expResult = 5.0;
        instance.setLaserCurrentPower(expResult);
        double result = instance.getLaserCurrentPower();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getStageX method, of class Model.
     */
    @Test
    public void testGetStageX() {
        System.out.println("getStageX");
        Model instance = new Model();
        double expResult = 5.0;
        instance.setStageX(expResult);
        double result = instance.getStageX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getStageY method, of class Model.
     */
    @Test
    public void testGetStageY() {
        System.out.println("getStageY");
        Model instance = new Model();
        double expResult = 13.0;
        instance.setStageY(expResult);
        double result = instance.getStageY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getStageZ method, of class Model.
     */
    @Test
    public void testGetStageZ() {
        System.out.println("getStageZ");
        Model instance = new Model();
        double expResult = -3.5;
        instance.setStageZ(expResult);
        double result = instance.getStageZ();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getEmittersCurrentSelection method, of class Model.
     */
    @Test
    public void testGetEmittersCurrentSelection() {
        System.out.println("getEmittersCurrentSelection");
        Model instance = new Model();
        String expResult = "Random";
        instance.setEmittersCurrentSelection(expResult);
        String result = instance.getEmittersCurrentSelection();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersRandomNumber method, of class Model.
     */
    @Test
    public void testGetEmittersRandomNumber() {
        System.out.println("getEmittersRandomNumber");
        Model instance = new Model();
        int expResult = 42;
        instance.setEmittersRandomNumber(expResult);
        int result = instance.getEmittersRandomNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersGridSpacing method, of class Model.
     */
    @Test
    public void testGetEmittersGridSpacing() {
        System.out.println("getEmittersGridSpacing");
        Model instance = new Model();
        int expResult = 5;
        instance.setEmittersGridSpacing(expResult);
        int result = instance.getEmittersGridSpacing();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersCsvFile method, of class Model.
     */
    @Test
    public void testGetEmittersCsvFile() {
        System.out.println("getEmittersCsvFile");
        Model instance = new Model();
        String expResult = "/file/path";
        instance.setEmittersCsvFile(expResult);
        String result = instance.getEmittersCsvFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersRandomButtonText method, of class Model.
     */
    @Test
    public void testGetEmittersRandomButtonText() {
        System.out.println("getEmittersRandomButtonText");
        Model instance = new Model();
        String expResult = "Grid";
        instance.setEmittersRandomButtonText(expResult);
        String result = instance.getEmittersRandomButtonText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersGridButtonText method, of class Model.
     */
    @Test
    public void testGetEmittersGridButtonText() {
        System.out.println("getEmittersGridButtonText");
        Model instance = new Model();
        String expResult = "Grid";
        instance.setEmittersGridButtonText(expResult);
        String result = instance.getEmittersGridButtonText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersCsvFileButtonText method, of class Model.
     */
    @Test
    public void testGetEmittersCsvFileButtonText() {
        System.out.println("getEmittersCsvFileButtonText");
        Model instance = new Model();
        String expResult = "From .csv file...";
        instance.setEmittersCsvFileButtonText(expResult);
        String result = instance.getEmittersCsvFileButtonText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getEmitters3DCheckBoxEnabled method, of class Model.
     */
    @Test
    public void testGetEmitters3DCheckBoxEnabled() {
        System.out.println("getEmitters3DCheckBoxEnabled");
        Model instance = new Model();
        boolean expResult = true;
        instance.setEmitters3DCheckBoxEnabled(expResult);
        boolean result = instance.getEmitters3DCheckBoxEnabled();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getEmitters3DMinZ method, of class Model.
     */
    @Test
    public void testGetEmitters3DMinZ() {
        System.out.println("getEmitters3DMinZ");
        Model instance = new Model();
        double expResult = 0.0;
        instance.setEmitters3DMinZ(expResult);
        double result = instance.getEmitters3DMinZ();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getEmitters3DMaxZ method, of class Model.
     */
    @Test
    public void testGetEmitters3DMaxZ() {
        System.out.println("getEmitters3DMaxZ");
        Model instance = new Model();
        double expResult = 3.2;
        instance.setEmitters3DMaxZ(expResult);
        double result = instance.getEmitters3DMaxZ();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFiducialsNumber method, of class Model.
     */
    @Test
    public void testGetFiducialsNumber() {
        System.out.println("getFiducialsNumber");
        Model instance = new Model();
        int expResult = 37;
        instance.setFiducialsNumber(expResult);
        int result = instance.getFiducialsNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFiducialsSignal method, of class Model.
     */
    @Test
    public void testGetFiducialsSignal() {
        System.out.println("getFiducialsSignal");
        Model instance = new Model();
        double expResult = 3000;
        instance.setFiducialsSignal(expResult);
        double result = instance.getFiducialsSignal();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBackgroundCurrentSelection method, of class Model.
     */
    @Test
    public void testGetBackgroundCurrentSelection() {
        System.out.println("getBackgroundCurrentSelection");
        Model instance = new Model();
        String expResult = "Uniform";
        instance.setBackgroundCurrentSelection(expResult);
        String result = instance.getBackgroundCurrentSelection();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBackgroundUniformSignal method, of class Model.
     */
    @Test
    public void testGetBackgroundUniformSignal() {
        System.out.println("getBackgroundUniformSignal");
        Model instance = new Model();
        float expResult = 2378;
        instance.setBackgroundUniformSignal(expResult);
        float result = instance.getBackgroundUniformSignal();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getBackgroundRandomFeatureSize method, of class Model.
     */
    @Test
    public void testGetBackgroundRandomFeatureSize() {
        System.out.println("getBackgroundRandomFeatureSize");
        Model instance = new Model();
        double expResult = 25;
        instance.setBackgroundRandomFeatureSize(expResult);
        double result = instance.getBackgroundRandomFeatureSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBackgroundRandomMinValue method, of class Model.
     */
    @Test
    public void testGetBackgroundRandomMinValue() {
        System.out.println("getBackgroundRandomMinValue");
        Model instance = new Model();
        float expResult = 25;
        instance.setBackgroundRandomMinValue(expResult);
        double result = instance.getBackgroundRandomMinValue();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getBackgroundRandomMaxValue method, of class Model.
     */
    @Test
    public void testGetBackgroundRandomMaxValue() {
        System.out.println("getBackgroundRandomMaxValue");
        Model instance = new Model();
        float expResult = 25;
        instance.setBackgroundRandomMaxValue(expResult);
        double result = instance.getBackgroundRandomMaxValue();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getBackgroundRandomFeatureSize method, of class Model.
     */
    @Test
    public void testGetBackgroundRandomSeed() {
        System.out.println("getBackgroundRandomMaxValue");
        Model instance = new Model();
        int expResult = 42;
        instance.setBackgroundRandomSeed(expResult);
        int result = instance.getBackgroundRandomSeed();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getBackgroundTifFile method, of class Model.
     */
    @Test
    public void testGetBackgroundTifFile() {
        System.out.println("getBackgroundTifFile");
        Model instance = new Model();
        String expResult = "/path/to/file";
        instance.setBackgroundTifFile(expResult);
        String result = instance.getBackgroundTifFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBackgroundUniformButtonText method, of class Model.
     */
    @Test
    public void testGetBackgroundUniformButtonText() {
        System.out.println("getBackgroundUniformButtonText");
        Model instance = new Model();
        String expResult = "Uniform";
        instance.setBackgroundUniformButtonText(expResult);
        String result = instance.getBackgroundUniformButtonText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBackgroundRandomButtonText() {
     * 
     */
    @Test
    public void testGetBackgroundRandomButtonText() {
        System.out.println("getBackgroundRandomButtonText");
        Model instance = new Model();
        String expResult = "Random";
        instance.setBackgroundRandomButtonText(expResult);
        String result = instance.getBackgroundRandomButtonText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getBackgroundTifFileButtonText method, of class Model.
     */
    @Test
    public void testGetBackgroundTifFileButtonText() {
        System.out.println("getBackgroundTifFileButtonText");
        Model instance = new Model();
        String expResult = "From .tif file...";
        instance.setBackgroundTifFileButtonText(expResult);
        String result = instance.getBackgroundTifFileButtonText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfCurrentSelection method, of class Model.
     */
    @Test
    public void testGetPsfCurrentSelection() {
        System.out.println("getPsfCurrentSelection");
        Model instance = new Model();
        String expResult = "Gaussian 2D";
        instance.setPsfCurrentSelection(expResult);
        String result = instance.getPsfCurrentSelection();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGaussian2dText method, of class Model.
     */
    @Test
    public void testGetPsfGaussian2dText() {
        System.out.println("getPsfGaussian2dText");
        Model instance = new Model();
        String expResult = "Gaussian 2D";
        instance.setPsfGaussian2dText(expResult);
        String result = instance.getPsfGaussian2dText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGaussian3dText method, of class Model.
     */
    @Test
    public void testGetPsfGaussian3dText() {
        System.out.println("getPsfGaussian3dText");
        Model instance = new Model();
        String expResult = "Gaussian 3D";
        instance.setPsfGaussian3dText(expResult);
        String result = instance.getPsfGaussian3dText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniNumBasis, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniNumBasis() {
        System.out.println("getPsfGibsonLanniNumBasis");
        Model instance = new Model();
        int expResult = 100;
        instance.setPsfGibsonLanniNumBasis(expResult);
        int result = instance.getPsfGibsonLanniNumBasis();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniNumSamples, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniNumSamples() {
        System.out.println("getPsfGibsonLanniNumSamples");
        Model instance = new Model();
        int expResult = 1000;
        instance.setPsfGibsonLanniNumSamples(expResult);
        int result = instance.getPsfGibsonLanniNumSamples();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniOversampling, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniOversampling() {
        System.out.println("getPsfGibsonLanniOversampling");
        Model instance = new Model();
        int expResult = 2;
        instance.setPsfGibsonLanniOversampling(expResult);
        int result = instance.getPsfGibsonLanniOversampling();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniSizeX, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniSizeX() {
        System.out.println("getPsfGibsonLanniSizeX");
        Model instance = new Model();
        int expResult = 32;
        instance.setPsfGibsonLanniSizeX(expResult);
        int result = instance.getPsfGibsonLanniSizeX();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniSizeY, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniSizeY() {
        System.out.println("getPsfGibsonLanniSizeY");
        Model instance = new Model();
        int expResult = 32;
        instance.setPsfGibsonLanniSizeY(expResult);
        int result = instance.getPsfGibsonLanniSizeY();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniNs, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniNs() {
        System.out.println("getPsfGibsonLanniNs");
        Model instance = new Model();
        double expResult = 1.33;
        instance.setPsfGibsonLanniNs(expResult);
        double result = instance.getPsfGibsonLanniNs();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNg0, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniNg0() {
        System.out.println("getPsfGibsonLanniNg0");
        Model instance = new Model();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNg0(expResult);
        double result = instance.getPsfGibsonLanniNg0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNg, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniNg() {
        System.out.println("getPsfGibsonLanniNg");
        Model instance = new Model();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNg(expResult);
        double result = instance.getPsfGibsonLanniNg();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNi0, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniNi0() {
        System.out.println("getPsfGibsonLanniNi0");
        Model instance = new Model();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNi0(expResult);
        double result = instance.getPsfGibsonLanniNi0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNi, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniNi() {
        System.out.println("getPsfGibsonLanniNi");
        Model instance = new Model();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNi(expResult);
        double result = instance.getPsfGibsonLanniNi();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniTi0, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniTi0() {
        System.out.println("getPsfGibsonLanniTi0");
        Model instance = new Model();
        double expResult = 150;
        instance.setPsfGibsonLanniTi0(expResult);
        double result = instance.getPsfGibsonLanniTi0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniTg0, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniTg0() {
        System.out.println("getPsfGibsonLanniTg0");
        Model instance = new Model();
        double expResult = 170;
        instance.setPsfGibsonLanniTg0(expResult);
        double result = instance.getPsfGibsonLanniTg0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniTg, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniTg() {
        System.out.println("getPsfGibsonLanniTg");
        Model instance = new Model();
        double expResult = 170;
        instance.setPsfGibsonLanniTg(expResult);
        double result = instance.getPsfGibsonLanniTg();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniResPsf, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniResPsf() {
        System.out.println("getPsfGibsonLanniResPsf");
        Model instance = new Model();
        double expResult = 0.0215;
        instance.setPsfGibsonLanniResPsf(expResult);
        double result = instance.getPsfGibsonLanniResPsf();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniResPsfAxial, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniResPsfAxial() {
        System.out.println("getPsfGibsonLanniResPsfAxial");
        Model instance = new Model();
        double expResult = 0.005;
        instance.setPsfGibsonLanniResPsfAxial(expResult);
        double result = instance.getPsfGibsonLanniResPsfAxial();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniSolver, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniSolver() {
        System.out.println("getPsfGibsonLanniSolver");
        Model instance = new Model();
        String expResult = "svd";
        instance.setPsfGibsonLanniSolver(expResult);
        String result = instance.getPsfGibsonLanniSolver();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniMaxRadius, of class Model.
     */
    @Test
    public void testGetPsfGibsonLanniMaxRadius() {
        System.out.println("getPsfGibsonLanniMaxRadius");
        Model instance = new Model();
        int expResult = 15;
        instance.setPsfGibsonLanniMaxRadius(expResult);
        int result = instance.getPsfGibsonLanniMaxRadius();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getAnalyzerCurrentSelection method, of class Model.
     */
    @Test
    public void testGetAnalyzerCurrentSelection() {
        System.out.println("getAnalyzerCurrentSelection");
        Model instance = new Model();
        String expResult = "SpotCounter";
        instance.setAnalyzerCurrentSelection(expResult);
        String result = instance.getAnalyzerCurrentSelection();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getControllerCurrentSelection method, of class Model.
     */
    @Test
    public void testGetControllerCurrentSelection() {
        System.out.println("getControllerCurrentSelection");
        Model instance = new Model();
        String expResult = "PI";
        instance.setControllerCurrentSelection(expResult);
        String result = instance.getControllerCurrentSelection();
        assertEquals(expResult, result);
    }
}
