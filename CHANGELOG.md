# Change Log
All notable changes to this project will be documented in this file.

## Unreleased

### Added
- `STORMsim` now has a method wrapper called `incrementTimeStep()`
  which advances the simulation by one frame without recording an
  image to the stack.

## [v0.3.0]
Contains [ALICA v0.2.1]

### Added
- `StateLogger` object for recording each fluorophore state transition
   and the time at which it occurs.
- Example script **example_run_generator_logging.bsh** which
  demonstrates how to log the groundtruth output from a simulation and
  save the results to a .csv file.

### Fixed
- Fixed a bug preventing the opening of a background file on Linux/Mac
  OS that was caused by a hard-coded backslash file separator
  character.

### Removed
- Removed the script **example_run_generator_moving_from_csv.bsh** and
  made a comment about how to implement trajectory loading from an
  external file in **example_run_generator_moving.bsh**.

## [v0.2.1]
### Added
- Electron multiplication (EM) gain and camera baseline parameters.

### Fixed
- The camera noise model was improperly accounting for electron
  multiplication noise. This is now fixed and encompasses both EMCCD's
  (by setting the EM gain to a non-zero value) and sCMOS (by setting
  the EM gain to zero.)
- A problem preventing the creation of new dStormProperties
  fluorophore models.	
	
## [v0.2.0]
Contains [ALICA v0.2.1]

### Added
- General N-state fluorophore system which replaces the individual xFluorophore
classes
- 3D fluorophore support (needs better PSF calculation)
- Moving 2D fluorophore

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

[ALICA v0.2.1]: https://github.com/LEB-EPFL/ALICA/releases/tag/v0.2.1
[ALICA v0.0.2]: https://github.com/LEB-EPFL/ALICA/releases/tag/v0.0.2
[ALICA v0.1.0]: https://github.com/LEB-EPFL/ALICA/releases/tag/v0.1.0
[v0.0.2]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.0.2
[v0.1.0]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.1.0
[v0.1.1]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.1.1
[v0.1.2]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.1.2
[v0.2.0]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.2.0
[v0.2.1]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.2.1
[v0.3.0]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.3.0
