.. contents:: Frequently Asked Questions
   :depth: 2

General
=======

What does SASS stand for?
-------------------------

SMLM Acquisition Simulation Software.

(SMLM stands for Single Molecule Localization Microscopy.)


Running Simulations
===================

How are custom fluorophore position lists formatted?
----------------------------------------------------

Customized fluorophore positions are imported into SASS from an
externally-generated file that you create yourself. This file should
contain two columns of comma-separated numerical values (for example,
a .csv file). Each row represents the position of one fluorophore; the
first column represents the fluorophore's x-position, while the second
column represents the fluorophore's y-position. The file should
contain no header or comments.

**Example**

+-----------+-----------+
| x, pixels | y, pixels |
+===========+===========+
|    1.2376 |    4.2340 |
+-----------+-----------+
|    2.7300 |    3.7105 |
+-----------+-----------+
|    2.4360 |    1.2887 |
+-----------+-----------+
|       ... |       ... |
+-----------+-----------+

The units of the values are in pixels, and the origin is in the upper
left-hand corner of the generated image stacks. This means, for
example, that a fluorophore at position (16, 16) will be in the center
of a 32x32 pixel image.
