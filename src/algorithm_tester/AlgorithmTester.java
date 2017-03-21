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
import algorithm_tester.spotcounter.SpotCounter;
import ij.ImageStack;
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
    ImageStack stack;
    ArrayList<EvaluationAlgorithm> analyzers;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AlgorithmTester tester = new AlgorithmTester();
        tester.execute();
    }
    
    public void execute() {
        analyzers = new ArrayList<EvaluationAlgorithm>();
        
        addAnalyzer(new AutoLase());
        addAnalyzer(new SpotCounter());
        
        /*
        // File chooser dialog
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        // Set the selected file and show in label
        System.out.print("Loading selected .tif file.");
        File tiff_file = fc.getSelectedFile();
        /*/
        File tiff_file = new File("C:\\Users\\stefko\\Desktop\\sim400orig.tif");
        //*/
        
        loadTiff(tiff_file);
        System.out.format("Tif file loaded: %s\n",tiff_file.getAbsolutePath());
        for (EvaluationAlgorithm analyzer: analyzers) {
            System.out.format("Starting analyzer: %s\n",analyzer.getClass().getName());
            analyzer.setImageStack(stack);
            System.out.print("ImageStack set. Starting analysis...\n");
            System.out.format("Stack size: %d\n", stack.getSize());
            long time_start = System.currentTimeMillis();
            analyzer.processStack();
            long time_end = System.currentTimeMillis();
            System.out.format("Analysis finished in %d ms.\n",time_end - time_start);
        }
        
        /*
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        //set a default filename (this is where you default extension first comes in)
        fc.setSelectedFile(new File("output.csv"));
        //Set an extension filter, so the user sees other XML files
        fc.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));
        returnVal = fc.showSaveDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        saveToCsv(fc.getSelectedFile());
        /*/
        saveToCsv(new File("C:\\Users\\stefko\\Desktop\\output.csv"));
        //*/
        
    }
    
    public void addAnalyzer(EvaluationAlgorithm analyzer) {
        analyzers.add(analyzer);
    }
    
    private void loadTiff(final File file) {
        final TiffParser parser = new TiffParser();
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                stack = parser.loadGeneralTiff(file);
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(AlgorithmTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveToCsv(File file) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlgorithmTester.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        HashMap<String, Double> output_map;
        
        // Print header
        writer.println("#Columns:");
        String analyzer_names = "frame_id,";
        for (EvaluationAlgorithm analyzer: analyzers) {
            output_map = analyzer.getOutputValues(0);
                for (String key: output_map.keySet()) {
                    analyzer_names = analyzer_names.concat(",").
                    concat(analyzer.getName()).concat(":").concat(key);
                }
        }
        writer.println(analyzer_names);
        
        // Print data
        for (int i=0; i<stack.getSize(); i++) {
            String s = String.format("%d",i+1);
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
