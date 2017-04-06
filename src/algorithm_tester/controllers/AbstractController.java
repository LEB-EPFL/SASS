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
package algorithm_tester.controllers;

import algorithm_tester.EvaluationAlgorithm;
import algorithm_tester.FeedbackController;
import algorithm_tester.ImageGenerator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author stefko
 */
public abstract class AbstractController implements FeedbackController {

    /**
     * History of output values of the controller(i.e. laser power values).
     * 1-based
     */
    protected final ArrayList<Double> output_history;

    /**
     * History of setpoint of the controller. 1-based
     */
    protected final ArrayList<Double> setpoint_history;
    
    /**
     * Custom settings map of the controller.
     */
    protected final HashMap<String,Double> settings;
    
    /**
     * Current setpoint value for the controller.
     */
    protected double setpoint;

    /**
     * Number of analyzed images.
     */
    protected int image_count;
    
    /**
     * Analyzer whose output value is compared with the setpoint to derive
     * the error signal.
     */
    protected EvaluationAlgorithm analyzer;

    /**
     * Generator to which the controller output will be fed.
     */
    protected ImageGenerator generator;
    
    /**
     * Initializes the history arrays and ensures 1-based indexing, sets up
     * image counter and settings.
     */
    public AbstractController() {
        output_history = new ArrayList<Double>();
        output_history.add(0.0); // padding so we can number from 1
        setpoint_history = new ArrayList<Double>();
        setpoint_history.add(0.0); // padding so we can number from 1
        image_count = 0;
        
        settings = new HashMap<String,Double>();
    }
    
    @Override
    public void setAnalyzer(EvaluationAlgorithm analyzer) {
        this.analyzer = analyzer;
    }
    
    @Override
    public EvaluationAlgorithm getAnalyzer() {
        return analyzer;
    }

    @Override
    public void setGenerator(ImageGenerator generator) {
        this.generator = generator;
    }
    
    @Override
    public double getOutputHistory(int image_no) {
        return output_history.get(image_no);
    }
    
    @Override
    public double getSetpointHistory(int image_no) {
        return setpoint_history.get(image_no);
    }
    
    @Override
    public HashMap<String, Double> getSettings() {
        return settings;
    }
}
