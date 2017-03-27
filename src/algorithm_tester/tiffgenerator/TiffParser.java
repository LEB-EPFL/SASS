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
package algorithm_tester.tiffgenerator;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefko
 */
public class TiffParser {
    private File tiff_file;
    private ImageStack ram_stack;
    
    public final ImageStack loadGeneralTiff(File file) {
        // Open the tiff via ImageJ
        Logger.getLogger(TiffParser.class.getName()).log(Level.INFO, "Trying to open general tiff.");
        Opener o = new Opener();
        ImagePlus win = o.openTiff(file.getParent().concat("\\"),file.getName());
        ImageStack stack = win.getImageStack();
        
        int width = stack.getWidth();
        int height = stack.getHeight();
        int bytesPerPixel;
        if(stack.getPixels(win.getSlice()) instanceof short[]) {
            Logger.getLogger(TiffParser.class.getName()).log(Level.INFO, "Array is instance of short[]");
            bytesPerPixel = 2;
        } else {
            throw new ArrayStoreException("Wrong image bit depth.");
        }
        
        ram_stack = new ImageStack(width, height);
        


        for (int i=1; i<=stack.getSize(); i++) {
            ram_stack.addSlice(String.valueOf(i), stack.getPixels(i));
            // Log the process
            if (i%100==0) {
                System.out.format("Loaded %d images from general tiff stack.\n",i);
            }
        }
        return ram_stack;
    }
}
