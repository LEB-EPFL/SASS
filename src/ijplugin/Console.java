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
package ijplugin;

import beanshell.ConsoleFrame;
import commandline.CommandLineInterface;

/**
 * Launched by FIJI
 * @author stefko
 */
public class Console {
    
    /**
     * Accessed from FIJI. Initializes console and prints welcome text.
     */
    public Console() {
        final ConsoleFrame console = new ConsoleFrame();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                console.setVisible(true);
            }
        });
        CommandLineInterface.printWelcomeText(console.getInterpreter().getOut());
        new Thread(console.getInterpreter()).start();
    }
}
