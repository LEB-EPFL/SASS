# Change Log
All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- Some Microscope components may now be serialized to JSON strings.
- A `SimulationManager` object was added to handle communications with
  multiple `Simulator` instances.
- A `RPCClient` was created for Java-based interactions with the
  `RPCServer`.
- `Observable`, `Listener`, and `Message` interfaces were added to
  handle the new (and more general) logging mechanism for state
  transitions of Microscope components.
  
### Changed
- The `RPCServer` now provides access to simulatons through a
  `SimulationManager` instance.
- The API of the `RemoteSimulationService` has been expanded to allow
  for full control over `Simulator` objects on the server.
- Fluorescence state transition logging has now been implemented as an
  Observer pattern using `Message` objects.

## [v0.8.0]

### Changed
- Ported the build system to Maven.
- Integrated the project into the Fiji/ImageJ ecosystem.

## [v0.7.0]
Contains [ALICA v0.2.5]

### Added
- An RPC server was created that allows for remote control of the
  simulation over the network.
- A new image abstraction layer called **ImageS** that separates image
  data logic and display from the SASS core code.

### Changed
- The package layout was restructured and simplified to make it easier
  for newcomers to understand and to better consolidate components
  that were added after the original layout was devised.
- The `ImageGenerator` interface was renamed to `Simulator`, while the
  original `Simulator` class was renamed to `ImageJSimulator` and
  moved down one directory to the `internal` package.
- The `SimEngine` class was renamed to `DefaultSimulator` and moved to
  ch.epfl.leb.sass.simulator.internal.

## [v0.6.1]
Contains [ALICA v0.2.5]

### Added
- A convenience method named `getFrameInfo()` was added to FrameLogger
  (called `FrameInfo`) that returns a single data structure containing
  the per-frame-information about each emitter.

### Changed
- Bulky `ALICA_dev.jar` was replaced by `ALICA_for_SASS_v0_2_5.jar`
  which reduced SASS jar size to 8MB.
  
## [v0.6.0]
Contains [ALICA v0.2.5]		

### Added
- You can now save and load simulation settings from the GUI.
- There is now a `FrameLogger` for recording the frames that a
  fluorophore is on. This produces larger log files than the
  StateLogger, but does not require post-processing to extract the
  frame numbers.
- It is now possible to generate random background patterns using
  `GenerateRandomBackground`.
- Emitter positions and state transitions may now be saved from the
  GUI.

### Changed
- A status report on PSF calculation is now output to the console for
  every 5000 calculations.
- Each simulation output is now displayed in its own plot for easier
  tracking of the simulation parameters over time.

### Fixed
- Fixed a cropping issue with the self-tuning PI controller dialog on
  Linux.
- The `GibsonLanniPSF` cache is now erased everytime a new builder is
  created for this PSF type. This prevents caching calculations done
  for previous simulations.
- The Save... button in the Initialize Simulation window is now
  properly displayed as a Save dialog.
- The latency bug that caused a pause during every tenth simulation
  frame in GUI mode has been fixed. The latency came from updating the
  original status plots, which have been changed in this version.
- Loggers now properly check for filename collisions to prevent
overwriting previously saved data.

### Removed
- The button that allowed saving the simulation outputs in the GUI has
  been removed because it didn't work. This functionality may be
  reimplemented in the future if there is interest.

## [v0.5.1]

### Changed
- The `GibsonLanniPSF` now caches computation results to avoid
  repetitive calculations.
- The `GibsonLanniPSF.Builder` now has a `solver()` method for setting
  the solver for the Bessel function coefficients. It accepts either
  "svd" or "qrd" as arguments. (SVD stands for singular value
  decomposition and QRD stands for QR decomposition.)
- The `GibsonLanniPSF.Builder` also now has a `resPSFAxial()` method
  for determining the spacing between axial planes of the
  computational grid.
- The `GibsonLanniPSF.Builder` also now has a `maxRadius()` method for
  setting an upper limit on the size of the area that the PSF is drawn
  onto. Reducing it can significantly speed up simulation times.

### Fixed
- PSF instances are not, in fact, immutable because their fields are
  not `final`.

## [v0.5.0]
Contains [ALICA v0.2.1]	

### Added
- There is now a `GibsonLanniPSF` for modeling realistic 3D PSF's and
  that can account for details such as sample, coverslip, and
  immersion media refractive indexes and thicknesses.
- A `PSFBuilder` interface was added to allow for a PSF to be built at
  various times during the simulation, rather than all at once. This
  addition is necessary to account for axial stage positions that
  might change during the simulation.
- Added a `Stage` component to represent the state of the microscope
  stage.
- Added functionality through the `BackgroundCommand` to build custom
  backgrounds for the simulations.
- Added a `Microscope`class to replace `Device`. This class integrates
  the various new and refactored components.
- Added a set of Dynamics classes to separate fluorophore dynamical
  systems from their creation logic.

### Changed
- Major API changes were made in this version. The purpose was to
  assign properties to components in a way that better matched a real
  microscope (e.g. a camera should not have a wavelength). The change
  to a builder based API is intended to make the scripting easier.
- PSF instances are now immutable.
- Optics-based logic was moved from the camera to a new `Objective`
  class, whereas the original camera logic was moved to a new `Camera`
  class in the components package.
- The `Laser` has been moved to the components package.
- Fluorophore generation is no longer setup by the user but executed
  by the microscope.
- Obstructors are now setup by the microscope as well. `GoldBeads` are
  now created by a `ObstructorCommand` object.
- `FluorophoreProperties` now has a wavelength property. The
  wavelength property was removed from the camera.
  
    
## [v0.4.0]
Contains [ALICA v0.2.1]

### Added
- `STORMsim` now has a method wrapper called `incrementTimeStep()`
  which advances the simulation by one frame without recording an
  image to the stack.
- `PositionLogger` object for recording the fluorophores' initial
  positions in x, y, z to a file.
- A `PSF` interface has been created to more easily extend the number
  of PSF models used by SASS.
- A `Gaussian3D` PSF class was added for modeling PSF's as a
  through-focus Gaussian beam.
- Added new methods to FluorophoreGenerator and scripts for creating
  3D fluorophore distributions.

### Changed
- The Emitter, Fluorophore, and Camera API's have been changed to
  decouple the camera logic from light sources.
- The PSF creation routines originally found in the `Emitter` class
  are now located in a `Gaussian2D` class that implements the `PSF`
  interface.
- `PositionLogger` and `StateLogger` now extend an `AbstractLogger`
  base class.
- `PositionLogger` and `StateLogger` now reside in the package
  *ch.epfl.leb.sass.simulator.loggers*.

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

## v0.0.2
Contains [ALICA v0.0.2]

### Added
- PALM and dSTORM fluorophore blinking models (not accessible via GUI, only BeanShell)
- Analyzer and Controller are now loaded from ALICA .jar
- BeanShell integration (executing the .jar causes a standalone BeanShell console to run, running scripts possible via command line)

### Removed
- Multiple simultaneous analyzers option. Create a new custom encapsulating analyzer
  if you want the same functionality.


## v0.0.1
### Added
- CHANGELOG.md was created for tracking changes to the project.
- default.nix was added to more easily port the development
  environment across machines.

[ALICA v0.2.5]: https://github.com/LEB-EPFL/ALICA/releases/tag/v0.2.5
[ALICA v0.2.4]: https://github.com/LEB-EPFL/ALICA/releases/tag/v0.2.4
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
[v0.4.0]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.4.0
[v0.5.0]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.5.0
[v0.5.1]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.5.1
[v0.6.0]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.6.0
[v0.6.1]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.6.1
[v0.7.0]: https://github.com/LEB-EPFL/SASS/releases/tag/v0.7.0
[v0.8.0]: https://github.com/LEB-EPFL/SASS/releases/tab/0.8.0
[Unreleased]: https://github.com/LEB-EPFL/SASS/compare/0.8.0...HEAD
