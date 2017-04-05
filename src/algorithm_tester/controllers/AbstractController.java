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
    protected final ArrayList<Double> history;
    protected final ArrayList<Double> setpoint_history;
    
    protected final HashMap<String,Double> settings;
    
    protected double target;
    protected int counter;
    
    protected EvaluationAlgorithm analyzer;
    protected ImageGenerator generator;
    
    public AbstractController() {
        history = new ArrayList<Double>();
        history.add(0.0); // padding so we can number from 1
        setpoint_history = new ArrayList<Double>();
        setpoint_history.add(0.0); // padding so we can number from 1
        counter = 0;
        
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
        return history.get(image_no);
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
