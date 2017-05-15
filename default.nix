with import <nixpkgs> {};

stdenv.mkDerivation {
  name = "STEADIER-SAILOR-0.0.0";
  version = "0.0.0";
  buildInputs = [
    jdk
    netbeans
  ];

  ijsrc = (builtins.toPath ./src/lib/ij-1.51j.jar);
  coltsrc = (builtins.toPath ./src/lib.colt.jar);

  buildPhase = ''
    mkdir -p $out/java/share
    cp -v $ijsrc $out/java/share/
    cp -v $coltsrc $out/java/share/
  '';

  meta = {
    description = "SMLM simulation software.";
    homepage = https://github.com/MStefko/STEADIER-SAILOR;
  };
}
