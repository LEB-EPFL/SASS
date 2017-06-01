with import <nixpkgs> {};

stdenv.mkDerivation {
  name = "SASS-0.0.2";
  version = "0.0.2";
  buildInputs = [
    jdk
    netbeans
  ];

  ijsrc = (builtins.toPath ./src/lib/ij-1.50e.jar);
  coltsrc = (builtins.toPath ./src/lib/colt.jar);
  math2src = (builtins.toPath ./src/lib/commons-math-2.2.jar);
  math3src = (builtins.toPath ./src/lib/commons-math3-3.4.1.jar);
  concsrc = (builtins.toPath ./src/lib/concurrent.jar);
  clisrc = (builtins.toPath ./src/lib/commons-cli-1.4.jar);
  bshsrc = (builtins.toPath ./src/lib/bsh-2.0b6.jar);
  alicasrc = (builtins.toPath ./src/lib/ALICA-v0.1.0.jar);
  mmcsrc = (builtins.toPath ./src/lib/MMCoreJ.jar);
  
  meta = {
    description = "SMLM Acquisition Simulation Software.";
    homepage = https://github.com/MStefko/SASS;
  };

  shellHook = ''
    export CLASSPATH="$ijsrc:$coltsrc:$math2src:$math3src:$concsrc:$clisrc:$bshsrc:$alicasrc:$mmcsrc:$CLASSPATH"
  '';

}
