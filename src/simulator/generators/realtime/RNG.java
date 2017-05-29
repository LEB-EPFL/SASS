/*
 * Copyright (C) 2017 stefko
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
package simulator.generators.realtime;

import cern.jet.random.Gamma;
import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister;
import java.util.Random;

/**
 * Random number generator for STORMsim. Ensures repeatability.
 * @author stefko
 */
public final class RNG {
    private static Random uniform = new Random(1);
    private static Poisson poisson = new Poisson(1.0, new MersenneTwister(uniform.nextInt()));
    private static Gamma gamma = new Gamma(1.0, 5.0, new MersenneTwister(uniform.nextInt()));
    private static Normal gaussian = new Normal(0.0, 1.0, new MersenneTwister(uniform.nextInt()));
    
    private RNG() { }
    
    /**
     * This resets the generators
     * @param seed
     */
    public static void setSeed(int seed) {
        uniform = new Random(seed);
        poisson = new Poisson(1.0, new MersenneTwister(uniform.nextInt()));
        gamma = new Gamma(1.0, 5.0, new MersenneTwister(uniform.nextInt()));
        gaussian = new Normal(0.0, 1.0, new MersenneTwister(uniform.nextInt()));
    }
    
    public static Random getUniformGenerator() {
        return uniform;
    }
    
    public static Poisson getPoissonGenerator() {
        return poisson;
    }
    
    public static Gamma getGammaGenerator() {
        return gamma;
    }
    
    public static Normal getGaussianGenerator() {
        return gaussian;
    }
    
}
