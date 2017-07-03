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
package ch.epfl.leb.sass.simulator.generators.realtime;

import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.SimpleProperties;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Fluorophore that can move between frames
 * @author Marcel Stefko
 */
public class MovingFluorophore extends Fluorophore {
    private final ArrayList<Point2D.Double> trajectory;
    private int image_count = 0;
    
    /**
     * Fluorophore which moves along a certain trajectory and then stops
     * @param camera
     * @param x initial x position
     * @param y initial y position
     * @param trajectory trajectory of fluorophore in absolute numbers
     */
    public MovingFluorophore(Camera camera, double signal, StateSystem state_system, int start_state, double x, double y, ArrayList<Point2D.Double> trajectory) {
        super(camera, signal, state_system, start_state, x, y);
        this.trajectory = trajectory;
    }
    
    @Override
    public void applyTo(float[][] pixels) {
        // move to new position and recalculate signature
        if (image_count<trajectory.size()) {
            Point2D new_position = trajectory.get(image_count);
            recalculateFluorophoreSignature(this, new_position);
        }
        // apply as in normal fluorophore
        super.applyTo(pixels);
        image_count++;
    }
    
    private void recalculateFluorophoreSignature(Point2D old_position, Point2D new_position) {
        // TODO: motion blurring
        this.setLocation(new_position);
        this.pixel_list = get_pixels_within_radius(3 * camera.fwhm_digital / 2.3548, camera.fwhm_digital);
    }
    
}
