package lejos.robotics.localization;

import java.util.ArrayList;

/**
 * A class that scans a room for beacons and identifies the angles to the beacons.
 * 
 * @author BB
 *
 */
public interface BeaconLocator {
	
	/**
     * <p>This method performs a scan around the robot. The angle values are always relative to the robot, because it does
     * not know which direction it is facing. 0 degrees is the forward direction the robot is facing. Angle increases 
     * counter-clockwise from 0. So 90 degrees is to the left of the robot, 180 is behind, and 270 is to the right.</p>
     *    
     * @return an ArrayList of double values indicating angles to the beacons 
     */
	public ArrayList<Double> locate();
}
