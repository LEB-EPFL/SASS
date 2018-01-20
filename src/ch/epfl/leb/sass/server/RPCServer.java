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
package ch.epfl.leb.sass.server;

import ch.epfl.leb.sass.ijplugin.Model;
import ch.epfl.leb.sass.simulator.generators.realtime.SimEngine;
import ch.epfl.leb.sass.simulator.SimpleSimulator;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.io.FileInputStream;

/**
 *
 * @author douglass
 */
public class RPCServer {
    
    public static RemoteSimulationServiceHandler handler;
    public static RemoteSimulationService.Processor processor;
    
    public static void main(String [] args) {
        try {
            
            // Testing: Load a model from file and create a simulation engine
            Model model;
            model = Model.read(new FileInputStream("/home/douglass/Desktop/simulation.sass"));
            
            SimpleSimulator simulator = new SimpleSimulator( new SimEngine(model.build()));
            
            handler = new RemoteSimulationServiceHandler(simulator);
            processor = new RemoteSimulationService.Processor(handler);
            
            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };
            
            new Thread(simple).start();
        } catch (Exception ex) {
            ex.printStackTrace();;
        }
    }
    
    public static void simple(RemoteSimulationService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
            
            System.out.println("Starting the SASS Simulation Server...");
            server.serve();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
