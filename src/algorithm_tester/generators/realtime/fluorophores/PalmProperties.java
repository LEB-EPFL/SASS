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
package algorithm_tester.generators.realtime.fluorophores;

import algorithm_tester.generators.realtime.Camera;
import algorithm_tester.generators.realtime.Fluorophore;
import algorithm_tester.generators.realtime.FluorophoreProperties;

/**
 *
 * @author stefko
 */
public class PalmProperties extends FluorophoreProperties {

    
    public final double k_a;
    public final double k_b;
    public final double k_d1;
    public final double k_d2;
    public final double k_r1;
    public final double k_r2;
    
    public final double T_a;
    public final double T_b;
    public final double T_d1;
    public final double T_d2;
    public final double T_r1;
    public final double T_r2;
   
    
    public PalmProperties(double signal, double background, 
            double k_a, double k_b, double k_d1, 
            double k_d2, double k_r1, double k_r2) {
        super(signal, background);
        if (k_a<=0.0 || k_b<0.0 || k_d1<0.0 || k_d2<0.0 || k_r1<0.0 || k_r2<0.0) {
            throw new IllegalArgumentException();
        }
        this.k_a = k_a;
        this.k_b = k_b;
        this.k_d1 = k_d1;
        this.k_d2 = k_d2;
        this.k_r1 = k_r1;
        this.k_r2 = k_r2;
        
        this.T_a = parseInverse(k_a);
        this.T_b = parseInverse(k_b);
        this.T_d1 = parseInverse(k_d1);
        this.T_d2 = parseInverse(k_d2);
        this.T_r1 = parseInverse(k_r1);
        this.T_r2 = parseInverse(k_r2);
    }
    
    public static double parseInverse(double d) {
        if (d<0.0)
            throw new IllegalArgumentException("Can't parse a negative double.");
        if (Double.isInfinite(d))
            return 0.0;
        else if (d == 0.0)
            return Double.POSITIVE_INFINITY;
        else
            return 1.0/d;
    }

    @Override
    public Fluorophore createFluorophore(Camera camera, double x, double y) {
        return new PalmFluorophore(this, camera, x, y);
    }
}
