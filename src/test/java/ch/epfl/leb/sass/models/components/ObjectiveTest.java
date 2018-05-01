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
package ch.epfl.leb.sass.models.components;

import ch.epfl.leb.sass.models.components.Objective;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kyle M. Douglass
 */
public class ObjectiveTest {
    
    public ObjectiveTest() {
    }

    /**
     * Test of psfFWHM method, of class Objective.
     */
    @Test
    public void testAiryFWHM() {
        System.out.println("airyFWHM");
        double wavelength = 0.65;
        Objective.Builder builder = new Objective.Builder();
        Objective instance = builder.NA(1.4).mag(60).build();
        double expResult = 14.3185;
        double result = instance.airyFWHM(wavelength);
        assertEquals(expResult, result, 0.0001);
    }
    
    /**
     * Test of psfFWHM method, of class Objective.
     */
    @Test
    public void testAiryRadius() {
        System.out.println("airyRadius");
        double wavelength = 0.65;
        Objective.Builder builder = new Objective.Builder();
        Objective instance = builder.NA(1.4).mag(60).build();
        double expResult = 16.9929;
        double result = instance.airyRadius(wavelength);
        assertEquals(expResult, result, 0.0001);
    }
    
}
