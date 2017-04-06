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
package algorithm_tester.generators.realtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
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
            result.add(new Fluorophore(fluo, cam, x, y));
        }
        return result;
    }
    
    public static ArrayList<Fluorophore> parseFluorophoresFromCsv(File file, Camera camera, FluorophoreProperties fluo, boolean rescale) throws FileNotFoundException, IOException {
        if (file==null) {
            file = getFileFromDialog();
        }
        
        ArrayList<Fluorophore> result = new ArrayList<Fluorophore>();
        double x; double y;
        BufferedReader br;
        String line = "";
        String splitBy = ",";
        br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            }
            String[] entries = line.split(splitBy);
            x = Double.parseDouble(entries[0]);
            y = Double.parseDouble(entries[1]);
            if (x>=0.0 && y>=0.0)
                result.add(new Fluorophore(fluo, camera, x, y));
        }
        
        if (rescale) {
            ArrayList<Fluorophore> result_rescaled = new ArrayList<Fluorophore>();
            double max_x_coord = 0.0;
            for (Fluorophore f: result) {
                if (f.x>max_x_coord)
                    max_x_coord = f.x;
            }
            double factor = camera.res_x/max_x_coord;
            for (Fluorophore f: result) {
                result_rescaled.add(new Fluorophore(fluo, camera, 
                        f.x*factor,
                        f.y*factor));
            }
            return result_rescaled;
        } else {
            return result;
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
