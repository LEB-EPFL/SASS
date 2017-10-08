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

package ch.epfl.leb.sass.simulator.loggers;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Records the fluorophore states to a file.
 *
 * The StateLogger is a singleton.
 *
 * @author Kyle M. Douglass
 */
public class StateLogger extends AbstractLogger {
    private static StateLogger uniqueInstance = new StateLogger();
    
    /**
     * Array of emitter IDs.
     */
    private ArrayList<Integer> ids = new ArrayList();
    
    /**
     * Array of times spent within a particular state.
     */
    private ArrayList<Double> elapsedTimes = new ArrayList();
    
    /**
     * Array of states that the emitter transitioned out of.
     */
    private ArrayList<Integer> initialStates = new ArrayList();
    
    /**
     * Array of states that the emitter transitioned into.
     */
    private ArrayList<Integer> nextStates = new ArrayList();

    /**
     * Constructor is private because StateLogger is a singleton.
     */
    private StateLogger() {
    }

    /**
     * @return An instance of the singleton.
     */
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
    
    /**
     * Saves the state of the logger to a file.
     * @throws IOException 
     */
    @Override
    public void saveLogFile() throws IOException {
        File logFile = new File(this.filename);
        if ( !(logFile.exists()) )
            logFile.createNewFile();
        
        FileWriter fileWriter = new FileWriter(this.filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        
        printWriter.println("id,time_elapsed,initial_state,next_state");
        
        for(int ctr = 0; ctr < this.ids.size(); ctr++) {
            printWriter.printf(
                "%d,%.4f,%d,%d%n",
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
    @Override
    public void reset() {
        this.filename = "";
        this.performLogging = false;
        this.ids = new ArrayList();
        this.elapsedTimes = new ArrayList();
        this.initialStates = new ArrayList();
        this.nextStates = new ArrayList();
    }
}
