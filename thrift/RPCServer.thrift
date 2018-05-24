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

exception ImageGenerationException { }
exception UnknownSimulationIdException { }

/**
 * RPC wrapper around the Simulator class.
 */
service RemoteSimulationService {

  /**
   * Creates a new simulation. The ID of the simulation is returned.
   */
  i32 createSimulation(),

  /**
   * Deletes a simulation with the given ID.
   */
  void deleteSimulation(1: i32 id) throws (1: UnknownSimulationIdException ex),
  
  /**
   * Returns the current value for the control signal.
   */
  double getControlSignal(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Gets the name of the JSON key for the camera information.
   */
  string getCameraJsonName(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Gets the name of the JSON key for the fluoresence information.
   */
  string getFluorescenceJsonName(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Returns the size of the field-of-view in object space units.
   */
  double getFovSize(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Returns the number of images that have been simulated.
   */
  i32 getImageCount(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Gets the name of the JSON key for the laser information.
   */
  string getLaserJsonName(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Increments the simulation by one time step and returns an image.
   */
  binary getNextImage(1: i32 id) throws(1: ImageGenerationException ex,
                                        2: UnknownSimulationIdException ex2),

  /**
   * Returns the object space pixel size.
   *
   * Units are the same as those of the camera's pixel size.
   */
  double getObjectSpacePixelSize(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Returns the simulation server's current status.
   */
  string getServerStatus(),

  /**
   * Returns a brief description of the ground truth signal.
   */
  string getShortTrueSignalDescription(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Returns the true simulation signal at the given image.
   */
  double getTrueSignal(1: i32 id, 2: i32 imageNum) throws (1: UnknownSimulationIdException ex),

  /**
   * Advances the simulation without creating a new image.
   */
  void incrementTimeStep(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Changes the simulation's control signal.
   */
  void setControlSignal(1: i32 id, 2: double power) throws (1: UnknownSimulationIdException ex)

  /**
   * Returns information about changes in the state of the simulation
   * as a JSON string.
   */
  string toJsonMessages(1: i32 id) throws (1: UnknownSimulationIdException ex),

  /**
   * Returns information about the state of the simulation as a JSON
   * string.
   */
  string toJsonState(1: i32 id) throws (1: UnknownSimulationIdException ex),
       
}
