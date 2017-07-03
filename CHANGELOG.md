# Change Log
All notable changes to this project will be documented in this file.

## [v0.2.0]
Contains [ALICA v0.2.1]

### Added
- General N-state fluorophore system which replaces the individual xFluorophore
classes
- 3D fluorophore support (needs better PSF calculation)

### Removed
- SimpleFluorophore, dSTORMFluorophore, PALMFluorophore classes

### Fixed
- Graph rendering in interactive mode
- Misaligned GUI glitch in InitSettingsFrame

## [v0.1.2]
Contains [ALICA v0.1.0]

### Added
- Moving variant of SimpleFluorophore (trajectory has to be specified explicitly
for every frame, and there is no motion blur).

## [v0.1.1]
Contains [ALICA v0.1.0]
### Fixed
- Class location in package `ch.epfl.leb.sass`
- Normalization of emitter PSF
- Parsing emitters from file now puts them at position identical with ThunderSTORM coordinate system

## [v0.1.0]
Contains [ALICA v0.1.0]

### Fixed
- BeanShell integration into FIJI

## [v0.0.2]
Contains [ALICA v0.0.2]

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

[ALICA v0.2.1]: https://github.com/MStefko/ALICA/releases/tag/v0.2.1
[ALICA v0.0.2]: https://github.com/MStefko/ALICA/releases/tag/v0.0.2
[ALICA v0.1.0]: https://github.com/MStefko/ALICA/releases/tag/v0.1.0
[v0.0.2]: https://github.com/MStefko/SASS/releases/tag/v0.0.2
[v0.1.0]: https://github.com/MStefko/SASS/releases/tag/v0.1.0
[v0.1.1]: https://github.com/MStefko/SASS/releases/tag/v0.1.1
[v0.1.2]: https://github.com/MStefko/SASS/releases/tag/v0.1.2
