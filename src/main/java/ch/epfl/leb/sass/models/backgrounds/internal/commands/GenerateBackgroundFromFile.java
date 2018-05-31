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
package ch.epfl.leb.sass.models.backgrounds.internal.commands;

import ch.epfl.leb.sass.models.backgrounds.BackgroundCommandBuilder;
import ch.epfl.leb.sass.models.backgrounds.BackgroundCommand;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.io.File;

/**
 * Constant overlay loaded from a tif image.
 * 
 * @author Marcel Stefko
 */
public final class GenerateBackgroundFromFile implements BackgroundCommand  {
    /**
     * The background image to be added.
     */
    private float pixels[][];
    
    /**
     * The number of pixels in x.
     */
    private final int nX;
    
    /**
     * The number of pixels in y.
     */
    private final int nY;
    
    public static class Builder implements BackgroundCommandBuilder {
        private File file;
        private int nX;
        private int nY;
        
        public Builder file(File file) { this.file = file; return this; }
        public Builder nX(int nX) { this.nX = nX; return this; }
        public Builder nY(int nY) { this.nY = nY; return this; }
        
        public GenerateBackgroundFromFile build() {
            return new GenerateBackgroundFromFile(this);
        }
    }
    
    /**
     * Load the background image by a file selection dialog
     * @param camera camera settings
     */
    /**
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
            throw new RuntimeException("Background image not selected");
        }
        File tif_file = fc.getSelectedFile();
        // load the file
        loadFile(tif_file);
        
        // verify that the image is larger or equal to camera resolution
        verifyDimensions(camera, pixels);
    }
    */
    
    /**
     * Load background image from specified file
     * @param camera camera settings
     * @param file tiff file to be loaded
     */
    private GenerateBackgroundFromFile(Builder builder) {
        this.nX = builder.nX;
        this.nY = builder.nY;
        loadFile(builder.file);
        verifyDimensions(pixels);
    }
    
    /**
     * Creates the background image.
     * @return The background image.
     */
    public float[][] generateBackground() {
        return pixels;
    }
    
    /**
     * Load the selected tif stack
     * @param file tif stack file
     */
    private void loadFile(File file) {
        Opener o = new Opener();
        ImagePlus win = o.openTiff(file.getParent().concat(File.separator),file.getName());
        ImageStack stack = win.getImageStack();
        ImageProcessor ip = stack.getProcessor(1);
        FloatProcessor fp = ip.convertToFloatProcessor();
        this.pixels = fp.getFloatArray();
    }
    
    /**
     * Check if image is big enough to cover the camera field of view.
     * 
     * Raises an exception if it is not large enough.
     * 
     * @param pixels image to be checked
     */
    private void verifyDimensions(float[][] pixels) {
        if (this.nX > pixels.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "Background image is too small!");
        }
        for (float[] row: pixels) {
            if (this.nY >row.length) {
                throw new ArrayIndexOutOfBoundsException(
                    "Background image is too small!");
            }
        }
    }
}
