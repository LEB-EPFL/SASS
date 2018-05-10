/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
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
package ch.epfl.leb.sass.models.fluorophores.internal.commands;

import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.models.fluorophores.internal.dynamics.FluorophoreDynamics;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This serves as the Invoker of a DefaultFluorophore command.
 * 
 * @author Kyle M.Douglass
 */
public final class GenerateFluorophoresFromCSV implements FluorophoreCommand {
    /**
     * The file containing the fluorophore position data.
     */
    private final File file;
    
    /**
     * The microscope camera.
     */
    private final Camera camera;
    
    /**
     * The set of properties that define the fluorophore dynamics.
     */
    private final FluorophoreDynamics fluorDynamics;
    
    /**
     * A builder for creating PSFs.
     */
    private final PSFBuilder psfBuilder;
    
    /**
     * Whether the positions should be rescaled to fit inside the field of view.
     */
    private final boolean rescale;
    
    /**
     * A builder for creating this command for fluorophore generation.
     */
    public static class Builder implements FluorophoreCommandBuilder {
        private File file;
        private Camera camera;
        private FluorophoreDynamics fluorDynamics;
        private PSFBuilder psfBuilder;
        private boolean rescale;
        
        public Builder file(File file) {
            this.file = file;
            return this;
        }
        @Override
        public Builder camera(Camera camera) {
            this.camera = camera;
            return this;
        }
        @Override
        public Builder fluorDynamics(FluorophoreDynamics fluorDynamics) {
            this.fluorDynamics = fluorDynamics;
            return this;
        }
        @Override
        public Builder psfBuilder(PSFBuilder psfBuilder) {
            this.psfBuilder = psfBuilder;
            return this;
        }
        public Builder rescale(boolean rescale) {
            this.rescale = rescale;
            return this;
        }
        
        @Override
        public FluorophoreCommand build() {
            return new GenerateFluorophoresFromCSV(this);
        }
    }
    
    /**
     * Creates a new GenerateFluorophoresRandom2D instance.
     * @param builder A Builder instance for this class.
     */
    private GenerateFluorophoresFromCSV(Builder builder) {
        this.camera = builder.camera;
        this.fluorDynamics = builder.fluorDynamics;
        this.file = builder.file;
        this.psfBuilder = builder.psfBuilder;
        this.rescale = builder.rescale;
    }
    
    /**
     * Executes the command that generates the fluorophores.
     * 
     * @return The list of Fluorophores.
     */
    @Override
    public List<Fluorophore> generateFluorophores() {
        try {
            return FluorophoreReceiver.generateFluorophoresFromCSV(
                    this.file,
                    this.camera,
                    this.psfBuilder,
                    this.fluorDynamics,
                    this.rescale);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(
                        GenerateFluorophoresFromCSV.class.getName())
                        .log(Level.WARNING, null, ex);
            
            // No file found; return empty list of fluorophores.
            return new ArrayList<Fluorophore>();
        } catch (IOException ex) {
            Logger.getLogger(
                        GenerateFluorophoresFromCSV.class.getName())
                        .log(Level.WARNING, null, ex);
            
            // Error reading file; return empty list of fluorophores.
            return new ArrayList<Fluorophore>();
        }
    }
}
