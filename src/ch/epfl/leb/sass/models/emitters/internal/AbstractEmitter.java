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
package ch.epfl.leb.sass.models.emitters.internal;

import ch.epfl.leb.sass.utils.RNG;
import ch.epfl.leb.sass.models.legacy.Camera;
import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister;
import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;
import ch.epfl.leb.sass.models.psfs.PSF;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.loggers.PositionLogger;
import ch.epfl.leb.sass.loggers.StateLogger;
import ch.epfl.leb.sass.loggers.FrameLogger;

/**
 * A point source of light and tools to compute its signature on a digital detector.
 * 
 * Emitters are general point sources of light that are imaged by an optical
 system and recorded by a digital sensor. The AbstractEmitter class contains tools for
 generating the digital images of point sources without any regard for the
 dynamics of the of the signal (apart from photon shot noise). Classes that
 extend the AbstractEmitter class are intended to implement the dynamics of the
 source's signal.
 * 
 * @author Marcel Stefko
 * @author Kyle M. Douglass
 */
public abstract class AbstractEmitter extends Point2D.Double  {
    
    /**
     * Running total of the number of emitters.
     */
    protected static int numberOfEmitters = 0;
    
    /**
     * A unique ID assigned to this emitter.
     */
    protected int id;
    
    /**
     * A copy of the state logger.
     */
    protected final StateLogger stateLogger = StateLogger.getInstance();
    
    /**
     * A copy of the position logger.
     */
    protected final PositionLogger positionLogger = PositionLogger.getInstance();

    /**
     * A copy of the frame logger.
     */
    protected final FrameLogger frameLogger = FrameLogger.getInstance();
    
    /**
     * A builder for creating/updating the emitter PSF.
     */
    protected PSFBuilder builder;
    
    /**
     * The PSF model that's created by the emitter.
     */
    protected PSF psf;
    
    /**
     * The emitter's z-position.
     */
    public double z;

    /**
     * List of pixels which are affected by this emitter's light (these pixels
     * need to be updated when the emitter is on).
     */
    protected ArrayList<Pixel> pixel_list;    

    /**
     * Poisson RNG for flickering simulation.
     */
    protected Poisson poisson;
    
    /**
     * Camera settings used for calculating PSF
     * @deprecated Will be removed in future versions.
     */
    @Deprecated
    protected final Camera camera;

    /**
     * Creates emitter at given position, and calculates its signature on the
     * image (what does it look like when it is turned on).
     * @param camera camera properties (needed for PSF calculation)
     * @param x x-position in image [pixels, with sub-pixel precision]
     * @param y y-position in image [pixels, with sub-pixel precision]
     * @deprecated Camera instances are being decoupled from Emitter. Use the
     *             {@link #Emitter(double, double, double, ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSFBuilder) }
     *             instead.
     */
    @Deprecated
    public AbstractEmitter(Camera camera, double x, double y) {
        super(x, y);
        this.z = 0;
        this.camera = camera;
        this.poisson = RNG.getPoissonGenerator();
        final double sigma = camera.fwhm_digital / 2.3548;
        // radius cutoff
        final double r = 3 * sigma;
        // generate pixels which will be added to image when emitter is on
        this.pixel_list = get_pixels_within_radius(r, camera.fwhm_digital);
        
        // Increment the number of emitters and assign the id.
        this.numberOfEmitters += 1;
        this.id = this.numberOfEmitters;
        
    }
    
    /**
     * Creates the emitter at given position, and calculates its image from the PSF and camera.
     * @param x x-position in image [pixels, with sub-pixel precision]
     * @param y y-position in image [pixels, with sub-pixel precision]
     * @param z z-position in image [pixels, with sub-pixel precision]
     * @param psfBuilder Builder for creating the emitter's PSF.
     */
    public AbstractEmitter(double x, double y, double z, PSFBuilder psfBuilder) {
        super(x, y);
        this.z = z;
        this.builder = psfBuilder;
        
        // Calculate the PSF.
        psfBuilder.eX(x).eY(y).eZ(z);
        
        this.psf = psfBuilder.build();
        this.poisson = RNG.getPoissonGenerator();
        this.camera = null;
        
        // generate pixels which will be added to the image when emitter is on
        // This must be called **after** super(x,y).
        this.pixel_list = this.getPixelsWithinRadius(this, this.psf.getRadius());
        
        // Compute the signature on each pixel created by this emitter
        this.psf.generateSignature(this.pixel_list);
        
        // Increment the number of emitters and assign the id.
        this.numberOfEmitters += 1;
        this.id = this.numberOfEmitters;
        
    }

    /**
     * Returns the signature that this emitter leaves on a given pixel (what
     * fraction of this emitter's photons hits this particular pixel).
     * @param x pixel x-position
     * @param y pixel y-position
     * @param camera_fwhm_digital camera fwhm value
     * @return signature value for this pixel
     * @throws MathException
     * @deprecated This behavior has been moved to the PSF interface
     */
    @Deprecated
    protected double generate_signature_for_pixel(int x, int y, double camera_fwhm_digital) throws MathException {
        final double sigma = camera_fwhm_digital / 2.3548;
        final double denom = sqrt(2.0)*sigma;
        return 0.25 *(Erf.erf((x-this.x+0.5)/denom) - Erf.erf((x-this.x-0.5)/denom)) *
                     (Erf.erf((y-this.y+0.5)/denom) - Erf.erf((y-this.y-0.5)/denom));
    }
    
    
    /**
     * Returns a list of pixels within a certain radius from this emitter 
     * (so that their signature is precalculated). Pixels outside this radius
     * are considered to have negligible signature.
     * @param radius radius value [pixels]
     * @param camera_fwhm_digital camera fwhm value
     * @return list of Pixels with precalculated signatures
     * @deprecated Use {@link #getPixelsWithinRadius(java.awt.geom.Point2D, double)} instead.
     */
    @Deprecated
    protected final ArrayList<Pixel> get_pixels_within_radius(double radius, double camera_fwhm_digital) {
        ArrayList<Pixel> result = new ArrayList<Pixel>();
        // Upper and lower bounds for the region.
        final int bot_x = (int) floor(this.x-radius);
        final int top_x = (int) ceil(this.x+radius);
        final int bot_y = (int) floor(this.y-radius);
        final int top_y = (int) ceil(this.y+radius);
        
        // Squared radius so we dont have to do the sqrt()
        final double radius2 = radius*radius;
        
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
                        Logger.getLogger(AbstractEmitter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    result.add(new Pixel(i,j,signature));
                }
            }
        }
        return result;
    }
    
    /**
     * Returns a list of pixels within a certain radius from a point.
     * 
     * This method locates all the pixels within a circular area surrounding a
     * given two-dimensional point whose center lies at (x, y). The coordinate
     * of a pixel is assumed to lie at the pixel's center, and a pixel is within
     * a given radius of another if the pixel's center lies within this circle.
     * 
     * @param point
     * @param radius radius value [pixels]
     * @return list of Pixels with pre-calculated signatures
     */
    public static final ArrayList<Pixel> getPixelsWithinRadius(Point2D point, double radius) {
        ArrayList<Pixel> result = new ArrayList<Pixel>();
        // If radius is less than one, return the pixel containing the point
        if (radius < 1)
        {   
            int x = (int) point.getX();
            int y = (int) point.getY();
            result.add(new Pixel(x,y,0));
            return result;
        }
                    
        // Upper and lower bounds for the region.
        final int bot_x = (int) floor(point.getX() - radius);
        final int top_x = (int) ceil(point.getX() + radius);
        final int bot_y = (int) floor(point.getY() - radius);
        final int top_y = (int) ceil(point.getY() + radius);
        
        // Squared radius so we dont have to do the sqrt()
        final double radius2 = radius*radius;
        
        // Iterate over all pixels in the square defined by the bounds and
        // filter out those which are too far, otherwise generate signature and
        // add to list.
        for (int i = bot_x; i<=top_x; i++) {
            for (int j=bot_y; j<=top_y; j++) {
                if (point.distanceSq((double) i, (double) j) <= radius2) {
                    result.add(new Pixel(i,j,0));
                }
            }
        }
        return result;
    }
    
    /**
     * Applies Poisson statistics to simulate flickering of an emitter.
     * 
     * @param baseBrightness mean of Poisson distribution to draw from
     * @return actual brightness of this emitter for this frame
     */
    protected double flicker(double baseBrightness) {
        return poisson.nextInt(baseBrightness);
    }
    
    /**
     * Returns list of pixels which need to be drawn on the image to accurately render the emitter.
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
                pixels[p.x][p.y] += brightness * p.getSignature();
            } catch (ArrayIndexOutOfBoundsException ex) {
                // pixel signature outside of frame, do nothing
                //Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Simulates the state evolution of the emitter for the next frame, and
     * returns the integrated brightness of this emitter for this frame.
     * @return brightness of emitter in this frame [photons emitted]
     */
    protected abstract double simulateBrightness();
    
    /**
     * Returns the emitter's ID.
     * @return The unique integer identifying the emitter.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Returns the emitter's PSF model.
     * @return The PSF model used to create the image of this emitter.
     */
    public PSF getPSF() {
        return this.psf;
    }
    
    /**
     * Change the emitter's PSF model.
     * @param psf The PSF model used to create the image of this emitter.
     */
    public void setPSF(PSF psf) {
        this.psf = psf;
    }
}
