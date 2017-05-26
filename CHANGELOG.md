# Change Log
All notable changes to this project will be documented in this file.

## [Unreleased]
### Added
- PALM and dSTORM fluorophore blinking models (not accessible via GUI)
- Analyzer and Controller are now loaded from ALICA .jar
- BeanShell basic support (executing the .jar causes a BeanShell console to run)

### Removed
- Multiple simultaneous analyzer option. Create a new custom encapsulating analyzer
  if you want the same functionality. Possibility to output multiple values from
  Analyzer will be implemented using JSONObjects


## [0.0.1]
### Added
- CHANGELOG.md was created for tracking changes to the project.
- default.nix was added to more easily port the development
  environment across machines.

[Unreleased]: https://github.com/MStefko/STEADIER-SAILOR/compare/0.0.1...HEAD
