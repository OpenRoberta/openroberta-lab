package lejos.robotics.mapping;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.robotics.RangeReadings;
import lejos.robotics.localization.MCLParticleSet;
import lejos.robotics.localization.MCLPoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.objectdetection.RangeFeature;
import lejos.robotics.pathfinding.Path;

/**
 * NavigationModel is an abstract class that has two implementations: NXTNavigationModel and PCNavigationModel.
 * 
 * It is used to hold all navigation data and transmit updates to the date and other events between a NXT
 * brick and the PC.
 * 
 * The abstract NAvigationModel class defines all the events and all data and methods that are common to the 
 * NXT and PC implementations.
 * 
 * The purpose of NavigationModel and the NXT and PC implementations is to to allow navigation tasks to be 
 * split between the NXT and the PC, to allow the PC to show a graphical display of the navigational data
 * and allow the user to interact with it.
 * 
 * This allows many different navigation applications to be developed which split processing between the PC
 * and the NXT.
 * 
 * @author Lawrie Griffiths
 *
 */
public abstract class NavigationModel {
	protected LineMap map;
	protected String nxtName;
	protected DataInputStream dis;
	protected DataOutputStream dos;
	protected int numReadings = 0;
	protected Pose currentPose = new Pose(0,0,0);
	protected Waypoint target = null;
	protected MCLParticleSet particles;
	protected MCLPoseProvider mcl;
	protected RangeReadings readings = new RangeReadings(1);
	protected Path path;
	protected Move lastMove = new Move(0,0,false);
	protected Move lastPlannedMove = new Move(0,0,false);
	protected RangeFeature feature = new RangeFeature(readings);
	protected boolean debug = false;
	
	/**
	 * Navigation events that are transmitted between the PC and the NXT (and vice versa).
	 * 
	 * @author Lawrie Griffiths
	 *
	 */
	public enum NavEvent {LOAD_MAP, GOTO, TRAVEL, ROTATE, STOP, GET_POSE, 
		SET_POSE, RANDOM_MOVE, TAKE_READINGS, GET_READINGS, FIND_CLOSEST, ADD_WAYPOINT, GET_PARTICLES, PARTICLE_SET,
		RANGE_READINGS, MOVE_STARTED, MOVE_STOPPED, WAYPOINT_REACHED, CLOSEST_PARTICLE, GET_ESTIMATED_POSE,
		ESTIMATED_POSE, PATH_COMPLETE, FEATURE_DETECTED, FIND_PATH, PATH, SET_TARGET, FOLLOW_PATH, ROTATE_TO,
		PATH_GENERATED, PATH_INTERRUPTED, CLEAR_PATH, ARC, START_NAVIGATOR, LOCALIZE, LOCATED, EXIT, CALCULATE_PATH,
		SOUND, GET_BATTERY, BATTERY, PILOT_PARAMS, RANGE_FEATURE_DETECTOR_PARAMS, RANGE_SCANNER_PARAMS,
		TRAVEL_SPEED, ROTATE_SPEED, RANDOM_MOVE_PARAMS}
	
	/**
	 * Test is the model has a map registered
	 * 
	 * @return true iff a map is registered
	 */
	public boolean hasMap() {
		return map != null;
	}
	
	/**
	 * Get the registered map
	 * 
	 * @return the LineMap
	 */
	public LineMap getMap() {
		return map;
	}
	
	/**
	 * Set the number of readings for MCL
	 * 
	 * @param number the number of readings
	 */
	public void setNumReadings(int number) {
		numReadings = number;
	}
	
	/**
	 * Get the current pose of the robot
	 * 
	 * @return the robot pose
	 */
	public Pose getRobotPose() {
		return currentPose;
	}
	
	/**
	 * Get the registered particle set
	 * 
	 * @return the MCLParticleSet or null
	 */
	public MCLParticleSet getParticles() {
		return particles;
	}
	
	/**
	 * Set the current robot pose
	 * 
	 * @param p the pose
	 */
	public void setRobotPose(Pose p) {
		currentPose = p;
	}
	
	/**
	 * Set the MCL Particle set
	 * 
	 * @param particles an MCLParticleSet
	 */
	public void setParticleSet(MCLParticleSet particles) {
		this.particles = particles;
	}
	
	/**
	 * Get the current range readings
	 * 
	 * @return the RangeReadings object
	 */
	public RangeReadings getReadings() {
		return readings;
	}
	
	/**
	 * Set the target waypoint that the robot is to go to
	 * 
	 * @param target the target waypoint
	 */
	public void setTarget(Waypoint target) {
		this.target = target;
	}
	
	/**
	 * Get the target waypoint
	 * 
	 * @return the target waypoint
	 */
	public Waypoint getTarget() {
		return target;
	}
	
	/**
	 * Get the registered path
	 * 
	 * @return the Path object
	 */
	public Path getPath() {
		return path;
	}
	
	/**
	 * Set debug output on of off
	 * 
	 * @param on true for on, false for off
	 */
	public void setDebug(boolean on) {
		debug = on;
	}
}
