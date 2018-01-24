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
package ch.epfl.leb.sass.models.legacy;

import ch.epfl.leb.sass.models.legacy.Camera;
import ch.epfl.leb.sass.models.legacy.FluorophoreProperties;
import ch.epfl.leb.sass.models.fluorophores.internal.DefaultFluorophore;
import ch.epfl.leb.sass.models.legacy.FluorophoreGenerator;
import java.util.ArrayList;
import ch.epfl.leb.sass.models.psfs.internal.Gaussian3D;
import ch.epfl.leb.sass.models.psfs.PSFBuilder;
import ch.epfl.leb.sass.models.legacy.SimpleProperties;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.geom.Point2D;

/**
 *
 * @author Kyle M. Douglass
 */
public class FluorophoreGeneratorTest {
    
    private Camera dummyCamera = null;
    private PSFBuilder testPSFBuilder = null;
    private FluorophoreGenerator fluorGen = new FluorophoreGenerator();
    
    // Mocking FluorophoreProperties proved a bit difficult, so I chose the
    // least likely implementation to change in the future.
    private FluorophoreProperties fluorProp = new SimpleProperties(2500, 50, 3, 100, 1000);
    
    public FluorophoreGeneratorTest() {
        this.dummyCamera     = mock(Camera.class);
        this.testPSFBuilder = new Gaussian3D.Builder();
    }
    
    /**
     * Test of generateFluorophoresGrid3D method, of class FluorophoreGenerator.
     */
    @Test
    public void testGenerateFluorophoresGrid3D() {
        // Setup a 32x32 pixel camera
        int res_x = 32;
        int res_y = 32;
        when(this.dummyCamera.getRes_X()).thenReturn(res_x);
        when(this.dummyCamera.getRes_Y()).thenReturn(res_y);

        int spacing  = 4;
        double zLow  = 4;
        double zHigh = 10;
        
        ArrayList<DefaultFluorophore> fluors = new ArrayList();
        fluors = this.fluorGen.generateFluorophoresGrid3D(
                spacing,
                zLow,
                zHigh,
                dummyCamera,
                testPSFBuilder,
                this.fluorProp);

        // There are ((32/4) - 1)^2 fluorophores
        assertEquals((res_x - spacing) * (res_y - spacing) / spacing / spacing,
                 fluors.size());
        
        // Check that the fluorophores span the correct range in z-values.
        assertEquals(zLow, fluors.get(0).z, 0.0001);
        assertEquals(zHigh, fluors.get(fluors.size() - 1).z, 0.0001);
  
    }

}
