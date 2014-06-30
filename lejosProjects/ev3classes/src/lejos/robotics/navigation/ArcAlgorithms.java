package lejos.robotics.navigation; // TODO: Move to lejos.geom?

import java.awt.geom.*;

import lejos.robotics.geometry.*;

/**
 * The static methods in this class can be used to to calculate theoretical routes and for displaying graphical representations of
 * the path of a robot. The methods getAvailablePaths() and getBestPath() are useful for this.
 *  
 * @author bb
 * @version November 2009
 *
 */
public class ArcAlgorithms { // TODO Change access from public to package level when done testing?
	
	private ArcAlgorithms(){} // Make sure no one instantiates this as an object.
	
	// TODO: Terminology: Waypoints? Paths? Routes?  
	
	/**
	 * Find the shortest path for a steering vehicle between two points. The start Pose and destination Pose include the
	 * heading, so the robot will execute an arc, then travel a straight line, followed by another arc to obtain the final
	 * heading. This algorithm searches through 16 combinations of paths in order to find the shortest path. Even though the
	 * radii supplied are positive (left turn), this algorithm searches through negative radii too (right turns) 
	 * 
	 * @param start The starting Pose
	 * @param turnRadius1 The turning radius for the first arc.
	 * @param destination The destination Pose
	 * @param turnRadius2 The turning radius for the final arc
	 * @return the sequence of moves
	 */
	public static Move [] getBestPath(Pose start, float turnRadius1, Pose destination, float turnRadius2) {
		// Get all paths TODO: This can probably be streamlined with arrays. Sort out Path (Move) container first.
		Move [][] paths1 = getAvailablePaths(start, turnRadius1, destination, turnRadius2);
		Move [][] paths2 = getAvailablePaths(start, turnRadius1, destination, -turnRadius2);
		Move [][] paths3 = getAvailablePaths(start, -turnRadius1, destination, turnRadius2);
		Move [][] paths4 = getAvailablePaths(start, -turnRadius1, destination, -turnRadius2);
		
		final int PATHS_PER_ARRAY = 4;
		final int ALL_PATHS = PATHS_PER_ARRAY * 4;
		Move [][] paths = new Move[ALL_PATHS][3];
		// TODO: This can probably be steamlined with arrays. Sort out Path (Move) container first.
		System.arraycopy(paths1, 0, paths, 0, PATHS_PER_ARRAY);
		System.arraycopy(paths2, 0, paths, 4, PATHS_PER_ARRAY);
		System.arraycopy(paths3, 0, paths, 8, PATHS_PER_ARRAY);
		System.arraycopy(paths4, 0, paths, 12, PATHS_PER_ARRAY);
		
		return getBestPath(paths);
	}
	
	/**
	 * This method gets all the available paths given a start Pose and destination Post. Only four possible paths will
	 * be generated. The two turn radii can be positive (left hand turn) or negative (right hand turn). Each path consists
	 * of an arc, then a straight line, followed by another arc to obtain the final heading. 
	 * 
	 * @param start The starting Pose
	 * @param turnRadius1 The turning radius for the first arc
	 * @param destination The destination Pose
	 * @param turnRadius2 The turning radius for the final arc
	 * @return the sequence of moves
	 */
	public static Move [][] getAvailablePaths(Pose start, float turnRadius1, Pose destination, float turnRadius2) {
		
		// TODO: These constants can perhaps be calculated based on existing parameters?
		final int PATHS = 4; // Currently doesn't calculate Pilot.backward() movement along P2 to P3. Would make this 8.
		final int MOVES_PER_PATH = 3;
				
		Move [] [] paths = new Move [PATHS] [MOVES_PER_PATH];
				
		// Draw start circle:
		Point startCircle = ArcAlgorithms.findCircleCenter(start.getLocation(), turnRadius1, start.getHeading());
		
		// Draw target circle:
		Point targetCircle = ArcAlgorithms.findCircleCenter(destination.getLocation(), turnRadius2, destination.getHeading());
		
		// Calculate "inner circle" (sometimes it is outer circle)
		float innerRadius = turnRadius1 - turnRadius2;
		
		float newHeading;
		Point p2inner;
				
		// TODO: Special case if radius = 0 for both? 
		
		// Special case if radius is the same for both turns. 
		if(innerRadius == 0) {
			newHeading = ArcAlgorithms.getHeading(startCircle, targetCircle); 
			p2inner = startCircle; 
		} else { // END OF EQUAL RADII CODE
			// Find the p2 equivalent on the inner circle 
			p2inner = ArcAlgorithms.findP2(startCircle, targetCircle, innerRadius);
			
			// To find arcLength, need to make new p1 that sits on inner circle. NOTE: turnRadius1 was a bug. Now turnRadius2. 
			Point p1inner = ArcAlgorithms.findPointOnHeading(start.getLocation(), start.getHeading() + 90, turnRadius2);
			
			// Find new heading:
			float sArc = ArcAlgorithms.getArc(p1inner, p2inner, innerRadius, start.getHeading(), true);
			newHeading = ArcAlgorithms.getHeading(start.getHeading(), sArc);
			
		} // END OF UNEQUAL RADII CODE
		
		// Find points p2 and p3:
		Point p2 = ArcAlgorithms.findPointOnHeading(p2inner, newHeading - 90, turnRadius2); // TODO: turnRadius2? Not turnRadius1?
		Point p3 = ArcAlgorithms.findPointOnHeading(targetCircle, newHeading - 90, turnRadius2);// TODO: turnRadius2? Not turnRadius1?
		
		// Find distance to drive straight segment:
		float p2p3 = ArcAlgorithms.distBetweenPoints(p2, p3);
		
		// Find arc lengths (forward and backward) to drive on startCircle:
		float startArcF = ArcAlgorithms.getArc(start.getLocation(), p2, turnRadius1, start.getHeading(), true);
		float startArcB = ArcAlgorithms.getArcBackward(startArcF);
						
		// Find arc lengths (forward and backward) to drive on targetCircle: TODO: Swap p3 and destination & remove -ve?
		float targetArcF = -ArcAlgorithms.getArc(destination.getLocation(), p3, turnRadius2, destination.getHeading(), false);
		float targetArcB = ArcAlgorithms.getArcBackward(targetArcF); // Prefer this for speed. It is exact.
		
		// TODO: This can probably be steamlined with arrays. Sort out Path (Move) container first.
		paths[0][0] = new Move(false, startArcF, turnRadius1);
		paths[0][1] = new Move(p2p3, 0, false);
		paths[0][2] = new Move(false, targetArcF, turnRadius2);
		
		paths[1][0] = new Move(false, startArcF, turnRadius1);
		paths[1][1] = new Move(p2p3, 0, false);
		paths[1][2] = new Move(false, targetArcB, turnRadius2);
		
		paths[2][0] = new Move(false, startArcB, turnRadius1);
		paths[2][1] = new Move(p2p3, 0, false);
		paths[2][2] = new Move(false, targetArcF, turnRadius2);
		
		paths[3][0] = new Move(false, startArcB, turnRadius1);
		paths[3][1] = new Move(p2p3, 0, false);
		paths[3][2] = new Move(false, targetArcB, turnRadius2);
		
		return paths;
	}
	
	/**
	 * This method calculates the moves needed to drive from a starting Pose to a final Point. The final heading 
	 * is indeterminate at the destination. To arrive at the destination, the robot drives an arc, then a straight line
	 * to the destination point.
	 * If the destination point is within the turning circle, the moves generated by that circle
	 * will all have Float.NaN for the distanceTraveled and arcAngle values.
	 * 
	 * @param start The starting Pose
	 * @param destination The destination Point
	 * @param turnRadius The turn radius
	 * @return A list of 4 paths.
	 *  
	 */
	public static Move [][] getAvailablePaths(Pose start, Point destination, float turnRadius) {
		
		// TODO: These variables can perhaps be calculated based on existing parameters?
		final int PATHS = 4; // Currently doesn't calculate Pilot.backward() movement along P2 to P3
		final int MOVES_PER_PATH = 2;
				
		Move [] [] paths = new Move [PATHS] [MOVES_PER_PATH];
		
		// TODO: Use Point instead of Point2D.Float? 
		Point p1 = new Point(start.getX(), start.getY());
		// the Point destination below should really return float, not double. Not sure why Laurie returns a double.
		Point p3 = new Point((float)destination.getX(), (float)destination.getY());
		
		for(int i = 0;i<PATHS;i++) { 
			float radius = turnRadius;
			// TODO: This method does too much. It should only get the paths with the sign of the radius parameter.
			// getBestPath needs to call it twice in order to get the negative version, then amalgamate the arrays.
			
			if(i>=PATHS/2) radius = -turnRadius; // Do calculations for +ve radius then -ve radius
			
			// Find two arc angles:
			Point c = ArcAlgorithms.findCircleCenter(p1, radius, start.getHeading());
			Point p2 = ArcAlgorithms.findP2(c, p3, radius);
			float arcLengthForward = ArcAlgorithms.getArc(p1, p2, radius, start.getHeading(), true);
			//double arcLengthBackward = ArcAlgorithms.getArc(p1, p2, radius, start.getHeading(), false);
			float arcLengthBackward = ArcAlgorithms.getArcBackward(arcLengthForward); // faster
			
			// Find straight line:
			double z = ArcAlgorithms.distBetweenPoints(c, p3);
			double p2p3 = ArcAlgorithms.distP2toP3(radius, z);
			
			paths[i][0] = new Move(false, arcLengthForward, radius);
			paths[i][1] = new Move((float)p2p3, 0, false);
			i++;
			paths[i][0] = new Move(false, arcLengthBackward, radius);
			paths[i][1] = new Move((float)p2p3, 0, false);
		}

		return paths;
	}
	
	/**
	 * This method generates the shortest path from a starting Pose to a final Point. The final heading 
	 * is indeterminate at the destination. To arrive at the destination, the robot drives an arc, then a straight line
	 * to the destination point.
	 * 
	 * @param start The starting Pose
	 * @param destination The destination Point
	 * @param radius The turn radius
	 * @return The shortest available path (given the parameters).
	 */
	public static Move [] getBestPath(Pose start, Point destination, float radius) {
		// Get all paths
		Move [][] paths = getAvailablePaths(start, destination, radius);
		return getBestPath(paths);
	}

	/**
	 * This helper method accepts a number of paths (an array of Move) and selects the shortest path.
	 * @param paths Any number of paths.
	 * @return The shortest path.
	 */
	public static Move [] getBestPath(Move [][] paths) {
		/* TODO: Note, the space-search algorithm that finds the shortest path should only drive the straight
		 * segment in reverse IF the distance is 1/2 the circumference of the minimum circle. The reasoning is that
		 * the vehicle will drive a maximum distance of 1/2 circumference for the arc turn, so the same distance in
		 * reverse for the straight segment is also acceptable. Later, when the circles at the target location are
		 * factored in, this will also have some sort of effect on the final solution.
		 */
		
		Move [] bestPath = null;
		
		// Now see which one has shortest travel distance:
		float minDistance = Float.POSITIVE_INFINITY;
		for(int i=0;i<paths.length;i++) {
			float dist = 0;
			for(int j=0;j<paths[i].length;j++) {
				dist += Math.abs(paths[i][j].getDistanceTraveled());
			}
			if(dist < minDistance) {
				minDistance = dist;
				bestPath = paths[i];
			}
		}
		
		return bestPath;
	}
	
	/**
	 * Given a starting point and heading, this method will calculate another Point that is distance away from the first
	 * point.
	 *  
	 * @param original The starting point
	 * @param heading The heading of the point
	 * @param distance The distance away from this point to calculate a new point
	 * @return A new point "distance" from the first point along the same heading. 
	 */
	public static Point findPointOnHeading(Point original, float heading, float distance) {
	
		// TODO: Do calculation to set theta angle according to "quadrant" of destination point? Probably not needed.
		double head = heading - 180;
		double pax = original.x - distance * Math.cos(Math.toRadians(head));
		double pay = original.y - distance * Math.sin(Math.toRadians(head));
		Point Pa = new Point((float)pax, (float)pay);
		return Pa;
	}
	
	/**
	 * This method calculates the angle generated by three points. The point pa is the center point in the angle, while
	 * p1 and p2 are outside the angle.
	 * 
	 * @param p1 An outer point, connects only with pa.
	 * @param p2 An outer point, connects only with pa.
	 * @param pa The central point, connects with both p1 and p2.
	 * @return The angle in degrees (between 0 and 180)
	 */
	// TODO: Reorder triangle order so middle variable is apex of angle.
	public static float getTriangleAngle(Point p1, Point p2, Point pa) {
		// Now calculate lengths of all lines on our P1-Pa-P2 triangle
		double a = distBetweenPoints(p1, p2);
		
		double b = distBetweenPoints(p1, pa);
		
		double c = distBetweenPoints(pa, p2);
		
		double angle = Math.pow(c, 2) - Math.pow(a, 2) - Math.pow(b, 2);
		
		angle = angle / (-2 * a * b);
		
		// TODO: At this point if angle is outside -1 to +1 then Math.acos() causes NaN. I think artifact decimal numbers
		// were causing the number to be >1 when it should not have been. This is a kludge to fix that:
		if(angle < -1 & angle > -1.1) angle = -1;
		if(angle > 1 & angle < 1.1) angle = 1;
		
		angle = Math.acos(angle);
		return (float)Math.toDegrees(angle);
	}
	
	/**
	 * Given the former heading and the change in heading, this method will calculate a new heading.
	 * @param oldHeading The old heading (original heading of robot) in degrees
	 * @param changeInHeading The change in angle, in degrees.
	 * @return The new heading, in degrees. (0 to 360)
	 */
	public static float getHeading(float oldHeading, float changeInHeading) {
		float heading = oldHeading + changeInHeading;
		if(heading >=360) heading -= 360;
		if(heading <0) heading += 360;
		return heading;
	}
	
	/**
	 * If p1 is the starting point of the robot, and p2 is the point at which the robot is pointing
	 * directly at the target point, this method calculates the angle to travel along the circle (an arc) 
	 * to get from p1 to p2.
	 * 
	 * @param p1 Start position
	 * @param p2 Take-off point on the circle
	 * @param radius Radius of circle AKA the turnRadius 
	 * @param heading Start heading vehicle is pointed, in degrees.
	 * @param forward Will the vehicle be moving forward along the circle arc?
	 * @return Length of travel along circle, in degrees
	 * 
	 */
	public static float getArc(Point p1, Point p2, float radius, float heading, boolean forward) {
		// I accidently got the radius sign confused. +ve radius is supposed to have circle center to left of robot:
		radius = -radius; // Kludge. Should really correct my equations.
		
		Point pa = ArcAlgorithms.findPointOnHeading(p1, heading, radius*2);
		float arcLength = ArcAlgorithms.getTriangleAngle(p1, p2, pa);
		arcLength *= -2;
		
		// TODO: Bit of a hack here. Math should be able to do it without conditional if-branches
		if(radius < 0) arcLength = 360 + arcLength;
		if(!forward) {
			// TODO: This 'if' could really be amalgamated with the if(radius < 0) branch 
			if(arcLength < 0)
				arcLength = arcLength + 360;
			else
				arcLength = arcLength - 360;
		}
		return arcLength;
	}
	
	/**
	 * Quick calculation of reverse arc instead of going through getArcLength() math again.
	 * @param forwardArc
	 * @return the backward arc
	 */
	public static float getArcBackward(float forwardArc) {
		float backwardArc = 0;
		
		if(forwardArc < 0)
			backwardArc = 360 + forwardArc;
		else if(forwardArc > 0)
			backwardArc = -360 + forwardArc;
		
		return backwardArc;
	}
	
		
	/**
	 * 
	 * If p1 is the starting point of the robot, and p2 is the point at which the robot is pointing
	 * directly at the target point, this method calculates the angle to travel along the circle (an arc) 
	 * to get from p1 to p2.
	 * 
	 * @param p1 Start position
	 * @param p2 Take-off point on circle
	 * @param radius Radius of circle
	 * @return Length of travel along circle, in degrees
	 * @deprecated This method is no longer used because it can't calculate >180 angles. Delete any time.
	 */
    @Deprecated
	public static double getArcOld(Point p1, Point p2, double radius) {
		// I accidently got the radius sign confused. +ve radius is supposed to have circle center to left of robot:
		radius = -radius; // Kludge. Should really correct my equations.
		
		// This equation can't generate angles >180 (the major angle), so if angle is actually >180 it will
		// generate the minor angle rather than the major angle.
		double d = distBetweenPoints(p1, p2);
		
		// The - in front of 2 below is a temp hack. Won't work for reverse movements by robot. 
		double angle = -2 * Math.asin(d / (2 * radius));
		return Math.toDegrees(angle);
	}
	
	/**
	 * Assume p2 is the "takeoff" point and p3 is the final target point, this private helper method is used to
	 * calculate the distance between p2 to p3, given only the radius and an intermediate value z. The methods findP2()
	 * and getAvailablePaths() both use this method.
	 * 
	 * @param radius The turn radius of the vehicle.
	 * @param z An intermediate value.
	 * @return
	 */
	private static double distP2toP3(double radius, double z) {
		double x = Math.pow(z, 2) - Math.pow(radius, 2);
		return Math.sqrt(x);
	}
	
	/**
	 * Calculates the distance between any two points.
	 * 
	 * @param a The first point
	 * @param b The second point
	 * @return The distance between points a and b.
	 */
	public static float distBetweenPoints(Point a, Point b) {
		// TODO: Should delete this method, just use Point2D.
		return (float) Point2D.distance(a.x, a.y, b.x, b.y);
		//double z = Math.pow((b.x - a.x), 2) + Math.pow((b.y - a.y), 2);
		//return (float)Math.sqrt(z);
	}
	
	/**
	 * Calculates the heading designated by two points. Heading always travels from the "from" point
	 * to the "to" point.
	 * 
	 * @param from Starting point.
	 * @param to Final point.
	 * @return Heading in degrees (0-360) 
	 */
	public static float getHeading(Point from, Point to) {
		Point xAxis = new Point(from.x + 30, from.y);
		float heading = ArcAlgorithms.getTriangleAngle(from, to, xAxis);
		if(to.y < from.y) heading = 360 - heading;
		return heading;
	}
	
	/**
	 * This method finds P2 if the vehicle is traveling to a point (with no heading).
	 * 
	 * @param c The center point of the turning circle.
	 * @param p3 The final target point
	 * @param radius The turn radius.
	 * @return P2, the takeoff point on the circle.
	 */
	public static Point findP2(Point c, Point p3, float radius) {
		// I accidently got the radius sign confused. +ve radius is supposed to have circle center to left of robot:
		radius = -radius; // Kludge. Should really correct my equations.
				
		double z = distBetweenPoints(c, p3);
		double a1 = p3.x - c.x;
		double o = p3.y - c.y;
		double angle = Math.atan2(o , a1) - Math.asin(radius / z);
		
		double x = distP2toP3(radius, z);
		double a2 = x * Math.cos(angle);
		double o1 = x * Math.sin(angle);
		
		double x2 = p3.x - a2;
		double y2 = p3.y - o1;
		
		return new Point((float)x2, (float)y2);
	}
	
	/**
	 * Calculates the center of a circle that rests on the tangent of the vehicle's starting heading. 
	 * It can calculate a circle to the right OR left of the heading tangent.
	 * To calculate a circle on the left side of the heading tangent, feed it a negative radius.
	 * 
	 * @param p1 the starting point of the vehicle.
	 * @param radius Turning radius of vehicle. A negative value produces a circle to the right of the heading.
	 * @param heading Start heading of vehicle, in degrees (not radians).
	 * @return The center point of the circle.
	 */
	public static Point findCircleCenter(Point p1, float radius, float heading) {
		// I accidently got the radius sign confused. +ve radius is supposed to have circle center to left of robot:
		radius = -radius; // TODO: Kludge. Should really correct my equations.
				
		double t = heading - 90; // TODO: Need to check if > 360 or < 0? Think cos/sin handle it.
		
		double a = p1.x + radius * Math.cos(Math.toRadians(t));
		double b = p1.y + radius * Math.sin(Math.toRadians(t));
		
		return new Point((float)a,(float)b);
	}
}
