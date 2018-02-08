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

/**
 * The default implementation of the ImageS interface.
 * 
 * The default implementation currently wraps ImageJ1's ImageStack class. See
 * https://imagej.nih.gov/ij/developer/api/ij/ImagePlus.html for more
 * information.
 * 
 * @author Kyle M. Douglass
 */
public class DefaultImageS implements ImageS<Short> {
    
    private ImagePlus imp;
    private ImageStack images;
    private String title = "SASS Image Dataset";
    
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
        ImagePlus imp = new ImagePlus(title, images);
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
     * Converts a 2D array of ints to 16-bit ints and adds it to the dataset.
     * 
     * @param image A 2D array of ints indexed by xy.
     */
    @Override
    public void addImage(int[][] image) {
        images.addSlice(( new FloatProcessor(image) ).convertToShortProcessor(false));
        
    }
    
    /**
     * Converts a 2D array of floats to 16-bit ints and adds it to the dataset.
     * 
     * @param image A 2D array of floats indexed by xy.
     */
    @Override
    public void addImage(float[][] image) {
        images.addSlice(( new FloatProcessor(image) ).convertToShortProcessor(false));
    }
    
    /**
     * Appends another ImageS dataset to the end of this one.
     * 
     * @param dataset The images to add to the dataset.
     */
    @Override
    public void concatenate(ImageS<Short> dataset) {
        short[] prim = new short[dataset.getWidth() * dataset.getHeight()];
        
        // ImageJ1 *requires* a short array and Java 8 streams do not support
        // Short.
        for (int i = 0; i < dataset.getSize(); i++) {
            Short[] pixels = dataset.getPixelData(i);
            for (int j = 0; j < pixels.length; j++) {
                prim[j] = (short)pixels[j];
            }
            images.addSlice("", prim);
        }
    }
    
    /**
     * Returns the pixel data at the given index as a 1D array.
     * 
     * @param index The index of the corresponding slice.
     * @return The pixel data at the provided index.
     */
    @Override
    public Short[] getPixelData(int index) {
        // ImageJ1 is 1-indexed and follows xy order, where x changes quickest.
        short[] temp = (short[])images.getPixels(index + 1);
        Short[] pixels = new Short[temp.length];
        for (int i = 0; i < temp.length; i++) {
            pixels[i] = new Short(temp[i]);
        }
        
        return pixels;
        //return Arrays.stream(temp).boxed().toArray(Short[]::new);
    }
    
    /**
     * Returns the pixel data at the given index as a 1D array of primitives.
     * 
     * @param index The index of the corresponding slice.
     * @return The pixel data at the provided index as a primitive data type.
     */
    @Override
    public Object getPixelDataPrimitive(int index) {
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
    public void updateView() {
        imp.updateAndRepaintWindow();
    }
    
    /**
     * Sets the active slice of the dataset (0-indexed).
     * 
     * This is the image that will be displayed in the viewer.
     * 
     * @param index The index of the slice to activate.
     */
    public void setSlice(int index) {
        imp.setSlice(index);
    }
    
    /**
     * Saves the images to a TIFF file.
     * 
     */
    @Override
    public void saveAsTiffStack(File file) {
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
    
}
