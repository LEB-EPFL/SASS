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
package algorithm_tester.generators.realtime;

import java.util.ArrayList;
import java.util.Random;

/**
 * Randomly populates the field of view with emitters.
 * @author Marcel Stefko
 */
public class EmitterGenerator {

    /**
     * Randomly populate the field of view with emitters.
     * @param n_fluos number of emitters to be generated
     * @param cam camera properties
     * @param fluo fluorophore properties
     * @return
     */
    public static ArrayList<Emitter> generateEmittersRandom(int n_fluos, Camera cam, Fluorophore fluo) {
        Random rnd = new Random();
        ArrayList<Emitter> result = new ArrayList<Emitter>();
        double x; double y;
        for (int i=0; i<n_fluos; i++) {
            x = 2 + (cam.res_x - 3)*rnd.nextDouble();
            y = 2 + (cam.res_y - 3)*rnd.nextDouble();
            result.add(new Emitter(fluo, cam, x, y));
        }
        return result;
    }
}
