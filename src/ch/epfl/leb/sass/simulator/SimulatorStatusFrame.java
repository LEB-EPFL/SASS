/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
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
package ch.epfl.leb.sass.simulator;

import java.text.DecimalFormat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Frame that displays the current status and recent history of the simulation.
 * 
 * The layout for the status frame was inspired by Karl Bellve's pgFocus GUI:
 * http://big.umassmed.edu/wiki/index.php/PgFocus
 * 
 * @author Kyle M. Douglass
 */
public class SimulatorStatusFrame extends javax.swing.JFrame {
    
    public final int SUBPLOT_COUNT = 4;
    private XYSeriesCollection datasets[];
    
    /** 
     * Creates a new status frame.
     * 
     * @param groundTruthYLabel The y-axis label for the ground truth signal.
     * @param analyzerYLabel The units output by the analyzer.
     * @param setpointYLabel The units of the controller setpoint.
     * @param outputYLabel The units output by the controller.
     */
    public SimulatorStatusFrame(
            String groundTruthYLabel,
            String analyzerYLabel,
            String setpointYLabel,
            String outputYLabel
    ) {
        String seriesLabel = "";
        String yLabel = "";
        CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(
                new NumberAxis("Simulation Outputs")
        );
        Font yLabelFont = new Font("Dialog", Font.PLAIN, 10);
        datasets = new XYSeriesCollection[4];
        for (int i = 0; i < SUBPLOT_COUNT; i++) {
            switch (i) {
                case 0:
                    seriesLabel = "True density";
                    yLabel = groundTruthYLabel;
                    break;
                case 1:
                    seriesLabel = "Analyzer output";
                    yLabel = analyzerYLabel;
                    break;
                case 2:
                    seriesLabel = "Setpoint";
                    yLabel = setpointYLabel;
                    break;
                case 3:
                    seriesLabel = "Controller output";
                    yLabel = outputYLabel;
                    break;
            }
            
            XYSeries timeseries = new XYSeries(seriesLabel);
            datasets[i] = new XYSeriesCollection(timeseries);

            NumberAxis numberaxis = new NumberAxis(yLabel);
            numberaxis.setAutoRangeIncludesZero(false);
            numberaxis.setNumberFormatOverride(new DecimalFormat("###0.00")); 
            XYPlot xyplot = new XYPlot(
                    datasets[i],
                    null,
                    numberaxis,
                    new StandardXYItemRenderer()
            );
            xyplot.setBackgroundPaint(Color.lightGray);
            xyplot.setDomainGridlinePaint(Color.white);
            xyplot.setRangeGridlinePaint(Color.white);
            xyplot.getRangeAxis().setLabelFont(yLabelFont);
            combineddomainxyplot.add(xyplot);
        }

        JFreeChart jfreechart = new JFreeChart(
                "",
                JFreeChart.DEFAULT_TITLE_FONT,
                combineddomainxyplot,
                true
        );
        jfreechart.setBorderPaint(Color.black);
        jfreechart.setBorderVisible(true);
        jfreechart.setBackgroundPaint(Color.white);
        combineddomainxyplot.setBackgroundPaint(Color.lightGray);
        combineddomainxyplot.setDomainGridlinePaint(Color.white);
        combineddomainxyplot.setRangeGridlinePaint(Color.white);
        ValueAxis valueaxis = combineddomainxyplot.getDomainAxis();
        valueaxis.setAutoRange(true);
        // Number of frames to display
        valueaxis.setFixedAutoRange(2000D);

        ChartPanel chartpanel = new ChartPanel(jfreechart);

        chartpanel.setPreferredSize(new Dimension(800, 500));
        chartpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chartpanel.setVisible(true);
        
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(chartpanel, BorderLayout.NORTH);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(jPanel);
        pack();
        setVisible(true);
        
    }
    
    /**
     * Adds a single new time point to the plot.
     *
     * @param frame The frame number
     * @param trueCount The true number of emitting molecules.
     * @param estimate Analyzer's estimate of the number of emitting molecules.
     * @param setpoint The controller's setpoint value.
     * @param laser The output of the laser.
     */
    public void updateGraph(
            int frame,
            double trueCount,
            double estimate,
            double setpoint,
            double laser) {
        
        double[] lastValue = new double[4];
        lastValue[0] = trueCount;
        lastValue[1] = estimate;
        lastValue[2] = setpoint;
        lastValue[3] = laser;

        for (int i = 0; i < SUBPLOT_COUNT; i++) {
            datasets[i].getSeries(0).add(frame, lastValue[i]);
        }
    } 
}
