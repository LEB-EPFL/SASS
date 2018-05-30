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
package ch.epfl.leb.sass.logging.internal;

import ch.epfl.leb.sass.logging.Message;
import ch.epfl.leb.sass.logging.MessageType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * A message containing information about a fluorophore state transition.
 * 
 * @author Kyle M. Douglass
 */
public class LaserPowerChange implements Message {
    
    public final double POWER;
    public final MessageType TYPE = MessageType.LASER_POWER_CHANGE;
    
    
    public LaserPowerChange(double power) {
        POWER = power;
    }
    
    /**
     * An identifier that indicates where this message originated from.
     * 
     * @return The message type.
     */
    public MessageType getType() {
        return TYPE;
    }
    
    /**
     * Returns the the message as a JSON string.
     * 
     * @return The properties of the fluorophore as a JSON string.
     */
    @Override
    public JsonElement toJson() {
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LaserPowerChange.class,
                                new LaserPowerChangeSerializer())
                        .create();
        return gson.toJsonTree(this);
    }
    
    class LaserPowerChangeSerializer
          implements JsonSerializer<LaserPowerChange> {
        @Override
        public JsonElement serialize(LaserPowerChange src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("type", new JsonPrimitive(src.getType().name()));
            result.add("power", new JsonPrimitive(src.POWER));
            return result;
        }
    }
}
