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
package ch.epfl.leb.sass.client;

import ch.epfl.leb.sass.server.RemoteSimulationService;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * A client for interaction with the SASS RPCServer.
 * 
 * @author Kyle M. Douglass
 */
public class RPCClient {
    
    /**
     * Has the transport layer been closed?
     */
    private boolean closed = false;
    
    /**
     * RPCClient logger.
     */
    private final static Logger LOGGER = Logger.getLogger(RPCClient.class
                                                                   .getName());
    
    /**
     * Thrift communications transport layer.
     */
    private TTransport transport;
    
    /**
     * The client that interfaces with the server-side simulation service.
     */
    private RemoteSimulationService.Client client;
    
    /**
     * Creates a RPCClient instance for communications with the RPCServer.
     * 
     * For RPCServers running on the same machine, use "localhost" for the
     * hostUrl argument.
     * 
     * @param hostUrl The URL of the RPCServer.
     * @param port The port that the RPCServer is listening on.
     */
    public RPCClient(String hostUrl, int port) {
        try {
            transport = new TSocket(hostUrl, port);
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);
            this.client = new RemoteSimulationService.Client(protocol);
      } catch (TException ex) {
          ex.printStackTrace();
      } 
    }
    
    /**
     * Closes the transport layer to the server.
     * 
     * This method must be called before the program terminates.
     */
    public void close() {
        if (!closed) {
            transport.close();
            closed = true;
        }
    }
    
    /**
     * Safety check that the transport layer is properly closed.
     */
    @Override
    protected void finalize() throws Throwable {
        if (!closed) {
            LOGGER.log(Level.WARNING,
                       "RPCClient.close() was not explicityly called.");
        }
        close();
        super.finalize();
    }
    
    /**
     * Returns a copy of service client.
     * 
     * Use the client to make calls to the RPC server.
     * 
     * @return A copy of the RemoteSimulationService client. 
     */
    public RemoteSimulationService.Client getClient() {
        return this.client;
    }
}
