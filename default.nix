with import <nixpkgs> {};

stdenv.mkDerivation {
  name = "STEADIER-SAILOR-0.0.1";
  version = "0.0.1";
  buildInputs = [
    jdk
    netbeans
  ];

  ijsrc = (builtins.toPath ./src/lib/ij-1.51j.jar);
  coltsrc = (builtins.toPath ./src/lib/colt.jar);
  math2src = (builtins.toPath ./src/lib/commons-math-2.2.jar);
  math3src = (builtins.toPath ./src/lib/commons-math3-3.4.1.jar);
  concsrc = (builtins.toPath ./src/lib/concurrent.jar);
  
  meta = {
    description = "SMLM simulation software.";
    homepage = https://github.com/MStefko/STEADIER-SAILOR;
  };

  shellHook = ''
    export CLASSPATH="$ijsrc:$coltsrc:$math2src:$math3src:$concsrc:$CLASSPATH"
  '';

}
