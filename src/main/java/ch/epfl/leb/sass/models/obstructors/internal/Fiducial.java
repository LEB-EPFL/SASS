/* 
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
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
package ch.epfl.leb.sass.models.obstructors.internal;

import ch.epfl.leb.sass.models.emitters.internal.AbstractEmitter;
import ch.epfl.leb.sass.models.obstructors.Obstructor;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;

public class Fiducial extends AbstractEmitter implements Obstructor {
    private final double brightness;
        
    public Fiducial(PSFBuilder psfBuilder,
            double brightness,
            double x,
            double y,
            double z) {
        super(x, y, z, psfBuilder);
        this.brightness = brightness;
    }

    @Override
    protected double simulateBrightness() {
        return flicker(brightness);
    }
}
