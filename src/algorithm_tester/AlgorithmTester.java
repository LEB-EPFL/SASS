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

import algorithm_tester.autolase.AutoLase;
import algorithm_tester.quickpalm.QuickPalm;
import algorithm_tester.spotcounter.SpotCounter;
import algorithm_tester.tiffgenerator.TiffGenerator;
import ij.process.ImageProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author stefko
 */
public class AlgorithmTester {
    ArrayList<EvaluationAlgorithm> analyzers;
    ImageGenerator generator;
    int image_count;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AlgorithmTester tester = new AlgorithmTester();
        tester.execute();
    }
    
    public void execute() {
        
        //*
        // File chooser dialog for choosing tif stack
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File tiff_file = fc.getSelectedFile();
        
        // File chooser dialog for saving output csv
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        //set a default filename (this is where you default extension first comes in)
        fc.setSelectedFile(new File("tester_output.csv"));
        //Set an extension filter, so the user sees other XML files
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
        
        generator = new TiffGenerator(tiff_file);
        
        analyzers = new ArrayList<EvaluationAlgorithm>();
        
        AutoLase autolase = new AutoLase();
        HashMap<String, Integer> autolase_params = new HashMap<String, Integer>();
        autolase_params.put("threshold", 70);
        autolase_params.put("averaging", 30);
        autolase.setCustomParameters(autolase_params);
        
        SpotCounter spotcounter = new SpotCounter();
        HashMap<String, Integer> spotcounter_params = new HashMap<String, Integer>();
        spotcounter_params.put("noise-tolerance", 90);
        spotcounter_params.put("box-size", 5);
        spotcounter.setCustomParameters(spotcounter_params);
        
        QuickPalm quickpalm = new QuickPalm();
        
        
        addAnalyzer(autolase);
        addAnalyzer(spotcounter);
        addAnalyzer(quickpalm);
        
        ImageProcessor ip = generator.getNextImage();
        image_count = 0;
        while (ip != null) {
            image_count++;
            for (EvaluationAlgorithm analyzer: analyzers)
                analyzer.processImage(ip);
            ip = generator.getNextImage();
        }
        saveToCsv(csv_output);
        System.exit(0);
    }
    
    public void addAnalyzer(EvaluationAlgorithm analyzer) {
        analyzers.add(analyzer);
    }
    
    private void saveToCsv(File file) {
        PrintWriter writer;
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
        for (EvaluationAlgorithm analyzer: analyzers) {
            parameter_map = analyzer.getCustomParameters();
                for (String key: parameter_map.keySet()) {
                    parameters = parameters.
                        concat(analyzer.getName()).concat(".").concat(key).
                        concat(":").concat(parameter_map.get(key).toString()).
                        concat(",");
                }
        }
        writer.println(parameters);
        
        HashMap<String, Double> output_map;
        // Print header
        writer.println("#Columns:");
        String analyzer_names = "frame-id";
        for (EvaluationAlgorithm analyzer: analyzers) {
            output_map = analyzer.getOutputValues(1);
                for (String key: output_map.keySet()) {
                    analyzer_names = analyzer_names.concat(",").
                    concat(analyzer.getName()).concat(":").concat(key);
                }
        }
        writer.println(analyzer_names);
        
        // Print data
        for (int i=1; i<=image_count; i++) {
            String s = String.format("%d",i);
            for (EvaluationAlgorithm analyzer: analyzers) {
                output_map = analyzer.getOutputValues(i);
                for (String key: output_map.keySet()) {
                    s = s.concat(String.format(",%f",output_map.get(key)));
                }
                
            }
            writer.println(s);
        }
        writer.close();
        
    }
}
