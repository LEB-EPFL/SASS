/*
 * Copyright (C) 2017 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Federale de Lausanne
 * 
 * Author: Marcel Stefko
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
package ch.epfl.leb.sass.models.fluorophores.internal;

/**
 * Class which describes a Markovian fluorophore state model. This class
 * provides transition rates and mean lifetimes for Markovian models based
 * on current laser illumination intensity.
 * @author stefko
 */
public class StateSystem {
    
    private final int N_states;
    
    private final double[][][] M_scaling;
    
    /**
     * Laser power value for which the currently stored lifetime values are calculated.
     */
    protected double current_laser_power = Double.NaN;
    
    /**
     * Transition matrix. Mk[i][j] holds the transition rate from the i-th
 state to the j-th state. Diagonal elements must be 0.
     */
    private final double[][] Mk;
    
    /**
     * Transition matrix. Mt[i][j] holds the mean ransition time from the j-th
 state to the j-th state.
     */
    private final double[][] Mt;
    
    /**
     * Initialize the state system.
     * @param N_states number of states
     * @param M_scaling double[][][] matrix of dimensions N x N x A.
     *  A can be different for each position in the matrix. This matrix can
     *  be interpreted as follows: 
     * 
     *  double[] P = M_scaling[i][j];
     * 
     *  k_ij(I) = P[0] + P[1]*I + P[2]*I^2 + ... P[n]*I^n;
     * 
     *  k_ij(I) is transition rate between i-th and j-th state under laser
     *  illumination intensity I.
     * 
     *  The first row of this matrix is considered the active state, the last
     *  row is considered the bleached state.
     */
    public StateSystem(int N_states, double[][][] M_scaling) {
        if (N_states < 2) {
            throw new IllegalArgumentException("Number of states must be at least 2.");
        }
        if (M_scaling.length != N_states) {
            throw new IllegalArgumentException("Rate scaling matrix dimensions do not match the number of states.");
        }
        for (double[][] row: M_scaling) {
            if (row.length != N_states) {
                throw new IllegalArgumentException("Rate scaling matrix dimensions do not match the number of states.");
            }
            for (double[] rate_polynomial: row) {
                if (rate_polynomial.length < 1) {
                    throw new IllegalArgumentException("Each rate scaling matrix element must contain at least one value.");
                }
            }
        }
        
        this.N_states = N_states;
        this.M_scaling = M_scaling;
        
        this.Mk = new double[N_states][N_states];
        this.Mt = new double[N_states][N_states];
        
        this.recalculate_lifetimes(1.0);
    }
    
    /**
     * Recalculates each element of the transition matrix, based on the scaling
     * matrix provided at initialization.
     * 
     * double[] P = M_scaling[i][j];
     * 
     *  k_ij(I) = P[0] + P[1]*I + P[2]*I^2 + ... P[n]*I^n;
     * 
     *  k_ij(I) is transition rate between i-th and j-th state under laser
     *  illumination intensity I.
     * @param laser_power illumination intensity I to recalculate for
     */
    public final void recalculate_lifetimes(double laser_power) {
        // do nothing if power is the same
        if (laser_power == current_laser_power) {
            return;
        }
        current_laser_power = laser_power;
        
        // iterate over every state transition
        for (int row=0; row<N_states; row++) {
            for (int col=0; col<N_states; col++) {
                // ignore diagonal elements
                if (row==col) {
                    Mk[row][col] = 0.0;
                    continue;
                }
                
                // add constant term which is guaranteed to exist
                Mk[row][col] = M_scaling[row][col][0];
                
                // iterate over linear, quadratic, ... terms and add them till they exist
                for (int i=1; i<M_scaling[row][col].length; i++) {
                    Mk[row][col] += M_scaling[row][col][i] * Math.pow(current_laser_power,i);
                }
                
                // if result is less than zero, throw exception
                if (Mk[row][col] < 0.0) {
                    throw new IllegalArgumentException("Calculation resulted in negative transition rate. Row: " + row + " Col: " + col);
                }
            }
        }
        // transform rates into lifetimes
        for (int row=0; row<N_states; row++) {
            for (int col=0; col<N_states; col++) {
                if (Mk[row][col] == 0.0) {
                    Mt[row][col] = Double.POSITIVE_INFINITY;
                } else {
                   Mt[row][col] = 1.0 / Mk[row][col];
                }
            }
        }
    }
    
    /**
     * 
     * @param from index of initial state
     * @param to index of final state
     * @return transition rate from one state to another
     */
    public final double getTransitionRate(int from, int to) {
        return Mk[from][to];
    }
    
    /**
     *
     * @param from index of initial state
     * @param to index of final state
     * @return mean transition lifetime from one state to another
     */
    public final double getMeanTransitionLifetime(int from, int to) {
        return Mt[from][to];
    }
    
    /**
     * Returns true if the state is the active state (the 0-th state)
     * @param state id of current state
     * @return (state==0)
     */
    public boolean isOnState(int state) {
        return state==0;
    }

    /**
     * Returns true if the state is the bleached state (the last state of the model)
     * @param state id of current state
     * @return state == (N_states - 1)
     */
    public boolean isBleachedState(int state) {
        return state == (N_states - 1);
    }
    
    /**
     *
     * @return number of states of this model
     */
    public int getNStates() {
        return this.N_states;
    }
}
