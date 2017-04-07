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
package algorithm_tester.generators.realtime.obstructors;

import algorithm_tester.generators.realtime.Camera;
import algorithm_tester.generators.realtime.Obstructor;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Constant overlay loaded from an image
 * @author Marcel Stefko
 */
public class ConstantBackground implements Obstructor  {
    /**
     * Image which will be added to every image.
     */
    private float[][] pixels;
    
    /**
     * Load the background image by a file selection dialog
     * @param camera camera settings
     */
    public ConstantBackground(Camera camera) {
        // initialize array with zeros
        pixels = new float[camera.res_x][camera.res_y];
        zeroPixels();
        
        // choose file
        JFileChooser fc = new JFileChooser();
        int returnVal;
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        //set a default filename 
        fc.setSelectedFile(new File("background.tif"));
        //Set an extension filter
        fc.setFileFilter(new FileNameExtensionFilter("TIF image","tif"));
        returnVal = fc.showOpenDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            throw new RuntimeException("You need to select a background image!");
        }
        File tif_file = fc.getSelectedFile();
        // load the file
        loadFile(tif_file);
        
        // verify that the image is larger or equal to camera resolution
        verifyDimensions(camera, pixels);
    }
    
    /**
     * Load background image from specified file
     * @param camera camera settings
     * @param file tiff file to be loaded
     */
    public ConstantBackground(Camera camera, File file) {
        pixels = new float[camera.res_x][camera.res_y];
        zeroPixels();
        loadFile(file);
        verifyDimensions(camera, pixels);
    }
    /**
     * Set all pixels in source image to zero.
     */
    private void zeroPixels() {
        for (int row=0; row<pixels.length; row++) {
            for (int col=0; col<pixels[row].length; col++) {
                pixels[row][col] = 0.0f;
            }
        }
    }
    
    @Override
    public void applyTo(float[][] pixels) {
        for (int row=0; row<pixels.length; row++) {
            for (int col=0; col<pixels[row].length; col++) {
                pixels[row][col] += this.pixels[row][col];
            }
        }
    }
    
    /**
     * Load selected tif stack
     * @param file tif stack file
     */
    private void loadFile(File file) {
        Opener o = new Opener();
        ImagePlus win = o.openTiff(file.getParent().concat("\\"),file.getName());
        ImageStack stack = win.getImageStack();
        ImageProcessor ip = stack.getProcessor(1);
        FloatProcessor fp = ip.convertToFloatProcessor();
        pixels = fp.getFloatArray();
    }
    
    /**
     * Check if image is big enough to cover the camera field of view. Raises
     * an exception if it is not.
     * @param camera camera settings
     * @param pixels image to be checked
     */
    private void verifyDimensions(Camera camera, float[][] pixels) {
        if (camera.res_x>pixels.length) {
            throw new ArrayIndexOutOfBoundsException("Background image is too small!");
        }
        for (float[] row: pixels) {
            if (camera.res_y>row.length) {
                throw new ArrayIndexOutOfBoundsException("Background image is too small!");
            }
        }
    }
}
