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
package simulator.generators.realtime;

import simulator.generators.realtime.fluorophores.SimpleFluorophore;
import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister;
import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;

/**
 * Any 2D point that emits (or reflects) light when shined on by laser.
 * @author Marcel Stefko
 */
public abstract class Emitter extends Point2D.Double  {

    /**
     * List of pixels which are affected by this emitter's light (these pixels
     * need to be updated when the emitter is on).
     */
    protected final ArrayList<Pixel> pixel_list;    

    /**
     * Poisson RNG for flickering simulation.
     */
    protected Poisson poisson;

    /**
     * Creates emitter at given position, and calculates its signature on the
     * image (what does it look like when it is turned on).
     * @param camera camera properties (needed for PSF calculation)
     * @param x x-position in image [pixels, with sub-pixel precision]
     * @param y y-position in image [pixels, with sub-pixel precision]
     */
    public Emitter(Camera camera, double x, double y) {
        super(x, y);
        this.poisson = RNG.getPoissonGenerator();
        // radius cutoff
        double r = 3 * camera.fwhm_digital / 2.3548;
        // generate pixels which will be added to image when emitter is on
        this.pixel_list = get_pixels_within_radius(r, camera.fwhm_digital);
        
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
    private double generate_signature_for_pixel(int x, int y, double camera_fwhm_digital) throws MathException {
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
                        Logger.getLogger(SimpleFluorophore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    result.add(new Pixel(i,j,signature));
                }
            }
        }
        return result;
    }
    
    /**
     * Applies Poisson statistics to simulate flickering of emitter.
     * @param base_brightness mean of poisson distribution to draw from
     * @return actual brightness of this emitter for this frame
     */
    protected double flicker(double base_brightness) {
        return poisson.nextInt(base_brightness);
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
     * Simulates the brightness pattern of this emitter for the next frame
     * duration, and renders the emitter onto the image.
     * @param pixels image to be drawn on
     */
    public void applyTo(float[][] pixels) {
        double brightness = this.simulateBrightness();
        for (Pixel p: this.getPixelList()) {
            try {
                pixels[p.x][p.y] += 0.25* brightness * p.getSignature();
            } catch (ArrayIndexOutOfBoundsException ex) {
                //Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Simulates the state evolution of the emitter for the next frame, and
     * returns the integrated brightness of this emitter for this frame.
     * @return brightness of emitter in this frame
     */
    protected abstract double simulateBrightness();
}
