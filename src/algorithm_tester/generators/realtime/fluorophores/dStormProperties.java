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
public class dStormProperties extends FluorophoreProperties {
    public final double k_bl;
    public final double k_triplet;
    public final double k_triplet_recovery;
    public final double k_dark;
    public final double k_dark_recovery;
    public final double k_dark_recovery_constant;
    
    public final double T_bleach;
    public final double T_triplet;
    public final double T_triplet_recovery;
    public final double T_dark;
    public final double T_dark_recovery;
    public final double T_dark_recovery_constant; 
    
    public dStormProperties(double signal, double background, double k_bl, 
            double k_triplet, double k_triplet_recovery, double k_dark, 
            double k_dark_recovery, double k_dark_recovery_constant) {
        super(signal, background);
        
        if (k_bl<0.0 || k_triplet<=0.0 || k_triplet_recovery<0.0 ||
            k_dark<0.0 || k_dark_recovery<0.0 || k_dark_recovery_constant<0.0) {
            throw new IllegalArgumentException("Rates must be positive.");
        }
        
        this.k_bl = k_bl;
        this.k_triplet = k_triplet;
        this.k_triplet_recovery = k_triplet_recovery;
        this.k_dark = k_dark;
        this.k_dark_recovery = k_dark_recovery;
        this.k_dark_recovery_constant = k_dark_recovery_constant;
        
        this.T_bleach = parseInverse(k_bl);
        this.T_triplet = parseInverse(k_triplet);
        this.T_triplet_recovery = parseInverse(k_triplet_recovery);
        this.T_dark = parseInverse(k_dark);
        this.T_dark_recovery = parseInverse(k_dark_recovery);
        this.T_dark_recovery_constant = parseInverse(k_dark_recovery_constant);
    }

    @Override
    public Fluorophore createFluorophore(Camera camera, double x, double y) {
        return new dStormFluorophore(this, camera, x, y);
    }
    
    private static double parseInverse(double d) {
        return PalmProperties.parseInverse(d);
    }
    
    public double getDarkRecoveryTime(double laser_power) {
        return parseInverse(k_dark_recovery_constant + k_dark_recovery*laser_power);
    }
}
