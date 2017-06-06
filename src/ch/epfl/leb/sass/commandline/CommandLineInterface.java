/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * Author: Marcel Stefko
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.epfl.leb.sass.commandline;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Main class of the project, launches the BeanShell script interface.
 * @author Marcel Stefko
 */
public final class CommandLineInterface {
    
    private static final Options options = constructOptions();
    private static final URL urlToWelcomeText = CommandLineInterface.class.getResource("/ch/epfl/leb/sass/commandline/welcome_text.txt");
    
    /**
     *
     * @return all understood options for ALICA execution
     */
    public static Options constructOptions() {
        final Options options = new Options();
        options.addOption("i", "interpreter", false, "run BeanShell interpreter inside current terminal window");
        options.addOption("s", "script", true, "execute BeanShell script (can be combined with -i)");
        options.addOption("h", "help", false, "show this help");
        return options;
    }
    
    /**
     * Shows help, launches the interpreter and executes scripts according to input args.
     * @param args input arguments
     */
    public static void main(String args[]) {
        // parse input arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("Parsing of arguments failed. Reason: " + ex.getMessage());
            System.err.println("Use -help for usage.");
            System.exit(1);
        }            
        
        // decide how do we make the interpreter available based on options
        Interpreter interpreter = null;
        // show help and exit
        if (line.hasOption("help")) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java -jar <jar-name>", options, true);
            System.exit(0);
        // launch interpreter inside current terminal
        } else if (line.hasOption("interpreter")) {
            // assign in, out and err streams to the interpreter
            interpreter = new Interpreter(new InputStreamReader(System.in), System.out, System.err, true);
            interpreter.setShowResults(true);
            // if a script was given, execute it before giving access to user
            if (line.hasOption("script")) {
                try {
                    interpreter.source(line.getOptionValue("script"));
                } catch (IOException ex) {
                    Logger.getLogger(BeanShellConsole.class.getName()).log(Level.SEVERE, "IOException while executing shell script.", ex);
                } catch (EvalError ex) {
                    Logger.getLogger(BeanShellConsole.class.getName()).log(Level.SEVERE, "EvalError while executing shell script.", ex);
                }
            }
            // give access to user
            new Thread( interpreter ).start(); 
        // only execute script and exit
        } else if (line.hasOption("script")) {
            interpreter = new Interpreter();
            try {
                interpreter.source(line.getOptionValue("script"));
                System.exit(0);
            } catch (IOException ex) {
                Logger.getLogger(BeanShellConsole.class.getName()).log(Level.SEVERE, "IOException while executing shell script.", ex);
                System.exit(1);
            } catch (EvalError ex) {
                Logger.getLogger(BeanShellConsole.class.getName()).log(Level.SEVERE, "EvalError while executing shell script.", ex);
                System.exit(1);
            }
        // if System.console() returns null, it means we were launched by
        // double-clicking the .jar, so launch own BeanShellConsole
        // if System.console() returns null, it means we were launched by
        // double-clicking the .jar, so launch own ConsoleFrame
        } else if (System.console() == null) {
            BeanShellConsole cframe = new BeanShellConsole("SASS BeanShell Prompt");
            cframe.setVisible(true);
            System.setOut(cframe.getInterpreter().getOut());
            System.setErr(cframe.getInterpreter().getErr());
            new Thread(cframe.getInterpreter()).start();
        // otherwise, show help
        } else {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java -jar <jar-name>", options, true);
            System.exit(0);
        }
        
        if (interpreter != null) {
            printWelcomeText(interpreter.getOut());
        }
    }
    
    /**
     * Reads the welcome_text file and prints it to a PrintStream.
     * @param out stream to print to
     */
    public static void printWelcomeText(PrintStream out) {
        try {
            InputStream inputStream = urlToWelcomeText.openStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String message = result.toString("UTF-8");
            out.print(message+"\n");
        } catch (IOException ex) {
            Logger.getLogger(CommandLineInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
