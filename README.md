# SMLM Acquisition Simulation Software

[![Build Status](https://travis-ci.org/LEB-EPFL/SASS.svg?branch=master)](https://travis-ci.org/LEB-EPFL/SASS)

Toolkit for simulating non-stationary fluorescence photodynamics and
real-time control systems for single molecule localization microscopy
(SMLM).

## Documentation

http://sass.readthedocs.io/en/latest/

## Usage

## API Notice ##

The API for SASS is still under development and may change until major
version 1.0.0. Any methods or classes that are marked as deprecated
will be removed by this release.

## Installation

Download the latest .jar from [releases].

### As standalone application
 - Executing the .jar file by double-clicking launches a BeanShell console. Example scripts can be found in `/scripts/` folder.
 - Running SASS from command line: `java -jar <SASS-jar-name> --help` brings up available options, such as executing a script, or an interactive session within the terminal.
 
### As FIJI plugin
Placing the .jar file in `FIJI/plugins/` folder (file MUST have an underscore in the name, e.g. `SASS_v0.0.2.jar`) enables FIJI integration:
 - `Plugins -> SASS -> GUI` launches an interactive simulator (not all options are available through the GUI).
 - `Plugins -> SASS -> Command Prompt` launches BeanShell console. Generated images can be analyzed immediately with FIJI.

## Acknowledgements
SASS uses adapted code and algorithms under GPL from following projects:
 - [SOFItool](https://github.com/lob-epfl/sofitool) by Arik Girsault and Tomas Lukes
 - [ALICA](https://github.com/MStefko/ALICA) by Marcel Stefko
 
 
 Many thanks to Dr. Kyle M. Douglass for guidance during this project.

[Releases]: https://github.com/MStefko/SASS/releases
