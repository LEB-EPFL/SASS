Remote Procedure Calls and the SASS Server
==========================================

Introduction
------------

It is possible to control a SASS simulation from a programming
language other than Java or even remotely over a network. This feature
is enabled by the SASS remote procedure call (RPC) server. The idea of
the RPC server is simple: it listens on a network port for commands
sent by other languages and/or computers. When it receives a command,
it performs the requested operation and returns any data that is
associated with the command.

For example, after initializing a simulation and starting the server,
a Python script on the same PC could adjust the laser power on the
simulated microscope. It could then ask the server to simulate a
number of new images and return them to the Python interpreter for
further processing.

As another example, a C++ program could run a simulation by connecting
to the server remotely over a network. The details of setting up your
network, such as ensuring the correct ports are open in your firewall,
are beyond the scope of this documentation.

The RPC service was created using `Apache Thrift`_.

Starting the server
-------------------

There are three ways to start the server: via the command line, inside
the ImageJ GUI, and via a Beanshell script.

Command line
++++++++++++

Enter the following command in a console window to start the server
from the command line ::

  java -jar PATH_TO_SASS_JAR -r CONFIGURATION_FILE

The above command requires two arguments. **PATH_TO_SASS_JAR** is the
path and name of the SASS .jar file, which can be downloaded from the
`releases`_ page of the GitHub repository. **CONFIGURATION_FILE** is a
file that specifies the simulation configuration. This file can be
created created and saved from inside the SASS ImageJ GUI.

The command will start the server on the default port, which was 9090
at the time of this writing. If instead you wish to specify the port
number, use ::

  java -jar PATH_TO_SASS_JAR -p PORT -r CONFIGURATION_FILE

.. _`releases`: https://github.com/LEB-EPFL/SASS/releases

ImageJ
++++++

1. Open the server configuration dialog from the menu bar by clicking
   **Plugins > SASS > Server**.
2. Enter the port number you wish to use for communications with the
   server. Usually the default (9090) is fine.
3. Next, you will need a configuration file that defines your
   simulation parameters. This should be a *.sass* file containing the
   simulation details. You can create one by navigating to **Plugins >
   SASS > Simulator**, adjusting the simulation parameters as desired,
   then clicking the **Save...** button.
4. Once you have a configuration file, click the **Select
   configuration...** button, navigate to your file, and open it.
5. The **Start** button should now be enabled. Click it and the
   simulation will initialize. (This may take a few seconds depending
   on the size of your simulation.)
6. When the server has started, you should see the **Server running**
   message in the status field.
7. To stop the server, either click the **Stop** button or exit the
   server control window.

If you are using Fiji, then you can see status updates from the server
by navigating to **Window >> Console** on the menu bar.

Beanshell script
++++++++++++++++

There is an example script called **example_server.bsh** in the
`scripts`_ folder of SASS that demonstrates how to launch the server
through a Beanshell script. After creating a Microscope instance named
*microscope*, simply create and launch the server with these lines ::

  RPCServer server = new RPCServer(microscope, 9090);
  server.serve();

Note that you will need to first import RPCServer with the command ::

  import ch.epfl.leb.sass.server.RPCServer;

This code will initialize the server to listen on port 9090 and launch
it. If you run the script from the command line, then you can kill the
server by typing **Ctrl-C**.

.. _`scripts`: https://github.com/kmdouglass/SASS/tree/master/scripts

Server communications
---------------------

Services
++++++++

The RPC server works by providing clearly-defined services to
clients. Roughly speaking, a service is just a command made by a
client that changes the simulation state and/or returns some data. A
client must therefore know what services are provided by the server.

The SASS RPC server is implemented using `Apache Thrift`_. The types
of services that are provided by the server are defined in the
`RPCServer.thrift`_ file in the *thrift* folder of the SASS root
directory. Here is what RPCServer.thrift file looked like at the time
of this writing (comments are omitted) ::

  namespace java ch.epfl.leb.sass.server
  namespace py remotesim

  exception ImageGenerationException { }
  exception UnknownSimulationIdException { }

  service RemoteSimulationService {


    i32 createSimulation(),

    void deleteSimulation(1: i32 id) throws (1: UnknownSimulationIdException ex),
  
    double getControlSignal(1: i32 id) throws (1: UnknownSimulationIdException ex),

    string getFluorescenceJsonName(1: i32 id) throws (1: UnknownSimulationIdException ex),

    double getFovSize(1: i32 id) throws (1: UnknownSimulationIdException ex),

    i32 getImageCount(1: i32 id) throws (1: UnknownSimulationIdException ex),

    binary getNextImage(1: i32 id) throws(1: ImageGenerationException ex,
                                          2: UnknownSimulationIdException ex2),

    double getObjectSpacePixelSize(1: i32 id) throws (1: UnknownSimulationIdException ex),
  
    string getServerStatus(),
  
    string getShortTrueSignalDescription(1: i32 id) throws (1: UnknownSimulationIdException ex),

    double getTrueSignal(1: i32 id, 2: i32 imageNum) throws (1: UnknownSimulationIdException ex),

    void incrementTimeStep(1: i32 id) throws (1: UnknownSimulationIdException ex),

    void setControlSignal(1: i32 id, 2: double power) throws (1: UnknownSimulationIdException ex)

    string toJsonMessages(1: i32 id) throws (1: UnknownSimulationIdException ex),

    string toJsonState(1: i32 id) throws (1: UnknownSimulationIdException ex),
       
  }

This file first defines the package names for Java and Python code,
respectively, and a few exceptions that the server will return when
something goes wrong. After that, it then defines the service that the
server provides. There are a number of method calls such as:

1. **setControlSignal()** - Adjusts the simulation's laser power.
2. **getNextImage()** - Simulates a new image.
3. **toJsonMessages()** - Dump the simulation message cache.
4. **toJsonState()** - Get information on the current state of the
   simulation's components.

To turn this script into code, it must be compiled by the Thrift
compiler. An example of how to do this for Java is located `in the
compile.sh file`_ inside the thrift folder. Compilation produces files
that enable the server in your target language.

**Note that the SASS RPC server sends images as tif-encoded byte
strings and the simulation state as JSON strings.** You will need to
decode this information after its received in your target language.

.. _`in the compile.sh file`: https://github.com/LEB-EPFL/SASS/blob/master/thrift/compile.sh

A Python client
+++++++++++++++

The general problem of setting up a client to interact with the
simulation is not so much a SASS problem but is rather more within the
scope of working with `Apache Thrift`_. There are many excellent
tutorials on their website on how to do this in a number of different
languages.

To get you started, we provide here a basic workflow to setup a
rudimentary Python client to control a SASS simulation.

1. `Get Apache Thrift`_.
2. Navigate into the folder containing the `RPCServer.thrift`_ file
   and open it. Add the namespace for your target lanuage. For Python,
   this has already been done for you.
3. Compile the thrift file into Python with the command
   `thrift -r --gen py RPCServer.thrift`.
4. Install the Thrift bindings for Python, preferably inside a virtual
   environment. `pip install thrift`
4. Enter the folder **gen-py** (or move it to a convenient directory).
5. Create an emtpy file named client.py.

Inside the client.py file, you will need to add the following code ::

  from thrift import Thrift
  from thrift.transport import TSocket
  from thrift.transport import TTransport
  from thrift.protocol import TBinaryProtocol
  from remotesim import RemoteSimulationService
  from PIL import Image
  from io import BytesIO

  def main():
     # Make socket
     transport = TSocket.TSocket('localhost', 9090)

     # Buffering is critical. Raw sockets are very slow
     transport = TTransport.TBufferedTransport(transport)

     # Wrap in a protocol
     protocol = TBinaryProtocol.TBinaryProtocol(transport)

     # Create a client to use the protocol encoder
     client = RemoteSimulationService.Client(protocol)

     # Connect!
     transport.open()

     try:
          x = client.getNextImage()
          img = Image.open(BytesIO(x))
          img.load()
          img.show()
     finally:
          transport.close()

  if __name__ == '__main__':
      main()

This will create the client and request the next image from the
simulation. **By default, the RPC Server will return images as
tif-encoded byte strings.** You therefore will need the libtiff
library in your target language to decode them. In Python, this can be
provided by `pillow`_.

A Java client
+++++++++++++

A simple Java client for the SASS RPC server `is already included in
SASS`_.

.. _`Apache Thrift`: https://thrift.apache.org/
.. _`Get Apache Thrift`: https://thrift.apache.org/download
.. _`RPCServer.thrift`: https://github.com/LEB-EPFL/SASS/blob/master/thrift/RPCServer.thrift
.. _`pillow`: https://github.com/python-pillow/Pillow
.. _`is already included SASS`: https://github.com/LEB-EPFL/SASS/blob/master/src/main/java/ch/epfl/leb/sass/client/RPCClient.java
