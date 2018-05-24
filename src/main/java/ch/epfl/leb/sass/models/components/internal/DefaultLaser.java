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

import ch.epfl.leb.sass.models.components.Laser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

/**
 * A source of light for illuminating the sample.
 */
public class DefaultLaser implements Laser {
    private double currentPower;
    private double maxPower;
    private double minPower;
    
    public static class Builder {
        private double currentPower;
        private double maxPower;
        private double minPower;
        
        public Builder currentPower(double currentPower) {
            this.currentPower = currentPower;
            return this;
        }
        public Builder maxPower(double maxPower) {
            this.maxPower = maxPower;
            return this;
        }
        public Builder minPower(double minPower) {
            this.minPower = minPower;
            return this;
        }
        
        public DefaultLaser build() {
            return new DefaultLaser(this);
        }
    }
    
    /**
     * Initialize the light source with given parameters.
     * @param builder A LightSource builder instance.
     */
    private DefaultLaser(Builder builder) {
        this.currentPower = builder.currentPower;
        this.maxPower = builder.maxPower;
        this.minPower = builder.minPower;
    }
    
    /**
     * Returns the current power.
     * @return current laser power
     */
    @Override
    public double getPower() {
        return currentPower;
    }
    
    /**
     * Sets the light source's power.
     * 
     * If the value is not within the limits, set it to the the closest allowed 
     * value.
     * 
     * @param newPower The power of the light source.
     */
    @Override
    public void setPower(double newPower) {
        if (newPower > maxPower)
            newPower = maxPower;
        else if (newPower < minPower)
            newPower = minPower;
        currentPower = newPower;
    }
    
    /**
     * Outputs the laser's properties as a JSON element.
     * 
     * @return A JSON tree describing the laser's properties.
     */
    @Override
    public JsonElement toJson() {
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(DefaultLaser.class,
                                             new DefaultLaserSerializer())
                        .create();
        return gson.toJsonTree(this);
    }
    
    class DefaultLaserSerializer implements JsonSerializer<DefaultLaser> {
        @Override
        public JsonElement serialize(DefaultLaser src, Type typeOfSrc,
                                  JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("currentPower", new JsonPrimitive(src.getPower()));
            return result;
        }
    }
}
