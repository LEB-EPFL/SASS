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
package commandline;

import beanshell.ConsoleFrame;
import bsh.EvalError;
import bsh.Interpreter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
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
 *
 * @author stefko
 */
public final class CommandLineInterface {
    
    private static final Options options = constructOptions();
    private static final URL urlToWelcomeText = CommandLineInterface.class.getResource("/beanshell/welcome_text.txt");
    
    public static Options constructOptions() {
        final Options options = new Options();
        options.addOption("i", "interpreter", false, "run BeanShell interpreter inside current terminal window");
        options.addOption("s", "script", true, "execute BeanShell script");
        options.addOption("h", "help", false, "show this help");
        return options;
    }
    
    public static void main(String args[]) {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("Parsing of arguments failed. Reason: " + ex.getMessage());
            System.err.println("Use -help for usage.");
            System.exit(1);
        }            
        Interpreter interpreter = null;
        if (line.hasOption("help")) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java -jar <jar-name>", options, true);
            System.exit(0);
        } else if (line.hasOption("interpreter")) {
            interpreter = new Interpreter(new InputStreamReader(System.in), System.out, System.err, true);
            interpreter.setShowResults(true);
            if (line.hasOption("script")) {
                try {
                    interpreter.source(line.getOptionValue("script"));
                } catch (IOException ex) {
                    Logger.getLogger(ConsoleFrame.class.getName()).log(Level.SEVERE, "IOException while executing shell script.", ex);
                } catch (EvalError ex) {
                    Logger.getLogger(ConsoleFrame.class.getName()).log(Level.SEVERE, "EvalError while executing shell script.", ex);
                }
            }
            new Thread( interpreter ).start(); 
        } else if (line.hasOption("script")) {
            interpreter = new Interpreter();
            try {
                interpreter.source(line.getOptionValue("script"));
                System.exit(0);
            } catch (IOException ex) {
                Logger.getLogger(ConsoleFrame.class.getName()).log(Level.SEVERE, "IOException while executing shell script.", ex);
                System.exit(1);
            } catch (EvalError ex) {
                Logger.getLogger(ConsoleFrame.class.getName()).log(Level.SEVERE, "EvalError while executing shell script.", ex);
                System.exit(1);
            }
        } else if (System.console() == null) {
            ConsoleFrame cframe = new ConsoleFrame();
            cframe.setVisible(true);
            System.setOut(cframe.getInterpreter().getOut());
            System.setErr(cframe.getInterpreter().getErr());
            new Thread(cframe.getInterpreter()).start();
        } else {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java -jar <jar-name>", options, true);
            System.exit(0);
        }
        if (interpreter != null) {
            printWelcomeText(interpreter.getOut());
        }
    }
    
    public static void printWelcomeText(PrintStream out) {
        try {
            InputStream inputStream = urlToWelcomeText.openStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            String message = result.toString("UTF-8");
            out.print(message+"\n");
        } catch (IOException ex) {
            Logger.getLogger(CommandLineInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
