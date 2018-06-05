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
public class FluorophoreStateTransition implements Message {
    
    public final int CURRENT_STATE;
    public final int ID;
    public final int NEXT_STATE;
    public final double TIME_ELAPSED;
    public final MessageType TYPE = MessageType.FLUOROPHORE;
    
    
    public FluorophoreStateTransition(int id, double timeElapsed,
                                      int currentState, int nextState) {
        ID = id;
        TIME_ELAPSED = timeElapsed;
        CURRENT_STATE = currentState;
        NEXT_STATE = nextState;
    }
    
    /**
     * An identifier that indicates where this message originated from.
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
                        .registerTypeAdapter(
                                FluorophoreStateTransition.class,
                                new FluorophoreStateTransitionSerializer())
                        .create();
        return gson.toJsonTree(this);
    }
    
    class FluorophoreStateTransitionSerializer
          implements JsonSerializer<FluorophoreStateTransition> {
        @Override
        public JsonElement serialize(FluorophoreStateTransition src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("type", new JsonPrimitive(src.getType().name()));
            result.add("fluorophore id", new JsonPrimitive(src.ID));
            result.add("time elapsed", new JsonPrimitive(src.TIME_ELAPSED));
            result.add("current state", new JsonPrimitive(src.CURRENT_STATE));
            result.add("next state", new JsonPrimitive(src.NEXT_STATE));
            return result;
        }
    }
}
