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
package algorithm_tester.controllers.pid;

import algorithm_tester.EvaluationAlgorithm;
import algorithm_tester.FeedbackController;
import algorithm_tester.ImageGenerator;
import ij.gui.GenericDialog;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Wrapper around MiniPID controller.
 * @author Marcel Stefko
 */
public class PIDController implements FeedbackController {
    private final HashMap<String,Double> settings;
    private EvaluationAlgorithm analyzer;
    private ImageGenerator generator;
    private final ArrayList<Double> history;
    private final ArrayList<Double> setpoint_history;
    double target;
    int counter;
    int interval;
    MiniPID miniPID;
    
    /**
     * Initialize PID controller with default settings.
     */
    public PIDController(boolean showDialog) {
        settings = new HashMap<String,Double>();
        if (!(showDialog)) {
            settings.put("P", 0.005);
            settings.put("I", 0.0025);
            settings.put("D", 0.0);
            settings.put("limit-low", 0.2);
            settings.put("limit-high", 5.0);
            settings.put("output-filter", 0.1);
        } else {
            getSettingsFromDialog();
        }
        counter = 0;
        history = new ArrayList<Double>();
        history.add(0.0); // padding so we can number from 1
        setpoint_history = new ArrayList<Double>();
        setpoint_history.add(0.0); // padding so we can number from 1
        interval = 10;
        
        miniPID = new MiniPID(settings.get("P"),
                settings.get("I"),settings.get("D"));
        miniPID.setOutputLimits(settings.get("limit-low"), settings.get("limit-high"));
        miniPID.setOutputFilter(settings.get("output-filter"));
    }
    
    @Override
    public void setTarget(double target) {
        this.target = target;
        miniPID.setSetpoint(target);
        settings.put("setpoint", target);
    }

    @Override
    public void adjust() {
        history.add(generator.getControlSignal());
        setpoint_history.add(target);
        counter++;
        
        double error = analyzer.getCurrentErrorSignal();
        double output = miniPID.getOutput(error);
        
        System.out.format("No: %d\n", counter);
        System.out.format(" Error signal: %5.2f\n", error);
        System.out.format(" Output signal: %5.2f\n", output);
        
        generator.setControlSignal(output);
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
    public HashMap<String, Double> getSettings() {
        return settings;
    }
    
    private void getSettingsFromDialog() {
        GenericDialog gd = new GenericDialog("Device initialization");
        gd.addMessage("PID controller");
        
        gd.addNumericField("P", 0.005, 5);
        gd.addNumericField("I", 0.0025, 5);
        gd.addNumericField("D", 0.0, 5);
        gd.addNumericField("limit-low", 0.2, 2);
        gd.addNumericField("limit-high",5.0,2);
        gd.addNumericField("output-filter",0.1,3);
        gd.showDialog();
        
        
        
        
        settings.put("P", gd.getNextNumber());
        settings.put("I", gd.getNextNumber());
        settings.put("D", gd.getNextNumber());
        settings.put("limit-low", gd.getNextNumber());
        settings.put("limit-high", gd.getNextNumber());
        settings.put("output-filter", gd.getNextNumber());
        
    }
}
