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
 *
 * @author stefko
 */
public class ConstantBackground implements Obstructor  {
    private float[][] pixels;
    
    public ConstantBackground(Camera camera) {
        pixels = new float[camera.res_x][camera.res_y];
        zeroPixels();
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
        loadFile(tif_file);
        verifyDimensions(camera, pixels);
    }
    
    public ConstantBackground(Camera camera, File file) {
        pixels = new float[camera.res_x][camera.res_y];
        zeroPixels();
        loadFile(file);
        verifyDimensions(camera, pixels);
    }
    
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
    
    private void loadFile(File file) {
        Opener o = new Opener();
        ImagePlus win = o.openTiff(file.getParent().concat("\\"),file.getName());
        ImageStack stack = win.getImageStack();
        ImageProcessor ip = stack.getProcessor(1);
        FloatProcessor fp = ip.convertToFloatProcessor();
        pixels = fp.getFloatArray();
    }
    
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
