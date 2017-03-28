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

import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister;
import ij.process.ImageProcessor;
import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;
import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 *
 * @author stefko
 */
public class Emitter extends Point2D.Double {
    public final Fluorophore fluo;
    
    public boolean state;
    public boolean is_bleached;
    
    private double Ton;
    private double Toff;
    private double Tbl;
    
    private final ArrayList<Pixel> pixel_list;
    
    private ExponentialDistribution gen_Ton;
    private ExponentialDistribution gen_Toff;
    private ExponentialDistribution gen_Tbl;
    private Poisson poisson;
    
    public Emitter(Fluorophore fluorophore, Camera camera, double x, double y) {
        super(x, y);
        this.fluo = fluorophore;
        this.state = false;
        this.is_bleached = false;
        
        double r = 3 * camera.fwhm_digital / 2.3548;
        this.pixel_list = get_pixels_within_radius(r, camera.fwhm_digital);
        
        
        poisson = new Poisson(1.0, new MersenneTwister(11*(int)x + 17*(int)y));
    }
    
    public void recalculate_lifetimes(double laser_power) {
        Ton = fluo.base_Ton;
        Toff = fluo.base_Toff / laser_power;
        Tbl = fluo.base_Tbl / laser_power;
        
        gen_Ton = new ExponentialDistribution(Ton);
        gen_Toff = new ExponentialDistribution(Toff);
        gen_Tbl = new ExponentialDistribution(Tbl);
    }
    
    public double simulate_brightness() {
        if (is_bleached)
            return 0.0;
        double t=0.0;
        double on_time = 0.0;
        double bleach_time = gen_Tbl.sample();
        boolean does_bleach = (bleach_time < 1.0);
        double limit = does_bleach ? bleach_time : 1.0;
        while (t<limit) {
            double lifetime;
            if (state) {
                lifetime = gen_Ton.sample();
                on_time += ((lifetime < limit-t) ? lifetime : limit-t);
            } else {
                lifetime = gen_Toff.sample();
            }
            t += lifetime;
            if (t<limit)
                state = !state;
        }
        if (does_bleach) {
            is_bleached = true;
            state = false;
        }
        return poisson.nextInt(on_time*fluo.signal);
    }
    
    public ArrayList<Pixel> getPixelList() {
        return this.pixel_list;
    }
    
    public void draw_on(ImageProcessor ip) {
        
    }
    
    public double generate_signature_for_pixel(int x, int y, double camera_fwhm_digital) throws MathException {
        double denom = sqrt(2.0)*camera_fwhm_digital / 2.3548;
        return (Erf.erf((x-this.x+0.5)/denom) - Erf.erf((x-this.x-0.5)/denom)) *
               (Erf.erf((y-this.y+0.5)/denom) - Erf.erf((y-this.y-0.5)/denom));
    }
    
    private ArrayList<Pixel> get_pixels_within_radius(double radius, double camera_fwhm_digital) {
        ArrayList<Pixel> result = new ArrayList<Pixel>();
        int bot_x = (int)(this.x-radius);
        int top_x = (int)(this.x+radius);
        int bot_y = (int)(this.y-radius);
        int top_y = (int)(this.y+radius);
        
        double radius2 = radius*radius;
        
        for (int i = bot_x; i<=top_x; i++) {
            for (int j=bot_y; j<=top_y; j++) {
                if (this.distanceSq((double) i, (double) j) <= radius2) {
                    double signature;
                    try {
                        signature = generate_signature_for_pixel(i, j, camera_fwhm_digital);
                    } catch (MathException ex) {
                        signature = 0.0;
                        Logger.getLogger(Emitter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    result.add(new Pixel(i,j,signature));
                }
            }
        }
        return result;
    }
    
}

