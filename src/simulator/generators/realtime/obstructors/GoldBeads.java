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
package simulator.generators.realtime.obstructors;

import simulator.generators.realtime.Camera;
import simulator.generators.realtime.Emitter;
import simulator.generators.realtime.Obstructor;
import java.util.ArrayList;
import java.util.Random;

/**
 * A number of constantly-shining gold beads interspersed in the frame.
 * @author Marcel Stefko
 */
public class GoldBeads implements Obstructor {
    private final ArrayList<GoldBead> beads;
    private final Random random;
    
    /**
     * Randomly places gold beads into the camera field of view.
     * @param beadCount number of gold beads
     * @param camera camera properties
     * @param brightness how bright the beads are [photons/frame]
     */
    public GoldBeads(int beadCount, Camera camera, double brightness) {
        random = new Random();
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

class GoldBead extends Emitter {
    private final double brightness;
    public GoldBead(Camera camera, double brightness, double x, double y) {
        super(camera, x, y);
        this.brightness = brightness;
    }

    @Override
    protected double simulateBrightness() {
        return flicker(brightness);
    }
}
