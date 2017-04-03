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

import static java.lang.Math.sqrt;

/**
 * Representation of a single pixel signature caused by a single emitter.
 * @author Marcel Stefko
 */
public class Pixel {
    public final int x;
    public final int y;
    private final double signature;
    
    /**
     * Initialize new pixel with position and signature.
     * @param x x-position [px]
     * @param y y-position [px]
     * @param signature relative brightness of this pixel due to emitter [-]
     */
    public Pixel(int x, int y, double signature) {
        this.x = x;
        this.y = y;
        this.signature = signature;
    }
    
    /**
     * Returns this pixel's signature
     * @return relative brightness of this pixel due to an emitter [-]
     */
    public double getSignature() {
        return this.signature;
    }
    
    /**
     * Calculates squared distance to another pixel
     * @param p another Pixel
     * @return squared distance between these pixels [px2]
     */
    public int distance_to_sq(Pixel p) {
        return (this.x-p.x)*(this.x-p.x) + (this.y-p.y)*(this.y-p.y);
    }
    
    /**
     * Calculates euclidean distance to another pixel
     * @param p another Pixel
     * @return euclidean distance between these pixels [px]
     */
    public double distance_to(Pixel p) {
        return sqrt((double) distance_to_sq(p));
    }
}
