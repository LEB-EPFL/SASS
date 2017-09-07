/** 
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * Author: Kyle M. Douglass
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

/**
 * Records the fluorophore states to a file.
 *
 * The StateLogger is a singleton.
 *
 * @author Kyle M. Douglass
 */

package ch.epfl.leb.sass.simulator;

import org.json.JSONObject;
import java.io.File;

public class StateLogger {
    private static StateLogger uniqueInstance = new StateLogger();
    private String filename = "";

    private StateLogger() {}

    public static StateLogger getInstance() {
	return uniqueInstance;
    }

    /**
     * Simple logger for state transitions and their times.
     * @param time_elapsed The time spent in the current state
     * @param current_state integer ID of the original state
     * @param next_state integer ID for the new fluorophore state
     */
    public void logStateTransition(double time_elapsed,
                              int current_state,
                              int next_state) {
    }

    /**
     * Logs all information from a fluorophore formatted in JSON
     * @param json State information encoded in a JSON object
     */
    public void logStateTransition(JSONObject json) {
    }
    
    /**
     * Return the current filename for the log file.
     * @return filename
     */
    public String getFilename() {
        return this.filename;
    }
    
    /**
     * Set the filename for logging the fluorophore state transitions.
     * @param filename The full path and filename of the log file
     */
    public void setFilename(String inFilename) {
        // Don't do anything if input filename equals the logger's current one
        if (inFilename.equals(this.filename))
            return;
        
        // Get a unique filename if the current one exists
        File logFile = new File(inFilename);
        int fileId = 1;
        String[] tokens;
        tokens = logFile.getName().split("\\.(?=[^\\.]+$)");
        String baseName;
        while (logFile.exists()) {
            // Remove last file type from filename and append a unique integer.
            // NOTE: example.tar.gz will become example.tar_1.gz
            // https://stackoverflow.com/questions/4545937/java-splitting-the-filename-into-a-base-and-extension#4546093
            baseName = tokens[0] + "_" + String.valueOf(fileId);
            
            if (2 == tokens.length)
                baseName += "." + tokens[1];
            
            baseName = logFile.getParent() + File.separator + baseName;
            logFile = new File(baseName);
            fileId++;
        }
            
        
        this.filename = logFile.toString();
    }
}
