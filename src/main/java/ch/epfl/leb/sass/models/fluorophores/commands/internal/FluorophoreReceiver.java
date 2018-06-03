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
package ch.epfl.leb.sass.models.fluorophores.commands.internal;

import ch.epfl.leb.sass.utils.RNG;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.components.Laser;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.models.fluorophores.internal.DefaultFluorophore;
import ch.epfl.leb.sass.models.illuminations.Illumination;
import ch.epfl.leb.sass.models.illuminations.internal.SquareUniformIllumination;
import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamics;
import ch.epfl.leb.sass.models.samples.RefractiveIndex;
import ch.epfl.leb.sass.models.samples.internal.UniformRefractiveIndex;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFileChooser;
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
public class FluorophoreReceiver {
    
    /**
     * The polarization of the electric field.
     * 
     * TODO: Remove this once functionality is added for fluorophore building.
     */
    private static Vector3D orientation = new Vector3D(1.0, 0.0, 0.0);
    
    /**
     * A uniform refractive index throughout the sample.
     * 
     * TODO: Remove this once functionality is added for fluorophore building.
     */
    private static RefractiveIndex refractiveIndex
            = new UniformRefractiveIndex(new Complex(1.33));
    
    /**
     * Randomly populate the field of view with fluorophores.
     * 
     * @param numFluors The number of fluorophores to add to the field of view.
     * @param camera The camera for determining the size of the field of view.
     * @param laser The laser that illuminates the fluorophores.
     * @param psfBuilder Builder for calculating microscope PSFs.
     * @param fluorDynamics The fluorophore dynamics properties.
     * @return The list of fluorophores.
     */
    public static ArrayList<Fluorophore> generateFluorophoresRandom2D(
            int numFluors,
            Camera camera,
            Laser laser,
            PSFBuilder psfBuilder,
            FluorophoreDynamics fluorDynamics) {
        Random rnd = RNG.getUniformGenerator();
        ArrayList<Fluorophore> result = new ArrayList();
        double x;
        double y;
        double z = 0;
        Fluorophore fluorophore;
        
        double sampleSpaceWidth = camera.getNX() * camera.getPixelSize();
        double sampleSpaceHeight = camera.getNY() * camera.getPixelSize();
        Illumination illumination = new SquareUniformIllumination(
                laser.getPower(), sampleSpaceWidth, sampleSpaceHeight, orientation,
                laser.getWavelength(), refractiveIndex);
        laser.addListener(illumination);
        
        for (int i=0; i < numFluors; i++) {
            x = camera.getNX() * rnd.nextDouble();
            y = camera.getNY() * rnd.nextDouble();
            fluorophore = new DefaultFluorophore(
                psfBuilder,
                illumination,
                fluorDynamics.getSignal(),
                fluorDynamics.getStateSystem(),
                fluorDynamics.getStartingState(),
                x, y, z);
            result.add(fluorophore);
            illumination.addListener(fluorophore.getIlluminationListener());
        }
        return result;
    }
    
    /**
     * Randomly populate the field of view with fluorophores in three dimensions.
     * 
     * @param numFluors The number of fluorophores to add to the field of view.
     * @param zLow The lower bound on the range in z in units of pixels
     * @param zHigh The upper bound on the range in z in units of pixels
     * @param camera The camera for determining the size of the field of view.
     * @param laser The laser that illuminates the fluorophores.
     * @param psfBuilder Builder for calculating microscope PSFs.
     * @param fluorDynamics The fluorophore dynamics properties.
     * @return The list of fluorophores.
     */
    public static ArrayList<Fluorophore> generateFluorophoresRandom3D(
            int numFluors,
            double zLow,
            double zHigh,
            Camera camera,
            Laser laser,
            PSFBuilder psfBuilder,
            FluorophoreDynamics fluorDynamics) {
        Random rnd = RNG.getUniformGenerator();
        ArrayList<Fluorophore> result = new ArrayList();
        double x;
        double y;
        double z;
        Fluorophore fluorophore;
        
        double sampleSpaceWidth = camera.getNX() * camera.getPixelSize();
        double sampleSpaceHeight = camera.getNY() * camera.getPixelSize();
        Illumination illumination = new SquareUniformIllumination(
                laser.getPower(), sampleSpaceWidth, sampleSpaceHeight, orientation,
                laser.getWavelength(), refractiveIndex);
        laser.addListener(illumination);
        
        for (int i=0; i < numFluors; i++) {
            x = camera.getNX() * rnd.nextDouble();
            y = camera.getNY() * rnd.nextDouble();
            z = (zHigh - zLow) * rnd.nextDouble() + zLow;
            fluorophore = new DefaultFluorophore(
                psfBuilder,
                illumination,
                fluorDynamics.getSignal(),
                fluorDynamics.getStateSystem(),
                fluorDynamics.getStartingState(),
                x, y, z);
            result.add(fluorophore);
            illumination.addListener(fluorophore.getIlluminationListener());
        }
        return result;
    }
    
    /**
     * Generate a rectangular grid of fluorophores.
     * 
     * @param spacing The distance along the grid between nearest neighbors.
     * @param camera The camera for determining the size of the field of view.
     * @param laser The laser that illuminates the fluorophores.
     * @param psfBuilder Builder for calculating microscope PSFs.
     * @param fluorDynamics The fluorophore dynamics properties.
     * @return The list of fluorophores.
     */
    public static ArrayList<Fluorophore> generateFluorophoresGrid2D(
            int spacing,
            Camera camera,
            Laser laser,
            PSFBuilder psfBuilder,
            FluorophoreDynamics fluorDynamics) {
        int limitX = camera.getNX();
        int limitY = camera.getNY();
        double z = 0.0;
        ArrayList<Fluorophore> result = new ArrayList();
        Fluorophore fluorophore; 
        
        double sampleSpaceWidth = camera.getNX() * camera.getPixelSize();
        double sampleSpaceHeight = camera.getNY() * camera.getPixelSize();
        Illumination illumination = new SquareUniformIllumination(
                laser.getPower(), sampleSpaceWidth, sampleSpaceHeight, orientation,
                laser.getWavelength(), refractiveIndex);
        laser.addListener(illumination);
        
        for (int i=spacing; i < limitX; i+=spacing) {
            for (int j=spacing; j < limitY; j+= spacing) {
                fluorophore = new DefaultFluorophore(
                    psfBuilder,
                    illumination,
                    fluorDynamics.getSignal(),
                    fluorDynamics.getStateSystem(),
                    fluorDynamics.getStartingState(),
                    i, j, z);
                result.add(fluorophore);
                illumination.addListener(fluorophore.getIlluminationListener());
            }
        }       
        return result;
    }
    
    /**
     * Create fluorophores on a 2D grid and step-wise in the axial direction.
     * 
     * @param spacing The distance along the grid between nearest neighbors.
     * @param zLow The lower bound on the range in z in units of pixels.
     * @param zHigh The upper bound on the range in z in units of pixels.
     * @param camera The camera for determining the size of the field of view.
     * @param laser The laser that illuminates the fluorophores.
     * @param psfBuilder Builder for calculating microscope PSFs.
     * @param fluorDynamics The fluorophore dynamics properties.
     * @return The list of fluorophores.
     */
    public static ArrayList<Fluorophore> generateFluorophoresGrid3D(
            int spacing,
            double zLow,
            double zHigh,
            Camera camera,
            Laser laser,
            PSFBuilder psfBuilder,
            FluorophoreDynamics fluorDynamics) {
        int limitX = camera.getNX();
        int limitY = camera.getNY();
        double numFluors = ((double) limitX - spacing) * ((double) limitY - spacing) / spacing / spacing;
        double zSpacing  = (zHigh - zLow) / (numFluors - 1);
        double z = zLow;
        
        ArrayList<Fluorophore> result = new ArrayList();
        Fluorophore fluorophore;
        
        double sampleSpaceWidth = camera.getNX() * camera.getPixelSize();
        double sampleSpaceHeight = camera.getNY() * camera.getPixelSize();
        Illumination illumination = new SquareUniformIllumination(
                laser.getPower(), sampleSpaceWidth, sampleSpaceHeight, orientation,
                laser.getWavelength(), refractiveIndex);
        laser.addListener(illumination);
        
        for (int i = spacing; i < limitX; i += spacing) {
            for (int j = spacing; j < limitY; j += spacing) {
                fluorophore = new DefaultFluorophore(
                    psfBuilder,
                    illumination,
                    fluorDynamics.getSignal(),
                    fluorDynamics.getStateSystem(),
                    fluorDynamics.getStartingState(),
                    i, j, z);
                result.add(fluorophore);
                illumination.addListener(fluorophore.getIlluminationListener());
                z += zSpacing;
            }
        }       
        return result;
    }
    
    /**
     * Parse a CSV file and generate fluorophores from it.
     * 
     * @param file The CSV file. If this is null, then a dialog is opened.
     * @param camera The camera for determining the size of the field of view.
     * @param laser The laser that illuminates the fluorophores.
     * @param psfBuilder Builder for calculating microscope PSFs.
     * @param fluorDynamics The fluorophore dynamics properties.
     * @param rescale if true, positions are rescaled to fit into frame,
     *  otherwise positions outside of frame are cropped
     * @return list of fluorophores.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static ArrayList<Fluorophore> generateFluorophoresFromCSV(
            File file,
            Camera camera,
            Laser laser,
            PSFBuilder psfBuilder,
            FluorophoreDynamics fluorDynamics,
            boolean rescale) throws FileNotFoundException, IOException {
        if (file==null) {
            file = getFileFromDialog();
        }
        
        System.out.println("Building fluorophore PSF's...");
        
        // load all fluorophores
        ArrayList<Fluorophore> result = new ArrayList();
        double x;
        double y;
        double z;
        int counter = 0;
        Fluorophore fluorophore;
        
        double sampleSpaceWidth = camera.getNX() * camera.getPixelSize();
        double sampleSpaceHeight = camera.getNY() * camera.getPixelSize();
        Illumination illumination = new SquareUniformIllumination(
                laser.getPower(), sampleSpaceWidth, sampleSpaceHeight, orientation,
                laser.getWavelength(), refractiveIndex);
        laser.addListener(illumination);
        
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
            
            try {
                z = Double.parseDouble(entries[2]);
            } catch (ArrayIndexOutOfBoundsException ex){
                // There is no z-column, so set the z-position to 0.0.
                z = 0.0;
            }
            // Ignore entries with negative x- and y-positions.
            if (x>=0.0 && y>=0.0) {
                
                // we subtract 0.5 to make the positions agree with how ThunderSTORM computes positions
                // i.e. origin is in the very top left of image, not in the center of top left pixel as it is in our simulation
                fluorophore = new DefaultFluorophore(
                    psfBuilder,
                    illumination,
                    fluorDynamics.getSignal(),
                    fluorDynamics.getStateSystem(),
                    fluorDynamics.getStartingState(),
                    x - 0.5, y - 0.5, z);
                result.add(fluorophore);
                illumination.addListener(fluorophore.getIlluminationListener());
                
                counter++;
                if (counter % 5000 == 0) {
                    System.out.println("Processed fluorophore number: " + counter);
                }
            }
        }
        
        System.out.println("Done building PSF's.");
        
        // rescale positions to fit into frame        
        if (rescale) {
            System.out.println("Building rescaled PSF's....");
            ArrayList<Fluorophore> result_rescaled = new ArrayList<Fluorophore>();
            double max_x_coord = 0.0;
            for (Fluorophore f: result) {
                // Remove the fluorophores that were added in the first step.
                illumination.deleteListener(f.getIlluminationListener());
                if (f.getX() > max_x_coord)
                    max_x_coord = f.getX();
            }
            double factor = camera.getNX()/max_x_coord;
            for (Fluorophore f: result) {
                fluorophore = new DefaultFluorophore(
                    psfBuilder,
                    illumination,
                    fluorDynamics.getSignal(),
                    fluorDynamics.getStateSystem(),
                    fluorDynamics.getStartingState(),
                    f.getX() * factor,
                    f.getY() * factor,
                    f.getZ() * factor);
                result_rescaled.add(fluorophore);
                illumination.addListener(fluorophore.getIlluminationListener());
            }
            
            System.out.println("Done building rescaled PSF's.");
            return result_rescaled;
        // or crop fluorophores outside of frame
        } else {
            ArrayList<Fluorophore> result_cropped = new ArrayList<Fluorophore>();
            for (Fluorophore f: result) {
                // Remove the fluorophores that were added in the first step.
                illumination.deleteListener(f.getIlluminationListener());
                if (f.getX() < camera.getNX() && f.getY() < camera.getNY()) {
                    result_cropped.add(f);
                    illumination.addListener(f.getIlluminationListener());
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
