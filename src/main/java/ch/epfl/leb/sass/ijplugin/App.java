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
package ch.epfl.leb.sass.ijplugin;

import ch.epfl.leb.sass.simulator.internal.ImageJSimulator;
import ch.epfl.leb.sass.models.Microscope;
import ch.epfl.leb.alica.interfaces.Analyzer;
import ch.epfl.leb.alica.interfaces.Controller;
import ch.epfl.leb.sass.utils.images.ImageS;
import ch.epfl.leb.sass.utils.images.ImageShapeException;
import ij.IJ;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Backend for the FIJI plugin GUI
 * @author Marcel Stefko
 */
public class App extends ImageJSimulator {
    private final ImageS imp;
    private final SimulatorStatusFrame statusFrame;
    private final int controller_tickrate;
    private Worker worker;
    private final InteractionWindow interaction_window;
    
    private final ArrayList<Double> generatorTrueSignal = new ArrayList<Double>();

    
    private final ArrayList<Double> controllerOutput = new ArrayList<Double>();
    private final ArrayList<Double> analyzerOutput = new ArrayList<Double>();
    private final ArrayList<Double> controllerSetpoint = new ArrayList<Double>();
    
    /**
     * Assemble the App from custom components.
     * 
     * @param microscope The microscope to be simulated.
     * @param analyzer An analyzer for processing images from the microscope.
     * @param controller A controller that adjusts the state of the microscope.
     */
    public App(
        Microscope microscope,
        Analyzer analyzer,
        Controller controller,
        int controller_tickrate
    ) throws ImageShapeException {
        super(microscope, analyzer, controller);
        if (controller_tickrate<1) {
            throw new IllegalArgumentException("Wrong controller tickrate!");
        }
        controller.setSetpoint(0.0);
        this.controller_tickrate = controller_tickrate;
        this.getNextImage();
        this.getNextImage();
        interaction_window = new InteractionWindow(analyzer, controller);
        imp = this.getStack();
        imp.view();
        
        // The units of the manual controller setpoint are the same as the
        // as the laser, not the analyzer output like the other controllers.
        String setpointLabel = "";
        if ("Manual".equals(controller.getName())) {
            setpointLabel = "mW";
        } else {
            setpointLabel = analyzer.getShortReturnDescription();
        }
        statusFrame = new SimulatorStatusFrame(
                this.getShortTrueSignalDescription(),
                analyzer.getShortReturnDescription(),
                setpointLabel,
                "mW"
        );
    }
    
    /**
     * Start continuously generating new images until stopped.
     */
    public void startSimulating() {
        worker = new Worker(this, controller, analyzer, imp);
        worker.start();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                interaction_window.setVisible(true);
            }
        });
    }
    
    /**
     * Stop generating new images.
     */
    public void stopSimulating() {
        worker.stop = true;
        try {
            worker.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Set new setpoint for the controller
     * @param value new setpoint value
     */
    public void setSetpoint(double value) {
        controller.setSetpoint(value);
    }
    
    /**
     * Return the handle for the status frame.
     * 
     * @return Plots with the simulation history.
     */
    public SimulatorStatusFrame getStatusFrame() {
        return statusFrame;
    }
    
    public int getControllerTickrate() {
        return controller_tickrate;
    }
    
    public ArrayList<Double> getGeneratorTrueSignal() {
        return generatorTrueSignal;
    }

    public ArrayList<Double> getControllerOutput() {
        return controllerOutput;
    }

    public ArrayList<Double> getAnalyzerOutput() {
        return analyzerOutput;
    }

    public ArrayList<Double> getControllerSetpoint() {
        return controllerSetpoint;
    }
}

class Worker extends Thread {
    public boolean stop;
    private final App app;
    private final Controller controller;
    private final Analyzer analyzer;
    private final ImageS imp;
    
    public Worker(App app, Controller controller, Analyzer active_analyzer, ImageS imp) {
        this.app = app;
        this.controller = controller;
        this.analyzer = active_analyzer;
        this.imp = imp;
        stop = false;
    }
    
    @Override
    public void run() {
        SimulatorStatusFrame statusFrame = app.getStatusFrame();
        while (!stop) {
            app.incrementCounter();
            
            try {
                ImageS is = app.getNextImage();
                    
                long time_start, time_end;
                time_start = System.nanoTime();
                analyzer.processImage(
                        is.getPixelData(0),
                        is.getWidth(),
                        is.getHeight(),
                        app.getObjectSpacePixelSize(),
                        time_start
                );
                time_end = System.nanoTime();
                System.out.format(
                        "%s: Image analyzed in %d microseconds.\n",
                        analyzer.getName(),
                        (time_end - time_start)/1000
                );

                if (app.getImageCount() % app.getControllerTickrate() == 0) {
                    //System.out.println(image_count);
                    controller.nextValue(analyzer.getBatchOutput());
                }
                app.setControlSignal(controller.getCurrentOutput());

                app.getAnalyzerOutput().add(analyzer.getIntermittentOutput());
                app.getControllerOutput().add(controller.getCurrentOutput());
                app.getControllerSetpoint().add(controller.getSetpoint());
                app.getGeneratorTrueSignal().add(app.getTrueSignal(app.getImageCount()));
            
             } catch (ImageShapeException ex) {
                IJ.showMessage(
                        "Critical error: simulated images don't match the " + 
                        "shape of the existing dataset"
                );
                ex.printStackTrace();
                stop = true;
            }
            
            imp.setSlice(imp.getSize());
            imp.updateView();
            try {
                sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Update the status frame.
            // Important: the status plots are only updated every 10 frames!!!
            if (app.getImageCount()%10==0) {
                int numFrames = app.getImageCount();
                statusFrame.updateGraph(
                        numFrames,
                        app.getGeneratorTrueSignal().get(numFrames - 1),
                        app.getAnalyzerOutput().get(numFrames - 1),
                        app.getControllerSetpoint().get(numFrames - 1),
                        app.getControllerOutput().get(numFrames - 1)
                );
            }
        }
    }
    
    private double getMin(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double min = arr[0];
        for (double d: arr) {
            min = min>d ? d : min;
        }
        return min;
    }
    
    private double getMax(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double max = arr[0];
        for (double d: arr) {
            max = max<d ? d : max;
        }
        return max;
    }
}
