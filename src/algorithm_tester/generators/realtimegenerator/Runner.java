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
package algorithm_tester.generators.realtimegenerator;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ShortProcessor;

/**
 * Test class for RealTimeGenerator
 * @author Marcel Stefko
 */
public class Runner {

    /**
     * Generates some images and saves them to a .tif file.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Device device = new Device();
        ImageStack stack = new ImageStack(400,400);
        
        for (int i=0; i<100; i++) {
            System.out.println(i);
            ShortProcessor ip = device.simulateFrame();
            stack.addSlice(ip);
        }
        device.changeLaserPower(0.3);
        for (int i=0; i<100; i++) {
            System.out.println(i);
            ShortProcessor ip = device.simulateFrame();
            stack.addSlice(ip);
        }
        device.changeLaserPower(5);
        for (int i=0; i<100; i++) {
            System.out.println(i);
            ShortProcessor ip = device.simulateFrame();
            stack.addSlice(ip);
        }
        ImagePlus imp = new ImagePlus("stack", stack);
        FileSaver fs = new FileSaver(imp);
        fs.saveAsTiffStack("C:\\Users\\stefko\\Desktop\\stackd.tif");
    }
}
