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
package simulator.generators.realtime.fluorophores;

import simulator.generators.realtime.Camera;
import simulator.generators.realtime.Fluorophore;

/**
 *
 * @author stefko
 */
public class dStormFluorophore extends Fluorophore {
    private final dStormProperties f_props;
    private dStormState state;
    
    private double T_dark_recovery;
    public dStormFluorophore(dStormProperties fluo, Camera camera, double x, double y) {
        super(camera, x, y);
        f_props = fluo;
        state = dStormState.DARK;
        this.recalculate_lifetimes(0.0);
    }

    @Override
    public boolean isOn() {
        return state.equals(dStormState.ACTIVE);
    }

    @Override
    public boolean isBleached() {
        return state.equals(dStormState.BLEACHED);
    }

    @Override
    public void recalculate_lifetimes(double laser_power) {
        T_dark_recovery = f_props.getDarkRecoveryTime(laser_power);
    }

    @Override
    protected double simulateBrightness() {
        if (isBleached())
            return 0.0;
        double t_rem = 1.0;
        double on_time = 0.0;
        while (t_rem > 0.0) {
            switch (state) {
                case DARK:
                    double t_recovery = nextExponential(T_dark_recovery);
                    if (t_recovery < t_rem) {
                        t_rem -= t_recovery;
                        state = dStormState.ACTIVE;
                    } else {
                        t_rem = 0.0;
                    }
                    break;
                
                case ACTIVE:
                    double t_bleach = nextExponential(f_props.T_bleach);
                    double t_triplet = nextExponential(f_props.T_triplet);
                    if (t_bleach<t_rem && t_bleach<t_triplet) {
                        on_time += t_bleach;
                        t_rem = 0.0;
                        state = dStormState.BLEACHED;
                    } else if (t_triplet<t_rem) {
                        on_time += t_triplet;
                        t_rem -= t_triplet;
                        state = dStormState.TRIPLET;
                    } else {
                        on_time += t_rem;
                        t_rem = 0.0;
                    }
                    break;
                    
                case TRIPLET:
                    double t_dark = nextExponential(f_props.T_dark);
                    double t_triplet_recovery = nextExponential(f_props.T_triplet_recovery);
                    if (t_dark<t_rem && t_dark<t_triplet_recovery) {
                        t_rem -= t_dark;
                        state = dStormState.DARK;
                    } else if (t_triplet_recovery<t_rem) {
                        t_rem -= t_triplet_recovery;
                        state = dStormState.ACTIVE;
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
    
}

enum dStormState {
    ACTIVE,
    TRIPLET,
    DARK,
    BLEACHED;
}
