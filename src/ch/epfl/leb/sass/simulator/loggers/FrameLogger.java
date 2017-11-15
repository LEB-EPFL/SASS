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
    private ArrayList<Double> time_on = new ArrayList();


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
     * Simple logger for positions  and their times.
     * @param frame The current frame
     * @param id The emitter's unique ID.
     * @param x x-position of the emitter
     * @param y y-position of the emitter
     * @param z z-position of the emitter
     * @param time_on the amount of time the emitter "id" stays on in the current frame
     */
    public void logFrame(int frame, int id, double x, double y, double z, double time_on) {
        if ( !(this.performLogging) )
            return;

        this.frame.add(frame);
        this.id.add(id);
        this.x.add(x);
        this.y.add(y);
        this.z.add(z);
        this.time_on.add(time_on);
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

    public ArrayList<Double> getTimeOn() { return this.time_on; }

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

        printWriter.println("frame,id,x,y,z,time_on");

        for(int ctr = 0; ctr < this.frame.size(); ctr++) {
            printWriter.printf(
                    "%d,%d,%.4f,%.4f,%.4f,%.4f%n",
                    this.frame.get(ctr),
                    this.id.get(ctr),
                    this.x.get(ctr),
                    this.y.get(ctr),
                    this.z.get(ctr),
                    this.time_on.get(ctr)
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
        this.time_on = new ArrayList();
    }
}
