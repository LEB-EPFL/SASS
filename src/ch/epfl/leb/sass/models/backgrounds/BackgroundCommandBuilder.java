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
package ch.epfl.leb.sass.models.backgrounds;

/**
 * Interface BackgroundCommand builders.
 * @author Kyle M. Douglass
 */
public interface BackgroundCommandBuilder {
    
    public BackgroundCommand build();
    
    /**
     * Sets the number of pixels of the images in the x-direction.
     * @param nX Number of pixels in x.
     * @return The very same builder object.
     */
    public BackgroundCommandBuilder nX(int nX);
    
    /**
     * Sets the number of pixels of the images in the y-direction.
     * @param nY Number of pixels in y.
     * @return The very same builder object.
     */
    public BackgroundCommandBuilder nY(int nY);
    
}
