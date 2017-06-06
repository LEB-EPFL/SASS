/* 
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * Author: Marcel Stefko
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
package ch.epfl.leb.sass.simulator;

import ch.epfl.leb.alica.Analyzer;
import ch.epfl.leb.alica.Controller;
import ch.epfl.leb.sass.simulator.generators.realtime.STORMsim;
import ch.epfl.leb.alica.analyzers.spotcounter.SpotCounter;
import ch.epfl.leb.alica.controllers.manual.ManualController;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONException;
import org.json.JSONObject;
import ch.epfl.leb.sass.simulator.generators.realtime.RNG;

/**
 * Carries out the actual simulation.
 * @author Marcel Stefko
 */
public class Simulator {

    /**
     * Analyzer which analyzes generated images
     */
    protected final Analyzer analyzer;

    /**
     * Generator which is the source of images to be analyzed.
     */
    protected final ImageGenerator generator;

    /**
     * Takes the output of a single analyzer, processes it, and outputs a
     * signal to the generator, for feedback loop control.
     */
    protected final Controller controller;

    /**
     * Number of already-generated images.
     */
    protected int image_count;
    
    protected HashMap<Integer,JSONObject> history = new HashMap<Integer,JSONObject>();
    
    /**
     * Initializes all analyzers, the generator and controller.
     */
    public Simulator() {
        // Real time generator
        generator = new STORMsim(null);
        analyzer = new SpotCounter(100, 5, true);
        // Set up controller
        //controller = new SimpleController();
        controller = new ManualController(30, 1);
        controller.setSetpoint(1.0);
    }
    
    /**
     * Initialize simulator from components
     * @param analyzer
     * @param generator
     * @param controller
     */
    public Simulator(Analyzer analyzer,
            ImageGenerator generator, Controller controller) {
        this.analyzer = analyzer;
        this.generator = generator;
        this.controller = controller;
    }
    
    /**
     * An example simulation
     * @param no_of_images
     * @param controller_refresh_rate
     * @param csv_save_path
     * @param tiff_save_path
     * @return
     */
    public ImageStack execute(int no_of_images, int controller_refresh_rate, String csv_save_path, String tiff_save_path) {
        if (no_of_images < 1 || controller_refresh_rate < 1) {
            throw new IllegalArgumentException("Wrong simulation parameters!");
        }
        
        ImageProcessor ip;
        for (image_count = 1; image_count <= no_of_images; image_count++) {
            JSONObject history_entry = new JSONObject();
            
            ip = generator.getNextImage();
            analyzer.processImage(ip.getPixelsCopy(),ip.getWidth(), ip.getHeight(), 0.100, 10);
            //System.out.println(image_count);
            if (image_count % controller_refresh_rate == 0) {
                double analyzer_batch_output = analyzer.getBatchOutput();
                controller.nextValue(analyzer_batch_output);
                generator.setControlSignal(controller.getCurrentOutput());
            }
            try {
                history_entry.put("true-signal",generator.getTrueSignal(image_count));
                history_entry.put("analyzer-output", analyzer.getIntermittentOutput());
                history_entry.put("controller-output", controller.getCurrentOutput());
                history_entry.put("controller-setpoint", controller.getSetpoint());
            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error in storing JSON values.", ex);
            }
            
            history.put(image_count, history_entry);
        }
        image_count -= 1; // to accurately represent image count
        
        if (csv_save_path != null) {
            File csv_file = new File(csv_save_path);
            saveToCsv(csv_file);
        }
        
        if (tiff_save_path != null) {
            File tiff_file = new File(tiff_save_path);
            generator.saveStack(tiff_file);
        }
        
        return generator.getStack();
    
    }
    /**
     * Saves the data for generator, analyzer and controller for each frame into a .csv file
     * @param file destination csv file
     */
    public void saveToCsv(File file) {// Open the file for writing
        PrintWriter writer;
        try {
            writer = new PrintWriter(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        HashMap<String, Double> output_map;
        // Print header: Column description
        writer.println("#Columns:");
        String column_names = "frame-id,true-signal,analyzer-output,controller-output,controller-setpoint";
        writer.println(column_names);
        
        // Print data - one line for each frame
        for (int i=1; i<=image_count; i++) {
            JSONObject e = history.get(i);
            String s;
            try {
                s = String.format("%d,%8.4e,%8.4e,%8.4e,%8.4e",
                        i,e.get("true-signal"),e.get("analyzer-output"),e.get("controller-output"),e.get("controller-setpoint"));
            } catch (JSONException ex) {
                s = String.format("%d",i);
                Logger.getLogger(Simulator.class.getName()).log(Level.FINER, null, ex);
            } catch (NullPointerException ex) {
                s = String.format("%d",i);
                Logger.getLogger(Simulator.class.getName()).log(Level.FINER, null, ex);
            }
            writer.println(s);
        }
        
        writer.close();
        
        
    }
    
    /**
     * Save current ImageStack to TIFF file
     * @param tiff_file file to save to
     */
    public void saveStack(File tiff_file) {
        generator.saveStack(tiff_file);
    }
    
    /**
     * Increments image counter in case an image was generated outside of
     * this class.
     */
    public void incrementCounter() {
        image_count++;
    }
    
    /**
     * Returns the number of generated images since simulation start.
     * @return number of generated images
     */
    public int getImageCount() {
        return image_count;
    }
}
