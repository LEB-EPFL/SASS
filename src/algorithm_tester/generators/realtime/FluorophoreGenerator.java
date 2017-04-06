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
 * Randomly populates the field of view with fluorophores.
 * @author Marcel Stefko
 */
public class FluorophoreGenerator {

    /**
     * Randomly populate the field of view with fluorophores.
     * @param n_fluos number of emitters to be generated
     * @param cam camera properties
     * @param fluo fluorophore properties
     * @return
     */
    public static ArrayList<Fluorophore> generateFluorophoresRandom(int n_fluos, Camera cam, FluorophoreProperties fluo) {
        Random rnd = new Random();
        ArrayList<Fluorophore> result = new ArrayList<Fluorophore>();
        double x; double y;
        for (int i=0; i<n_fluos; i++) {
            x = cam.res_x*rnd.nextDouble();
            y = cam.res_y*rnd.nextDouble();
            result.add(new Fluorophore(fluo, cam, x, y));
        }
        return result;
    }
}
