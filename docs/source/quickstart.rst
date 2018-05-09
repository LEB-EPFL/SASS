Quickstart
==========

.. contents::
   :depth: 3

Installation
++++++++++++

SASS is both a standalone application and a `Fiji <http://fiji.sc/>`_
plugin.

Standalone
----------
1. Download the latest .jar file from the `SASS releases page
   <https://github.com/MStefko/SASS/releases>`_.
2. You will also need to download the latest `ALICA_ACPack
   <https://github.com/LEB-EPFL/ALICA_ACPack>`_ .jar, which contains
   the run-time components for control systems simulations.
3. Place both .jars in the folder of your choosing.

Fiji
----
1. Download the latest .jar file from the `SASS releases page
   <https://github.com/MStefko/SASS/releases>`_.
2. You will also need to download the latest `ALICA_ACPack
   <https://github.com/LEB-EPFL/ALICA_ACPack>`_ .jar, which contains
   the run-time components for control systems simulations.
3. Copy the SASS .jar file into your *~/Fiji.app/plugins/* folder,
   where *Fiji.app* is root directory for your Fiji installation.
4. Copy the ALICA_ACPack .jar file into your *~/Fiji.app/jars* folder.
5. Restart Fiji.

You should now see *SASS* appear as a menu item in the the *Plugins*
menu.

Run a simulation
++++++++++++++++

Standalone
----------

Before starting, make sure that you have a copy of the file
`example_random_2d_fluorophores.bsh
<https://github.com/LEB-EPFL/SASS/blob/master/scripts/example_random_2d_fluorophores.bsh>`_
from the SASS respository's *scripts* folder. When using SASS in
standalone mode, it is most commonly used as a command line
application.

1. From the command line, navigate to the folder where you placed the
   SASS .jar file that you downloaded in the installation step.
2. Enter the command `java -jar SASS_-<VERSION>.jar -s
   example_random_2d_fluorophores.bsh`.
3. If you want to save the simulation's output, ensure that any call
   to the `saveStack(...)` method is uncommented inside the script and
   rerun the simulation.

Fiji
----

1. Launch Fiji. (If you're launch Fiji from the command line, ensure
   that you are first in the Fiji root directory.)
2. Navigate to *Plugins > SASS > Simulator*.
3. Ensure that **Manual** is selected in the *Controller* drop-down
   box.
4. Click the *Initialize* button.
5. Rearrange the windows so that you can find the dialog with the
   controller set point and the *Start* and *Stop* buttons.
6. Click *Start* to start the simulation. You should see images begin
   streaming into the simulation's image stack.
7. Click the *Stop* button to pause the simulation.
8. Change the *Controller setpoint* value and click *Start* again to
   resume the simulation with a new laser power.



