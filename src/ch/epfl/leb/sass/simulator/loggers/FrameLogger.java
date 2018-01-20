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
 * Reports the positions of all fluorophores visible in each frame in a file
 *
 * The FrameLogger is a singleton.
 *
 * @author Baptiste Ottino
 */
public class FrameLogger extends AbstractLogger{
    private static FrameLogger uniqueInstance = new FrameLogger();

    private ArrayList<Integer> frame = new ArrayList();
    private ArrayList<Integer> id = new ArrayList();
    private ArrayList<Double> x = new ArrayList();
    private ArrayList<Double> y = new ArrayList();
    private ArrayList<Double> z = new ArrayList();
    private ArrayList<Double> brightness = new ArrayList();
    private ArrayList<Double> timeOn = new ArrayList();
    
    /**
     * Should only the current frame be logged, or all frames?
     */
    private boolean logCurrentFrameOnly = false;

    /**
     * Constructor is private because FrameLogger is a singleton.
     */
    private FrameLogger() {
    }

    /**
     * @return An instance of the singleton.
     */
    public static FrameLogger getInstance() {
        return uniqueInstance;
    }

    /**
     * Logs emitter information for each full frame.
     * 
     * Correct operation of this method when logCurrentFrameOnly is true assumes
     * that values for the frame argument either are the same as previous calls
     * to this method or monotonically increasing.
     * 
     * @param frame The current frame
     * @param id The emitter's unique ID.
     * @param x x-position of the emitter
     * @param y y-position of the emitter
     * @param z z-position of the emitter
     * @param brightness the apparent brightness of the fluorophore on the frame
     * in number of photons
     * @param timeOn the amount of time the emitter "id" stays on in the current frame
     */
    public void logFrame(
            int frame,
            int id,
            double x,
            double y,
            double z,
            double brightness,
            double timeOn) {
        if ( !(this.performLogging) )
            return;

        // Save only the current frame's information
        if (    (this.logCurrentFrameOnly) &&
                (this.frame.size() > 0) &&
                (frame != this.frame.get(this.frame.size() - 1))) {
            this.frame.clear();
            this.id.clear();
            this.x.clear();
            this.y.clear();
            this.z.clear();
            this.brightness.clear();
            this.timeOn.clear();
        }
        
        this.frame.add(frame);
        this.id.add(id);
        this.x.add(x);
        this.y.add(y);
        this.z.add(z);
        this.brightness.add(brightness);
        this.timeOn.add(timeOn);
    }

    /**
     * Returns all the logged arrays in a single data structure.
     * 
     * This method is provided for convenience when all frame information is
     * required.
     * 
     * @return A FramInfo data structure containing all the logged data. 
     */
    public ArrayList<FrameInfo> getFrameInfo() {
        ArrayList<FrameInfo> info = new ArrayList();
        
        for (int i = 0; i < this.frame.size(); i++) {
            info.add(new FrameInfo(
                    this.frame.get(i),
                    this.id.get(i),
                    this.x.get(i),
                    this.y.get(i),
                    this.z.get(i),
                    this.brightness.get(i),
                    this.timeOn.get(i)
                )
            );          
        }
        
        return info;
    }
    
    public ArrayList<Integer> getFrame() { return this.frame; }

    public ArrayList<Integer> getId() {
        return this.id;
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

    public ArrayList<Double> getBrightness() { return this.brightness; }
    
    public ArrayList<Double> getTimeOn() { return this.timeOn; }

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

        printWriter.println("frame,id,x,y,z,brightness,time_on");

        for(int ctr = 0; ctr < this.frame.size(); ctr++) {
            printWriter.printf("%d,%d,%.4f,%.4f,%.4f,%.4f,%.4f%n",
                    this.frame.get(ctr),
                    this.id.get(ctr),
                    this.x.get(ctr),
                    this.y.get(ctr),
                    this.z.get(ctr),
                    this.brightness.get(ctr),
                    this.timeOn.get(ctr)
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
        this.frame = new ArrayList();
        this.id = new ArrayList();
        this.x = new ArrayList();
        this.y = new ArrayList();
        this.z = new ArrayList();
        this.brightness = new ArrayList();
        this.timeOn = new ArrayList();
    }
    
    /**
     * Toggles whether only the current frame or all frames should be logged.
     * 
     * Setting this to true will erase any information already held by the
     * FrameLogger.
     * 
     * @param logCurrentFrame If true, only information on the current frame is
     * retained.
     */
    public void setLogCurrentFrameOnly(boolean logCurrentFrame) {
        this.logCurrentFrameOnly = logCurrentFrame;
    }
    
    /**
     * Indicates whether only the current frame or all frames are logged.
     * @return If return value is true, only information about the current frame
     *         is returned.
     */
    public boolean getLogCurrentFrameOnly() {
        return this.logCurrentFrameOnly;
    }
}
