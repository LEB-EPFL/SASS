/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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

import ch.epfl.leb.sass.server.RPCServer;

/**
 * Contains the GUI form data for the SASS server.
 * 
 * @author Kyle M. Douglass
 */
public class ServerModel {
    
    /**
     * The server will listen on this network port.
     */
    private int port = 9090;
    
    /**
     * The full filename of the simulation configuration.
     */
    private String configFile = "Choose a file...";
    
    /**
     * The simulation model for configuring and building the microscope.
     */
    private Model simulationModel = null;
    
    /**
     * The RPCServer instance.
     */
    private RPCServer server;
    
    private boolean startButtonEnabled = false;
    private boolean stopButtonEnabled = false;
    private boolean selectConfigButtonEnabled = true;
    private boolean portTextEnabled = true;
    
    // Getters
    //--------------------------------------------------------------------------
    public int getPort() { return port; }
    public String getConfigFile() { return configFile; }
    public Model getSimulationModel() { return simulationModel; }
    public boolean getStartButtonEnabled() { return startButtonEnabled; }
    public boolean getStopButtonEnabled() { return stopButtonEnabled; }
    public boolean getPortTextEnabled() { return portTextEnabled; }
    public boolean getSelectConfigButtonEnabled() {
        return selectConfigButtonEnabled;
    }
    public RPCServer getServer() { return server; }
    
    // Setters
    //--------------------------------------------------------------------------
    public void setPort(int port) { this.port = port; }
    public void setConfigFile(String filename) {this.configFile = filename; }
    public void setSimulationModel(Model simulationModel) {
        this.simulationModel = simulationModel;
    }
    public void setStartButtonEnabled(boolean enabled) {
        startButtonEnabled = enabled;
    }
    public void setStopButtonEnabled(boolean enabled) {
        stopButtonEnabled = enabled;
    }
    public void setSelectConfigButtonEnabled(boolean enabled) {
        selectConfigButtonEnabled = enabled;
    }
    public void setPortTextEnabled(boolean enabled) {
        portTextEnabled = enabled;
    }
    public void setServer(RPCServer server) {
        this.server = server;
    }
    
}
