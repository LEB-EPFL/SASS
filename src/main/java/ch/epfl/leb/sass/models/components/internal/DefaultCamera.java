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
package ch.epfl.leb.sass.models.components.internal;

import ch.epfl.leb.sass.models.components.Camera;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * Represents the parameters of the camera.
 */
public final class DefaultCamera implements Camera {

    /**
     * readout noise of camera (standard deviation)
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
     * DefaultCamera pixel baseline (zero signal mean) [ADU]
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
        
        public DefaultCamera build() {
            return new DefaultCamera(this);
        }
    }
    
    private DefaultCamera(Builder builder) {
        this.aduPerElectron = builder.aduPerElectron;
        this.emGain = builder.emGain;
        this.baseline = builder.baseline;
        this.darkCurrent = builder.darkCurrent;
        this.pixelSize = builder.pixelSize;
        this.quantumEfficiency = builder.quantumEfficiency;
        this.readoutNoise = builder.readoutNoise;
        this.thermal_noise = builder.thermalNoise;
        this.nX = builder.nX;
        this.nY = builder.nY;
    }
    
    @Override
    public double getAduPerElectron() { return this.aduPerElectron; }
    
    @Override
    public int getBaseline() { return this.baseline; }
    
    @Override
    public double getDarkCurrent() { return this.darkCurrent; }
    
    @Override
    public int getEmGain() { return this.emGain; }
    
    @Override
    public double getPixelSize() { return this.pixelSize; }
    
    @Override
    public double getQuantumEfficiency() { return this.quantumEfficiency; }
    
    @Override
    public double getReadoutNoise() { return this.readoutNoise; }
    
    @Override
    public double getThermalNoise() { return this.thermal_noise; }
    
    /**
     * @return The number of pixels in x.
     */
    @Override
    public int getNX() {
        return this.nX;
    }
    
    /**
     * @return The number of pixels in y.
     */
    @Override
    public int getNY() {
        return this.nY;
    }
    /**
     * Outputs the camera's properties as a JSON element.
     * 
     * @return A JSON tree describing the camera's properties.
     */
    public JsonElement toJson() {
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(DefaultCamera.class,
                                             new DefaultCameraSerializer())
                        .create();
        return gson.toJsonTree(this);
    }

    class DefaultCameraSerializer implements JsonSerializer<DefaultCamera> {
        @Override
        public JsonElement serialize(DefaultCamera src, Type typeOfSrc,
                                  JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("aduPerElectron", new JsonPrimitive(src.getAduPerElectron()));
            result.add("baseline", new JsonPrimitive(src.getBaseline()));
            result.add("darkCurrent", new JsonPrimitive(src.getDarkCurrent()));
            result.add("emGain", new JsonPrimitive(src.getEmGain()));
            result.add("nPixelsX", new JsonPrimitive(src.getNX()));
            result.add("nPixelsY", new JsonPrimitive(src.getNY()));
            result.add("pixelSize", new JsonPrimitive(src.getPixelSize()));
            result.add("quantumEfficiency",
                       new JsonPrimitive(src.getQuantumEfficiency()));
            result.add("readoutNoise",
                       new JsonPrimitive(src.getReadoutNoise()));
            result.add("thermalNoise", 
                       new JsonPrimitive(src.getThermalNoise()));
            return result;
        }
    }
}