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
package algorithm_tester;

/**
 * Interface for a laser intensity controller.
 * @author Marcel Stefko
 */
public interface FeedbackController {
    
    /**
     * Sets target value for error signal.
     * @param target desired error signal value (setpoint)
     */
    public void setTarget(double target);
    
    /**
     * Defines which EvaluationAlgorithms output should be used as error signal.
     * @param analyzer 
     */
    public void setAnalyzer(EvaluationAlgorithm analyzer);
    
    /**
     * Defines which generator should be controlled by the feedback loop.
     * @param generator
     */
    public void setGenerator(ImageGenerator generator);
    
    /**
     * Update internal state after new image has been generated, and send
     * feedback to the generator.
     */
    public void adjust();
    
    /**
     * Returns the controller output value (i.e. current laser power) for given
     * image number.
     * @param image_no 1-based number of desired image
     * @return controller output value at the time the image was acquired
     */
    public double getHistory(int image_no);
}
