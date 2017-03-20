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
package algorithm_tester;

import java.util.Collections;
import java.util.Comparator;
import org.micromanager.data.Coords;

/**
 *
 * @author stefko
 */
public class SortedCoordsList extends java.util.ArrayList<Coords> {
    /**
     * Takes in a Coords iterable from Datastore, sorts it,
     * and turns it into a list.
     */
    public SortedCoordsList() {
        // Empty constructor, create an empty list.
        super();
    }
    
    public SortedCoordsList(java.lang.Iterable<Coords> iter) {
        super();
        // Just add the unordered coords into the list
        for (Coords item: iter) {
            this.add(item);
        }
        // Sort the list using the CoordsComparator
        Collections.sort(this, SortedCoordsList.CoordsComparator);
    }
    
    private static final Comparator<Coords> CoordsComparator = new Comparator<Coords>() {
        // Compare the String outputs of Coords.toString method.
        @Override
        public int compare(Coords o1, Coords o2) {
            return o1.toString().compareTo(o2.toString());
        }
        
    };
}