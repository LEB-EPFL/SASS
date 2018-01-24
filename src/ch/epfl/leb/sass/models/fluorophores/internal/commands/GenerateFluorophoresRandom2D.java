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

import ch.epfl.leb.sass.models.fluorophores.internal.DefaultFluorophore;
import ch.epfl.leb.sass.models.fluorophores.internal.dynamics.FluorophoreDynamics;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import java.util.List;

/**
 * This serves as the Invoker of a DefaultFluorophore command.
 * 
 * @author Kyle M.Douglass
 */
public final class GenerateFluorophoresRandom2D implements FluorophoreCommand {
    /**
     * The number of fluorophores to create.
     */
    private final int numFluors;
    
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
     * A builder for creating this command for fluorophore generation.
     */
    public static class Builder implements FluorophoreCommandBuilder {
        private int numFluors;
        private Camera camera;
        private FluorophoreDynamics fluorDynamics;
        private PSFBuilder psfBuilder;
        
        public Builder numFluors(int numFluors) {
            this.numFluors = numFluors;
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
        
        public FluorophoreCommand build() {
            return new GenerateFluorophoresRandom2D(this);
        }
    }
    
    /**
     * Creates a new GenerateFluorophoresRandom2D instance.
     * @param builder A Builder instance for this class.
     */
    private GenerateFluorophoresRandom2D(Builder builder) {
        this.camera = builder.camera;
        this.fluorDynamics = builder.fluorDynamics;
        this.numFluors = builder.numFluors;
        this.psfBuilder = builder.psfBuilder;
    }
    
    /**
     * Executes the command that generates the fluorophores.
     * 
     * @return The list of fluorophores.
     */
    @Override
    public List<DefaultFluorophore> generateFluorophores() {
        return FluorophoreReceiver.generateFluorophoresRandom2D(
                this.numFluors, 
                this.camera,
                this.psfBuilder,
                this.fluorDynamics);        
    }
}
