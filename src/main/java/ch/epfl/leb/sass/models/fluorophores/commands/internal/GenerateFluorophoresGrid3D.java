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
import ch.epfl.leb.sass.models.components.Laser;
import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.components.Objective;
import ch.epfl.leb.sass.models.fluorophores.Fluorophore;
import ch.epfl.leb.sass.models.fluorophores.commands.FluorophoreCommand;
import ch.epfl.leb.sass.models.fluorophores.commands.FluorophoreCommandBuilder;
import ch.epfl.leb.sass.models.photophysics.FluorophoreDynamics;

import java.util.List;

/**
 * This serves as the Invoker of a DefaultFluorophore command.
 * 
 * @author Kyle M.Douglass
 */
public final class GenerateFluorophoresGrid3D implements FluorophoreCommand {
    /**
     * The spacing between neighboring fluorophores [pixels].
     */
    private final int spacing;
    
    /**
     * The lower bound on the fluorophore z-position.
     */
    private final double zLow;
    
    /**
     * The upper bound on the fluorophore z-position.
     */
    private final double zHigh;
    
    /**
     * The microscope camera.
     */
    private final Camera camera;
    
    /**
     * The set of properties that define the fluorophore dynamics.
     */
    private final FluorophoreDynamics fluorDynamics;
    
    /**
     * The laser that illuminates the fluorophores.
     */
    private final Laser laser;
    
    /**
     * The microscope objective.
     */
    private final Objective objective;
    
    /**
     * A builder for creating PSFs.
     */
    private final PSFBuilder psfBuilder;
    
    /**
     * A builder for creating this command for fluorophore generation.
     */
    public static class Builder implements FluorophoreCommandBuilder {
        private int spacing;
        private double zLow;
        private double zHigh;
        private Camera camera;
        private Laser laser;
        private Objective objective;
        private double wavelength;
        private FluorophoreDynamics fluorDynamics;
        private PSFBuilder psfBuilder;
        
        public Builder spacing(int spacing) {
            this.spacing = spacing;
            return this;
        }
        public Builder zLow(double zLow) { this.zLow = zLow; return this; }
        public Builder zHigh(double zHigh) { this.zHigh = zHigh; return this; }
        @Override
        public Builder camera(Camera camera) {
            this.camera = camera;
            return this;
        }
        @Override
        public Builder laser(Laser laser) {
            this.laser = laser;
            return this;
        }
        @Override
        public Builder fluorDynamics(FluorophoreDynamics fluorDynamics) {
            this.fluorDynamics = fluorDynamics;
            return this;
        }
        @Override
        public Builder objective(Objective objective) {
            this.objective = objective;
            return this;
        }
        @Override
        public Builder psfBuilder(PSFBuilder psfBuilder) {
            this.psfBuilder = psfBuilder;
            return this;
        }
        
        public FluorophoreCommand build() {
            return new GenerateFluorophoresGrid3D(this);
        }
    }
    
    /**
     * Creates a new GenerateFluorophoresRandom2D instance.
     * @param builder A Builder instance for this class.
     */
    private GenerateFluorophoresGrid3D(Builder builder) {
        this.camera = builder.camera;
        this.fluorDynamics = builder.fluorDynamics;
        this.laser = builder.laser;
        this.spacing = builder.spacing;
        this.objective = builder.objective;
        this.psfBuilder = builder.psfBuilder;
        this.zLow = builder.zLow;
        this.zHigh = builder.zHigh;
    }
    
    /**
     * Executes the command that generates the fluorophores.
     * 
     * @return The list of Fluorophores.
     */
    @Override
    public List<Fluorophore> generateFluorophores() {
        return FluorophoreReceiver.generateFluorophoresGrid3D(
                this.spacing,
                this.zLow,
                this.zHigh,
                this.camera,
                this.laser,
                this.objective,
                this.psfBuilder,
                this.fluorDynamics);        
    }
}
