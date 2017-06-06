# Change Log
All notable changes to this project will be documented in this file.

## [v0.1.1]
Compatible with [ALICA v0.1.0]
### Fixed
- Main class location in package `ch.epfl.leb.sass`
- Normalization of emitter PSF
- Parsing emitters from file now puts them at position identical with ThunderSTORM coordinate system

## [v0.1.0]
Compatible with [ALICA v0.1.0]

### Fixed
- BeanShell integration into FIJI

## [v0.0.2]
Compatible with [ALICA v0.0.2]

### Added
- PALM and dSTORM fluorophore blinking models (not accessible via GUI, only BeanShell)
- Analyzer and Controller are now loaded from ALICA .jar
- BeanShell integration (executing the .jar causes a standalone BeanShell console to run, running scripts possible via command line)

### Removed
- Multiple simultaneous analyzers option. Create a new custom encapsulating analyzer
  if you want the same functionality.


## [v0.0.1]
### Added
- CHANGELOG.md was created for tracking changes to the project.
- default.nix was added to more easily port the development
  environment across machines.

[ALICA v0.0.2]: https://github.com/MStefko/ALICA/releases/tag/v0.0.2
[ALICA v0.1.0]: https://github.com/MStefko/ALICA/releases/tag/v0.1.0
[v0.0.2]: https://github.com/MStefko/SASS/releases/tag/v0.0.2
[v0.1.0]: https://github.com/MStefko/SASS/releases/tag/v0.1.0
[v0.1.1]: https://github.com/MStefko/SASS/releases/tag/v0.1.1
