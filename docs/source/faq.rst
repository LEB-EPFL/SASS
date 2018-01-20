Frequently Asked Questions
==========================

.. contents::
   :depth: 3

General
+++++++

What are the advantages of SASS over other SMLM simulators?
-----------------------------------------------------------

- Complete integration with ImageJ/Fiji.
- Incorporates automated control systems into the simulation
  environment.
- Allows for dynamic adjustment of the illumination *during* a
  simulation.
- Easy-to-use script interface via Beanshell and the `example
  scripts`_.
- Interfaces are available for extending simulation attributes, such
  as PSF generation, background, fiducial markers, and custom
  fluorophore photophysics.

.. _`example scripts`: https://github.com/LEB-EPFL/SASS/tree/master/scripts

What does SASS stand for?
-------------------------

SMLM Acquisition Simulation Software.

(SMLM stands for Single Molecule Localization Microscopy.)

Running Simulations
+++++++++++++++++++

How is the coordinate system in SASS defined?
---------------------------------------------

Coordinates in SASS are typically in units of pixels unless otherwise
noted in the documentation. **Please note that the origin of the
Cartesian coordinate system lies at the center of a pixel, not at a
corner.**

How are custom fluorophore position lists formatted?
----------------------------------------------------

Customized fluorophore positions are imported into SASS from an
externally-generated file that you create yourself. This file should
contain two columns (*optionally three*) of comma-separated numerical
values (for example, a .csv file). Each row represents the position of
one fluorophore; the first column represents the fluorophore's
x-position, while the second column represents the fluorophore's
y-position. If you want to do 3D simulations, there should be a third
column for the z-position. The file should contain no header or
comments.

**Example**

The column labels **should not** be included in the file; they are
illustrated here only to indicate which columns correspond to x and y.

+-----------+-----------+-------------------------+
| x, pixels | y, pixels | (optional) z, arb. units|
+===========+===========+=========================+
|    1.2376 |    4.2340 |                  0.0000 |
+-----------+-----------+-------------------------+
|    2.7300 |    3.7105 |                  0.0000 |
+-----------+-----------+-------------------------+
|    2.4360 |    1.2887 |                  0.0000 |
+-----------+-----------+-------------------------+
|       ... |       ... |                     ... |
+-----------+-----------+-------------------------+

The units of the values are in pixels, and, **for imports from CSV
files only**, the origin is in the upper left-hand corner of the
generated image stacks, not the center of the upper left pixel. After
import into SASS, there is an implicit subtraction of half a pixel
from the x- and y-coordinate values which shifts the coordinate system
into the one used by SASS. This is done to preserve the same relative
pixel locations when importing from the same file into SASS or
ThunderSTORM.

For example, a fluorophore with a position in the CSV file at (15.5,
15.5) will lie at the center of a pixel in ThunderSTORM. To get it to
lie at the center of a pixel in SASS, 0.5 is subtracted from each
coordinate to make the resulting position (15, 15). Because the origin
is at a pixel center in SASS, so to will be this fluorophore's
position.

Check out `ThunderSTORM <http://zitmen.github.io/thunderstorm/>`_ for
more information.

What are the units for the axial (z) direction?
-----------------------------------------------

The units of the values in the z-column of the fluorophore position
lists can be any unit that you want, so long as you are consistent in
your choice of units for the properties of the various simulation
components.

For example, if the you specify the fluorophore z-positions in
microns, then you should use microns for the fluorescence wavelength,
stage displacement, and other values that require a length.

How is the stage z-displacement property used?
----------------------------------------------

The z-displacement of the stage is used for some 3D point spread
functions that depend on the emitter's distance from the coverslip.

- z = 0 corresponds to the coverslip surface.
- Negative z-positions correspond to moving the stage downwards on an
  inverted microscope. For example, a stage z-position of -2 microns
  corresponds to a focal volume that is located +2 microns above the
  coverslip surface.
