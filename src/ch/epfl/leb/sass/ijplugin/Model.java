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
import ch.epfl.leb.sass.simulator.generators.realtime.components.Camera;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Laser;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Objective;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Stage;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.dynamics.SimpleDynamics;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.*;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.Gaussian2D;
import ch.epfl.leb.sass.simulator.generators.realtime.obstructors.commands.GenerateFiducialsRandom2D;
import ch.epfl.leb.sass.simulator.generators.realtime.backgrounds.commands.*;
import ij.IJ;
import java.io.File;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;


/**
 * Model for the InitializeSimulation window.
 * 
 * @author Kyle M. Douglass
 */
public class Model implements Serializable {
    
    private int cameraNX;
    private int cameraNY;
    private double cameraReadoutNoise;
    private double cameraDarkCurrent;
    private double cameraQuantumEfficiency;
    private double cameraAduPerElectron;
    private int cameraEmGain;
    private int cameraBaseline;
    private double cameraPixelSize;
    private double cameraThermalNoise;
    
    private double objectiveNa;
    private double objectiveMag;
    
    private double fluorophoreSignal;
    private double fluorophoreWavelength;
    private double fluorophoreTOn;
    private double fluorophoreTOff;
    private double fluorophoreTBl;
    
    private double laserMinPower;
    private double laserMaxPower;
    private double laserCurrentPower;
    
    private double stageX;
    private double stageY;
    private double stageZ;
    
    private String emittersCurrentSelection;
    private int emittersRandomNumber;
    private int emittersGridSpacing;
    private String emittersCsvFile;
    private String emittersRandomButtonText;
    private String emittersGridButtonText;
    private String emittersCsvFileButtonText;
    
    private int fiducialsNumber;
    private double fiducialsSignal;
    
    private String backgroundCurrentSelection;
    private float backgroundUniformSignal;
    private String backgroundTifFile;
    private String backgroundUniformButtonText;
    private String backgroundTifFileButtonText;
    
    // Getters
    //--------------------------------------------------------------------------
    public int getCameraNX() { return cameraNX; }
    public int getCameraNY() { return cameraNY; }
    public double getCameraReadoutNoise() { return cameraReadoutNoise; }
    public double getCameraDarkCurrent() { return cameraDarkCurrent; }
    public double getCameraQuantumEfficiency() {
        return cameraQuantumEfficiency;
    }
    public double getCameraAduPerElectron() { return cameraAduPerElectron; }
    public int getCameraEmGain() { return cameraEmGain; }
    public int getCameraBaseline() { return cameraBaseline; }
    public double getCameraPixelSize() { return cameraPixelSize; }
    public double getCameraThermalNoise() { return cameraThermalNoise; }
    
    public double getObjectiveNa() { return objectiveNa; }
    public double getObjectiveMag() { return objectiveMag; }
    
    public double getFluorophoreSignal() { return fluorophoreSignal; }
    public double getFluorophoreWavelength() { return fluorophoreWavelength; }
    public double getFluorophoreTOn() { return fluorophoreTOn; }
    public double getFluorophoreTOff() { return fluorophoreTOff; }
    public double getFluorophoreTBl() { return fluorophoreTBl; }
    
    public double getLaserMinPower() { return laserMinPower; }
    public double getLaserMaxPower() { return laserMaxPower; }
    public double getLaserCurrentPower() { return laserCurrentPower; }
    
    public double getStageX() { return stageX; }
    public double getStageY() { return stageY; }
    public double getStageZ() { return stageZ; }
    
    public String getEmittersCurrentSelection() {
        return emittersCurrentSelection;
    }
    public int getEmittersRandomNumber() { return emittersRandomNumber; }
    public int getEmittersGridSpacing() { return emittersGridSpacing; }
    public String getEmittersCsvFile() { return emittersCsvFile; }
    public String getEmittersRandomButtonText() {
        return emittersRandomButtonText;
    }
    public String getEmittersGridButtonText() {
        return emittersGridButtonText;
    }
    public String getEmittersCsvFileButtonText() {
        return emittersCsvFileButtonText;
    }
    
    public int getFiducialsNumber() { return fiducialsNumber; }
    public double getFiducialsSignal() { return fiducialsSignal;}
    
    public String getBackgroundCurrentSelection() {
        return backgroundCurrentSelection;
    }
    public float getBackgroundUniformSignal() {
        return backgroundUniformSignal;
    }
    public String getBackgroundTifFile() {
        return backgroundTifFile;
    }
    public String getBackgroundUniformButtonText() {
        return backgroundUniformButtonText;
    }
    public String getBackgroundTifFileButtonText() {
        return backgroundTifFileButtonText;
    }
    
    // Setters
    //--------------------------------------------------------------------------
    public void setCameraNX(int nX) { cameraNX = nX; }
    public void setCameraNY(int nY) { cameraNY = nY; }
    public void setCameraReadoutNoise(double readoutNoise) {
        cameraReadoutNoise = readoutNoise;
    }
    public void setCameraDarkCurrent(double darkCurrent) {
        cameraDarkCurrent = darkCurrent;
    }
    public void setCameraQuantumEfficiency(double quantumEfficiency) {
        cameraQuantumEfficiency = quantumEfficiency;
    }
    public void setCameraAduPerElectron(double aduPerElectron) {
        cameraAduPerElectron = aduPerElectron;
    }
    public void setCameraEmGain(int emGain) { cameraEmGain = emGain; }
    public void setCameraBaseline(int baseline) { cameraBaseline = baseline; }
    public void setCameraPixelSize(double pixelSize) {
        cameraPixelSize = pixelSize;
    }
    public void setCameraThermalNoise(double thermalNoise) {
        cameraThermalNoise = thermalNoise;
    }
    
    public void setObjectiveNa(double na) { objectiveNa = na; }
    public void setObjectiveMag(double mag) { objectiveMag = mag; }
    
    public void setFluorophoreSignal(double signal) {
        fluorophoreSignal = signal;
    }
    public void setFluorophoreWavelength(double wavelength) {
        fluorophoreWavelength = wavelength;
    }
    public void setFluorophoreTOn(double tOn) { fluorophoreTOn = tOn; }
    public void setFluorophoreTOff(double tOff) { fluorophoreTOff = tOff; }
    public void setFluorophoreTBl(double tBl) { fluorophoreTBl = tBl; }
    
    public void setLaserMinPower(double minPower) { laserMinPower = minPower; }
    public void setLaserMaxPower(double maxPower) { laserMaxPower = maxPower; }
    public void setLaserCurrentPower(double currentPower) {
        laserCurrentPower = currentPower;
    }
    
    public void setStageX(double x) { stageX = x; }
    public void setStageY(double y) { stageY = y; }
    public void setStageZ(double z) { stageZ = z; }
    
    public void setEmittersCurrentSelection(String currentSelection) {
        emittersCurrentSelection = currentSelection;
    }
    public void setEmittersRandomNumber(int number) {
        emittersRandomNumber = number;
    }
    public void setEmittersGridSpacing(int spacing) {
        emittersGridSpacing = spacing;
    }
    public void setEmittersCsvFile(String filename) {
        emittersCsvFile = filename;
    }
    public void setEmittersRandomButtonText(String text) {
        emittersRandomButtonText = text;
    }
    public void setEmittersGridButtonText(String text) {
        emittersGridButtonText = text;
    }
    public void setEmittersCsvFileButtonText(String text) {
        emittersCsvFileButtonText = text;
    }
    
    public void setFiducialsNumber(int number) {
        fiducialsNumber = number;
    }
    public void setFiducialsSignal(double signal) {
        fiducialsSignal = signal; }
    
    public void setBackgroundCurrentSelection(String currentSelection) {
        backgroundCurrentSelection = currentSelection;
    }
    public void setBackgroundUniformSignal(float signal) {
        backgroundUniformSignal = signal;
    }
    public void setBackgroundTifFile(String filename) {
        backgroundTifFile = filename;
    }
    public void setBackgroundUniformButtonText(String text) {
        backgroundUniformButtonText = text;
    }
    public void setBackgroundTifFileButtonText(String text) {
        backgroundTifFileButtonText = text;
    }
    
    /**
     * Builds a microscope from the model parameters.
     * @return A new microscope built from the model parameters.
     */
    public Microscope build() {
        Camera.Builder cameraBuilder = new Camera.Builder();
        Objective.Builder objectiveBuilder = new Objective.Builder();
        Stage.Builder stageBuilder = new Stage.Builder();
        SimpleDynamics.Builder fluorPropBuilder = new SimpleDynamics.Builder();
        Laser.Builder laserBuilder = new Laser.Builder();
        Gaussian2D.Builder psfBuilder = new Gaussian2D.Builder();
        FluorophoreCommandBuilder fluorPosBuilder = null;
        GenerateFiducialsRandom2D.Builder fidBuilder = 
                new GenerateFiducialsRandom2D.Builder();
        BackgroundCommandBuilder backgroundBuilder = null;
        
        cameraBuilder.nX(cameraNX);
        cameraBuilder.nY(cameraNY);
        cameraBuilder.readoutNoise(cameraReadoutNoise);
        cameraBuilder.darkCurrent(cameraDarkCurrent);
        cameraBuilder.quantumEfficiency(cameraQuantumEfficiency);
        cameraBuilder.aduPerElectron(cameraAduPerElectron);
        cameraBuilder.emGain(cameraEmGain);
        cameraBuilder.baseline(cameraBaseline);
        cameraBuilder.pixelSize(cameraPixelSize);

        objectiveBuilder.NA(objectiveNa);
        objectiveBuilder.mag(objectiveMag);

        stageBuilder.x(0);
        stageBuilder.y(0);
        stageBuilder.z(stageZ);

        fluorPropBuilder.signal(fluorophoreSignal);
        fluorPropBuilder.wavelength(fluorophoreWavelength);
        fluorPropBuilder.tOn(fluorophoreTOn);
        fluorPropBuilder.tOff(fluorophoreTOff);
        fluorPropBuilder.tBl(fluorophoreTBl);

        fidBuilder.numFiducials(fiducialsNumber); // Set to zero if you don't want fiducials
        fidBuilder.brightness(fiducialsSignal);
        
        laserBuilder.currentPower(laserCurrentPower);
        laserBuilder.minPower(laserMinPower);
        laserBuilder.maxPower(laserMaxPower);

        if (emittersCurrentSelection.equals(emittersRandomButtonText)) {
            // Random fluorophore distributions
            try {
                GenerateFluorophoresRandom2D.Builder tempPosBuilder = new GenerateFluorophoresRandom2D.Builder();
                tempPosBuilder.numFluors(emittersRandomNumber);
                fluorPosBuilder = tempPosBuilder;
            } catch (Exception ex) {
                IJ.showMessage("Error in emitter position parsing.");
            }
        } else if (emittersCurrentSelection.equals(emittersGridButtonText)) {
            // Fluorophore distributions on a square grid
            try {
                GenerateFluorophoresGrid2D.Builder tempPosBuilder = new GenerateFluorophoresGrid2D.Builder();
                tempPosBuilder.spacing(emittersGridSpacing);
                fluorPosBuilder = tempPosBuilder;
            } catch (Exception ex) {
                IJ.showMessage("Error in emitter position parsing.");
            }
        } else if (emittersCurrentSelection.equals(emittersCsvFileButtonText)) {
            // Parse fluorophore positions from a CSV file
            GenerateFluorophoresFromCSV.Builder tempPosBuilder = new GenerateFluorophoresFromCSV.Builder();
            tempPosBuilder.file(new File(emittersCsvFile));
            tempPosBuilder.rescale(false);
            fluorPosBuilder = tempPosBuilder;
        }

        if (backgroundCurrentSelection.equals(backgroundUniformButtonText)) {
            try {
                GenerateUniformBackground.Builder tempBuilder = new GenerateUniformBackground.Builder();
                tempBuilder.backgroundSignal(backgroundUniformSignal);
                backgroundBuilder = tempBuilder;
            } catch (NumberFormatException ex) {
                IJ.showMessage("Error in parsing of numerical values.");
            } catch (Exception ex) {
                IJ.showMessage("Error in device component intialization.");
            }
        } else if (backgroundCurrentSelection.equals(backgroundTifFileButtonText)) {
            try {
                GenerateBackgroundFromFile.Builder tempBuilder = new GenerateBackgroundFromFile.Builder();
                tempBuilder.file(new File(backgroundTifFile));
                backgroundBuilder = tempBuilder;
            } catch (ArrayIndexOutOfBoundsException ex) {
                IJ.showMessage("Error in background loading. The image is not large enough.");
            } catch (Exception ex) {
                IJ.showMessage("Error in background loading.");
            }
        }
        
        return new Microscope(
            cameraBuilder,
            laserBuilder,
            objectiveBuilder,
            psfBuilder,
            stageBuilder,
            fluorPosBuilder,
            fluorPropBuilder,
            fidBuilder,
            backgroundBuilder
        );
    }
    
    /**
     * Saves the model's state to a file.
     * 
     * @param fileOut The output stream to the file.
     */
    public void write(FileOutputStream fileOut) {
         try {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
         } catch (IOException ex) {
            ex.printStackTrace();
      }
    }
    
    /**
     * Loads a model from a file.
     * 
     * @param fileIn The input stream from the file.
     */
    public static Model read(FileInputStream fileIn) {
        Model model = null;
        try {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            model = (Model) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException ex) {
           ex.printStackTrace();
        } catch (ClassNotFoundException c) {
           c.printStackTrace();
        }
        
        return model;
    }
}
