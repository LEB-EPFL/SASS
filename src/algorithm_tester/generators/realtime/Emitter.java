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
 * Emitter defined by its coordinates and underlying fluorophore 
 * state time constants.
 * @author Marcel Stefko
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
    
    /**
     * Creates new emitter and precalculates its projection on the camera.
     * @param fluorophore underlying fluorophore properties
     * @param camera camera properties (for projection calculations)
     * @param x x-position [pixels]
     * @param y y-position [pixels]
     */
    public Emitter(Fluorophore fluorophore, Camera camera, double x, double y) {
        super(x, y);
        this.fluo = fluorophore;
        this.state = false;
        this.is_bleached = false;
        
        // radius cutoff
        double r = 3 * camera.fwhm_digital / 2.3548;
        // generate pixels which will be added to image when emitter is on
        this.pixel_list = get_pixels_within_radius(r, camera.fwhm_digital);
        // Poisson RNG
        poisson = new Poisson(1.0, new MersenneTwister(11*(int)x + 17*(int)y));
    }
    
    /**
     * Recalculates the lifetimes of this emitter based on current laser power.
     * @param laser_power current laser power
     */
    public void recalculate_lifetimes(double laser_power) {
        if (laser_power < 0.0000001) {
            laser_power = 0.0000001;
        }
        // Calculate time constants
        Ton = fluo.base_Ton;
        Toff = fluo.base_Toff / laser_power;
        Tbl = fluo.base_Tbl / laser_power;
        
        // Initialize new RNGs
        gen_Ton = new ExponentialDistribution(Ton);
        gen_Toff = new ExponentialDistribution(Toff);
        gen_Tbl = new ExponentialDistribution(Tbl);
    }
    
    /**
     * Simulates the state of the emitter for the next frame and returns its
     * integrated brightness over the duration of the frame.
     * @return emitter brightness in this frame [photons]
     */
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
    
    /**
     * Returns list of pixels which need to be drawn on the image to accurately
     * render the emitter.
     * @return list of Pixels
     */
    public ArrayList<Pixel> getPixelList() {
        return this.pixel_list;
    }
    
    /**
     * Returns the signature that this emitter leaves on a given pixel (what
     * fraction of this emitter's photons hits this particular pixel).
     * @param x pixel x-position
     * @param y pixel y-position
     * @param camera_fwhm_digital camera fwhm value
     * @return signature value for this pixel
     * @throws MathException
     */
    public double generate_signature_for_pixel(int x, int y, double camera_fwhm_digital) throws MathException {
        double denom = sqrt(2.0)*camera_fwhm_digital / 2.3548;
        return (Erf.erf((x-this.x+0.5)/denom) - Erf.erf((x-this.x-0.5)/denom)) *
               (Erf.erf((y-this.y+0.5)/denom) - Erf.erf((y-this.y-0.5)/denom));
    }
    
    /**
     * Returns a list of pixels within a certain radius from this emitter 
     * (so that their signature is precalculated). Pixels outside this radius
     * are considered to have negligible signature.
     * @param radius radius value [pixels]
     * @param camera_fwhm_digital camera fwhm value
     * @return list of Pixels with precalculated signatures
     * @throws MathException
     */
    private ArrayList<Pixel> get_pixels_within_radius(double radius, double camera_fwhm_digital) {
        ArrayList<Pixel> result = new ArrayList<Pixel>();
        // Upper and lower bounds for the region.
        int bot_x = (int)(this.x-radius);
        int top_x = (int)(this.x+radius);
        int bot_y = (int)(this.y-radius);
        int top_y = (int)(this.y+radius);
        
        // Squared radius so we dont have to do the sqrt()
        double radius2 = radius*radius;
        
        // Iterate over all pixels in the square defined by the bounds and
        // filter out those which are too far, otherwise generate signature and
        // add to list.
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

