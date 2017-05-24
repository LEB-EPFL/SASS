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
package algorithm_tester.generators.realtime.fluorophores;

import algorithm_tester.generators.realtime.Camera;
import algorithm_tester.generators.realtime.Fluorophore;

/**
 *
 * @author stefko
 */
public class PalmFluorophore extends Fluorophore {
    private final PalmProperties f_props;
    private PalmState state;
    
    
    public PalmFluorophore(PalmProperties f_props, Camera camera, double x, double y) {
        super(camera, x, y);
        this.f_props = f_props;
        this.state = PalmState.INACTIVE;
    }

    @Override
    public boolean isOn() {
        return this.state.equals(PalmState.ACTIVE);
    }

    @Override
    public boolean isBleached() {
        return this.state.equals(PalmState.BLEACHED);
    }

    @Override
    protected double simulateBrightness() {
        if (isBleached())
            return 0.0;
        double t_rem = 1.0; // remaining time
        double on_time = 0.0;
        while (t_rem > 0.0) {
            switch (state) {
                case INACTIVE:
                    double t_activation = nextExponential(f_props.T_a);
                    if (t_activation < t_rem) {
                        t_rem -= t_activation;
                        state = PalmState.ACTIVE;
                    } else {
                        t_rem = 0.0;
                    }
                    break;
                    
                case ACTIVE:
                    double t_bleach = nextExponential(f_props.T_b);
                    double t_dark1 = nextExponential(f_props.T_d1);
                    double t_dark2 = nextExponential(f_props.T_d2);
                    if (t_bleach < t_rem && t_bleach < t_dark1 && t_bleach < t_dark2) {
                        on_time += t_bleach;
                        // we are bleaching
                        t_rem = 0.0;
                        state = PalmState.BLEACHED;
                    } else if (t_dark1 < t_rem && t_dark1 < t_dark2) {
                        on_time += t_dark1;
                        t_rem -= t_dark1;
                        state = PalmState.DARK_1;
                    } else if (t_dark2 < t_rem) {
                        on_time += t_dark2;
                        t_rem -= t_dark2;
                        state = PalmState.DARK_2;
                    } else {
                        t_rem = 0.0;
                    }
                    break;
                    
                case DARK_1:
                    double t_recovery_1 = nextExponential(f_props.T_r1);
                    if (t_recovery_1 < t_rem) {
                        t_rem -= t_recovery_1;
                        state = PalmState.ACTIVE;
                    } else {
                        t_rem = 0.0;
                    }
                    break;
                    
                case DARK_2:
                    double t_recovery_2 = nextExponential(f_props.T_r2);
                    if (t_recovery_2 < t_rem) {
                        t_rem -= t_recovery_2;
                        state = PalmState.ACTIVE;
                    } else {
                        t_rem = 0.0;
                    }
                    break;
                    
                case BLEACHED:
                    t_rem = 0.0;
                    break;
            }
        }
        return flicker(on_time*f_props.signal);
    }

    @Override
    public void recalculate_lifetimes(double laser_power) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

enum PalmState {
    INACTIVE,
    ACTIVE,
    DARK_1,
    DARK_2,
    BLEACHED;
}
