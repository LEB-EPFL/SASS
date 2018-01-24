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
package ch.epfl.leb.sass.models.obstructors.internal.commands;

import ch.epfl.leb.sass.models.components.Camera;
import ch.epfl.leb.sass.models.components.Stage;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.obstructors.Obstructor;
import java.util.List;

/**
 *
 * @author Kyle M. Douglass
 */
public final class GenerateFiducialsRandom2D implements ObstructorCommand {
    
    /**
     * The number of gold beads to add.
     */
    private int numFiducials;
    
    /**
     * The microscope camera.
     */
    private final Camera camera;
    
    /**
     * The brightness of the obstructor (for individual fiducials).
     */
    private final double brightness;
    
    /**
     * The microscope stage and its position.
     */
    private final Stage stage;
    
    /**
     * A builder for creating PSFs.
     */
    private final PSFBuilder psfBuilder;
    
    /**
     * A builder for creating this command for obstructor generation.
     */
    public static class Builder implements ObstructorCommandBuilder {
        private int numFiducials;
        private Camera camera;
        private Stage stage;
        private double brightness;
        private PSFBuilder psfBuilder;
        
        public Builder numFiducials(int numFiducials) {
            this.numFiducials = numFiducials;
            return this;
        }
        @Override
        public Builder camera(Camera camera) {
            this.camera = camera;
            return this;
        }
        @Override
        public Builder stage(Stage stage) { this.stage = stage; return this; }
        @Override
        public Builder brightness(double brightness) {
            this.brightness = brightness;
            return this;
        }
        @Override
        public Builder psfBuilder(PSFBuilder psfBuilder) {
            this.psfBuilder = psfBuilder;
            return this;
        }
        
        public ObstructorCommand build() {
            return new GenerateFiducialsRandom2D(this);
        }
    }
    
    /**
     * Creates a new instance of this command.
     * @param builder 
     */
    private GenerateFiducialsRandom2D(Builder builder) {
        this.camera = builder.camera;
        this.numFiducials = builder.numFiducials;
        this.brightness = builder.brightness;
        this.stage = builder.stage;
        this.psfBuilder = builder.psfBuilder;
    }
    
    /**
     * Executes the command that generates the fluorophores.
     * 
     * @return The list of fluorophores.
     */
    @Override
    public List<Obstructor> generateObstructors() {
        return ObstructorReceiver.generateGoldBeadsRandom2D(
                this.numFiducials,
                this.brightness,
                this.camera,
                this.stage,
                this.psfBuilder);        
    }
    
}
