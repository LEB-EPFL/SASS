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

package ch.epfl.leb.sass.loggers;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Records the fluorophore positions to a file.
 * 
 * Currently, the position logger logs the initial positions of emitters to a
 * file, i.e. it will not track moving fluorophores. This feature may be added
 * in the future if desired.
 *
 * The PositionLogger is a singleton.
 *
 * @author Kyle M. Douglass
 */
public class PositionLogger extends AbstractLogger{
    private static PositionLogger uniqueInstance = new PositionLogger();
    
    /**
     * Position  information for recording.
     */
    private ArrayList<Integer> ids = new ArrayList();
    private ArrayList<Double> x = new ArrayList();
    private ArrayList<Double> y = new ArrayList();
    private ArrayList<Double> z = new ArrayList();

    
    /**
     * Constructor is private because StateLogger is a singleton.
     */
    private PositionLogger() {
    }

    /**
     * @return An instance of the singleton.
     */
    public static PositionLogger getInstance() {
	return uniqueInstance;
    }

    /**
     * Simple logger for positions  and their times.
     * @param id The emitter's unique ID.
     * @param x x-position of the emitter
     * @param y y-position of the emitter
     * @param z z-position of the emitter
     */
    public void logPosition(int id, double x, double y, double z) {
        if ( !(this.performLogging) )
            return;
        
        this.ids.add(id);
        this.x.add(x);
        this.y.add(y);
        this.z.add(z);
    }
    
    public ArrayList<Integer> getIds() {
        return this.ids;
    }
    
    public ArrayList<Double> getX() {
        return this.x;
    }
    
    public ArrayList<Double> getY() {
        return this.y;
    }
    
    public ArrayList<Double> getZ() {
        return this.z;
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
        
        printWriter.println("id,x,y,z");
        
        for(int ctr = 0; ctr < this.ids.size(); ctr++) {
            printWriter.printf(
                "%d,%.4f,%.4f,%.4f%n",
                this.ids.get(ctr),
                this.x.get(ctr),
                this.y.get(ctr),
                this.z.get(ctr)
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
        this.x = new ArrayList();
        this.y = new ArrayList();
        this.z = new ArrayList();
    }
}
