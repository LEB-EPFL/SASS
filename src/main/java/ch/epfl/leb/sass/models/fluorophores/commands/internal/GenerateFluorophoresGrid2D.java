/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
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
package ch.epfl.leb.sass.models.fluorophores.commands.internal;

import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.models.fluorophores.commands.FluorophoreCommand;
import ch.epfl.leb.sass.models.fluorophores.commands.FluorophoreCommandBuilder;
import ch.epfl.leb.sass.models.illuminations.Illumination;
import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamics;

import java.util.List;

/**
 * This serves as the Invoker of a DefaultFluorophore command.
 * 
 * @author Kyle M.Douglass
 */
public final class GenerateFluorophoresGrid2D implements FluorophoreCommand {
    /**
     * The spacing between neighboring fluorophores [pixels].
     */
    private final int spacing;
    
    /**
     * The microscope camera.
     */
    private final Camera camera;
    
    /**
     * The set of properties that define the fluorophore dynamics.
     */
    private final FluorophoreDynamics fluorDynamics;
    
    /**
     * The illumination profile on the sample.
     */
    private final Illumination illumination;
    
    /**
     * A builder for creating PSFs.
     */
    private final PSFBuilder psfBuilder;
    
    /**
     * A builder for creating this command for fluorophore generation.
     */
    public static class Builder implements FluorophoreCommandBuilder {
        private int spacing;
        private Camera camera;
        private Illumination illumination;
        private FluorophoreDynamics fluorDynamics;
        private PSFBuilder psfBuilder;
        
        public Builder spacing(int spacing) {
            this.spacing = spacing;
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
        public Builder illumination(Illumination illumination) {
            this.illumination = illumination;
            return this;
        }
        @Override
        public Builder psfBuilder(PSFBuilder psfBuilder) {
            this.psfBuilder = psfBuilder;
            return this;
        }
        
        @Override
        public FluorophoreCommand build() {
            return new GenerateFluorophoresGrid2D(this);
        }
    }
    
    /**
     * Creates a new GenerateFluorophoresRandom2D instance.
     * @param builder A Builder instance for this class.
     */
    private GenerateFluorophoresGrid2D(Builder builder) {
        this.camera = builder.camera;
        this.fluorDynamics = builder.fluorDynamics;
        this.illumination = builder.illumination;
        this.spacing = builder.spacing;
        this.psfBuilder = builder.psfBuilder;
    }
    
    /**
     * Executes the command that generates the fluorophores.
     * 
     * @return The list of fluorophores.
     */
    @Override
    public List<Fluorophore> generateFluorophores() {
        return FluorophoreReceiver.generateFluorophoresGrid2D(
                this.spacing, 
                this.camera,
                this.illumination,
                this.psfBuilder,
                this.fluorDynamics);        
    }
}
