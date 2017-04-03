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
package algorithm_tester.controllers.simple;

import algorithm_tester.EvaluationAlgorithm;
import algorithm_tester.FeedbackController;
import algorithm_tester.ImageGenerator;
import java.util.ArrayList;

/**
 *
 * @author stefko
 */
public class SimpleController implements FeedbackController {
    private EvaluationAlgorithm analyzer;
    private ImageGenerator generator;
    private ArrayList<Double> history;
    double target;
    int counter;
    int interval;
    
    public SimpleController() {
        counter = 0;
        history = new ArrayList<Double>();
        history.add(0.0); // padding so we can number from 1
        interval = 10;
    }
    
    public void setTarget(double target) {
        this.target = target;
    }
    
    private void decrementPower() {
        System.out.println("Decrementing power.");
        double current = generator.getControlSignal();
        generator.setControlSignal(current/1.2);
    }
    
    private void incrementPower() {
        System.out.println("Incrementing power.");
        double current = generator.getControlSignal();
        generator.setControlSignal(current*1.2);
    }
    
    
    @Override
    public void adjust() {
        history.add(generator.getControlSignal());
        counter++;
        if ((counter % interval) != 0) {
            return;
        }
        double error = analyzer.getCurrentErrorSignal();
        double r = error/target;
        System.out.format("Error: %5.2f, target: %5.2f, r: %5.2f\n", error, target, r);
        if (r>1.15) {
            decrementPower();
        } else if (r<0.9) {
            incrementPower();
        }
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
