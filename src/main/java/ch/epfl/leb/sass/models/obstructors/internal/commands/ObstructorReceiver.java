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
package ch.epfl.leb.sass.models.obstructors.internal.commands;

import ch.epfl.leb.sass.models.obstructors.Obstructor;
import ch.epfl.leb.sass.models.obstructors.internal.Fiducial;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.components.Stage;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.utils.RNG;
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
        
        // Place fiducials in the focal plane; negative sign means that minus
        // moves the stage down, which scans upwards in z in an inverted
        // microscope.
        double z = -stage.getZ();
        
        for (int i=0; i < numBeads; i++) {
            x = camera.getNX() * rnd.nextDouble();
            y = camera.getNY() * rnd.nextDouble();
            result.add(new Fiducial(psfBuilder, brightness, x, y, z));
        }
        return result;
    }
}
