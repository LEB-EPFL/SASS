/** 
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
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class StateLogger {
    private static StateLogger uniqueInstance = new StateLogger();
    
    /**
     * The name of the log file.
     */
    private String filename = "";
    
    /**
     * Determines whether the StateLogger is active or not.
     */
    private boolean performLogging = false;
    
    /**
     * State information for recording.
     */
    private ArrayList<Integer> ids = new ArrayList();
    private ArrayList<Double> elapsedTimes = new ArrayList();
    private ArrayList<Integer> initialStates = new ArrayList();
    private ArrayList<Integer> nextStates = new ArrayList();

    private StateLogger() {
    }

    public static StateLogger getInstance() {
	return uniqueInstance;
    }

    /**
     * Simple logger for state transitions and their times.
     * @param id integer ID of the emitter
     * @param timeElapsed The time spent in the current state
     * @param initialState integer ID of the original state
     * @param nextState integer ID for the new fluorophore state
     */
    public void logStateTransition(int id, double timeElapsed, int initialState, int nextState) {
        if ( !(this.performLogging) )
            return;
        
        this.ids.add(id);
        this.elapsedTimes.add(timeElapsed);
        this.initialStates.add(initialState);
        this.nextStates.add(nextState);
    }
    
    /**
     * Return the current filename for the log file.
     * @return filename
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
            
            baseName = logFile.getParent() + File.separator + baseName;
            logFile = new File(baseName);
            fileId++;
        }
        
        this.filename = logFile.toString();
    }
    
    public ArrayList<Integer> getIds() {
        return this.ids;
    }
    
    public ArrayList<Double> getElapsedTimes() {
        return this.elapsedTimes;
    }
    
    public ArrayList<Integer> getInitialStates() {
        return this.initialStates;
    }
    
    public ArrayList<Integer> getNextStates() {
        return this.nextStates;
    }
    
    public boolean getPerformLogging() {
        return performLogging;
    }
    
    public void setPerformLogging(boolean isActive) {
        performLogging = isActive;
    }
    
    /**
     * Saves the state of the logger to a file.
     * @throws IOException 
     */
    public void saveLogFile() throws IOException {
        File logFile = new File(this.filename);
        if ( !(logFile.exists()) )
            logFile.createNewFile();
        
        FileWriter fileWriter = new FileWriter(this.filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        
        printWriter.print("id,time_elapsed,initial_state,next_state%n");
        
        for(int ctr = 0; ctr < this.ids.size(); ctr++) {
            printWriter.printf(
                "%d,%.4f,%d,%d",
                this.ids.get(ctr),
                this.elapsedTimes.get(ctr),
                this.initialStates.get(ctr),
                this.nextStates.get(ctr)
            );
        }
        
        printWriter.close();
    }
    
    /**
     * Resets the logger to its initial state.
     */
    public void reset() {
        this.filename = "";
        this.performLogging = false;
        this.ids = new ArrayList();
        this.elapsedTimes = new ArrayList();
        this.initialStates = new ArrayList();
        this.nextStates = new ArrayList();
    }
}
