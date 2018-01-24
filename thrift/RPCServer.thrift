/**
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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
 *
 */

namespace java ch.epfl.leb.sass.server
namespace py remotesim

/**
 * Contains information about the simulation's current frame.
 */
struct EmitterState {
  1: i32 frameNumber,
  2: i32 fluorophoreID,
  3: double x,
  4: double y,
  5: double z,
  6: double brightness,
  7: double timeOn
}

service RemoteSimulationService {

  /**
   * Returns the simulation server's current status.
   */
  string getServerStatus(),

  /**
   * Increments the simulation by one time step and returns an image.
   */
  binary getNextImage(),

  /**
   * Changes the simulation'ss fluorescence activation laser power.
   */
  void setActivationLaserPower(1: double power),

  /**
   * Returns information about the current state of each emitter.
   */
  list<EmitterState> getSimulationState()
       
}
