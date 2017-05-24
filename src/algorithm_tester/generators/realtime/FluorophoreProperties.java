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
package algorithm_tester.generators.realtime;

/**
 *
 * @author stefko
 */
public class FluorophoreProperties {
    public final double signal;
    public final double background;
    
    public FluorophoreProperties(double signal, double background) {
        if (signal < 0.0 || background < 0.0) {
            throw new IllegalArgumentException();
        }
        
        this.signal = signal;
        this.background = background;
    }
}
