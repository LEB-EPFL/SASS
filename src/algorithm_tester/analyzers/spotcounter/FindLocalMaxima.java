///////////////////////////////////////////////////////////////////////////////
//FILE:          FindLocalMaxima.java
//-----------------------------------------------------------------------------
//
// AUTHOR:       Nico Stuurman
//
// COPYRIGHT:    University of California, San Francisco 2015
//
// LICENSE:      This file is distributed under the BSD license.
//               License text is included with the source distribution.
//
//               This file is distributed in the hope that it will be useful,
//               but WITHOUT ANY WARRANTY; without even the implied warranty
//               of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
//               IN NO EVENT SHALL THE COPYRIGHT OWNER OR
//               CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
//               INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
package algorithm_tester.analyzers.spotcounter;

import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;
import ij.plugin.filter.GaussianBlur;
import java.awt.Polygon;
import java.awt.Rectangle;



/**
 * Find local maxima in an Image (or ROI) using the algorithm described in
 * Neubeck and Van Gool. Efficient non-maximum suppression. 
 * Pattern Recognition (2006) vol. 3 pp. 850-855
 *
 * Jonas Ries brought this to my attention and send me C code implementing one of the
 * described algorithms
 */
public class FindLocalMaxima {
   private static final GaussianBlur FILTER = new GaussianBlur();
   private static final ImageCalculator IC = new ImageCalculator();
   
    /**
     * Different filters for image preprocessing.
     */
    public enum FilterType {

       /**
        * No preprocessing.
        */
       NONE ("None"),

       /**
        */
       GAUSSIAN1_5("Gaussian1_5");
      
      private final String s_;
      
      FilterType (String s) {
         this.s_ = s;
      }
      
       /**
        *
        * @return
        */
       @Override
      public String toString() {
         return s_;
      }
      
       /**
        *
        * @param s
        * @return
        */
       public static FilterType equals (String s) {
         if (s.equals(NONE.toString())) {
            return NONE;
         }
         if (s.equals(GAUSSIAN1_5.toString())) {
            return GAUSSIAN1_5;
         }
         return null;
      }
      
   }

   /**
    * Static utility function to find local maxima in an Image
    * 
    * 
    * @param iProc - ImageProcessor object in which to look for local maxima
    * @param n - minimum distance to other local maximum
    * @param threshold - value below which a maximum will be rejected
    * @param filterType - Prefilter the image.  Either none or Gaussian1_5
    * @return Polygon with maxima 
    */
   public static Polygon FindMax(
           ImageProcessor iProc, 
           int n, 
           int threshold, 
           FilterType filterType) {
      
      Polygon maxima = new Polygon();
      Rectangle roi = iProc.getRoi();
      
      // Prefilter if needed
      switch (filterType) {
         case GAUSSIAN1_5 : 
            // TODO: if there is an ROI, we only need to filter_ in the ROI
            ImageProcessor iProcG1 = iProc.duplicate();
            ImageProcessor iProcG5 = iProc.duplicate();
            FILTER.blurGaussian(iProcG1, 0.4, 0.4, 0.01); 
            FILTER.blurGaussian(iProcG5, 2.0, 2.0, 0.01);
            ImagePlus p1 = new ImagePlus("G1", iProcG1);
            ImagePlus p5 = new ImagePlus("G5", iProcG5);
            IC.run("subtract", p1, p5);
            iProc = p1.getProcessor();
                      
            break;
      }


      // divide the image up in blocks of size n and find local maxima
      int n2 = 2*n + 1;
      // calculate borders once
      int xRealEnd = roi.x + roi.width;
      int xEnd = xRealEnd - n;
      int yRealEnd = roi.y + roi.height;
      int yEnd = yRealEnd - n;
      for (int i=roi.x + n/2; i < xEnd; i+=n2) {
         for (int j=roi.y + n/2; j < yEnd; j+=n2) {
            int mi = i;
            int mj = j;
            for (int i2=i; i2 < i + n2 && i2 < xRealEnd - n/2; i2++) {
               for (int j2=j; j2 < j + n2 && j2 < yRealEnd - n/2; j2++) {
                  // revert getPixel to get after debugging
                  if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj)) {
                     mi = i2;
                     mj = j2;
                  }
               }
            }
            // is the candidate really a local maximum?
            // check surroundings (except for the pixels that we already checked)
            boolean stop = false;
            // columns in block to the left
            if (mi - n < i && i>0) {
               for (int i2=mi-n; i2<i; i2++) {
                  for (int j2=mj-n; j2<=mj+n; j2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj)) {
                        stop = true;
                     }
                  }
               }
            }
            // columns in block to the right
            if (!stop && mi + n >= i + n2 ) {
               for (int i2=i+n2; i2<=mi+n; i2++) {
                   for (int j2=mj-n; j2<=mj+n; j2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj)) {
                        stop = true;
                     }
                  }
               }
            }
            // rows on top of the block
            if (!stop && mj - n < j && j > 0) {
               for (int j2 = mj - n; j2 < j; j2++) {
                  for (int i2 = mi - n; i2 <= mi + n; i2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj))
                        stop = true;
                  }
               }
            }
            // rows below the block
            if (!stop && mj + n >= j + n2) {
               for (int j2 = j + n2; j2 <= mj + n; j2++) {
                  for (int i2 = mi - n; i2 <= mi + n; i2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj))
                        stop = true;
                  }
               }
            }
            if (!stop && (threshold == 0 || 
                    (iProc.getPixel(mi, mj) - 
                      ( (iProc.getPixel(mi - n , mj - n) + iProc.getPixel(mi -n, mj + n) +
                       iProc.getPixel(mi + n, mj  - n) + iProc.getPixel(mi + n, mj + n)) / 4) ) 
                    > threshold))
               maxima.addPoint(mi, mj);
         }
      }


      return maxima;
   }


   // Filters local maxima list using the ImageJ findMaxima Threshold algorithm

    /**
     *
     * @param iProc
     * @param inputPoints
     * @param threshold
     * @return
     */
       public static Polygon noiseFilter(ImageProcessor iProc, Polygon inputPoints, int threshold)
   {
      Polygon outputPoints = new Polygon();

      for (int i=0; i < inputPoints.npoints; i++) {
         int x = inputPoints.xpoints[i];
         int y = inputPoints.ypoints[i];
         int value = iProc.getPixel(x, y) - threshold;
         if (    value > iProc.getPixel(x-1, y-1) ||
                 value > iProc.getPixel(x-1, y)  ||
                 value > iProc.getPixel(x-1, y+1)||
                 value > iProc.getPixel(x, y-1) ||
                 value > iProc.getPixel(x, y+1) ||
                 value > iProc.getPixel(x+1, y-1) ||
                 value > iProc.getPixel(x+1, y) ||
                 value > iProc.getPixel(x+1, y+1)
               )
            outputPoints.addPoint(x, y);
      }

      return outputPoints;
   }

}