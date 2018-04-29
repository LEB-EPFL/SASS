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
package ch.epfl.leb.sass.ijplugin;

import ch.epfl.leb.sass.commandline.BeanShellConsole;
import ch.epfl.leb.sass.commandline.CommandLineInterface;
import ij.plugin.PlugIn;

/**
 * Wrapper for initialization of BeanShell console
 * @author Marcel Stefko
 */
public class CommandPrompt implements PlugIn {
    private BeanShellConsole console;
    
    /**
     * Initializes new BeanShell console
     */
    public CommandPrompt() {
        console = new BeanShellConsole("SASS BeanShell prompt");
    }
    
    /**
     * Set input and output streams, and print welcome text.
     * @param string
     */
    @Override
    public void run(String string) {
        System.setOut(console.getInterpreter().getOut());
        System.setErr(console.getInterpreter().getErr());
        new Thread(console.getInterpreter()).start();
        CommandLineInterface.printWelcomeText(console.getInterpreter().getOut());
    }
    
}
