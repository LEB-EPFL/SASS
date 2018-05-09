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

import ch.epfl.leb.sass.models.Microscope;
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
     * Test of getCameraNX method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraNX() {
        System.out.println("getCameraNX");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 32;
        instance.setCameraNX(expResult);
        int result = instance.getCameraNX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraNY method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraNY() {
        System.out.println("getCameraNY");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 64;
        instance.setCameraNY(expResult);
        int result = instance.getCameraNY();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraReadoutNoise method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraReadoutNoise() {
        System.out.println("getCameraReadoutNoise");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 2.5;
        instance.setCameraReadoutNoise(expResult);
        double result = instance.getCameraReadoutNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraDarkCurrent method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraDarkCurrent() {
        System.out.println("getCameraDarkCurrent");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.5;
        instance.setCameraDarkCurrent(expResult);
        double result = instance.getCameraDarkCurrent();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraQuantumEfficiency method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraQuantumEfficiency() {
        System.out.println("getCameraQuantumEfficiency");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.8;
        instance.setCameraQuantumEfficiency(expResult);
        double result = instance.getCameraQuantumEfficiency();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraAduPerElectron method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraAduPerElectron() {
        System.out.println("getCameraAduPerElectron");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 2.1;
        instance.setCameraAduPerElectron(expResult);
        double result = instance.getCameraAduPerElectron();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraEmGain method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraEmGain() {
        System.out.println("getCameraEmGain");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 300;
        instance.setCameraEmGain(expResult);
        int result = instance.getCameraEmGain();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraBaseline method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraBaseline() {
        System.out.println("getCameraBaseline");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 100;
        instance.setCameraBaseline(100);
        int result = instance.getCameraBaseline();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCameraPixelSize method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraPixelSize() {
        System.out.println("getCameraPixelSize");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 16.0;
        instance.setCameraPixelSize(expResult);
        double result = instance.getCameraPixelSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getCameraThermalNoise method, of class IJPluginModel.
     */
    @Test
    public void testGetCameraThermalNoise() {
        System.out.println("getCameraThermalNoise");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.4;
        instance.setCameraThermalNoise(expResult);
        double result = instance.getCameraThermalNoise();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getObjectiveNa method, of class IJPluginModel.
     */
    @Test
    public void testGetObjectiveNa() {
        System.out.println("getObjectiveNa");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1.2;
        instance.setObjectiveNa(expResult);
        double result = instance.getObjectiveNa();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getObjectiveMag method, of class IJPluginModel.
     */
    @Test
    public void testGetObjectiveMag() {
        System.out.println("getObjectiveMag");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 63;
        instance.setObjectiveMag(expResult);
        double result = instance.getObjectiveMag();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getFluorophoreCurrentSelection method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreCurrentSelection() {
        System.out.println("getFluorophoreCurrentSelection");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Simple";
        instance.setFluorophoreCurrentSelection(expResult);
        String result = instance.getFluorophoreCurrentSelection();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getFluorophoreSimpleText method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreSimpleText() {
        System.out.println("getFluorophoreSimpleText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Simple";
        instance.setFluorophoreSimpleText(expResult);
        String result = instance.getFluorophoreSimpleText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getFluorophorePalmText method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophorePalmText() {
        System.out.println("getFluorophorePalmText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "PALM";
        instance.setFluorophorePalmText(expResult);
        String result = instance.getFluorophorePalmText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getFluorophoreStormText method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreStormText() {
        System.out.println("getFluorophoreStormText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "STORM";
        instance.setFluorophoreStormText(expResult);
        String result = instance.getFluorophoreStormText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getFluorophoreSignal method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreSignal() {
        System.out.println("getFluorophoreSignal");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1500;
        instance.setFluorophoreSignal(expResult);
        double result = instance.getFluorophoreSignal();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreWavelength method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreWavelength() {
        System.out.println("getFluorophoreWavelength");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.532;
        instance.setFluorophoreWavelength(expResult);
        double result = instance.getFluorophoreWavelength();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreTOn method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreTOn() {
        System.out.println("getFluorophoreTOn");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 3;
        instance.setFluorophoreTOn(expResult);
        double result = instance.getFluorophoreTOn();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreTOff method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreTOff() {
        System.out.println("getFluorophoreTOff");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 100;
        instance.setFluorophoreTOff(expResult);
        double result = instance.getFluorophoreTOff();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFluorophoreTBl method, of class IJPluginModel.
     */
    @Test
    public void testGetFluorophoreTBl() {
        System.out.println("getFluorophoreTBl");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1000;
        instance.setFluorophoreTBl(expResult);
        double result = instance.getFluorophoreTBl();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmWavelength method, of class IJPluginModel.
     */
    @Test
    public void testGetPalmWavelength() {
        System.out.println("getPalmWavelength");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.58;
        instance.setPalmWavelength(expResult);
        double result = instance.getPalmWavelength();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmSignal method, of class IJPluginModel.
     */
    @Test public void testGetPalmSignal() {
        System.out.println("getPalmSignal");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1250;
        instance.setPalmSignal(expResult);
        double result = instance.getPalmSignal();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmKA method, of class IJPluginModel.
     */
    @Test public void testGetPalmKA() {
        System.out.println("getPalmKA");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 10;
        instance.setPalmKA(expResult);
        double result = instance.getPalmKA();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmKB method, of class IJPluginModel.
     */
    @Test public void testGetPalmKB() {
        System.out.println("getPalmKB");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 5;
        instance.setPalmKB(expResult);
        double result = instance.getPalmKB();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmKD1 method, of class IJPluginModel.
     */
    @Test public void testGetPalmKD1() {
        System.out.println("getPalmKD1");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.1;
        instance.setPalmKD1(expResult);
        double result = instance.getPalmKD1();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmKR1 method, of class IJPluginModel.
     */
    @Test public void testGetPalmKR1() {
        System.out.println("getPalmKR1");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.1;
        instance.setPalmKR1(expResult);
        double result = instance.getPalmKR1();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmKD2 method, of class IJPluginModel.
     */
    @Test public void testGetPalmKD2() {
        System.out.println("getPalmKD2");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.1;
        instance.setPalmKD2(expResult);
        double result = instance.getPalmKD2();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPalmKR2 method, of class IJPluginModel.
     */
    @Test
    public void testGetPalmKR2() {
        System.out.println("getPalmKR2");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.1;
        instance.setPalmKR2(expResult);
        double result = instance.getPalmKR2();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getStormKBl method, class IJPluginModel.
     */
    @Test
    public void testGetStormKBl() {
        System.out.println("getStormKBl");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.001;
        instance.setStormKBl(expResult);
        double result = instance.getStormKBl();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getStormKTriplet method, class IJPluginModel.
     */
    @Test
    public void testGetStormKTriplet() {
        System.out.println("getStormKTriplet");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.001;
        instance.setStormKTriplet(expResult);
        double result = instance.getStormKTriplet();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getStormKTripletRecovery method, class IJPluginModel.
     */
    @Test
    public void testGetStormKTripletRecovery() {
        System.out.println("getStormKTripletRecovery");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.001;
        instance.setStormKTripletRecovery(expResult);
        double result = instance.getStormKTripletRecovery();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getStormKDark method, class IJPluginModel.
     */
    @Test
    public void testGetStormKDark() {
        System.out.println("getStormKDark");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.001;
        instance.setStormKDark(expResult);
        double result = instance.getStormKDark();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getStormKDarkRecovery method, class IJPluginModel.
     */
    @Test
    public void testGetStormKDarkRecovery() {
        System.out.println("getStormKDarkRecovery");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.001;
        instance.setStormKDarkRecovery(expResult);
        double result = instance.getStormKDarkRecovery();
        assertEquals(expResult, result, 0.0);
    }
    
     /**
     * Test of getStormKDarkRecoveryConstant method, class IJPluginModel.
     */
    @Test
    public void testGetStormKDarkRecoveryConstant() {
        System.out.println("getStormKDarkRecoveryConstant");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.001;
        instance.setStormKDarkRecoveryConstant(expResult);
        double result = instance.getStormKDarkRecoveryConstant();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLaserMinPower method, of class IJPluginModel.
     */
    @Test
    public void testGetLaserMinPower() {
        System.out.println("getLaserMinPower");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.0;
        instance.setLaserMinPower(expResult);
        double result = instance.getLaserMinPower();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLaserMaxPower method, of class IJPluginModel.
     */
    @Test
    public void testGetLaserMaxPower() {
        System.out.println("getLaserMaxPower");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 150.0;
        instance.setLaserMaxPower(expResult);
        double result = instance.getLaserMaxPower();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLaserCurrentPower method, of class IJPluginModel.
     */
    @Test
    public void testGetLaserCurrentPower() {
        System.out.println("getLaserCurrentPower");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 5.0;
        instance.setLaserCurrentPower(expResult);
        double result = instance.getLaserCurrentPower();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getStageX method, of class IJPluginModel.
     */
    @Test
    public void testGetStageX() {
        System.out.println("getStageX");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 5.0;
        instance.setStageX(expResult);
        double result = instance.getStageX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getStageY method, of class IJPluginModel.
     */
    @Test
    public void testGetStageY() {
        System.out.println("getStageY");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 13.0;
        instance.setStageY(expResult);
        double result = instance.getStageY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getStageZ method, of class IJPluginModel.
     */
    @Test
    public void testGetStageZ() {
        System.out.println("getStageZ");
        IJPluginModel instance = new IJPluginModel();
        double expResult = -3.5;
        instance.setStageZ(expResult);
        double result = instance.getStageZ();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getEmittersCurrentSelection method, of class IJPluginModel.
     */
    @Test
    public void testGetEmittersCurrentSelection() {
        System.out.println("getEmittersCurrentSelection");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Random";
        instance.setEmittersCurrentSelection(expResult);
        String result = instance.getEmittersCurrentSelection();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersRandomNumber method, of class IJPluginModel.
     */
    @Test
    public void testGetEmittersRandomNumber() {
        System.out.println("getEmittersRandomNumber");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 42;
        instance.setEmittersRandomNumber(expResult);
        int result = instance.getEmittersRandomNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersGridSpacing method, of class IJPluginModel.
     */
    @Test
    public void testGetEmittersGridSpacing() {
        System.out.println("getEmittersGridSpacing");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 5;
        instance.setEmittersGridSpacing(expResult);
        int result = instance.getEmittersGridSpacing();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersCsvFile method, of class IJPluginModel.
     */
    @Test
    public void testGetEmittersCsvFile() {
        System.out.println("getEmittersCsvFile");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "/file/path";
        instance.setEmittersCsvFile(expResult);
        String result = instance.getEmittersCsvFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersRandomButtonText method, of class IJPluginModel.
     */
    @Test
    public void testGetEmittersRandomButtonText() {
        System.out.println("getEmittersRandomButtonText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Grid";
        instance.setEmittersRandomButtonText(expResult);
        String result = instance.getEmittersRandomButtonText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersGridButtonText method, of class IJPluginModel.
     */
    @Test
    public void testGetEmittersGridButtonText() {
        System.out.println("getEmittersGridButtonText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Grid";
        instance.setEmittersGridButtonText(expResult);
        String result = instance.getEmittersGridButtonText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittersCsvFileButtonText method, of class IJPluginModel.
     */
    @Test
    public void testGetEmittersCsvFileButtonText() {
        System.out.println("getEmittersCsvFileButtonText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "From .csv file...";
        instance.setEmittersCsvFileButtonText(expResult);
        String result = instance.getEmittersCsvFileButtonText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getEmitters3DCheckBoxEnabled method, of class IJPluginModel.
     */
    @Test
    public void testGetEmitters3DCheckBoxEnabled() {
        System.out.println("getEmitters3DCheckBoxEnabled");
        IJPluginModel instance = new IJPluginModel();
        boolean expResult = true;
        instance.setEmitters3DCheckBoxEnabled(expResult);
        boolean result = instance.getEmitters3DCheckBoxEnabled();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getEmitters3DMinZ method, of class IJPluginModel.
     */
    @Test
    public void testGetEmitters3DMinZ() {
        System.out.println("getEmitters3DMinZ");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.0;
        instance.setEmitters3DMinZ(expResult);
        double result = instance.getEmitters3DMinZ();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getEmitters3DMaxZ method, of class IJPluginModel.
     */
    @Test
    public void testGetEmitters3DMaxZ() {
        System.out.println("getEmitters3DMaxZ");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 3.2;
        instance.setEmitters3DMaxZ(expResult);
        double result = instance.getEmitters3DMaxZ();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFiducialsNumber method, of class IJPluginModel.
     */
    @Test
    public void testGetFiducialsNumber() {
        System.out.println("getFiducialsNumber");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 37;
        instance.setFiducialsNumber(expResult);
        int result = instance.getFiducialsNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFiducialsSignal method, of class IJPluginModel.
     */
    @Test
    public void testGetFiducialsSignal() {
        System.out.println("getFiducialsSignal");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 3000;
        instance.setFiducialsSignal(expResult);
        double result = instance.getFiducialsSignal();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBackgroundCurrentSelection method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundCurrentSelection() {
        System.out.println("getBackgroundCurrentSelection");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Uniform";
        instance.setBackgroundCurrentSelection(expResult);
        String result = instance.getBackgroundCurrentSelection();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBackgroundUniformSignal method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundUniformSignal() {
        System.out.println("getBackgroundUniformSignal");
        IJPluginModel instance = new IJPluginModel();
        float expResult = 2378;
        instance.setBackgroundUniformSignal(expResult);
        float result = instance.getBackgroundUniformSignal();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getBackgroundRandomFeatureSize method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundRandomFeatureSize() {
        System.out.println("getBackgroundRandomFeatureSize");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 25;
        instance.setBackgroundRandomFeatureSize(expResult);
        double result = instance.getBackgroundRandomFeatureSize();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBackgroundRandomMinValue method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundRandomMinValue() {
        System.out.println("getBackgroundRandomMinValue");
        IJPluginModel instance = new IJPluginModel();
        float expResult = 25;
        instance.setBackgroundRandomMinValue(expResult);
        double result = instance.getBackgroundRandomMinValue();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getBackgroundRandomMaxValue method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundRandomMaxValue() {
        System.out.println("getBackgroundRandomMaxValue");
        IJPluginModel instance = new IJPluginModel();
        float expResult = 25;
        instance.setBackgroundRandomMaxValue(expResult);
        double result = instance.getBackgroundRandomMaxValue();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getBackgroundRandomFeatureSize method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundRandomSeed() {
        System.out.println("getBackgroundRandomMaxValue");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 42;
        instance.setBackgroundRandomSeed(expResult);
        int result = instance.getBackgroundRandomSeed();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getBackgroundTifFile method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundTifFile() {
        System.out.println("getBackgroundTifFile");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "/path/to/file";
        instance.setBackgroundTifFile(expResult);
        String result = instance.getBackgroundTifFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBackgroundUniformButtonText method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundUniformButtonText() {
        System.out.println("getBackgroundUniformButtonText");
        IJPluginModel instance = new IJPluginModel();
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
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Random";
        instance.setBackgroundRandomButtonText(expResult);
        String result = instance.getBackgroundRandomButtonText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getBackgroundTifFileButtonText method, of class IJPluginModel.
     */
    @Test
    public void testGetBackgroundTifFileButtonText() {
        System.out.println("getBackgroundTifFileButtonText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "From .tif file...";
        instance.setBackgroundTifFileButtonText(expResult);
        String result = instance.getBackgroundTifFileButtonText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfCurrentSelection method, of class IJPluginModel.
     */
    @Test
    public void testGetPsfCurrentSelection() {
        System.out.println("getPsfCurrentSelection");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Gaussian 2D";
        instance.setPsfCurrentSelection(expResult);
        String result = instance.getPsfCurrentSelection();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGaussian2dText method, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGaussian2dText() {
        System.out.println("getPsfGaussian2dText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Gaussian 2D";
        instance.setPsfGaussian2dText(expResult);
        String result = instance.getPsfGaussian2dText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGaussian3dText method, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGaussian3dText() {
        System.out.println("getPsfGaussian3dText");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "Gaussian 3D";
        instance.setPsfGaussian3dText(expResult);
        String result = instance.getPsfGaussian3dText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniNumBasis, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniNumBasis() {
        System.out.println("getPsfGibsonLanniNumBasis");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 100;
        instance.setPsfGibsonLanniNumBasis(expResult);
        int result = instance.getPsfGibsonLanniNumBasis();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniNumSamples, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniNumSamples() {
        System.out.println("getPsfGibsonLanniNumSamples");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 1000;
        instance.setPsfGibsonLanniNumSamples(expResult);
        int result = instance.getPsfGibsonLanniNumSamples();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniOversampling, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniOversampling() {
        System.out.println("getPsfGibsonLanniOversampling");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 2;
        instance.setPsfGibsonLanniOversampling(expResult);
        int result = instance.getPsfGibsonLanniOversampling();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniSizeX, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniSizeX() {
        System.out.println("getPsfGibsonLanniSizeX");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 32;
        instance.setPsfGibsonLanniSizeX(expResult);
        int result = instance.getPsfGibsonLanniSizeX();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniSizeY, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniSizeY() {
        System.out.println("getPsfGibsonLanniSizeY");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 32;
        instance.setPsfGibsonLanniSizeY(expResult);
        int result = instance.getPsfGibsonLanniSizeY();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniNs, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniNs() {
        System.out.println("getPsfGibsonLanniNs");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1.33;
        instance.setPsfGibsonLanniNs(expResult);
        double result = instance.getPsfGibsonLanniNs();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNg0, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniNg0() {
        System.out.println("getPsfGibsonLanniNg0");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNg0(expResult);
        double result = instance.getPsfGibsonLanniNg0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNg, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniNg() {
        System.out.println("getPsfGibsonLanniNg");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNg(expResult);
        double result = instance.getPsfGibsonLanniNg();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNi0, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniNi0() {
        System.out.println("getPsfGibsonLanniNi0");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNi0(expResult);
        double result = instance.getPsfGibsonLanniNi0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniNi, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniNi() {
        System.out.println("getPsfGibsonLanniNi");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 1.5;
        instance.setPsfGibsonLanniNi(expResult);
        double result = instance.getPsfGibsonLanniNi();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniTi0, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniTi0() {
        System.out.println("getPsfGibsonLanniTi0");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 150;
        instance.setPsfGibsonLanniTi0(expResult);
        double result = instance.getPsfGibsonLanniTi0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniTg0, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniTg0() {
        System.out.println("getPsfGibsonLanniTg0");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 170;
        instance.setPsfGibsonLanniTg0(expResult);
        double result = instance.getPsfGibsonLanniTg0();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniTg, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniTg() {
        System.out.println("getPsfGibsonLanniTg");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 170;
        instance.setPsfGibsonLanniTg(expResult);
        double result = instance.getPsfGibsonLanniTg();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniResPsf, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniResPsf() {
        System.out.println("getPsfGibsonLanniResPsf");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.0215;
        instance.setPsfGibsonLanniResPsf(expResult);
        double result = instance.getPsfGibsonLanniResPsf();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniResPsfAxial, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniResPsfAxial() {
        System.out.println("getPsfGibsonLanniResPsfAxial");
        IJPluginModel instance = new IJPluginModel();
        double expResult = 0.005;
        instance.setPsfGibsonLanniResPsfAxial(expResult);
        double result = instance.getPsfGibsonLanniResPsfAxial();
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of getPsfGibsonLanniSolver, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniSolver() {
        System.out.println("getPsfGibsonLanniSolver");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "svd";
        instance.setPsfGibsonLanniSolver(expResult);
        String result = instance.getPsfGibsonLanniSolver();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPsfGibsonLanniMaxRadius, of class IJPluginModel.
     */
    @Test
    public void testGetPsfGibsonLanniMaxRadius() {
        System.out.println("getPsfGibsonLanniMaxRadius");
        IJPluginModel instance = new IJPluginModel();
        int expResult = 15;
        instance.setPsfGibsonLanniMaxRadius(expResult);
        int result = instance.getPsfGibsonLanniMaxRadius();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getAnalyzerCurrentSelection method, of class IJPluginModel.
     */
    @Test
    public void testGetAnalyzerCurrentSelection() {
        System.out.println("getAnalyzerCurrentSelection");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "SpotCounter";
        instance.setAnalyzerCurrentSelection(expResult);
        String result = instance.getAnalyzerCurrentSelection();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getControllerCurrentSelection method, of class IJPluginModel.
     */
    @Test
    public void testGetControllerCurrentSelection() {
        System.out.println("getControllerCurrentSelection");
        IJPluginModel instance = new IJPluginModel();
        String expResult = "PI";
        instance.setControllerCurrentSelection(expResult);
        String result = instance.getControllerCurrentSelection();
        assertEquals(expResult, result);
    }
}
