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
package algorithm_tester.analyzers.quickpalm;

import ij.process.ImageProcessor;
import java.util.ArrayList;

/**
 *
 * @author stefko
 */
public class QuickPalmCore {
	
    private final MyDialogs dg;
    private final MyFunctions f;
    private final MyIO io;
    
    
        
    public QuickPalmCore() {
        dg = new MyDialogs();
        f = new MyFunctions();
        io = new MyIO();
        
        f.ptable.reset(); // erase particle table
        if (!dg.analyseParticles(f))
            throw new RuntimeException("Error in analysis setup.");
    }
        
    public int processImage( ImageProcessor ip, int frame ) {
        if (!((ip.getBitDepth() == 8) || (ip.getBitDepth() == 16))) {
            System.out.format("Image depth: %d",ip.getBitDepth());
            throw new UnsupportedOperationException("8 or 16 bit greyscale image required");
        }
        return this.f.detectParticles(ip, this.dg, frame);
    }
        
}


