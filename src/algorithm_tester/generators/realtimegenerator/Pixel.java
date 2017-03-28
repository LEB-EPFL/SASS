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
package algorithm_tester.generators.realtimegenerator;

import static java.lang.Math.sqrt;

/**
 *
 * @author stefko
 */
public class Pixel {
    public final int x;
    public final int y;
    private final double signature;
    
    public Pixel(int x, int y, double signature) {
        this.x = x;
        this.y = y;
        this.signature = signature;
    }
    
    public double getSignature() {
        return this.signature;
    }
    
    public int distance_to_sq(Pixel p) {
        return (this.x-p.x)*(this.x-p.x) + (this.y-p.y)*(this.y-p.y);
    }
    
    public double distance_to(Pixel p) {
        return sqrt((double) distance_to_sq(p));
    }
}
