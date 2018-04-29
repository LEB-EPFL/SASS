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
package ch.epfl.leb.sass.loggers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.io.IOException;

/**
 * Abstract class for logging simulation results.
 * @author Kyle M. Douglass
 */
public abstract class AbstractLogger {
    
    /**
     * Saves the state of the logger to a file.
     * @throws java.io.IOException
     */
    public abstract void saveLogFile() throws IOException;
    
     /**
     * Resets the logger to its initial state.
     */
    public abstract void reset();
    
    /**
     * The name of the log file.
     */
    protected String filename = "";
    
    /**
     * Determines whether the StateLogger is active or not.
     */
    protected boolean performLogging = false;
    
    /**
     * Return the current filename for the log file.
     * @return The filename of the log file.
     */
    public String getFilename() {
        return this.filename;
    }
    
    /**
     * Set the filename for logging the fluorophore state transitions and create the file.
     * @param inFilename The full path and filename of the log file
     * @throws IOException
     */
    public void setFilename(String inFilename) throws IOException {
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
            
            String parent = logFile.getParent();
            if (parent == null) {
                // Get current working directory in case no path was supplied
                Path path = FileSystems.getDefault().getPath(".");
                parent = path.toString();
            }
            baseName = parent + File.separator + baseName;
            logFile = new File(baseName);
            fileId++;
        }
        
        this.filename = logFile.toString();
    }
    
    /**
     * Indicates whether the logger is active.
     * @return A boolean indicating whether the logger is active.
     */
    public boolean getPerformLogging() {
        return performLogging;
    }
    
    /**
     * Activates and deactivates the logger.
     * @param isActive Indicates whether the logger should be active.
     */
    public void setPerformLogging(boolean isActive) {
        performLogging = isActive;
    }
}
