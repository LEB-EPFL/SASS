/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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
package ch.epfl.leb.sass.utils.images.internal;

import ij.ImageStack;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.io.FileSaver;
import java.io.File;
import java.nio.ByteBuffer;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;

/**
 * The default implementation of the ImageS interface.
 * 
 * The default implementation currently wraps ImageJ1's ImageStack class. See
 * https://imagej.nih.gov/ij/developer/api/ij/ImagePlus.html for more
 * information.
 * 
 * @author Kyle M. Douglass
 */
public class DefaultImageS implements ImageS {
    
    private ImagePlus imp;
    private ImageStack images;
    private String title = "SASS Image Dataset";
    private final int BITDEPTH = 16;
    
    /**
     * Creates a new and empty DefaultImageS.
     */
    public DefaultImageS(int width, int height) {
        images = new ImageStack(width, height);
    }
    
    /**
     * Creates a new DefaultImageS object from a 2D array of ints.
     * 
     * The first index of the input array should correspond to x; the second
     * corresponds to y.
     * 
     * @param pixels The 2D array of pixel values.
     */
    public DefaultImageS(int[][] pixels) {
        images = new ImageStack(pixels.length, pixels[0].length);
        
        FloatProcessor fp = new FloatProcessor(pixels);
        images.addSlice(fp.convertToShortProcessor(false));
        imp = new ImagePlus(title, images);
    }
    
    /**
     * Creates a new DefaultImageS object from a 2D array of floats.
     * 
     * The first index of the input array should correspond to x; the second
     * corresponds to y.
     * 
     * @param pixels The 2D array of pixel values.
     */
    public DefaultImageS(float[][] pixels) {
        images = new ImageStack(pixels.length, pixels[0].length);
        
        FloatProcessor fp = new FloatProcessor(pixels);
        images.addSlice(fp.convertToShortProcessor(false));
        imp = new ImagePlus(title, images);
    }
    
    @Override
    public int getBitDepth() {
        if (images.getSize() == 0) {
            return BITDEPTH;
        }
        return images.getBitDepth();
    }

    /**
     * Returns the title of the image stack.
     * 
     * @return The title of the image stack.
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the image stack.
     * 
     * @param title The title of the image stack.
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Serializes the image stack to a TIFF-encoded byte array.
     * 
     * @return A TIFF-encoded byte array. 
     */
    @Override
    public byte[] serializeToArray() {
        if (imp == null) {
            // The ImagePlus was not created in the constructor.
            imp = new ImagePlus(title, images);
        }
        
        FileSaver fs = new FileSaver(imp);
        return fs.serialize();
    }

    /**
     * Returns a buffer containing the dataset in a TIFF-encoded byte array.
     * 
     * @return A buffer containing the dataset in a TIFF-encoded byte array.
     */
    @Override
    public ByteBuffer serializeToBuffer() {
        return ByteBuffer.wrap(serializeToArray());
    }

    /**
     * Adds a 2D array of shorts to the dataset.
     * 
     * @param image A 2D array of shorts.
     */
    @Override
    public void addImage(short[][] image) throws ImageShapeException {
        checkSize(image.length, image[0].length);
        
        short[] flattened = new short[image.length * image[0].length];
        int s = 0;
        
        // Note that ImageJ1 flattens arrays according to column-row order, i.e.
        // the slowest changing variable is the column, not the row!
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                flattened[s] = image[j][i];
                s++;
            }
        }
        
        images.addSlice("", flattened);
    }
    
    /**
     * Converts a 2D array of ints to 16-bit shorts and adds it to the dataset.
     * 
     * @param image A 2D array of ints indexed by xy.
     * @throws ch.epfl.leb.sass.utils.images.ImageShapeException
     */
    @Override
    public void addImage(int[][] image) throws ImageShapeException {
        checkSize(image.length, image[0].length);
        images.addSlice(( new FloatProcessor(image) ).convertToShortProcessor(false)); 
    }
    
    /**
     * Converts a 2D array of floats to 16-bit shorts and adds it to the dataset.
     * 
     * @param image A 2D array of floats indexed by xy.
     * @throws ch.epfl.leb.sass.utils.images.ImageShapeException
     */
    @Override
    public void addImage(float[][] image) throws ImageShapeException {
        checkSize(image.length, image[0].length);
        images.addSlice(( new FloatProcessor(image) ).convertToShortProcessor(false));
    }
    
    /**
     * Appends another ImageS dataset to the end of this one.
     * 
     * @param dataset The images to add to the dataset.
     */
    @Override
    public void concatenate(ImageS dataset) throws ImageShapeException {
        checkSize(dataset.getWidth(), dataset.getHeight());
        
        for (int i = 0; i < dataset.getSize(); i++) {
            short[] pixels = dataset.getPixelData(i);
            images.addSlice("", pixels);
        }
    }
    
    /**
     * Returns the pixel data at the given index as a 1D array.
     * 
     * @param index The index of the corresponding slice.
     * @return The pixel data at the provided index.
     */
    @Override
    public short[] getPixelData(int index) {
        // ImageJ1 is 1-indexed and follows xy order, where x changes quickest.
        return (short[])images.getPixels(index + 1);
    }
    
    /**
     * Displays the images in a ImagePlus window.
     * 
     */
    @Override
    public void view() {
        if (imp == null) {
            // The ImagePlus was not created in the constructor.
            imp = new ImagePlus(title, images);
        }
            imp.show();
    }
    
    /**
     * Updates the dataset viewer to show the currently active slice.
     * 
     */
    @Override
    public void updateView() {
        if (imp == null) {
            // The ImagePlus was not created in the constructor.
            imp = new ImagePlus(title, images);
        }
        imp.updateAndRepaintWindow();
    }
    
    /**
     * Gets the active slice of the dataset (0-indexed).
     * 
     * This is the image that will be displayed in the viewer.
     * 
     * @return The index of the active slice.
     */
    @Override
    public int getSlice() {
        if (imp == null) {
            // The ImagePlus was not created in the constructor.
            imp = new ImagePlus(title, images);
        }
        return imp.getSlice();
    }
    
    /**
     * Sets the active slice of the dataset (0-indexed).
     * 
     * This is the image that will be displayed in the viewer.
     * 
     * @param index The index of the slice to activate.
     */
    @Override
    public void setSlice(int index) {
        if (imp == null) {
            // The ImagePlus was not created in the constructor.
            imp = new ImagePlus(title, images);
        }
        imp.setSlice(index);
    }
    
    /**
     * Saves the images to a TIFF file.
     * 
     */
    @Override
    public void saveAsTiffStack(File file) throws IllegalArgumentException {
        if (imp == null) {
            // The ImagePlus was not created in the constructor.
            imp = new ImagePlus(title, images);
        }
        
        FileSaver fs = new FileSaver(imp);
        fs.saveAsTiffStack(file.getAbsolutePath());
    }
    
    /**
     * 
     * Returns the width of the images in the dataset.
     * 
     * @return The width of the images in the dataset.
     */
    @Override
    public int getWidth() {
        return images.getWidth();
    }
    
     /**
     * 
     * Returns the height of the images in the dataset.
     * 
     * @return The height of the images in the dataset.
     */
    @Override
    public int getHeight() {
        return images.getHeight();
    }
    
    /**
     * Returns the number of images in the dataset.
     * 
     * @return The number of images in the dataset.
     */
    @Override
    public int getSize() {
        return images.getSize();
    }
    
    /**
     * Verify that the size of the array matches the size of the dataset.
     * 
     * @param width The width of the input array.
     * @param height The height of the input array.
     */
    private void checkSize(int width, int height) throws ImageShapeException {
        if ( (width != this.getWidth()) || (height != this.getHeight()) ) {
            throw new ImageShapeException(
                    "Error: trying to add two ImageS datasets with different" +
                    " widths and/or heights."
            );
        }
    }
    
}
