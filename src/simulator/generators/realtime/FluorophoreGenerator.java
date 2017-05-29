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
package simulator.generators.realtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Randomly populates the field of view with fluorophores.
 * @author Marcel Stefko
 */
public class FluorophoreGenerator {

    /**
     * Randomly populate the field of view with fluorophores.
     * @param n_fluos number of emitters to be generated
     * @param cam camera properties
     * @param fluo fluorophore properties
     * @return
     */
    public static ArrayList<Fluorophore> generateFluorophoresRandom(int n_fluos, Camera cam, FluorophoreProperties fluo) {
        Random rnd = new Random();
        ArrayList<Fluorophore> result = new ArrayList<Fluorophore>();
        double x; double y;
        for (int i=0; i<n_fluos; i++) {
            x = cam.res_x*rnd.nextDouble();
            y = cam.res_y*rnd.nextDouble();
            result.add(fluo.createFluorophore(cam, x, y));
        }
        return result;
    }
    
    public static ArrayList<Fluorophore> generateFluorophoresGrid(int spacing, Camera cam, FluorophoreProperties fluo) {
        int limit_x = cam.res_x;
        int limit_y = cam.res_y;
        ArrayList<Fluorophore> result = new ArrayList<Fluorophore>();
        for (int i=spacing; i<limit_x; i+=spacing) {
            for (int j=spacing; j<limit_y; j+= spacing) {
                result.add(fluo.createFluorophore(cam, i, j));
            }
        }       
        return result;
    }
    
    /**
     * Parses fluorophore positions from csv file.
     * All lines which don't start with "#" have to contain at least 2 doubles,
     * which are interpreted as x and y positions in pixels.
     * @param file csv file, if null, a dialog is opened
     * @param camera camera settings
     * @param fluo fluorophore settings
     * @param rescale if true, positions are rescaled to fit into frame,
     *  otherwise positions outside of frame are cropped
     * @return list of fluorophores
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static ArrayList<Fluorophore> parseFluorophoresFromCsv(File file, Camera camera, FluorophoreProperties fluo, boolean rescale) throws FileNotFoundException, IOException {
        if (file==null) {
            file = getFileFromDialog();
        }
        
        // load all fluorophores
        ArrayList<Fluorophore> result = new ArrayList<Fluorophore>();
        double x; double y;
        BufferedReader br;
        String line;
        String splitBy = ",";
        br = new BufferedReader(new FileReader(file));
        // read all lines
        while ((line = br.readLine()) != null) {
            // skip comments
            if (line.startsWith("#")) {
                continue;
            }
            
            // read 2 doubles from beginning of line
            String[] entries = line.split(splitBy);
            x = Double.parseDouble(entries[0]);
            y = Double.parseDouble(entries[1]);
            // ignore ones with negative positions
            if (x>=0.0 && y>=0.0)
                result.add(fluo.createFluorophore(camera, x, y));
        }
        
        // rescale positions to fit into frame        
        if (rescale) {
            ArrayList<Fluorophore> result_rescaled = new ArrayList<Fluorophore>();
            double max_x_coord = 0.0;
            for (Fluorophore f: result) {
                if (f.x>max_x_coord)
                    max_x_coord = f.x;
            }
            double factor = camera.res_x/max_x_coord;
            for (Fluorophore f: result) {
                result_rescaled.add(fluo.createFluorophore(camera, f.x*factor, f.y*factor));
            }
            return result_rescaled;
        // or crop fluorophores outside of frame
        } else {
            ArrayList<Fluorophore> result_cropped = new ArrayList<Fluorophore>();
            for (Fluorophore f: result) {
                if (f.x < camera.res_x && f.y < camera.res_y) {
                    result_cropped.add(f);
                }
            }
            return result_cropped;
        }
    }
    
    private static File getFileFromDialog() {
        JFileChooser fc = new JFileChooser();
        int returnVal;
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        //set a default filename 
        fc.setSelectedFile(new File("emitters.csv"));
        //Set an extension filter
        fc.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));
        returnVal = fc.showOpenDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            throw new RuntimeException("You need to select an emitter file!");
        }
        return fc.getSelectedFile();
    }
}
