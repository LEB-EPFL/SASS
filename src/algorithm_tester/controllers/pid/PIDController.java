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
package algorithm_tester.controllers.pid;

import algorithm_tester.EvaluationAlgorithm;
import algorithm_tester.FeedbackController;
import algorithm_tester.ImageGenerator;
import java.util.ArrayList;

/**
 *
 * @author stefko
 */
public class PIDController implements FeedbackController {
    private EvaluationAlgorithm analyzer;
    private ImageGenerator generator;
    private ArrayList<Double> history;
    double target;
    int counter;
    int interval;
    MiniPID miniPID;
    
    public PIDController() {
        counter = 0;
        history = new ArrayList<Double>();
        history.add(0.0); // padding so we can number from 1
        interval = 10;
        
        miniPID = new MiniPID(0.005,0.0025,0.0);
        miniPID.setOutputLimits(0.2, 5.0);
        miniPID.setOutputFilter(0.1);
    }
    
    @Override
    public void setTarget(double target) {
        this.target = target;
        miniPID.setSetpoint(target);
    }

    @Override
    public void adjust() {
        history.add(generator.getControlSignal());
        counter++;
        
        double error = analyzer.getCurrentErrorSignal();
        double output = miniPID.getOutput(error);
        
        System.out.format("No: %d\n", counter);
        System.out.format(" Error signal: %5.2f\n", error);
        System.out.format(" Output signal: %5.2f\n", output);
        
        generator.setControlSignal(output);
    }

    @Override
    public double getHistory(int image_no) {
        return history.get(image_no);
    }
    
    

    @Override
    public void setAnalyzer(EvaluationAlgorithm analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public void setGenerator(ImageGenerator generator) {
        this.generator = generator;
    }
    
}
