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
package algorithm_tester.spotcounter;

import algorithm_tester.EvaluationAlgorithm;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author stefko
 */
public class SpotCounter implements EvaluationAlgorithm {
    ImageStack stack;
    private final ArrayList<Double> spot_counts;
    private final ArrayList<Double> min_dists;
    SpotCounterCore analyzer;
    
    public SpotCounter() {
        spot_counts = new ArrayList<Double>();
        min_dists = new ArrayList<Double>();
        analyzer = new SpotCounterCore();
    }
    
    @Override
    public void setImageStack(ImageStack stack) {
        this.stack = stack;
    }

    @Override
    public void processStack() {
        ResultsTable result;
        for (int i=1; i<=stack.getSize(); i++) {
            result = analyzer.analyze(stack.getProcessor(i));
            spot_counts.add(result.getValue("n", i-1));
            min_dists.add(result.getValue("min_dist", i-1));
        }
    }

    @Override
    public HashMap<String, Double> getOutputValues(int image_no) {
        HashMap<String, Double> map = new LinkedHashMap<String, Double>();
        map.put("spot_count", spot_counts.get(image_no));
        map.put("min_dist", min_dists.get(image_no));
        return map;
    }


    @Override
    public String getName() {
        return "SpotCounter";
    }
    
}


///////////////////////////////////////////////////////////////////////////////
//Class:         SpotCounterCore
//-----------------------------------------------------------------------------
//
// AUTHOR:       Nico Stuurman (adapted 2017 by Marcel Stefko)
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
class SpotCounterCore {

    private final int nPasses_ = 1;
    private int pasN_ = 0;
    private final String preFilterChoice_ = "";
    private boolean start_ = true;
    private ResultsTable res_;
    private ResultsTable res2_;
    private static final boolean outputAllSpots_ = false;
    private static final FindLocalMaxima.FilterType filter_
            = FindLocalMaxima.FilterType.NONE;
    private static final int boxSize_ = 3;
    private static final int noiseTolerance_ = 1000;

    public ResultsTable analyze(ImageProcessor ip) {

        Overlay ov = getSpotOverlay(ip);
        if (start_) {
            res_ = new ResultsTable();
            if (outputAllSpots_) {
                res2_ = new ResultsTable();
            }
            res_.setPrecision(1);
            pasN_ = 0;
            start_ = false;
        }
        res_.incrementCounter();
        pasN_ += 1;
        res_.addValue("n", ov.size());
        
        // determine lowest distance
        res_.addValue("min_dist", getLowestDistance(ov));
        
        /*if (ov.size() > 0) {
            double sumRoiIntensities = 0;
            Rectangle originalRoi = ip.getRoi();
            for (int i = 0; i < ov.size(); i++) {
                Roi roi = ov.get(i);
                ip.setRoi(roi);
                sumRoiIntensities += ip.getStatistics().mean;
                if (outputAllSpots_) {
                    res2_.incrementCounter();
                    res2_.addValue("Image #", pasN_);
                    res2_.addValue("Spot Intensity", ip.getStatistics().mean);
                }
            }
            ip.setRoi(originalRoi);
            double mean = sumRoiIntensities / ov.size();
            res_.addValue("Spot mean", mean);
        }
        res_.addValue("Image mean", ip.getStatistics().mean);
        
        if (pasN_ == nPasses_) {
            String output = new String();
            // difficult way to get size of the resultstable to stay compatible
            // with older ij.jar versions that do not have res_.size()
            int size = res_.getColumn(0).length;
            for (int i = 0; i < size; i++) {
                output += "" + (i + 1) + "\t" + res_.getValue("n", i) + "\t"
                        + res_.getValue("Spot mean", i) + "\t"
                        + res_.getValue("Image mean", i) + "\n";
            }
        }
        */
        return res_;

    }
    
    private double getLowestDistance(Overlay ov) {
        double min_dist2 = 10000.0;
        double dist2;
        for (int i=0; i<ov.size(); i++) {
            Rectangle rect_i = ov.get(i).getBounds();
            for (int j=i; j<ov.size(); j++) {
                Rectangle rect_j = ov.get(j).getBounds();
                dist2 = ((rect_i.getCenterX() - rect_j.getCenterX())* 
                         (rect_i.getCenterX() - rect_j.getCenterX()) ) +
                        ((rect_i.getCenterY() - rect_j.getCenterY())* 
                         (rect_i.getCenterY() - rect_j.getCenterY()) );
                if (dist2<min_dist2)
                    min_dist2 = dist2;
            }
        }
        return sqrt(min_dist2);
    }

    /**
     * Finds local maxima and returns them as an collection of Rois (an overlay)
     *
     * @param ip - ImageProcessor to be analyzed
     * @return overlay with local maxima
     */
    private Overlay getSpotOverlay(ImageProcessor ip) {
        Polygon pol = FindLocalMaxima.FindMax(
                ip, boxSize_, noiseTolerance_, filter_);
        int halfSize = boxSize_ / 2;
        Overlay ov = new Overlay();
        for (int i = 0; i < pol.npoints; i++) {
            int x = pol.xpoints[i];
            int y = pol.ypoints[i];
            boolean use = true;
            if (use) {
                Roi roi = new Roi(x - halfSize, y - halfSize, boxSize_, boxSize_);
                roi.setStrokeColor(Color.RED);
                ov.add(roi);
            }
        }
        return ov;
    }
}
