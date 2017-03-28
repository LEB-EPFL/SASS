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
package algorithm_tester.generators.realtimegenerator;

/**
 *
 * @author stefko
 */
public class Fluorophore {
    public final double signal;
    public final double background;
    public final double base_Ton;
    public final double base_Toff;
    public final double base_Tbl;
    
    public Fluorophore(double signal_per_frame, double background_per_frame, 
            double base_Ton_frames, 
            double base_Toff_frames, double base_Tbl_frames) {
        signal = signal_per_frame;
        background = background_per_frame;
        base_Ton = base_Ton_frames;
        base_Toff = base_Toff_frames;
        base_Tbl = base_Tbl_frames;
    }
}
