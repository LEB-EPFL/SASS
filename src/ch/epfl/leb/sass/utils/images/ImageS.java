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
package ch.epfl.leb.sass.utils.images;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * An abstraction layer for an 3-dimensional image stack in SASS.
 * 
 * This interface allows developers to more easily substitute other backends for
 * image data into SASS. For example, one could write an implementation for
 * ImgLib2 datatypes to replace ImageJ's original ImageStack.
 * 
 * This interface should be used everywhere image data is passed between SASS
 * components. It represents a general purpose, 3-dimensional image stack.
 * 
 * @param <T> The datatype of the pixels.
 * @author Kyle M. Douglass
 */
public interface ImageS<T>{
    /**
     * Returns the title (or, equivalently, the name) of the image dataset.
     * 
     * @return The title of the dataset.
     */
    public String getTitle();
    
    /**
     * Sets the title (or, equivalently, the name) of the dataset.
     * 
     * @param title The title to give to the image dataset.
     */
    public void setTitle(String title);
    
    /**
     * Returns the bit depth of the pixels.
     * 
     * @return The bit depth of the pixels.
     */
    public int getBitDepth();
    
    /**
     * Serializes the dataset into a TIFF-encoded byte array.
     * 
     * @return The image data encoded as a TIFF-file byte string.
     */
    public byte[] serializeToArray();
    
    /**
     * Returns a buffer containing the dataset in a TIFF-encoded byte array.
     * 
     * @return A ByteBuffer containing the TIFF-encoded dataset.
     */
    public ByteBuffer serializeToBuffer();
    
    /**
     * Adds a single image to the dataset.
     * 
     * This method accepts a 2D array of pixels where the indexes are in XY
     * order. The length of the array in X and Y must equal the lengths of the
     * existing images.
     * 
     * @param image The image data to add to the dataset.
     */
    public void addImage(int[][] image);
    
    /**
     * Adds a single image to the dataset.
     * 
     * This method accepts a 2D array of pixels where the indexes are in XY
     * order. The length of the array in X and Y must equal the lengths of the
     * existing images.
     * 
     * @param image The image data to add to the dataset.
     */
    public void addImage(float[][] image);
    
    /**
     * Appends another ImageS dataset to the end of this one.
     * 
     * @param dataset The images to add to the dataset.
     */
    public void concatenate(ImageS<T> dataset);
    
    /**
     * Returns the image data at the slice corresponding to index.
     * 
     * @param index
     * @return 
     */
    public T[] getPixelData(int index);
    
    /**
     * Returns the pixel data at the given index as a 1D array of primitives.
     * 
     * This method will break the abstraction because the program will need to
     * know the underlying datatype of the image data. It is provided primarily
     * for backwards compatability and it is not recommended.
     * 
     * @param index The index of the corresponding slice.
     * @return The pixel data at the provided index.
     */
    public Object getPixelDataPrimitive(int index);
    
    /**
     * Displays the images.
     * 
     */
    public void view();
    
    /**
     * Updates the dataset viewer to show the currently active slice.
     * 
     */
    public void updateView();
    
    /**
     * Sets the active slice of the dataset (0-indexed).
     * 
     * * This is the image that will be displayed in the viewer.
     * 
     * @param index The index of the slice to activate.
     */
    public void setSlice(int index);
    
    /**
     * Saves the images to a TIFF file.
     * 
     * @param file The TIFF file where the dataset will be saved.
     */
    public void saveAsTiffStack(File file);
    
    /**
     * 
     * Returns the width of the images in the dataset.
     * 
     * @return The width of the images in the dataset.
     */
    public int getWidth();
    
     /**
     * 
     * Returns the height of the images in the dataset.
     * 
     * @return The height of the images in the dataset.
     */
    public int getHeight();
    
    /**
     * Returns the number of images in the dataset.
     * 
     * @return The number of images in the dataset.
     */
    public int getSize();
    
}