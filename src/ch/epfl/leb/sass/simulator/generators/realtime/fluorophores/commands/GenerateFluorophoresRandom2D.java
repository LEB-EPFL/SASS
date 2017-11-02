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
package ch.epfl.leb.sass.simulator.generators.realtime.fluorophores.commands;

import ch.epfl.leb.sass.simulator.generators.realtime.Fluorophore;
import ch.epfl.leb.sass.simulator.generators.realtime.FluorophoreGenerator;
import ch.epfl.leb.sass.simulator.generators.realtime.FluorophoreProperties;
import ch.epfl.leb.sass.simulator.generators.realtime.components.Camera;
import ch.epfl.leb.sass.simulator.generators.realtime.psfs.PSFBuilder;
import java.util.List;

/**
 * This serves as the Invoker of a Fluorophore command.
 * 
 * @author Kyle M.Douglass
 */
public final class GenerateFluorophoresRandom2D implements FluorophoreCommand {
    /**
     * The number of fluorophores to create.
     */
    private int numFluors;
    
    /**
     * The microscope camera.
     */
    private Camera camera;
    
    /**
     * The set of properties that define the fluorophore dynamics.
     */
    private FluorophoreProperties fluorProp;
    
    /**
     * A builder for creating PSFs.
     */
    private PSFBuilder psfBuilder;
    
    /**
     * A builder for creating this command for fluorophore generation.
     */
    public static class Builder {
        private int numFluors;
        private Camera camera;
        private FluorophoreProperties fluorProp;
        private PSFBuilder psfBuilder;
        
        public Builder numFluors(int numFluors) {
            this.numFluors = numFluors;
            return this;
        }
        public Builder camera(Camera camera) {
            this.camera = camera;
            return this;
        }
        public Builder fluorProp(FluorophoreProperties fluorProp) {
            this.fluorProp = fluorProp;
            return this;
        }
        public Builder psfBuilder(PSFBuilder psfBuilder) {
            this.psfBuilder = psfBuilder;
            return this;
        }
        
        public GenerateFluorophoresRandom2D build() {
            return new GenerateFluorophoresRandom2D(this);
        }
    }
    
    /**
     * Creates a new GenerateFluorophoresRandom2D instance.
     * @param builder A Builder instance for this class.
     */
    private GenerateFluorophoresRandom2D(Builder builder) {
        this.camera = builder.camera;
        this.fluorProp = builder.fluorProp;
        this.numFluors = builder.numFluors;
        this.psfBuilder = builder.psfBuilder;
    }
    
    /**
     * Executes the command that generates the fluorophores.
     */
    @Override
    public List<Fluorophore> generateFluorophores() {
        return FluorophoreReceiver.generateFluorophoresRandom2D(
                this.numFluors, 
                this.camera,
                this.psfBuilder,
                this.fluorProp);        
    }
}
