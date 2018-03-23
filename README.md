# SMLM Acquisition Simulation Software

- [![Build Status](https://travis-ci.org/LEB-EPFL/SASS.svg?branch=master)](https://travis-ci.org/LEB-EPFL/SASS)
- [![Join the chat at https://gitter.im/leb_epfl/SASS](https://badges.gitter.im/leb_epfl/SASS.svg)](https://gitter.im/leb_epfl/SASS?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

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
Placing the .jar file in `FIJI/plugins/` folder (file MUST have an underscore in the name, e.g. `SASS_v0.6.1.jar`) enables FIJI integration:
 - `Plugins -> SASS -> Simulation` launches an interactive simulator (not all options are available through the GUI).
 - `Plugins -> SASS -> Command Prompt` launches BeanShell console. Generated images can be analyzed immediately with FIJI.

## Where to find help

- How to use SASS: https://gitter.im/leb_epfl/SASS
- Bug reports: https://github.com/LEB-EPFL/SASS/issues
- Feature requests: https://github.com/LEB-EPFL/SASS/issues
- Developer questions: https://gitter.im/leb_epfl/SASS

## Acknowledgements

SASS uses adapted code and algorithms from the following projects:
 - [SOFItool](https://github.com/lob-epfl/sofitool) by Arik Girsault and Tomas Lukes (GPL)
 - [ALICA](https://github.com/MStefko/ALICA) by Marcel Stefko (GPL)
 - [MicroscPSF-ImageJ](https://github.com/hijizhou/MicroscPSF-ImageJ) by Jizhou Li (MIT)
 - [OpenSimplexNoise](https://gist.github.com/KdotJPG/b1270127455a94ac5d19) by Kurt Spencer (Unlicense/Public Domain)
 
In addition, SASS relies on these projects to provide core
functionality.
 - [Apache Thrift](https://thrift.apache.org/)
 - [ImageJ](https://imagej.net/Welcome)

[Releases]: https://github.com/LEB-EPFL/SASS/releases
