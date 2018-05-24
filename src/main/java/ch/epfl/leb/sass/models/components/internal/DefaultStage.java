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
package ch.epfl.leb.sass.models.components.internal;

import ch.epfl.leb.sass.models.components.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * The sample stage.
 *
 * @author Kyle M. Douglass
 */
public class DefaultStage  implements Stage {
    /**
     * The stage x-position.
     */
    private double x;
    
    /**
     * The stage y-position.
     */
    private double y;
    
    /**
     * The stage z-position.
     * 
     * Negative values refer to moving the stage downwards on an inverted light
     * microscope. This corresponds to scanning upwards through the sample.
     */
    private double z;
    
    /**
     * Builder for creating stage instances.
     */
    public static class Builder {
        private double x;
        private double y;
        private double z;
        
        public Builder x(double x) { this.x = x; return this; }
        public Builder y(double y) { this.y = y; return this; }
        public Builder z(double z) { this.z = z; return this; }
        
        public DefaultStage build() {
            return new DefaultStage(this);
        }
    }
    
    private DefaultStage(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.z = builder.z;
    }
    
    /**
     * @return The stage's x-position.
     */
    @Override
    public double getX() {
        return this.x;
    }
    
    /**
     * Set the stage's x-position.
     * @param x 
     */
    @Override
    public void setX(double x) {
        this.x = x;
    }
    
    /**
     * @return The stage's y-position.
     */
    @Override
    public double getY() {
        return this.y;
    }
    
    /**
     * Set the stage's y-position.
     * @param y 
     */
    @Override
    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * @return The stage's z-position.
     */
    @Override
    public double getZ() {
        return this.z;
    }
    
    /**
     * Set the stage's z-position.
     * @param z 
     */
    @Override
    public void setZ(double z) {
        this.z = z;
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
                                             new DefaultStageSerializer())
                        .create();
        return gson.toJsonTree(this);
    } 
    
     class DefaultStageSerializer implements JsonSerializer<DefaultStage> {
        @Override
        public JsonElement serialize(DefaultStage src, Type typeOfSrc,
                                  JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("x", new JsonPrimitive(src.getX()));
            result.add("y", new JsonPrimitive(src.getY()));
            result.add("z", new JsonPrimitive(src.getZ()));
            return result;
        }
    }
}
