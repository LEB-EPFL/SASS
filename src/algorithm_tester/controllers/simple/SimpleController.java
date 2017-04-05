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
package algorithm_tester.controllers.simple;

import algorithm_tester.controllers.AbstractController;

/**
 * Controller akin to one implemented by the original AutoLase plugin.
 * @author Marcel Stefko
 */
public class SimpleController extends AbstractController {
    int interval;
    
    public SimpleController() {
        super();
        settings.put("interval", 10.0);
        interval = 10;
    }
    
    @Override
    public void adjust() {
        history.add(generator.getControlSignal());
        setpoint_history.add(target);
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
    public void setTarget(double target) {
        this.target = target;
    }
    
    // Decreases power by 1/5th
    private void decrementPower() {
        System.out.println("Decrementing power.");
        double current = generator.getControlSignal();
        generator.setControlSignal(current/1.2);
    }
    
    // Increases power by 1/5th
    private void incrementPower() {
        System.out.println("Incrementing power.");
        double current = generator.getControlSignal();
        generator.setControlSignal(current*1.2);
    }
    

    
}
