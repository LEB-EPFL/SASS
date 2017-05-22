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
package algorithm_tester;

import ch.epfl.leb.alica.Analyzer;
import ch.epfl.leb.alica.Controller;
import algorithm_tester.generators.realtime.STORMsim;
import ch.epfl.leb.alica.analyzers.autolase.AutoLase;
import ch.epfl.leb.alica.analyzers.quickpalm.QuickPalm;
import ch.epfl.leb.alica.analyzers.spotcounter.SpotCounter;
import ch.epfl.leb.alica.controllers.pid.PID_controller;
import ij.process.ImageProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Carries out the actual simulation.
 * @author Marcel Stefko
 */
public class AlgorithmTester {

    
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
    
    /**
     * Initializes all analyzers, the generator and controller.
     */
    public AlgorithmTester() {
        // Real time generator
        generator = new STORMsim(null);
        
        analyzer = new SpotCounter(100, 5, true);
        // Set up controller
        //controller = new SimpleController();
        controller = new PID_controller(1.0,0.0,0.0,0.0,0.0);
        controller.setSetpoint(0.0);
    }
    
    public AlgorithmTester(Analyzer analyzer,
            ImageGenerator generator, Controller controller) {
        this.analyzer = analyzer;
        this.generator = generator;
        this.controller = controller;
    }
    
    /**
     * Main function which executes the testing procedure.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AlgorithmTester tester = new AlgorithmTester();
        tester.execute();
    }
    
    /**
     * Define the testing procedure in this method.
     */
    public void execute() {
        
        
        JFileChooser fc = new JFileChooser();
        int returnVal;
        
        //*
        // File chooser dialog for saving output csv
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        //set a default filename 
        fc.setSelectedFile(new File("tester_output.csv"));
        //Set an extension filter
        fc.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));
        returnVal = fc.showSaveDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File csv_output = fc.getSelectedFile();
        
        /*/
        File tiff_file = new File("C:\\Users\\stefko\\Desktop\\sim400orig.tif");
        File csv_output = new File("C:\\Users\\stefko\\Desktop\\output.csv");
        //*/
        
        
        /* Tiff generator
        // Analyze all images from the generator. End of analysis is marked
        // by a null pointer.
        fc.setFileFilter(new FileNameExtensionFilter("TIF file","tif"));
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        returnVal = fc.showOpenDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File tiff_file = fc.getSelectedFile();
        
        
        generator = new TiffGenerator(tiff_file);
        ImageProcessor ip = generator.getNextImage();
        image_count = 0;
        while (ip != null) {
            image_count++;
            for (EvaluationAlgorithm analyzer: analyzers)
                analyzer.processImage(ip);
            ip = generator.getNextImage();
        }
        
        /*/

        
        
        
        
        
        ImageProcessor ip;
        for (image_count = 0; image_count < 25; image_count++) {
            ip = generator.getNextImage();
            analyzer.processImage(ip.getPixelsCopy(),ip.getWidth(), ip.getHeight(), 0.100, 10);
            //System.out.println(image_count);
            if (image_count % 10 == 0) {
                controller.nextValue(analyzer.getBatchOutput());
                generator.setControlSignal(controller.getCurrentOutput());
            }
        }
        
        fc.setFileFilter(new FileNameExtensionFilter("TIF file","tif"));
        fc.setSelectedFile(new File("gen_stack.tif"));
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        returnVal = fc.showSaveDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        generator.saveStack(fc.getSelectedFile());
        
        
        //*/
        
        // Save analysis results to a csv file.
        saveToCsv(csv_output);
        System.exit(0);
    }
    
    /**
     * Saves the data generated by analyzers for each frame into a .csv file
     * @param file destination csv file
     */
    public void saveToCsv(File file) {
        throw new UnsupportedOperationException();// Open the file for writing
        /*PrintWriter writer;
        try {
            writer = new PrintWriter(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlgorithmTester.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        
        HashMap<String, Integer> parameter_map;
        // Print header: analyzer settings
        writer.println("#Analyzer settings:");
        String parameters = "";
        for (Analyzer analyzer: analyzers.values()) {
            parameter_map = analyzer.getCustomParameters();
            for (String key: parameter_map.keySet()) {
                parameters = parameters.
                    concat(analyzer.getName()).concat(".").concat(key).
                    concat(":").concat(parameter_map.get(key).toString()).
                    concat(",");
            }
        }
        writer.println(parameters);
        
        HashMap<String,Double> setting_map;
        writer.println("#Controller settings:");
        String settings = "";
        setting_map = controller.getSettings();
        for (String key: setting_map.keySet()) {
            settings = settings.concat(String.format(
                "%s:%f,",key,setting_map.get(key)));
        }
        writer.println(parameters);
        
        HashMap<String, Double> output_map;
        // Print header: Column description
        writer.println("#Columns:");
        String analyzer_names = "frame-id,true-signal,laser-power";
        for (Analyzer analyzer: analyzers.values()) {
            output_map = analyzer.getOutputValues(1);
                for (String key: output_map.keySet()) {
                    analyzer_names = analyzer_names.concat(",").
                    concat(analyzer.getName()).concat(":").concat(key);
                }
        }
        writer.println(analyzer_names);
        
        // Print data - one line for each frame
        for (int i=1; i<=image_count; i++) {
            String s = String.format("%d,%5.2f,%5.2f",
                    i,generator.getTrueSignal(i),controller.getOutputHistory(i));
            
            for (Analyzer analyzer: analyzers.values()) {
                output_map = analyzer.getOutputValues(i);
                for (String key: output_map.keySet()) {
                    s = s.concat(String.format(",%f",output_map.get(key)));
                }
                
            }
            writer.println(s);
        }
        
        writer.close();
        */
        
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
