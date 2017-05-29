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
package simulator.generators.realtime.fluorophores;

import simulator.generators.realtime.Camera;
import simulator.generators.realtime.Fluorophore;
import simulator.generators.realtime.FluorophoreProperties;

/**
 * SimpleProperties properties (signal, background values and time constants).
 * @author Marcel Stefko
 */
public class SimpleProperties extends FluorophoreProperties {
    /** 
     * mean on-time with unit laser power [frames]
     */
    public final double base_Ton;

    /**
     * mean off-time with unit laser power [frames]
     */
    public final double base_Toff;

    /**
     * mean bleaching time with unit laser power [frames]
     */
    public final double base_Tbl;
    
    /**
     * Initialize fluorophore with given properties
     * @param signal_per_frame photons emitted if fluorophore is fully on
     * @param background_per_frame constant background of the fluorophore
     * @param base_Ton_frames mean on-time with unit laser power [frames]
     * @param base_Toff_frames mean off-time with unit laser power [frames]
     * @param base_Tbl_frames mean bleaching time with unit laser power [frames]
     */
    public SimpleProperties(double signal_per_frame, double background_per_frame, 
            double base_Ton_frames, 
            double base_Toff_frames, double base_Tbl_frames) {
        super(signal_per_frame, background_per_frame);
        base_Ton = base_Ton_frames;
        base_Toff = base_Toff_frames;
        base_Tbl = base_Tbl_frames;
    }

    @Override
    public Fluorophore createFluorophore(Camera camera, double x, double y) {
        return new SimpleFluorophore(this, camera, x, y);
    }
}
