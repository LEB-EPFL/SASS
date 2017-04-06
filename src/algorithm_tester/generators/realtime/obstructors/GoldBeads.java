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
package algorithm_tester.generators.realtime.obstructors;

import algorithm_tester.generators.realtime.Camera;
import algorithm_tester.generators.realtime.Emitter;
import algorithm_tester.generators.realtime.Obstructor;
import algorithm_tester.generators.realtime.Pixel;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author stefko
 */
public class GoldBeads implements Obstructor {
    ArrayList<GoldBead> beads;
    Random random;
    
    
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
    public float[][] applyTo(float[][] pixels) {
        for (GoldBead b: beads) {
            pixels = b.applyTo(pixels);
        }
        return pixels;
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
