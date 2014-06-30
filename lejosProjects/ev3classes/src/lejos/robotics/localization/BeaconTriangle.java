package lejos.robotics.localization;

import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Pose;

/**
 * <p>This class represents three beacons in a triangle. Uses triangulation to calculate the position of the robot.</p>
 * 
 * <p>Note: The algorithm in calcPose() is taken from "Generalized Geometric Triangulation Algorithm for 
 * Mobile Robot Absolute Self-Localization" from University of Minho, Portugal, 2003.<br>
 * http://repositorium.sdum.uminho.pt/bitstream/1822/2023/1/BF-003328.pdf</p>
 * 
 * <p>Improved version from 2006 (not implemented by this class):<br>
 * http://repositorium.sdum.uminho.pt/bitstream/1822/6057/1/An Improved Version of the Generalized Geometric Triangulation Algorithm.pdf</p>
 * 
 * @author Andy Shaw and BB
 *
 */
public class BeaconTriangle {
	
	// The three beacons in triangle array:
    Point beacon1;
    Point beacon2;
    Point beacon3;
    
	public BeaconTriangle(Point beacon1, Point beacon2, Point beacon3) {
		this.beacon1 = beacon1;
		this.beacon2 = beacon2;
		this.beacon3 = beacon3;
	}
	
	/**
     * Triangulates the pose of a robot given three angles to the three beacons. The angle to a beacon must correspond to
     * the proper beacon as given in the BeaconTriangle constructor. For example, the angle a1 in the method calcPose() must 
     * be the angle to beacon1, as given in the constructor. 
     * 
     * @param a1 Angle from robot current heading to beacon 1 (in degrees)
     * @param a2 Angle from robot current heading to beacon 2 (in degrees)
     * @param a3 Angle from robot current heading to beacon 3 (in degrees)
     * @return the pose of the robot (x, y, heading)
     */
    public Pose calcPose(double a1, double a2, double a3)
    {
    	// ** NOTE: Line numbers indicate the line from the algorithm in the 2003 paper cited above. **
    	// line 1: if less than 3 beacon angles, abort. Unneeded here due to method signature. Will handle in PoseProvider.
    	
    	double Y12 = a2 - a1; // line 2
        if (a1 > a2) Y12 = 360 + Y12; // line 3
        double Y31 = a1 - a3; // line 4
        if (a3 > a1) Y31 = 360 + Y31; // line 5
        
        // Find distance between beacons:
        double L12 = beacon1.distance(beacon2); // line 6
        double L31 = beacon3.distance(beacon1);  // line 7
        
        // line 8:
        double phi = beacon2.angleTo(beacon1);
        while (phi <= -180) phi = phi + 360;
        while (phi > 180) phi = phi - 360;
        
        // line 9: 
        double head12 = beacon1.angleTo(beacon2);
        double head13 = beacon1.angleTo(beacon3);
        double sigma = head13 - head12;
        sigma = 180 - sigma;
        while (sigma <= -180) sigma = sigma + 360;
        while (sigma > 180) sigma = sigma - 360;
        
        double gamma = sigma - Y31; // line 10
        
        // line 11:
        double tau = atan((sin(Y12)*(L12*sin(Y31) - L31*sin(gamma)))/(L31*sin(Y12)*cos(gamma) - L12*cos(Y12)*sin(Y31)));
        
        if (Y12 < 180 && tau < 0) tau = tau + 180; // line 12
        if (Y12 > 180 && tau > 0) tau = tau - 180; // line 13
        
        // TODO: Add calc from line 14 in 2006 paper here to improve algorithm?
        
        double L1;
        if (Math.abs(sin(Y12)) > Math.abs(Y31)) // line 14
            L1 = (L12*sin(tau + Y12))/sin(Y12);
        else // Line 15:
            L1 = (L31*sin(tau + sigma - Y31))/sin(Y31);
        
        double xR = beacon1.x - L1*cos(phi+tau); // line 16
        double yR = beacon1.y - L1*sin(phi+tau); // line 17
        double aR = phi + tau - a1; // line 18
        
        if (aR <= -180) aR = aR + 360; // line 19
        if (aR > 180) aR = aR - 360; // line 20
        
        return new Pose((float)xR, (float)yR, (float)aR);
    }
    
    private static double sin(double d)
    {
        return Math.sin(Math.toRadians(d));
    }

    private static double cos(double d)
    {
        return Math.cos(Math.toRadians(d));
    }

    private static double atan(double r)
    {
        return Math.toDegrees(Math.atan(r));
    }    
}
