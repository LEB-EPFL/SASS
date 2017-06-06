# STORM Acquisition Simulation Software

Toolkit for testing various algorithms for estimating fluorophore density in STORM and PALM datasets, and simulating real-time control of the acquisition using a feedback loop.

## Usage
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
