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
- Easy-to-use script interface via Beanshell and the [example
  scripts.](https://github.com/LEB-EPFL/SASS/tree/master/scripts)
- Interfaces are available for extending simulation attributes, such
  as PSF generation, background, fiducial markers, and customized
  custom fluorophore photophysics.

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

+-----------+-----------+----------------------+
| x, pixels | y, pixels | (optional) z, pixels |
+===========+===========+======================+
|    1.2376 |    4.2340 |               0.0000 |
+-----------+-----------+----------------------+
|    2.7300 |    3.7105 |               0.0000 |
+-----------+-----------+----------------------+
|    2.4360 |    1.2887 |               0.0000 |
+-----------+-----------+----------------------+
|       ... |       ... |                  ... |
+-----------+-----------+----------------------+

The units of the values are in pixels, and the origin is in the upper
left-hand corner of the generated image stacks. This means, for
example, that a fluorophore at position (16, 16) will be in the center
of a 32x32 pixel image.

The coordinate system in x and y is identical to that used by
`ThunderSTORM <http://zitmen.github.io/thunderstorm/>`_.

How is the stage z-displacement property used?
----------------------------------------------

The z-displacement of the stage is used for some 3D point spread
functions that depend on the emitter's distance from the coverslip.

- z = 0 corresponds to the coverslip surface.
- Negative z-positions correspond to moving the stage downwards on an
  inverted microscope. A stage z-position of -2 microns therefore
  corresponds to a focal volume that is located +2 microns above the
  coverslip surface.
