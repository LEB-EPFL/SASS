with import <nixpkgs> {};

(
  let

  javalang = pkgs.python35Packages.buildPythonPackage rec {

      name = "javalang-${version}";
      version = "0.11.0";

      src = pkgs.fetchurl {
        url = "https://github.com/c2nes/javalang/archive/javalang-0.11.0.tar.gz";
	sha256 = "99817f218e29b9004236f59953a9540af34e140a9fcaeabefa4f44ebe0fa22dd";
      };

      buildInputs = [ pkgs.python35Packages.six ];

      meta = {
        homepage = "https://github.com/c2nes/javalang";
        description = " Pure Python Java parser and tools";
      };
    };
    
    javasphinx = pkgs.python35Packages.buildPythonPackage rec {

      name = "javasphinx-${version}";
      version = "0.9.15";

      src = pkgs.fetchurl {
        url = "https://github.com/bronto/javasphinx/archive/javasphinx-0.9.15.tar.gz";
	sha256 = "3af6e85a5d0502ee7a6e292a0a23978ba1f25bbc3e6c197a455e2fa4598cb406";
      };

      buildInputs = [ javalang
      		      pkgs.python35Packages.lxml
                      pkgs.python35Packages.beautifulsoup4
	       	      pkgs.python35Packages.future
		      pkgs.python35Packages.docutils
		      pkgs.python35Packages.sphinx];

      meta = {
        homepage = "https://github.com/bronto/javasphinx";
	description = "Sphinx extension for documenting Java projects";
      };
    
    };

  in pkgs.python35.withPackages (ps: [ ps.sphinx pkgs.netbeans javalang javasphinx ])
).env