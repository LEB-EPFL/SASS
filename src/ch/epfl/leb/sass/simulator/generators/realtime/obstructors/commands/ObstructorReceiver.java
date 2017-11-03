/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
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
package ch.epfl.leb.sass.simulator.generators.realtime.obstructors.commands;

import ch.epfl.leb.sass.simulator.generators.realtime.Obstructor;
import ch.epfl.leb.sass.simulator.generators.realtime.obstructors.Fiducial;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Camera;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Stage;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSFBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.RNG;
import java.util.ArrayList;
import java.util.Random;

/**
 * Creates obstructors after receiving commands.
 * 
 * @author Kyle M. Douglass
 */
public class ObstructorReceiver {
    
    public static ArrayList<Obstructor> generateGoldBeadsRandom2D(
            int numBeads,
            double brightness,
            Camera camera,
            Stage stage,
            PSFBuilder psfBuilder) {
        Random rnd = RNG.getUniformGenerator();
        ArrayList<Obstructor> result = new ArrayList();
        double x;
        double y;
        
        // Place fiducials in the focal plane
        double z = stage.getZ();
        
        for (int i=0; i < numBeads; i++) {
            x = camera.getNX() * rnd.nextDouble();
            y = camera.getNY() * rnd.nextDouble();
            result.add(new Fiducial(psfBuilder, brightness, x, y, z));
        }
        return result;
    }
}
