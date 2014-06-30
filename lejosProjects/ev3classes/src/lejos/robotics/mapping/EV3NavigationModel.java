package lejos.robotics.mapping;

import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Battery;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.remote.nxt.SocketConnector;
import lejos.robotics.RangeScanner;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RotatingRangeScanner;
import lejos.robotics.localization.*;
import lejos.robotics.navigation.*;
import lejos.robotics.objectdetection.*;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.PathFinder;
import lejos.utility.Delay;
import lejos.utility.PilotProps;

/**
 * NXT version of the navigation model.
 * 
 * All local navigation objects, including pilots, navigators, path finders,
 * feature detectors, and range scanners can be added to the model.
 * 
 * Where possible, the model registers itself as an event listener and when the event occurs,
 * updates the model and sends the event and the updates to the PC.
 * 
 * A receiver thread receives events from the PC, updates the local model, and uses the navigation
 * objects to implement the event if it involves robot behaviour.
 * 
 * There are set methods to set various navigation parameters.
 * 
 * @author Lawrie Griffiths
 *
 */
public class EV3NavigationModel extends NavigationModel implements MoveListener, NavigationListener, WaypointListener, FeatureListener {
	protected Navigator navigator; // Only one navigator is allowed
	protected MoveController pilot; // Only one pilot is allowed
	protected PoseProvider pp; // Only one pose provider is allowed
	protected ArrayList<FeatureDetector> detectors = new ArrayList<FeatureDetector>(); // Multiple feature detectors allowed
	protected PathFinder finder; // Only one local path finder is allowed
	protected RangeScanner scanner; // Only one scanner is allowed
	protected NavEventListener listener;
	
	protected float clearance = 10;
	protected float maxDistance = 40;
	protected boolean autoSendPose = true;
	protected boolean sendMoveStart = false, sendMoveStop = true;
	
	private float oldRange = -1;	
	private Thread receiver;
	private boolean  running = true;
	
	/**
	 * Create the model and start the receiver thread
	 */
	public EV3NavigationModel() {
		receiver = new Thread(new Receiver());
		receiver.start();
	}
	
	/**
	 * Log a message
	 * 
	 * @param message the message
	 */
	public void log(String message) {
		System.out.println(message);
	}

	/**
	 * Display an error message to the user
	 * 
	 * @param message the error message
	 */
	public void error(String message) {
		System.out.println(message);
	}
	
	/**
	 * Display a fatal error and shut down the program
	 * 
	 * @param message the error message
	 */
	public void fatal(String message) {
		System.out.println(message);
		Delay.msDelay(5000);
		System.exit(1);
	}
	
	/**
	 * Add a navigator to the model
	 * 
	 * @param navigator the path controller
	 */
	public void addNavigator(Navigator navigator) {
		this.navigator = navigator;
		navigator.addNavigationListener(this);
		addPoseProvider(navigator.getPoseProvider());
		addPilot(navigator.getMoveController());
	}
	
	/**
	 * Add a pilot to the model
	 * 
	 * @param pilot the move controller
	 */
	public void addPilot(MoveController pilot) {
		this.pilot = pilot;
		pilot.addMoveListener(this);
	}
	
	/**
	 * Add a pose provider (which might be MCL) to the model
	 * 
	 * @param pp the pose provider
	 */
	public void addPoseProvider(PoseProvider pp) {
		this.pp = pp;
		if (pp instanceof MCLPoseProvider) {
			mcl = (MCLPoseProvider) pp;
			scanner = mcl.getScanner();
		}
	}
	
	/**
	 * Add a range scanner to the model
	 * 
	 * @param scanner the range scanner
	 */
	public void addRangeScanner(RangeScanner scanner) {
		this.scanner = scanner;
	}
	
	/**
	 * Add a feature detector to the model
	 * 
	 * @param detector the feature detector
	 */
	public void addFeatureDetector(FeatureDetector detector) {
		detectors.add(detector);
		detector.addListener(this);
	}
	
	/**
	 * Set parameters for a random move
	 * 
	 * @param maxDistance the maximum distance of the move
	 * @param clearance the clearance distance around the robot
	 */
	public void setRandomMoveParameters(float maxDistance, float clearance) {
		this.maxDistance = maxDistance;
		this.clearance = clearance;
	}
	
	/**
	 * Set or unset automatic sending of the robot pose to the PC when a move stops
	 * 
	 * @param on true if the pose is to be sent, else false
	 */
	public void setAutoSendPose(boolean on) {
		this.autoSendPose = on;
	}
	
	/**
	 * Sets whether events are sent to the PC when a move stops
	 * 
	 * @param on true iff an event should be sent
	 */
	public void setSendMoveStart(boolean on) {
		sendMoveStart = on;
	}
	
	/**
	 * Sets whether events are sent to the PC when a move starts
	 * 
	 * @param on true iff an event should be sent
	 */
	public void setSendMoveStop(boolean on) {
		sendMoveStop = on;
	}
	
	/**
	 * Shut down the receiver thread
	 */
	public void shutDown() {
		running = false;
	}
	
	public void addListener(NavEventListener listener) {
		this.listener = listener;
	}
	
	/**
	 * The Receiver thread receives events from the PC
	 * 
	 * @author Lawrie Griffiths
	 *
	 */
	class Receiver implements Runnable {
		public void run() {
			//NXTCommConnector connector = Bluetooth.getNXTCommConnector();
			NXTCommConnector connector = new SocketConnector();
			NXTConnection conn = connector.waitForConnection(0, NXTConnection.PACKET);
			dis = conn.openDataInputStream();
			dos = conn.openDataOutputStream();
			if (listener != null) listener.whenConnected();
			if (debug) log("Connected");
			while(running) {
				try {
					// Wait for any outstanding apply moves
					if (mcl != null && mcl.isBusy()) Thread.yield();
					byte event = dis.readByte();
					NavEvent navEvent = NavEvent.values()[event];
					if (debug) log(navEvent.name());
					if (listener != null) listener.eventReceived(navEvent);
					
					synchronized(this) {					
						switch (navEvent) {
						case LOAD_MAP: // Map sent from PC
							if (map == null) map = new LineMap();
							map.loadObject(dis);
							if (mcl != null) mcl.setMap(map);
							break;
						case GOTO: // Update of target and request to go to the new target
							if (target == null) target = new Waypoint(0,0);
							target.loadObject(dis);
							if (navigator != null)navigator.goTo(target);
							break;
						case STOP: // Request to stop the robot
							if (navigator != null) navigator.stop();
							if (pilot != null) pilot.stop();
							break;
						case TRAVEL: // Request to travel a given distance
							float distance = dis.readFloat();
							if (pilot != null) pilot.travel(distance);
							break;
						case ROTATE: // Request to rotate a given angle
							float angle = dis.readFloat();
							if (pilot != null && pilot instanceof RotateMoveController) ((RotateMoveController) pilot).rotate(angle);
							break;
						case ARC: // Request to travel an arc og given radius and angle
							float radius = dis.readFloat();
							angle = dis.readFloat();						
							if (pilot != null && pilot instanceof ArcMoveController) ((ArcMoveController) pilot).arc(radius,angle);
							break;							
						case ROTATE_TO: // Request to rotate to a given angle
							angle = dis.readFloat();
							if (pp != null && pilot != null && pilot instanceof RotateMoveController) ((RotateMoveController) pilot).rotate(angleTo(angle));
							break;
						case GET_POSE: // Request to get the pose and return it to the PC
							if (pp == null) break;
							// Suppress sending moves to PC while taking readings
							boolean saveSendMoveStart = sendMoveStart;
							boolean saveSendMoveStop = sendMoveStop;
							sendMoveStart = false;
							sendMoveStop = false;
							currentPose = pp.getPose();
							sendMoveStart = saveSendMoveStart;
							sendMoveStop = saveSendMoveStop;
							dos.writeByte(NavEvent.SET_POSE.ordinal());
							currentPose.dumpObject(dos);
							break;
						case SET_POSE: // Request to set the current pose of the robot
							if (currentPose == null) currentPose = new Pose(0,0,0);
							currentPose.loadObject(dis);
							if (pp != null) pp.setPose(currentPose);
							break;
						case ADD_WAYPOINT: // Request to add a waypoint
							Waypoint wp = new Waypoint(0,0);
							wp.loadObject(dis);
							if (navigator != null) navigator.addWaypoint(wp);
							break;
						case FIND_CLOSEST: // Request to find particle by co-ordinates and
							               // send its details to the PC
							float x = dis.readFloat();
							float y = dis.readFloat();
							if (particles != null) {
								dos.writeByte(NavEvent.CLOSEST_PARTICLE.ordinal());
								particles.dumpClosest(readings, dos, x, y);
							}
							break;
						case PARTICLE_SET: // Particle set send from PC
							if (particles == null) particles = new MCLParticleSet(map,0,0);
						    particles.loadObject(dis);
						    mcl.setParticles(particles);
						    break;
						case TAKE_READINGS: // Request to take range readings and send them to the PC
							if (scanner != null) {
								readings = scanner.getRangeValues();
								dos.writeByte(NavEvent.RANGE_READINGS.ordinal());
								readings.dumpObject(dos);
							}
							break;
						case GET_READINGS: // Request to send current readings to the PC
							dos.writeByte(NavEvent.RANGE_READINGS.ordinal());
							if (mcl != null) readings = mcl.getRangeReadings();
							readings.dumpObject(dos);
							break;
						case GET_PARTICLES: // Request to send particles to the PC
							if (particles == null) break;
							dos.writeByte(NavEvent.PARTICLE_SET.ordinal());
							particles.dumpObject(dos);
							break;
						case GET_ESTIMATED_POSE: // Request to send estimated pose to the PC
							if (mcl == null) break;
							dos.writeByte(NavEvent.ESTIMATED_POSE.ordinal());
							mcl.dumpObject(dos);
							break;
						case FIND_PATH: // Find a path to the target
							if (target == null) target = new Waypoint(0,0);
							target.loadObject(dis);
							if (finder != null) {
								dos.writeByte(NavEvent.PATH.ordinal());
								try {
									path = finder.findRoute(currentPose, target);
									path.dumpObject(dos);
								} catch (DestinationUnreachableException e) {
									dos.writeInt(0);
								}
							}
							break;
						case FOLLOW_PATH: // Follow a route sent from the PC
							if (path == null) path = new Path();
							path.loadObject(dis);
							if (navigator != null) navigator.followPath(path);
							break;
						case START_NAVIGATOR:
							if (navigator != null) navigator.followPath();
							break;
						case CLEAR_PATH: // Clear the current path in the navigator
							if (navigator != null) navigator.clearPath();
							break;
						case RANDOM_MOVE: // Request to make a random move
							randomMove();
							break;
						case LOCALIZE:
							localize();
							break;
						case EXIT:
							System.exit(0);
						case SOUND:
							Sound.systemSound(false, dis.readInt());
							break;
						case GET_BATTERY:
							dos.writeByte(NavEvent.BATTERY.ordinal());
							dos.writeFloat(Battery.getVoltage());
							dos.flush();
							break;
						case PILOT_PARAMS:
							float wheelDiameter = dis.readFloat();
							float trackWidth = dis.readFloat();
							int leftMotor = dis.readInt();
							int rightMotor = dis.readInt();
							boolean reverse = dis.readBoolean();
							PilotProps props = new PilotProps();
							String[] motors = {"A","B","C"};
							props.setProperty(PilotProps.KEY_WHEELDIAMETER,"" + wheelDiameter);
							props.setProperty(PilotProps.KEY_TRACKWIDTH,"" + trackWidth);
							props.setProperty(PilotProps.KEY_LEFTMOTOR,motors[leftMotor]);
							props.setProperty(PilotProps.KEY_RIGHTMOTOR,motors[rightMotor]);
							props.setProperty(PilotProps.KEY_REVERSE,"" + reverse);
							props.storePersistentValues();
							break;
						case RANGE_FEATURE_DETECTOR_PARAMS:
							int delay = dis.readInt();
							float maxDist = dis.readFloat();
							for (FeatureDetector detector : detectors) {
								if (detector instanceof RangeFeatureDetector) {
									((RangeFeatureDetector) detector).setDelay(delay);
									((RangeFeatureDetector) detector).setMaxDistance(maxDist);
								}
							}
							break;
						case RANGE_SCANNER_PARAMS:
							int gearRatio = dis.readInt();
							int headMotor = dis.readInt();
							RegulatedMotor[] regulatedMotors = {Motor.A, Motor.B, Motor.C};
							if (scanner instanceof RotatingRangeScanner) {
								((RotatingRangeScanner) scanner).setGearRatio(gearRatio);
								((RotatingRangeScanner) scanner).setHeadMotor(regulatedMotors[headMotor]);
							}
							break;
						case TRAVEL_SPEED:
							float travelSpeed = dis.readFloat();
							if (pilot != null) pilot.setTravelSpeed(travelSpeed);
							break;
						case ROTATE_SPEED:
							float rotateSpeed = dis.readFloat();
							if (pilot != null && pilot instanceof RotateMoveController) {
								((RotateMoveController)pilot).setRotateSpeed(rotateSpeed);
							}
							break;
						case RANDOM_MOVE_PARAMS:
							maxDistance = dis.readFloat();
							clearance = dis.readFloat();
							break;
						}
					}
				} catch (IOException ioe) {
					fatal("IOException in receiver:");
				}
			}		
		}
		
		private void randomMove() {
			if (pilot != null && pilot instanceof RotateMoveController) {
			    float angle = (float) Math.random() * 360;
			    float distance = (float) Math.random() * maxDistance;
			    readings = mcl.getReadings();
			    
			    if (angle > 180f) angle -= 360f;

			    float forwardRange;
			    // Get forward range
			    try {
			    	forwardRange = readings.getRange(0f); // Range for angle 0 (forward)
			    } catch (Exception e) {
			    	forwardRange = 0;
			    }
			    
			    // Don't move forward if we are near a wall
			    if (forwardRange < 0
			        || distance + clearance < forwardRange)
			      pilot.travel(distance);
			    
			    ((RotateMoveController) pilot).rotate(angle);
			    if (debug) log("Random moved done");
			}			
		}
		
		private void localize() {
			boolean saveSendMoveStart = sendMoveStart;
			boolean saveSendMoveStop = sendMoveStop;
			sendMoveStart = false;
			sendMoveStop = false;
			while (true) {
				try {
					mcl.getPose();
					dos.writeByte(NavEvent.PARTICLE_SET.ordinal());
					particles.dumpObject(dos);
					readings = mcl.getReadings();
					dos.writeByte(NavEvent.RANGE_READINGS.ordinal());
					readings.dumpObject(dos);
					if (goodEstimate()) {
						// Send the estimate to the PC
						dos.writeByte(NavEvent.ESTIMATED_POSE.ordinal());
						mcl.dumpObject(dos);
						dos.writeByte(NavEvent.LOCATED.ordinal());
						dos.flush();
						break;
					}
					randomMove();
					dos.writeByte(NavEvent.PARTICLE_SET.ordinal());
					particles.dumpObject(dos);
				} catch (IOException ioe) {
					fatal("IOException in localize");
				}
			}
			sendMoveStart = saveSendMoveStart;
			sendMoveStop = saveSendMoveStop;
		}
		
		private   boolean goodEstimate() {
		    //float sx = mcl.getSigmaX();
		    //float sy = mcl.getSigmaY();
		    float xr = mcl.getXRange();
		    float yr = mcl.getYRange();
		    return xr < 50 && yr < 50 ;
		  }
		
		// Calculate the angle for ROTATE_TO
		private int angleTo(float angle) {
			int angleTo = ((int) (angle - pp.getPose().getHeading())) % 360;
			return (angleTo < 180 ? angleTo : angleTo - 360);
		}
	}

	/**
	 * Called when the pilot starts a move
	 */
	public void moveStarted(Move event, MoveProvider mp) {
		if (!sendMoveStart) return;
		try {
			synchronized(receiver) {
				if (debug) log("Sending move started");
				dos.writeByte(NavEvent.MOVE_STARTED.ordinal());
				event.dumpObject(dos);
			}
		} catch (IOException ioe) {
			fatal("IOException in moveStarted");	
		}
	}

	/**
	 * Called when a move stops
	 */
	public void moveStopped(Move event, MoveProvider mp) {
		if (!sendMoveStop) return;
		try {
			synchronized(receiver) {
				if (debug) log("Sending move stopped");
				dos.writeByte(NavEvent.MOVE_STOPPED.ordinal());
				event.dumpObject(dos);
				if (pp != null && autoSendPose) {
					if (debug) log("Sending set pose");
					dos.writeByte(NavEvent.SET_POSE.ordinal());
					pp.getPose().dumpObject(dos);
				}
			}
		} catch (IOException ioe) {
			fatal("IOException in moveStopped");	
		}	
	}

	/**
	 * Called when a feature is detected.
	 * Only range features currently supported
	 */
	public void featureDetected(Feature feature, FeatureDetector detector) {
		if (dos == null) return;
		if (!(feature instanceof RangeFeature)) return;
		float range = ((RangeFeature) feature).getRangeReading().getRange();
		if (range < 0) return;
		if  (pilot == null || !pilot.isMoving()) {
			if (range == oldRange) return;
		}
		oldRange = range;
		try {
			synchronized(receiver) {
				dos.writeByte(NavEvent.FEATURE_DETECTED.ordinal());
				((RangeFeature) feature).dumpObject(dos);
			}
		} catch (IOException ioe) {
			fatal("IOException in featureDetected");	
		}
	}

	/**
	 * Send a waypoint generated on the NXT to the PC
	 */
	public void addWaypoint(Waypoint wp) {
		try {
			synchronized(receiver) {
				dos.writeByte(NavEvent.ADD_WAYPOINT.ordinal());
				// TODO: send waypoint to the PC
				dos.flush();
			}
		} catch (IOException ioe) {
			fatal("IOException in addWaypoint");	
		}	}

	/**
	 * Called when a waypoint has been reached
	 */
	public void atWaypoint(Waypoint waypoint, Pose pose, int sequence) {
		try {
			synchronized(receiver) {
				dos.writeByte(NavEvent.WAYPOINT_REACHED.ordinal());
				waypoint.dumpObject(dos);
			}
		} catch (IOException ioe) {
			fatal("IOException in atWaypoint");	
		}	
	}

	/**
	 * Called when a path has been completed
	 */
	public void pathComplete(Waypoint waypoint, Pose pose, int sequence) {
		try {
			synchronized(receiver) {
				dos.writeByte(NavEvent.PATH_COMPLETE.ordinal());
				dos.flush();
			}
		} catch (IOException ioe) {
			fatal("IOException in pathComplete");	
		}
	}

	/**
	 * Called when a path has been interrupted
	 */
	public void pathInterrupted(Waypoint waypoint, Pose pose, int sequence) {
		try {
			synchronized(receiver) {
				dos.writeByte(NavEvent.PATH_INTERRUPTED.ordinal());
				dos.flush();
			}
		} catch (IOException ioe) {
			fatal("IOException in pathInterrupted");	
		}
	}

	/**
	 * Called when a path finder has finished generating a path
	 */
	public void pathGenerated() {
		try {
			synchronized(receiver) {
				dos.writeByte(NavEvent.PATH_GENERATED.ordinal());
				dos.flush();
			}
		} catch (IOException ioe) {
			fatal("IOException in pathGenerated");	
		}
	}
}
