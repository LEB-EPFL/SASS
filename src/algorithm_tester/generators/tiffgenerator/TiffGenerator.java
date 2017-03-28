/*
 * Copyright (C) 2017 stefko
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
package algorithm_tester.generators.tiffgenerator;

import algorithm_tester.ImageGenerator;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author stefko
 */
public class TiffGenerator implements ImageGenerator {
    private ImageStack stack;
    private int count;
    
    public TiffGenerator(File file) {
        TiffParser parser = new TiffParser();
        System.out.print("Loading selected .tif file.");
        stack = parser.loadGeneralTiff(file);
        System.out.format("Tif file loaded: %s\n",file.getAbsolutePath());
        count = 0;
    }
    
    @Override
    public ImageProcessor getNextImage() {
        count++;
        if (count > stack.getSize())
            return null;
        return stack.getProcessor(count);
    }

    @Override
    public void setCustomParameters(HashMap<String, Double> map) {
        return;
    }

    @Override
    public HashMap<String, Double> getCustomParameters() {
        return new HashMap<String, Double>();
    }
    
}
