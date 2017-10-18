/* 
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
package ch.epfl.leb.sass.simulator.generators.realtime;

import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.SimpleProperties;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSF;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Populates a field of view with fluorophores.
 * 
 * The FluorophoreGenerator contains a number of methods for creating actual
 * fluorophore instances and in different arrangements, such as placing them on
 * a grid, randomly distributing them in the FOV, and placing them according to
 * input from a text file.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public class FluorophoreGenerator {

    /**
     * Randomly populate the field of view with fluorophores.
     * @param n_fluos number of emitters to be generated
     * @param cam camera properties
     * @param fluo fluorophore properties
     * @return
     * @deprecated Use {@link #generateFluorophoresRandom2D(int, int, int, ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSF, ch.epfl.leb.sass.simulator.generators.realtime.FluorophoreProperties)}
     *           instead.
     */
    @Deprecated
    public static ArrayList<Fluorophore> generateFluorophoresRandom(int n_fluos, Camera cam, FluorophoreProperties fluo) {
        Random rnd = RNG.getUniformGenerator();
        ArrayList<Fluorophore> result = new ArrayList<Fluorophore>();
        double x; double y;
        for (int i=0; i<n_fluos; i++) {
            x = cam.res_x*rnd.nextDouble();
            y = cam.res_y*rnd.nextDouble();
            result.add(fluo.createFluorophore(cam, x, y));
        }
        return result;
    }
    
    /**
     * Randomly populate the field of view with fluorophores.
     * 
     * @param numFluors The number of fluorophores to add to the field of view.
     * @param camera The camera for determining the size of the field of view.
     * @param psf The PSF of the microscope.
     * @param fluorProp The fluorophore dynamics properties.
     * @return The list of fluorophores.
     */
    public static ArrayList<Fluorophore> generateFluorophoresRandom2D(int numFluors, Camera camera, PSF psf, FluorophoreProperties fluorProp) {
        Random rnd = RNG.getUniformGenerator();
        ArrayList<Fluorophore> result = new ArrayList();
        double x;
        double y;
        double z = 0;
        for (int i=0; i < numFluors; i++) {
            x = camera.res_x*rnd.nextDouble();
            y = camera.res_y*rnd.nextDouble();
            result.add(fluorProp.newFluorophore(psf, x, y, z));
        }
        return result;
    }
    
    /**
     * Generate a rectangular grid of fluorophores
     * @param spacing distance between nearest neighbors
     * @param cam Camera
     * @param fluo type of fluorophore
     * @return
     */
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
    
    public static ArrayList<Fluorophore3D> generate3DFluorophoresGrid(int spacing, Camera cam, FluorophoreProperties fluo) {
        int limit_x = cam.res_x;
        int limit_y = cam.res_y;
        ArrayList<Fluorophore3D> result = new ArrayList<Fluorophore3D>();
        for (int i=spacing; i<limit_x; i+=spacing) {
            for (int j=spacing; j<limit_y; j+= spacing) {
                result.add(fluo.createFluorophore3D(cam, i, j, ((-limit_y/2)+j)/100.0));
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
                // we subtract 0.5 to make the positions agree with how ThunderSTORM computes positions
                // i.e. origin is in the very top left of image, not in the center of top left pixel as it is in our simulation
                result.add(fluo.createFluorophore(camera, x-0.5, y-0.5));
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
    
    /**
     * Parses moving fluorophores and their trajectories from a csv file.
     * CSV file column format:
     *  emitter_no[-],frame_no[-],x[px],y[px]
     * Frame and emitter numbers must be strictly increasing, but don't have to 
     * be consecutive (gaps in frame numbers are interpolated).
     * @param file csv file, if null, a dialog is opened
     * @param camera camera settings
     * @param fluo moving fluorophore settings
     * @return list of fluorophores
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static ArrayList<MovingFluorophore> parseMovingFluorophoresFromCsv(File file, Camera camera, SimpleProperties fluo) throws IOException {
        if (file==null) {
            file = getFileFromDialog();
        }
        
        final ArrayList<MovingFluorophore> result = new ArrayList<MovingFluorophore>();;
        final BufferedReader br;
        String line;
        final String splitBy = ",";
        br = new BufferedReader(new FileReader(file));
        
        // aggregator variables
        int trajectory_no = -1;
        int expected_frame_no = 1;
        ArrayList<Point2D.Double> current_trajectory = new ArrayList<Point2D.Double>();
        double initial_x = 0, initial_y = 0;
        // read all lines
        
        
        while ((line = br.readLine()) != null) {
            // skip comments
            if (line.startsWith("#")) {
                continue;
            }
            
            // read 2 integers and 2 doubles from beginning of line
            final String[] entries = line.split(splitBy);
            final int new_trajectory_no = Integer.parseInt(entries[0]);
            final int new_frame_no = Integer.parseInt(entries[1]);
            final double x = Double.parseDouble(entries[2]);
            final double y = Double.parseDouble(entries[3]);
            
            // parse first trajectory number on first read line
            if (trajectory_no == -1) {
                trajectory_no = new_trajectory_no;
                initial_x = x;
                initial_y = y;
                expected_frame_no = 1;
            }
            
            // read the trajectory number and ensure we are still on the same
            // trajectory, if not, finalize this trajectory and create next one
            if (new_trajectory_no != trajectory_no) {
                trajectory_no = new_trajectory_no;
                result.add(fluo.createMovingFluorophore(camera, current_trajectory.get(0).x, current_trajectory.get(0).y, current_trajectory));
                current_trajectory = new ArrayList<Point2D.Double>();
                initial_x = x;
                initial_y = y;
                expected_frame_no = 1;
            }
            
            if (new_frame_no < expected_frame_no) {
                throw new RuntimeException("Error in parsing of frame numbers!");
            } else if (new_frame_no == expected_frame_no) {
                // add point to current trajectory
                current_trajectory.add(new Point2D.Double(x, y));
            } else {
                int frame_gap_size = new_frame_no - expected_frame_no + 1;
                
                // fill in the gap between frames via weighted average
                for (int i=1; i<frame_gap_size; i++) {
                    current_trajectory.add(new Point2D.Double(
                        (i*x + (frame_gap_size-i)*initial_x) / frame_gap_size, 
                        (i*y + (frame_gap_size-i)*initial_y) / frame_gap_size));
                }
                // add the actual frame
                current_trajectory.add(new Point2D.Double(x, y));
            }
            initial_x = x;
            initial_y = y;
            expected_frame_no = new_frame_no + 1;
        }
        // add last fluorophore
        result.add(fluo.createMovingFluorophore(camera, current_trajectory.get(0).x, current_trajectory.get(0).y, current_trajectory));
        return result;
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
