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


import algorithm_tester.controllers.AbstractController;
import ij.gui.GenericDialog;

/**
 * Wrapper around MiniPID controller.
 * @author Marcel Stefko
 */
public class PIDController extends AbstractController {
    int interval;
    MiniPID miniPID;
    
    /**
     * Initialize PID controller with default settings or via dialog.
     * @param showDialog if true, dialog is shown, otherwise default settings
     * are used
     */
    public PIDController(boolean showDialog) {
        super();      
        if (!(showDialog)) {
            settings.put("P", 0.005);
            settings.put("I", 0.0025);
            settings.put("D", 0.0);
            settings.put("limit-low", 0.0);
            settings.put("limit-high", 5.0);
            settings.put("output-filter", 0.1);
        } else {
            setSettingsFromDialog();
        }

        interval = 10;
        miniPID = new MiniPID(settings.get("P"),
                settings.get("I"),settings.get("D"));
        miniPID.setOutputLimits(settings.get("limit-low"), settings.get("limit-high"));
        miniPID.setOutputFilter(settings.get("output-filter"));
    }
    
    public PIDController(double P, double I, double D, double limit_low, double limit_high,
                double output_filter) {
        super();
        settings.put("P", P);
        settings.put("I", I);
        settings.put("D", D);
        settings.put("limit-low", limit_low);
        settings.put("limit-high", limit_high);
        settings.put("output-filter", output_filter);
        interval = 10;
        miniPID = new MiniPID(settings.get("P"),
                settings.get("I"),settings.get("D"));
        miniPID.setOutputLimits(settings.get("limit-low"), settings.get("limit-high"));
        miniPID.setOutputFilter(settings.get("output-filter"));
    }
    
    @Override
    public void setTarget(double target) {
        this.setpoint = target;
        miniPID.setSetpoint(target);
        settings.put("setpoint", target);
    }

    @Override
    public void adjust() {
        output_history.add(generator.getControlSignal());
        setpoint_history.add(setpoint);
        image_count++;
        
        double error = analyzer.getCurrentErrorSignal();
        double output = miniPID.getOutput(error);
        
        System.out.format("No: %d\n", image_count);
        System.out.format(" Error signal: %5.2f\n", error);
        System.out.format(" Output signal: %5.2f\n", output);
        
        generator.setControlSignal(output);
    }

    
    private void setSettingsFromDialog() {
        GenericDialog gd = new GenericDialog("Device initialization");
        gd.addMessage("PID controller");
        
        gd.addNumericField("P", 0.005, 5);
        gd.addNumericField("I", 0.0025, 5);
        gd.addNumericField("D", 0.0, 5);
        gd.addNumericField("limit-low", 0.0, 2);
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
