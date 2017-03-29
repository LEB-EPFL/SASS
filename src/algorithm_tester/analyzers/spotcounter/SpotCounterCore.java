package algorithm_tester.analyzers.spotcounter;

///////////////////////////////////////////////////////////////////////////////

import ij.gui.Overlay;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    private final int boxSize_;
    private final int noiseTolerance_;
    
    public SpotCounterCore(int noiseTolerance, int boxSize) {
        boxSize_ = boxSize;
        noiseTolerance_ = noiseTolerance;
    }

    /**
     * Analyzes the image and returns information about current state.
     *
     * @param ip - image to be analyzed
     * @return ResultsTable which contains information about analysis results.
     */
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
        
        HashMap<String, Double> map = getFrameStats(ov);
        for (String key: map.keySet())
            res_.addValue(key, map.get(key));
        return res_;

    }
    
    /**
     * Analyzes the overlay and returns statistics about spot positions.
     *
     * @param ov - Overlay which contains spot position information
     * @return HashMap with spot position statistics.
     */
    private HashMap<String, Double> getFrameStats(Overlay ov) {
        HashMap<String, Double> map = new LinkedHashMap<String, Double>();
        
        double dist2;
        double[] min_distances = new double[ov.size()];
        for (int i=0; i<ov.size(); i++) {
            Rectangle rect_i = ov.get(i).getBounds();
            double min_dist2 = 1000000000.0;
            for (int j=0; j<ov.size(); j++) {
                if (i==j)
                    continue;
                Rectangle rect_j = ov.get(j).getBounds();
                dist2 = ((rect_i.getCenterX() - rect_j.getCenterX())* 
                         (rect_i.getCenterX() - rect_j.getCenterX()) ) +
                        ((rect_i.getCenterY() - rect_j.getCenterY())* 
                         (rect_i.getCenterY() - rect_j.getCenterY()) );
                if (dist2<min_dist2)
                    min_dist2 = dist2;
            }
            min_distances[i] = sqrt(min_dist2);
        }
        
        Arrays.sort(min_distances);
        double mean = 0.0;
        for (double val: min_distances)
            mean += val;
        mean /= ov.size();
        
        map.put("min-distance", min_distances[0]);
        map.put("mean-distance", mean);
        int p10 = (int) ceil((double) ov.size() / 10.0);
        map.put("p10-distance", min_distances[p10]);
        return map;
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
