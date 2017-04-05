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
package algorithm_tester.analyzers;

import algorithm_tester.EvaluationAlgorithm;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefko
 */
public abstract class AbstractAnalyzer implements EvaluationAlgorithm {
    
    protected ArrayList<Double> output_history;
    protected LinkedHashMap<String, Integer> parameters;
    
    @Override
    public double getCurrentErrorSignal() {
        return getErrorSignal(output_history.size()-1);
    }
    
    @Override
    public LinkedHashMap<String, Integer> getCustomParameters() {
        return parameters;
    }
    
    @Override
    public double getErrorSignal(int image_no) {
        double signal;
        try {
            signal = output_history.get(image_no);
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            signal = 0.0;
        }
        return signal;
    }
    
}
