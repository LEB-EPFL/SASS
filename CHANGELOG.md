# Change Log
All notable changes to this project will be documented in this file.

## Unreleased

## [v0.0.2]
Compatible with [ALICA v0.0.2]

### Added
- PALM and dSTORM fluorophore blinking models (not accessible via GUI, only BeanShell)
- Analyzer and Controller are now loaded from ALICA .jar
- BeanShell basic support (executing the .jar causes a BeanShell console to run)

### Removed
- Multiple simultaneous analyzer option. Create a new custom encapsulating analyzer
  if you want the same functionality. Possibility to output multiple values from
  Analyzer will be implemented using JSONObjects


## [v0.0.1]
### Added
- CHANGELOG.md was created for tracking changes to the project.
- default.nix was added to more easily port the development
  environment across machines.

[Unreleased]: https://github.com/MStefko/STEADIER-SAILOR/compare/v0.0.2...HEAD
[ALICA v0.0.2]: https://github.com/MStefko/ALICA/releases/tag/v0.0.2