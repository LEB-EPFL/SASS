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
package ch.epfl.leb.sass.simulator.generators.realtime.obstructors;

import ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSFBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.Camera;
import ch.epfl.leb.sass.simulator.generators.realtime.Emitter;
import ch.epfl.leb.sass.simulator.generators.realtime.Obstructor;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import ch.epfl.leb.sass.simulator.generators.realtime.RNG;

/**
 * A number of constantly-shining gold beads interspersed in the frame.
 * @author Marcel Stefko
 * @deprecated Use Fiducials class instead.
 */
@Deprecated
public class GoldBeads implements Obstructor {
    private final List<GoldBead> beads;
    private final Random random;
    
    /**
     * Randomly places gold beads into the camera field of view.
     * @param beadCount number of gold beads
     * @param camera camera properties
     * @param brightness how bright the beads are [photons/frame]
     */
    public GoldBeads(int beadCount, Camera camera, double brightness) {
        random = RNG.getUniformGenerator();
        beads = new ArrayList<GoldBead>();
        for (int i=0; i<beadCount; i++) {
            double x = random.nextDouble() * camera.res_x;
            double y = random.nextDouble() * camera.res_y;
            beads.add(new GoldBead(camera, brightness, x, y));
        }
    }
    
    @Override
    public void applyTo(float[][] pixels) {
        for (GoldBead b: beads) {
            b.applyTo(pixels);
        }
    }
    
}
