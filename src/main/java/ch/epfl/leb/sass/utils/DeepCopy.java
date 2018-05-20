/**
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
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Makes a deep copy of a serializable object.
 * 
 * @author Kyle M. Douglass
 */
public class DeepCopy {
    
    private final static Logger LOGGER = 
            Logger.getLogger(DeepCopy.class.getName());
    /**
    * Makes a deep copy of any Java object that is passed.
    * 
    * @see <a href="https://www.journaldev.com/17129/java-deep-copy-object">https://www.journaldev.com/17129/java-deep-copy-object</a>
    */
    public static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = 
                    new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            
            ByteArrayInputStream inputStream = 
                    new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = 
                    new ObjectInputStream(inputStream);
            
            return objInputStream.readObject();
        }
        catch (Exception ex) {
            String msg = "Could not serialize object.";
            LOGGER.log(Level.SEVERE, msg);
            ex.printStackTrace();
            return null;
   }
 }
    
}
