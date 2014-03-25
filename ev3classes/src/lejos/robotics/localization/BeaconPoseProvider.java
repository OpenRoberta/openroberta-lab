package lejos.robotics.localization;

import java.util.ArrayList;

import lejos.hardware.Sound;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.*;

/**
 * <p>A PoseProvider that uses beacon triangulation to pinpoint the pose (x, y, heading) of a robot. 
 * <b>The starting position of the robot must be such that it roughly points in the direction of beacon 2, with beacon 1 to the right
 * of the robot, and beacon 3 to the left.</b> This ensures that when it performs the first scan, the beacons are scanned in order
 * 1, 2, 3. After the first scan, the robot can move about on its own and will be able to scan them in any order.</p>
 * 
 * <p>After it does a scan, you will either hear a beep or a buzz. A beep means the calculation worked. A buzz means something went
 * wrong with the calculation. This could be the beacon ordering is wrong, or it didn't scan three beacons in, or
 * the calculated beacon pose deviated significantly from the estimated odometry pose. In this case, the results are thrown out,
 * the odometry pose is used, and it will try another scan after the next move. If you hear a lot of buzzes in a row, it probably
 * means it will not recover.</p>  
 * 
 * <p>The class uses an OdometryPoseProvider internally for three reasons: 
 * <li>1. To serve as a backup pose provider if the BeaconLocator detects less than 3 beacons, or greater than 3 (meaning 
 * a false reflection occurred).
 * <li>2. It is time consuming to scan beacons after every move. Instead, this will scan about every 5 moves. In between
 * these scans, it uses the OdometryPoseProvider to navigate.
 * <li>3. It is essential to scan the beacons in order. This allows it to determine the identity of each beacon so they are
 * properly fed to the equations. The OdometryPoseProvider keeps track of heading so it knows which beacons were scanned.</p>
 * 
 * <p>The downside of using an OdometryPoseProvider to assist with detecting pose is that the robot needs to be placed
 * with an approximate heading prior to first scan. Also, if the robot is moved at any time (lifted and placed with a different 
 * heading), it probably will no longer be able to identify the beacons and become hopelessly lost. As long as the wheels are
 * always on the ground, this should not happen.</p>
 * 
 * @author BB
 *
 */
public class BeaconPoseProvider implements PoseProvider, MoveListener {
	/*
	 * DEV NOTES: Possible improvements:
	 * 1. TODO: It is subjective and arbitrary for the programmer to say to it, "Do a beacon scan after every X moves." How do 
	 * I know 5 is the right amount? If I use distance, who is to say 200 cm is the right amount to allow odometry before 
	 * doing another scan? It would be nice if it somehow had a model of potential error building up, and did a scan when 
	 * the potential error got too high.
	 * TODO: Alt constructor that allows distance to determine when it does scan. Accepts double val.
	 */
	
	// 2. TODO: Ability to not even tell it the coordinates of the beacons. Instead it starts off with the assumption that the 
	// robot is at 0,0,0. Then it does the particle set with MCL, applies a move, then scan, then weeds out the point cloud. 
	// Keeps going until it has coordinates of three beacons figured out. Optimally this would be included in BeaconTriangle 
	// in most or all code. 
	// Also add methods for saving and loading beacon coordinates to flash memory, then could call this after it has located 
	// them, and load them in before next trial.
	
	// 3. TODO: Could also do a sanity check on beacon pose calculation to see if x, y is within say 100 cm of odometry
	//  pose estimate. If not, then probably a false beacon was scanned and a true beacon was missed (rare) which resulted in it 
	// thinking it successfully scanned in 3 actual beacons. In this case, it should go with odometry. 
	
	private BeaconTriangle bt;
	private BeaconLocator bl;
	private OdometryPoseProvider opp;
	private int moves = 0; // number of moves it has made since getPose() scanned
	private int scanInterval; // perform scan after scanInterval moves have been made
	private double distance = 0; // distance traveled since getPose() scanned
	private boolean hasScanned = false; // whether it has successfully scanned position yet
	
	// TODO: Alt constructor that does not attempt to sort the beacons using the odometry (no MoveProvider).
	// This constructor will be useful for BeaconLocators that can identify the beacon order/id, such as an NXTCamBeaconLocator.
	
	/**
	 * <p>Creates a PoseProvider using beacons. The BeaconLocator must be capable of scanning the angles to 3 beacons.
	 * The BeaconTriangle contains the coordinates of the three beacons. The MoveProvider is simply a Pilot, such as
	 * DifferentialPilot, which is used to keep track of odometry.</p>
	 * 
	 * <p>By default, this class takes a scan every 5 moves of the robot.</p>
	 * 
	 * @param bl a BeaconLocator
	 * @param bt a BeaconTriangle containing the three beacon coordinates
	 * @param mp a MoveProvider (Pilot) such as DifferentialPilot. Used for odometry.
	 */
	public BeaconPoseProvider(BeaconLocator bl, BeaconTriangle bt, MoveProvider mp) {
		this(bl, bt, mp, 5);
	}
	
	/**
	 * <p>Creates a PoseProvider using beacons. The BeaconLocator must be capable of scanning the angles to 3 beacons.
	 * The BeaconTriangle contains the coordinates of the three beacons. The MoveProvider is simply a Pilot, such as
	 * DifferentialPilot, which is used to keep track of odometry.</p>
	 * 
	 * @param bl a BeaconLocator
	 * @param bt a BeaconTriangle containing the three beacon coordinates
	 * @param mp a MoveProvider (Pilot) such as DifferentialPilot. Used for odometry.
	 * @param scanInterval The number of moves to make between each physical scan.
	 */
	public BeaconPoseProvider(BeaconLocator bl, BeaconTriangle bt, MoveProvider mp, int scanInterval) {
		this.bt = bt;
		this.bl = bl;
		opp = new OdometryPoseProvider(mp);
		mp.addMoveListener(this);
		this.scanInterval = scanInterval;
		moves = scanInterval; // make sure it starts by doing a scan
	}
	
	/**
	 * Converts the absolute Cartesian heading into the relative angle values the robot sees when scanning.
	 * 
	 * @param robotBearing The heading the robot is currently at
	 * @param cartesianHeading The absolute angle to a beacon from the robot, in Cartesian angle.
	 * @return relative angle
	 */
	private static double convertToRelative(double robotBearing, double cartesianHeading) {
		
		double relativeAngle = 360 - (robotBearing - cartesianHeading);
		// normalize so value is between 0 to 360
		while(relativeAngle >= 360) relativeAngle -= 360;
		while(relativeAngle < 0) relativeAngle += 360;
		return relativeAngle;
	}
	
	public Pose getPose() {
	    if(moves >= scanInterval) {
	    	ArrayList<Double> beaconAngles = bl.locate();
			//System.out.println("BEFORE SORT:");
			//for(int i=0;i<beaconAngles.size();i++)
			//	System.out.println("a" + i + ": " + beaconAngles.get(i));
			//Button.ESCAPE.waitForPressAndRelease();
			
	    	// Use OdometryPoseProvider (opp) to sort the beacons into the expected scan sequence.
			if (beaconAngles.size() == 3) {
				// Now try to sort the identifications of the beacons out. This method keeps track of Pose
				// using OdometryPoseProvider, which helps it id beacons.
				if(hasScanned) { // Can only do this after one successful scan. Otherwise opp.setPose() has not been set to proper coordinates.
					Pose robot = opp.getPose();
					double a1 = robot.angleTo(bt.beacon1);
					double a2 = robot.angleTo(bt.beacon2);
					double a3 = robot.angleTo(bt.beacon3);
					double h = robot.getHeading();
					
					// TODO: I think it was useless to convert these first because only angles are used!
					a1 = convertToRelative(h, a1);
					a2 = convertToRelative(h, a2);
					a3 = convertToRelative(h, a3);
					
					ArrayList<Double> odometryAngles = new ArrayList<Double>(3);
					odometryAngles.add(a1);
					odometryAngles.add(a2);
					odometryAngles.add(a3);
					
					//System.out.println("ODOMETRY:");
					//System.out.println("A1 " + a1);
					//System.out.println("A2 " + a2);
					//System.out.println("A3 " + a3);
					//Button.ESCAPE.waitForPressAndRelease();
					
					boolean success = sortBeacons(beaconAngles, odometryAngles);
					
					System.out.println("AFTER SORT:");
					for(int i=0;i<beaconAngles.size();i++)
						System.out.println("a" + i + ": " + beaconAngles.get(i));
					//Button.ESCAPE.waitForPressAndRelease();
				}
				
	            Pose p = bt.calcPose(beaconAngles.get(0), beaconAngles.get(1), beaconAngles.get(2));
	            System.out.println("POSE CALC");
				System.out.println(p.getX() + ", " + p.getY());
				System.out.println("H: " + p.getHeading());
				//Button.ESCAPE.waitForPressAndRelease();
	            	            
	            opp.setPose(p); // update OdometryPoseProvider with latest pose
	            moves = 0;
	            distance = 0;
	            hasScanned = true;
	            System.out.println("set to " + hasScanned);
	            return p;
	        } else Sound.buzz();
	    }
	    // If beacons <> 3 then use OdometryPP reading:
        return opp.getPose();
	}

	/**
	 * <p>This sorts the beacons into the proper order so that beacons.get(0) returns beacon1, and so on.
	 * This method assumes beacons will always contain the beacon angles that were scanned in counter-clockwise.</p>
	 * 
	 * Strategy: Pick widest theoretical angle between beacons. There will always be one at least 120 degs.
	 * Then determine which beacon is clockwise of this angle.
	 * 
	 * @param beacons an array of three angles.
	 * @param a1
	 * @param a2
	 * @param a3
	 */
	private static boolean sortBeacons(ArrayList<Double> beacons, ArrayList<Double> angles) {
		// Could take a1, a2, a3 instead. Then do sanity check when done to make sure the difference between a1 and 1, a2 and 2,
		// a3 and 3 are all about the same, such as all about 20 degrees from one another--or at least more similar than other 
		// combos. My second sanity check below does this.
		
		// 1. Find largest angle between three angles 
		// Find angles between between different beacon angles and robot
		ArrayList<Double> midAngles = new ArrayList<Double>();
		for(int i=0;i<angles.size();i++) {
			int i2 = i+1;
			if(i2>=angles.size()) i2 = 0;
			double midAngle = betweenAngles(angles.get(i2), angles.get(i));
			//System.out.println("A" + i2 + "" + i + ": " + midAngle);
			midAngles.add(midAngle);
		}
		
		int largestIndex = getLargestIndex(midAngles);
		//System.out.println("Largest index " + largestIndex);
		double largestAngle = midAngles.get(largestIndex);
		//System.out.println("Largest angle " + largestAngle);
		
		// Figure out which 
		int aAngleCounterClockwiseIndex = largestIndex + 1;
		if(aAngleCounterClockwiseIndex >= 3) aAngleCounterClockwiseIndex = 0;
		//System.out.println("aAngleCounterClockwiseIndex " + aAngleCounterClockwiseIndex);
		
		// Now you want to calculate the theoretical middle angle between largest angle. 
		double middleAngle = angles.get(largestIndex) + (largestAngle/2);
		if(middleAngle >= 360) middleAngle -= 360;
		//System.out.println("Middle angle " + middleAngle);
		
		// 2. Find largest angle between three angles in beacons
		//double b12 = betweenAngles(beacons.get(1), beacons.get(0));
		//double b23 = betweenAngles(beacons.get(2), beacons.get(1));
		//double b31 = betweenAngles(beacons.get(0), beacons.get(2));
		//System.out.println("B12 " + b12);
		//System.out.println("B23 " + b23);
		//System.out.println("B31 " + b31);
		
		//ArrayList<Double> bAngles = new ArrayList<Double>();
		//bAngles.add(b12);
		//bAngles.add(b23);
		//bAngles.add(b31);
		//int bLargestIndex = getLargestIndex(bAngles);
		//System.out.println("Beacons Largest index " + bLargestIndex);
		//System.out.println("Beacons Largest angle " + bAngles.get(bLargestIndex));
		
		// Sanity check? If largest angle equals another, then this is a bad situation. Abort and use Odometry.
		// What if there are two similar largest angles? Could variability screw this up? If there are two similar ones,
		// then what it could do is start to look at which is closest to the expected angle. Too dangerous.
		
		// Another sanity check? Check if order of both sets of angles goes greatest, least, median or whatever. However,
		// this could be dangerous because if two are very similar, a bit of real-world variability could cause it to throw 
		// out good results. Too dangerous.
		
		// OK, so now it should pick the next beacon angle counter-clockwise of middleAngle. This will then be designated
		// as the same angle index as the theoretical one counter-clockwise.
		int beaconAngleCounterClockwiseIndex = 0; // index of beacon to right of middleAngle
		double smallestDiff = Double.POSITIVE_INFINITY;
		
		for(int i=0;i<3;i++) {
			double indexedAngle = beacons.get(i);
			if(indexedAngle < middleAngle) indexedAngle += 360; // since measuring angle counter clockwise, smaller angles actually add 360 
				
			double diff = indexedAngle - middleAngle;
			if(diff < smallestDiff) {
				smallestDiff = diff;
				beaconAngleCounterClockwiseIndex = i;
			}
		}
		
		//System.out.println("Beacon index counterclockwise of middle angle " + beaconAngleCounterClockwiseIndex);
		//System.out.println("Beacon angle counterclockwise of middle angle " + beacons.get(beaconAngleCounterClockwiseIndex));
		
		// 3. Now re-sort so that beacons in same order as a1, a2, a3 based on largest angle.
		double [] tempAngles = new double[3];
		
		int  bIndex = beaconAngleCounterClockwiseIndex;
		int anglesOrdered = 0;
		for(int aIndex = aAngleCounterClockwiseIndex;anglesOrdered<3;aIndex++, bIndex++) {
			if(aIndex >=3) aIndex = 0; // loop values to start
			if(bIndex >=3) bIndex = 0; // loop values to start
			
			// NOW WHAT?  
			
			//System.out.println("aIndex " + aIndex);
			//System.out.println("bIndex " + bIndex);
			//System.out.println("beacons.size() " + beacons.size());
			//System.out.println("angles.size() " + angles.size());
			//System.out.println("tempAngles.length " + tempAngles.length);
			// If a31, then 1. If a23 then 3. If a12 then 2. But assigning the actual scanned beacon angle that is after the halfway mark.
			tempAngles[aIndex] = beacons.get(bIndex);
			anglesOrdered++;
		}
		
		beacons.clear();
		for(int i=0;i<tempAngles.length;i++)
			beacons.add(tempAngles[i]);
		
		return true; // true means it thinks the calc worked. If not, abort and use odometry.  
	}

	public static int getLargestIndex(ArrayList<Double> angles) {
		double largest = 0;
		int largestIndex = 0;
		for(int i=0;i<3;i++)
			if(angles.get(i) > largest) {
				largest = angles.get(i);
				largestIndex = i;
			}
		return largestIndex;
	}
	
	/**
	 * Subtracts two angles in a clockwise direction.
	 * @param greater The angle counter-clockwise of the other angle
	 * @param lesser The other angle, clockwise from the other one
	 * @return
	 */
	private static double betweenAngles(double greater, double lesser) {
		if(greater < lesser) greater += 360;
		double ab = greater - lesser;
		
		return ab;
	}
	
	public void setPose(Pose aPose) {
		// TODO When this is called, it sets the current robot location as 0,0 or whatever they set it as.
		// All subsequent calculations must add this offset of both coordinates and heading.
		// Needs to know current pose to calculate this. 
	}

	public void moveStarted(Move move, MoveProvider mp) {
		
	}

	public void moveStopped(Move move, MoveProvider mp) {
		// Add distance and/or rotation moved since last reset
		distance += move.getDistanceTraveled();
		moves++;
	}
}
