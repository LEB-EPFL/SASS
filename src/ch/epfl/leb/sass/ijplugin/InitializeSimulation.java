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


import ch.epfl.leb.sass.ijplugin.ButtonGroupUtils;
import ch.epfl.leb.sass.simulator.generators.realtime.SimEngine;
import ch.epfl.leb.sass.simulator.generators.realtime.Microscope;
import ch.epfl.leb.sass.simulator.generators.realtime.RNG;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.dynamics.SimpleDynamics;
import ch.epfl.leb.sass.simulator.generators.realtime.obstructors.commands.GenerateFiducialsRandom2D;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.FluorophoreCommandBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.GenerateFluorophoresRandom2D;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.GenerateFluorophoresGrid2D;
import ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands.GenerateFluorophoresFromCSV;
import ch.epfl.leb.sass.simulator.generators.realtime.backgrounds.commands.BackgroundCommandBuilder;
import ch.epfl.leb.sass.simulator.generators.realtime.backgrounds.commands.GenerateUniformBackground;
import ch.epfl.leb.sass.simulator.generators.realtime.components.*;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.Gaussian2D;
import ch.epfl.leb.alica.Analyzer;
import ch.epfl.leb.alica.analyzers.AnalyzerFactory;
import ch.epfl.leb.alica.analyzers.AnalyzerSetupPanel;
import ch.epfl.leb.alica.controllers.ControllerFactory;
import ch.epfl.leb.sass.simulator.generators.realtime.backgrounds.commands.GenerateBackgroundFromFile;
import ij.IJ;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Frame for basic setup of a simulation.
 * @author Marcel Stefko
 */
public class InitializeSimulation extends java.awt.Dialog {

    Model model = new Model();
    File emitterCsvFile = new File("");
    File backgroundTifFile = new File("");
    GUI main;
    private final AnalyzerFactory analyzer_factory;
    private final ControllerFactory controller_factory;
    
    /**
     * Assemble the frame and display it
     * @param parent
     * @param modal should the window be persistent
     * @param main GUI to notify
     */
    public InitializeSimulation(java.awt.Frame parent, boolean modal, GUI main) {
        super(parent, modal);
        this.main = main;
        this.analyzer_factory = new AnalyzerFactory();
        this.controller_factory = new ControllerFactory();
        initComponents();
        
        // Populate the dropdown menus of analyzer, controller and laser
        cb_analyzer_setup.removeAllItems();
        for (String key: analyzer_factory.getProductNameList()) {
            cb_analyzer_setup.addItem(key);
        }
        cb_analyzer_setup.setSelectedItem(analyzer_factory.getSelectedProductName());
        
        cb_controller_setup.removeAllItems();
        for (String key: controller_factory.getProductNameList()) {
            cb_controller_setup.addItem(key);
        }
        cb_controller_setup.setSelectedItem(controller_factory.getSelectedProductName());
        

        // update the setup panels
        updateAnalyzerSetupPanel();
        updateControllerSetupPanel();
    }
    
    private void updateAnalyzerSetupPanel() {
        analyzer_panel.removeAll();
        javax.swing.JPanel panel = analyzer_factory.getSelectedSetupPanel();
        analyzer_panel.add(panel);
        panel.setBounds(5,5,200,150);
        panel.revalidate();
        panel.repaint();
        analyzer_panel.validate();
        analyzer_panel.repaint();
    }
    
    private void updateControllerSetupPanel() {
        controller_panel.removeAll();
        javax.swing.JPanel panel = controller_factory.getSelectedSetupPanel();
        controller_panel.add(panel);
        panel.setBounds(5,5,200,150);
        panel.revalidate();
        panel.repaint();
        controller_panel.validate();
        controller_panel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emittersButtons = new javax.swing.ButtonGroup();
        backgroundButtons = new javax.swing.ButtonGroup();
        panel6 = new java.awt.Panel();
        jLabel1 = new javax.swing.JLabel();
        cb_analyzer_setup = new javax.swing.JComboBox();
        analyzer_panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cb_controller_setup = new javax.swing.JComboBox();
        controller_panel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        e_controller_tickrate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        e_max_controller_output = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cameraSizeX = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cameraSizeY = new javax.swing.JTextField();
        cameraReadoutNoise = new javax.swing.JTextField();
        cameraDarkCurrent = new javax.swing.JTextField();
        cameraQuantumEfficiency = new javax.swing.JTextField();
        cameraAduPerElectron = new javax.swing.JTextField();
        cameraEmGain = new javax.swing.JTextField();
        cameraBaseline = new javax.swing.JTextField();
        cameraPixelSize = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        objectiveNA = new javax.swing.JTextField();
        objectiveMag = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        fluorPropSignal = new javax.swing.JTextField();
        fluorPropWavelength = new javax.swing.JTextField();
        fluorPropTOn = new javax.swing.JTextField();
        fluorPropTOff = new javax.swing.JTextField();
        fluorPropTBleached = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        laserMinPower = new javax.swing.JTextField();
        laserMaxPower = new javax.swing.JTextField();
        laserInitPower = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        stageZ = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        emittersRandomButton = new javax.swing.JRadioButton();
        jLabel45 = new javax.swing.JLabel();
        emittersRandomNumber = new javax.swing.JTextField();
        emittersCsvButton = new javax.swing.JRadioButton();
        emittersChooseFile = new javax.swing.JButton();
        emittersGridButton = new javax.swing.JRadioButton();
        emittersGridSpacing = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        fiducialsNumber = new javax.swing.JTextField();
        fiducialsSignal = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        backgroundUniformButton = new javax.swing.JRadioButton();
        backgroundTifButton = new javax.swing.JRadioButton();
        jLabel52 = new javax.swing.JLabel();
        backgroundUniformSignal = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        backgroundChooseFile = new javax.swing.JButton();
        initializeSimulation = new javax.swing.JButton();

        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("Analyzer:");

        cb_analyzer_setup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cb_analyzer_setupPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        analyzer_panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        analyzer_panel.setPreferredSize(new java.awt.Dimension(210, 160));

        javax.swing.GroupLayout analyzer_panelLayout = new javax.swing.GroupLayout(analyzer_panel);
        analyzer_panel.setLayout(analyzer_panelLayout);
        analyzer_panelLayout.setHorizontalGroup(
            analyzer_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        analyzer_panelLayout.setVerticalGroup(
            analyzer_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel6Layout = new javax.swing.GroupLayout(panel6);
        panel6.setLayout(panel6Layout);
        panel6Layout.setHorizontalGroup(
            panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(cb_analyzer_setup, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel6Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(analyzer_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel6Layout.setVerticalGroup(
            panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cb_analyzer_setup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(analyzer_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel2.setText("Controller:");

        cb_controller_setup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cb_controller_setupPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        controller_panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        controller_panel.setPreferredSize(new java.awt.Dimension(210, 160));

        javax.swing.GroupLayout controller_panelLayout = new javax.swing.GroupLayout(controller_panel);
        controller_panel.setLayout(controller_panelLayout);
        controller_panelLayout.setHorizontalGroup(
            controller_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        controller_panelLayout.setVerticalGroup(
            controller_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        jLabel3.setText("Tick rate [frames]:");

        e_controller_tickrate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        e_controller_tickrate.setText("40");

        jLabel4.setText("Max output:");

        e_max_controller_output.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        e_max_controller_output.setText("50");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(controller_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(cb_controller_setup, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(e_max_controller_output, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                    .addComponent(e_controller_tickrate))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cb_controller_setup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(e_controller_tickrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(e_max_controller_output, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(controller_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Camera"));

        jLabel5.setText("Size, x");

        cameraSizeX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraSizeX.setText("32");

        jLabel6.setText("Size, y");

        jLabel7.setText("Readout noise");

        jLabel8.setText("Dark current");

        jLabel9.setText("Quantum efficiency");

        jLabel10.setText("Sensitivity");

        jLabel11.setText("EM gain");

        jLabel12.setText("Baseline");

        jLabel13.setText("Pixel size");

        cameraSizeY.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraSizeY.setText("32");

        cameraReadoutNoise.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraReadoutNoise.setText("1.6");

        cameraDarkCurrent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraDarkCurrent.setText("0.06");

        cameraQuantumEfficiency.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraQuantumEfficiency.setText("0.8");

        cameraAduPerElectron.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraAduPerElectron.setText("2.2");

        cameraEmGain.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraEmGain.setText("0");

        cameraBaseline.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraBaseline.setText("100");
        cameraBaseline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cameraBaselineActionPerformed(evt);
            }
        });

        cameraPixelSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cameraPixelSize.setText("6.5");

        jLabel14.setText("pixels");

        jLabel15.setText("pixels");

        jLabel16.setText("electrons");

        jLabel17.setText("electrons / frame");

        jLabel18.setText("--");

        jLabel19.setText("ADU / electron");

        jLabel20.setText("--");

        jLabel21.setText("ADU");

        jLabel22.setText("µm");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraSizeX, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraSizeY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraReadoutNoise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraDarkCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraQuantumEfficiency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraAduPerElectron, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraEmGain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraBaseline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cameraPixelSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cameraAduPerElectron, cameraBaseline, cameraDarkCurrent, cameraEmGain, cameraPixelSize, cameraQuantumEfficiency, cameraReadoutNoise, cameraSizeX, cameraSizeY});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cameraSizeX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cameraSizeY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cameraReadoutNoise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cameraDarkCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cameraQuantumEfficiency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cameraAduPerElectron, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cameraEmGain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cameraBaseline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cameraPixelSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Objective"));

        jLabel23.setText("Numerical aperture");

        jLabel24.setText("Magnification");

        objectiveNA.setText("1.3");

        objectiveMag.setText("60");

        jLabel25.setText("--");

        jLabel26.setText("--");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(objectiveMag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(objectiveNA, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {objectiveMag, objectiveNA});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(objectiveNA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(objectiveMag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Fluorophores"));

        jLabel33.setText("Signal");

        jLabel34.setText("Wavelength");

        jLabel35.setText("tOn");

        jLabel36.setText("tOff");

        jLabel37.setText("tBleached");

        fluorPropSignal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fluorPropSignal.setText("1500");

        fluorPropWavelength.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fluorPropWavelength.setText("0.68");

        fluorPropTOn.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fluorPropTOn.setText("3");

        fluorPropTOff.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fluorPropTOff.setText("100");
        fluorPropTOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fluorPropTOffActionPerformed(evt);
            }
        });

        fluorPropTBleached.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fluorPropTBleached.setText("5000");

        jLabel38.setText("photons / frame");

        jLabel39.setText("µm");

        jLabel40.setText("frames");

        jLabel41.setText("frames");

        jLabel42.setText("frames");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fluorPropWavelength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fluorPropTOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fluorPropTOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fluorPropTBleached, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fluorPropSignal, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fluorPropSignal, fluorPropTBleached, fluorPropTOff, fluorPropTOn, fluorPropWavelength});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(fluorPropSignal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(fluorPropWavelength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(fluorPropTOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(fluorPropTOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(fluorPropTBleached, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Laser"));

        jLabel27.setText("Minimum power");

        jLabel28.setText("Maximum power");

        jLabel29.setText("Initial power");

        laserMinPower.setText("0.0");

        laserMaxPower.setText("100.0");

        laserInitPower.setText("1.0");

        jLabel30.setText("mW");

        jLabel31.setText("mW");

        jLabel32.setText("mW");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(laserInitPower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(laserMinPower)
                            .addComponent(laserMaxPower, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {laserInitPower, laserMaxPower, laserMinPower});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(laserMinPower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(laserMaxPower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(laserInitPower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Stage"));

        jLabel43.setText("z-position");

        stageZ.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        stageZ.setText("0.0");

        jLabel44.setText("µm");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stageZ, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel44)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(stageZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Emitters"));

        emittersButtons.add(emittersRandomButton);
        emittersRandomButton.setSelected(true);
        emittersRandomButton.setText("Random");
        emittersRandomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emittersRandomButtonActionPerformed(evt);
            }
        });

        jLabel45.setText("Number");

        emittersRandomNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        emittersRandomNumber.setText("100");

        emittersButtons.add(emittersCsvButton);
        emittersCsvButton.setText("From .csv file");
        emittersCsvButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emittersCsvButtonActionPerformed(evt);
            }
        });
        emittersCsvButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                emittersCsvButtonPropertyChange(evt);
            }
        });

        emittersChooseFile.setText("Choose file...");
        emittersChooseFile.setEnabled(false);
        emittersChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emittersChooseFileActionPerformed(evt);
            }
        });

        emittersButtons.add(emittersGridButton);
        emittersGridButton.setText("Grid");
        emittersGridButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emittersGridButtonActionPerformed(evt);
            }
        });

        emittersGridSpacing.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        emittersGridSpacing.setText("5");
        emittersGridSpacing.setEnabled(false);

        jLabel46.setText("Spacing");

        jLabel47.setText("pixels");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emittersRandomButton)
                    .addComponent(emittersGridButton)
                    .addComponent(emittersCsvButton)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emittersChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel46)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emittersGridSpacing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jLabel47))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emittersRandomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {emittersGridSpacing, emittersRandomNumber});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(emittersRandomButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(emittersRandomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emittersGridButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emittersGridSpacing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emittersCsvButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(emittersChooseFile)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Fiducials"));

        jLabel48.setText("Number");

        jLabel49.setText("Signal");

        fiducialsNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fiducialsNumber.setText("1");

        fiducialsSignal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fiducialsSignal.setText("1000");

        jLabel50.setText("--");

        jLabel51.setText("photons / frame");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fiducialsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fiducialsSignal, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50)
                    .addComponent(jLabel51))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fiducialsNumber, fiducialsSignal});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(fiducialsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(fiducialsSignal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Background"));

        backgroundButtons.add(backgroundUniformButton);
        backgroundUniformButton.setSelected(true);
        backgroundUniformButton.setText("Uniform");
        backgroundUniformButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundUniformButtonActionPerformed(evt);
            }
        });

        backgroundButtons.add(backgroundTifButton);
        backgroundTifButton.setText("From .tif file");
        backgroundTifButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundTifButtonActionPerformed(evt);
            }
        });

        jLabel52.setText("Signal");

        backgroundUniformSignal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        backgroundUniformSignal.setText("10");

        jLabel53.setText("photons");

        backgroundChooseFile.setText("Choose file...");
        backgroundChooseFile.setEnabled(false);
        backgroundChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundChooseFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(backgroundChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel52)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(backgroundUniformSignal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel53))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(backgroundUniformButton)
                                    .addComponent(backgroundTifButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 138, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backgroundUniformButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(backgroundUniformSignal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backgroundTifButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backgroundChooseFile)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        initializeSimulation.setText("Initialize");
        initializeSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initializeSimulationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addGap(532, 532, 532)
                .addComponent(initializeSimulation)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(initializeSimulation)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void cb_analyzer_setupPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cb_analyzer_setupPopupMenuWillBecomeInvisible
        analyzer_factory.selectProduct((String) cb_analyzer_setup.getSelectedItem());
        updateAnalyzerSetupPanel();
    }//GEN-LAST:event_cb_analyzer_setupPopupMenuWillBecomeInvisible

    private void cb_controller_setupPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cb_controller_setupPopupMenuWillBecomeInvisible
        controller_factory.selectProduct((String) cb_controller_setup.getSelectedItem());
        updateControllerSetupPanel();
    }//GEN-LAST:event_cb_controller_setupPopupMenuWillBecomeInvisible

    private void fluorPropTOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fluorPropTOffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fluorPropTOffActionPerformed

    private void cameraBaselineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cameraBaselineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cameraBaselineActionPerformed

    private void backgroundChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backgroundChooseFileActionPerformed
        JFileChooser fc = new JFileChooser();
        int returnVal;
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        //set a default filename 
        fc.setSelectedFile(new File("background.tif"));
        //Set an extension filter
        fc.setFileFilter(new FileNameExtensionFilter("Tif file","tif"));
        returnVal = fc.showOpenDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        backgroundTifFile = fc.getSelectedFile();
        // background_file_label.setText(backgroundTifFile.getName());
    }//GEN-LAST:event_backgroundChooseFileActionPerformed

    private void backgroundUniformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backgroundUniformButtonActionPerformed
        backgroundUniformSignal.setEnabled(true);
        backgroundChooseFile.setEnabled(false);
    }//GEN-LAST:event_backgroundUniformButtonActionPerformed

    private void backgroundTifButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backgroundTifButtonActionPerformed
        backgroundUniformSignal.setEnabled(false);
        backgroundChooseFile.setEnabled(true);
    }//GEN-LAST:event_backgroundTifButtonActionPerformed

    private void emittersGridButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emittersGridButtonActionPerformed
        emittersRandomNumber.setEnabled(false);
        emittersGridSpacing.setEnabled(true);
        emittersChooseFile.setEnabled(false);
    }//GEN-LAST:event_emittersGridButtonActionPerformed

    private void emittersChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emittersChooseFileActionPerformed
        JFileChooser fc = new JFileChooser();
        int returnVal;
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        //set a default filename
        fc.setSelectedFile(new File("emitters.csv"));
        //Set an extension filter
        fc.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));
        returnVal = fc.showOpenDialog(null);
        if  (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        emitterCsvFile = fc.getSelectedFile();
        //emitter_file_label.setText(emitterCsvFile.getName());
    }//GEN-LAST:event_emittersChooseFileActionPerformed

    private void emittersCsvButtonPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_emittersCsvButtonPropertyChange

    }//GEN-LAST:event_emittersCsvButtonPropertyChange

    private void emittersCsvButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emittersCsvButtonActionPerformed
        emittersRandomNumber.setEnabled(false);
        emittersGridSpacing.setEnabled(false);
        emittersChooseFile.setEnabled(true);
    }//GEN-LAST:event_emittersCsvButtonActionPerformed

    private void emittersRandomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emittersRandomButtonActionPerformed
        emittersRandomNumber.setEnabled(true);
        emittersGridSpacing.setEnabled(false);
        emittersChooseFile.setEnabled(false);
    }//GEN-LAST:event_emittersRandomButtonActionPerformed

    private void initializeSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initializeSimulationActionPerformed

        try {
            model.setCameraNX(Integer.parseInt(cameraSizeX.getText()));
            model.setCameraNY(Integer.parseInt(cameraSizeY.getText()));
            model.setCameraReadoutNoise(Double.parseDouble(cameraReadoutNoise.getText()));
            model.setCameraDarkCurrent(Double.parseDouble(cameraDarkCurrent.getText()));
            model.setCameraQuantumEfficiency(Double.parseDouble(cameraQuantumEfficiency.getText()));
            model.setCameraAduPerElectron(Double.parseDouble(cameraAduPerElectron.getText()));
            model.setCameraEmGain(Integer.parseInt(cameraEmGain.getText()));
            model.setCameraBaseline(Integer.parseInt(cameraBaseline.getText()));
            model.setCameraPixelSize(Double.parseDouble(cameraPixelSize.getText()));
            
            model.setObjectiveNa(Double.parseDouble(objectiveNA.getText()));
            model.setObjectiveMag(Double.parseDouble(objectiveMag.getText()));
            
            model.setStageX(0);
            model.setStageY(0);
            model.setStageZ(Double.parseDouble(stageZ.getText()));

            model.setFluorophoreSignal(Double.parseDouble(fluorPropSignal.getText()));
            model.setFluorophoreWavelength(Double.parseDouble(fluorPropWavelength.getText()));
            model.setFluorophoreTOn(Double.parseDouble(fluorPropTOn.getText()));
            model.setFluorophoreTOff(Double.parseDouble(fluorPropTOff.getText()));
            model.setFluorophoreTBl(Double.parseDouble(fluorPropTBleached.getText()));

            model.setLaserCurrentPower(Double.parseDouble(laserInitPower.getText()));
            model.setLaserMinPower(Double.parseDouble(laserMinPower.getText()));
            model.setLaserMaxPower(Double.parseDouble(laserMaxPower.getText()));
            
            model.setFiducialsNumber(Integer.parseInt(fiducialsNumber.getText()));
            model.setFiducialsSignal(Integer.parseInt(fiducialsSignal.getText()));
            
        } catch (NumberFormatException ex) {
            IJ.showMessage("Error in parsing of numerical values.");
            return;
        } catch (Exception ex) {
            IJ.showMessage("Error in device component intialization.");
            return;
        }
        
        String selectedEmitterButton = ButtonGroupUtils.getSelectedButtonText(this.emittersButtons);
        model.setEmittersCurrentSelection(selectedEmitterButton);
        model.setEmittersRandomNumber(Integer.parseInt(emittersRandomNumber.getText()));
        model.setEmittersGridSpacing(Integer.parseInt(emittersGridSpacing.getText()));
        model.setEmittersCsvFile(emitterCsvFile.toString());
        model.setEmittersRandomButtonText(emittersRandomButton.getText());
        model.setEmittersGridButtonText(emittersGridButton.getText());
        model.setEmittersCsvFileButtonText(emittersCsvButton.getText());
        
        String selectedBackgroundButton = ButtonGroupUtils.getSelectedButtonText(this.backgroundButtons);
        model.setBackgroundCurrentSelection(selectedBackgroundButton);
        model.setBackgroundUniformSignal(Integer.parseInt(backgroundUniformSignal.getText()));
        model.setBackgroundTifFile(backgroundTifFile.toString());
        model.setBackgroundUniformButtonText(backgroundUniformButton.getText());
        model.setBackgroundTifFileButtonText(backgroundTifButton.getText());
        
        // Now that we have setup all the components, we assemble the
        // microscope and the simulator.
        Microscope microscope = model.build();

        // The simulation engine
        SimEngine generator = new SimEngine(microscope);

        double max_controller_output;
        int controller_tickrate;
        try {
            max_controller_output = Double.parseDouble(e_max_controller_output.getText());
            controller_tickrate = Integer.parseInt(e_controller_tickrate.getText());
        } catch (NumberFormatException ex) {
            IJ.showMessage("Error in parsing controller parameters.");
            return;
        }
        
        if (max_controller_output < 0.0 || controller_tickrate < 1) {
            IJ.showMessage("Error in controller parameter values.");
            return;
        }
        controller_factory.setMaxControllerOutput(max_controller_output);
        
        
        App app = new App(analyzer_factory.build(), generator, controller_factory.build(), controller_tickrate);
        main.setApp(app);
        this.dispose();
    }//GEN-LAST:event_initializeSimulationActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel analyzer_panel;
    private javax.swing.ButtonGroup backgroundButtons;
    private javax.swing.JButton backgroundChooseFile;
    private javax.swing.JRadioButton backgroundTifButton;
    private javax.swing.JRadioButton backgroundUniformButton;
    private javax.swing.JTextField backgroundUniformSignal;
    private javax.swing.JTextField cameraAduPerElectron;
    private javax.swing.JTextField cameraBaseline;
    private javax.swing.JTextField cameraDarkCurrent;
    private javax.swing.JTextField cameraEmGain;
    private javax.swing.JTextField cameraPixelSize;
    private javax.swing.JTextField cameraQuantumEfficiency;
    private javax.swing.JTextField cameraReadoutNoise;
    private javax.swing.JTextField cameraSizeX;
    private javax.swing.JTextField cameraSizeY;
    private javax.swing.JComboBox cb_analyzer_setup;
    private javax.swing.JComboBox cb_controller_setup;
    private javax.swing.JPanel controller_panel;
    private javax.swing.JTextField e_controller_tickrate;
    private javax.swing.JTextField e_max_controller_output;
    private javax.swing.ButtonGroup emittersButtons;
    private javax.swing.JButton emittersChooseFile;
    private javax.swing.JRadioButton emittersCsvButton;
    private javax.swing.JRadioButton emittersGridButton;
    private javax.swing.JTextField emittersGridSpacing;
    private javax.swing.JRadioButton emittersRandomButton;
    private javax.swing.JTextField emittersRandomNumber;
    private javax.swing.JTextField fiducialsNumber;
    private javax.swing.JTextField fiducialsSignal;
    private javax.swing.JTextField fluorPropSignal;
    private javax.swing.JTextField fluorPropTBleached;
    private javax.swing.JTextField fluorPropTOff;
    private javax.swing.JTextField fluorPropTOn;
    private javax.swing.JTextField fluorPropWavelength;
    private javax.swing.JButton initializeSimulation;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField laserInitPower;
    private javax.swing.JTextField laserMaxPower;
    private javax.swing.JTextField laserMinPower;
    private javax.swing.JTextField objectiveMag;
    private javax.swing.JTextField objectiveNA;
    private java.awt.Panel panel6;
    private javax.swing.JTextField stageZ;
    // End of variables declaration//GEN-END:variables

}
