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
package ch.epfl.leb.sass.simulator.generators.realtime.components;

/**
 * Represents the parameters of the camera.
 */
public final class Camera {

    /**
     * frame rate [frames/second]
     */
    private int acqSpeed;

    /**
     * readout noise of camera [RMS]
     */
    private double readoutNoise;

    /**
     * dark current [electrons/second/pixel]
     */
    private double darkCurrent;

    /**
     * quantum efficiency [0.0-1.0]
     */
    private double quantumEfficiency;

    /**
     * Conversion factor between camera's analog-to-digital units (ADU) and electrons. [-]
     */
    private double aduPerElectron;
    
    /**
     * Electron multiplication (EM) gain of the camera.
     * This may be set to zero for sensors without EM gain, such as CMOS
     * sensors.
     */
    private int emGain;

    /**
     * Camera pixel baseline (zero signal mean) [ADU]
     */
    private int baseline;
    
    /**
     * physical size of pixel
     */
    private double pixelSize;


    /**
     * noise in frame caused by dark current [electrons/frame/pixel]
     */
    private double thermal_noise;
    
    /**
     * horizontal image size [pixels]
     */
    private int nX;

    /**
     * vertical image size [pixels]
     */
    private int nY;
    
    public static class Builder {
        private int acqSpeed;
        private double readoutNoise;
        private double darkCurrent;
        private double quantumEfficiency;
        private double aduPerElectron;
        private int emGain;
        private int baseline;
        private double pixelSize;
        private double thermalNoise;
        private int nX;
        private int nY;
        
        public Builder acqSpeed(int acqSpeed) {
            this.acqSpeed = acqSpeed;
            return this;
        }
        public Builder readoutNoise(double readoutNoise) {
            this.readoutNoise = readoutNoise;
            return this;
        }
        public Builder darkCurrent(double darkCurrent) {
            this.darkCurrent = darkCurrent;
            return this;
        }
        public Builder quantumEfficiency(double quantumEfficiency) {
            this.quantumEfficiency = quantumEfficiency;
            return this;
        }
        public Builder aduPerElectron(double aduPerElectron) {
            this.aduPerElectron = aduPerElectron;
            return this;
        }
        public Builder emGain(int emGain) {
            this.emGain = emGain;
            return this;
        }
        public Builder baseline(int baseline) {
            this.baseline = baseline;
            return this;
        }
        public Builder pixelSize(double pixelSize) {
            this.pixelSize = pixelSize;
            return this;
        }
        public Builder thermalNoise(double thermalNoise) {
            this.thermalNoise = thermalNoise;
            return this;
        }
        public Builder nX(int nX) { this.nX = nX; return this; }
        public Builder nY(int nY) { this.nY = nY; return this; }
        
        public Camera build() {
            return new Camera(this);
        }
    }
    
    private Camera(Builder builder) {
        this.aduPerElectron = builder.aduPerElectron;
        this.emGain = builder.emGain;
        this.acqSpeed = builder.acqSpeed;
        this.baseline = builder.baseline;
        this.darkCurrent = builder.darkCurrent;
        this.pixelSize = builder.pixelSize;
        this.quantumEfficiency = builder.quantumEfficiency;
        this.readoutNoise = builder.readoutNoise;
        this.thermal_noise = builder.thermalNoise;
        this.nX = builder.nX;
        this.nY = builder.nY;
    }
    
    public double getAduPerElectron() { return this.aduPerElectron; }
    public int getAcqSpeed() { return this.acqSpeed; }
    public int getBaseline() { return this.baseline; }
    public double getDarkCurrent() { return this.darkCurrent; }
    public int getEmGain() { return this.emGain; }
    public double getPixelSize() { return this.pixelSize; }
    public double getQuantumEfficiency() { return this.quantumEfficiency; }
    public double getReadoutNoise() { return this.readoutNoise; }
    public double getThermalNoise() { return this.thermal_noise; }
    
    /**
     * @return The number of pixels in x.
     */
    public int getNX() {
        return this.nX;
    }
    
    /**
     * @return The number of pixels in y.
     */
    public int getNY() {
        return this.nY;
    }
}

