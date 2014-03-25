package lejos.robotics.navigation;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

/*
 * DEV NOTES: Should add an optional method to auto-calibrate the steering. With low power, rotate steering all 
 * the way to the left and record tacho limit, then all the way to the right and record tacho limit. Assumes
 * symmetrical steering values and estimates straight tacho value as average of two tacho counts. Make alternate
 * constructor that doesn't use leftTacho and rightTacho values but calls auto calibrate method.
 */

// TODO: Wikipedia article on Homotopy principle has some ideas about calculating position and steering of vehicle:
// http://en.wikipedia.org/wiki/Homotopy_principle#A_car_in_the_plane
// EQUATION: x sin A = y cos A 
// where angle A describes orientation of the car  

/**
 * <p>Vehicles that are controlled by the SteeringPilot class use a similar steering mechanism to a car, in which the 
 * front wheels pivot from side to side to control direction.</p>
 * 
 * <p>If you issue a command for travel(1000) and then issue a command travel(-500) before
 * it completes the travel(1000) movement, it will call stop, properly inform movement listeners that 
 * the forward movement was halted, and then start moving backward 500 units. This makes movements from the SteeringPilot
 * leak-proof and incorruptible.</p> 
 *
 * <p>Note: A DifferentialPilot robot can simulate a SteeringPilot robot by calling {@link DifferentialPilot#setMinRadius(double)}
 * and setting the value to something greater than zero (example: 15 cm).</p>
 * 
 * @see lejos.robotics.navigation.DifferentialPilot#setMinRadius(double)
 * @author BB
 *
 */
public class SteeringPilot implements ArcMoveController, RegulatedMotorListener {

	private lejos.robotics.RegulatedMotor driveMotor;
	private lejos.robotics.RegulatedMotor steeringMotor;
	private double minTurnRadius;
	private double driveWheelDiameter;
	
	private boolean isMoving;
	private int oldTacho;
	
	/**
	 * Rotate motor to this tacho value in order to achieve minimum left hand turn. 
	 */
	private int minLeft;
	
	/**
	 * Rotate motor to this tacho value in order to achieve minimum right hand turn. 
	 */
	private int minRight;
	
	/**
	 * Indicates the type of movement (arc, travel) that vehicle is engaged in.
	 */
	private Move moveEvent = null;
	
	// TODO: Possibly will need to allow multiple listeners
	private MoveListener listener = null;
	
	/**
	 * <p>Creates an instance of the SteeringPilot. The drive wheel measurements are written on the side of the LEGO tire, such
	 * as 56 x 26 (= 56 mm or 5.6 centimeters).</p>
	 * 
	 * The accuracy of this class is dependent on physical factors:
	 * <li> the surface the vehicle is driving on (hard smooth surfaces are much better than carpet)
	 * <li> the accuracy of the steering vehicle (backlash in the steering mechanism will cause turn-angle accuracy problems)
	 * <li> the ability of the steering robot to drive straight (if you see your robot trying to drive straight and it is driving
	 * a curve instead, accuracy will be thrown off significantly) 
	 * <li> When using SteeringPilot with ArcPoseController, the starting position of the robot is also important. Is it truly
	 * lined up with the x axis? Are your destination targets on the floor accurately measured? 
	 * 
	 * <p>Note: If your drive motor is geared for faster movement, you must multiply the wheel size by the 
	 * gear ratio. e.g. If gear ratio is 3:1, multiply wheel diameter by 3.  If your drive motor is geared for
	 * slower movement, divide wheel size by gear ratio.</p> 
	 * 	 * 
	 * @param driveWheelDiameter The diameter of the wheel(s) used to propel the vehicle.
	 * @param driveMotor The motor used to propel the vehicle, such as Motor.B
	 * @param steeringMotor The motor used to steer the steering wheels, such as Motor.C
	 * @param minTurnRadius The smallest turning radius the vehicle can turn. e.g. 41 centimeters
	 * @param leftTurnTacho The tachometer the steering motor must turn to in order to turn left with the minimum turn radius.
	 * @param rightTurnTacho The tachometer the steering motor must turn to in order to turn right with the minimum turn radius.
	 */
	public SteeringPilot(double driveWheelDiameter, lejos.robotics.RegulatedMotor driveMotor, 
			lejos.robotics.RegulatedMotor steeringMotor, double minTurnRadius, int leftTurnTacho, int rightTurnTacho) {
		this.driveMotor = driveMotor;
		this.steeringMotor = steeringMotor;
		this.driveMotor.addListener(this);
		this.driveWheelDiameter = driveWheelDiameter;
		this.minTurnRadius = minTurnRadius;
		this.minLeft = leftTurnTacho;
		this.minRight = rightTurnTacho;
		
		this.isMoving = false;	
	}
	
	/**
	 * <p>This method calibrates the steering mechanism by turning the wheels all the way to the right and
	 * left until they encounter resistance and recording the tachometer values. These values determine the
	 * outer bounds of steering. The center steering value is the average of the two. NOTE: This method only
	 * works with steering robots that are symmetrical (same maximum steering threshold left and right). </p> 
	 *   
	 *   TODO: Should be able to get steering parity right from this class! No need to fish for boolean.
	 * <p>When you run the method, if you notice the wheels turn left first, then right, it means you need
	 * to set the reverse parameter to true for proper calibration. NOTE: The next time you run the calibrate
	 * method it will still turn left first, but...  </p>
	 * @param reverse Reverses the direction of the steering motor.
	 */
	public void calibrateSteering() {
		
		// TODO: Not really necessary to check for stall. Could just rotate for about 2 seconds and take a tacho reading. 
		// This would help with RemoteMotor and remote SteeringPilot, which doesn't implement isStalled().
		
		steeringMotor.setSpeed(100);
		steeringMotor.setStallThreshold(10, 100);
				
		steeringMotor.forward();
		while(!steeringMotor.isStalled()) Thread.yield();
		int r = steeringMotor.getTachoCount();
		
		steeringMotor.backward();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(!steeringMotor.isStalled()) Thread.yield();
		int l = steeringMotor.getTachoCount();
					
		int center = (l + r) / 2; // TODO: Maybe reset tacho to zero? Seems like there is no center variable.
		
		/*
		System.out.println("Left " + l);
		System.out.println("Right " + r);
		System.out.println("Center " + center);
		*/
		
		// Adjust values so they are still meaningful when tachocount is reset to zero below (0 = center):
		r -= center;
		l -= center;
		/* System.out.println("LEFT " + l);
		System.out.println("RIGHT " + r); */
				
		minRight = r;
		minLeft = l;
		
		// TODO: I'm not sure if reverse steering works yet with actual SteeringPilot class. 
		
		/* TODO: I'm not totally satisfied with this final step in calibration. It invariably doesn't quite rotate
		 * to the center value (off by one) and then all subsequent center positions are off by one degree. Would
		 * rather store the center/left/right values and use them, but this class wasn't programmed that way with 
		 * calibration and values in mind.  
		 */
		steeringMotor.rotateTo(center);
		// System.out.println("CENTER:" + steeringMotor.getTachoCount());
		steeringMotor.resetTachoCount();
		//steeringMotor.flt();
		steeringMotor.setStallThreshold(50,1000); // Reset to defaults.
	}
	
	/**
	 * In practice, a robot might steer tighter with one turn than the other.
	 * Currently returns minimum steering radius for the least tight turn direction.  
	 * @return minimum turning radius, in centimeters
	 */
	public double getMinRadius() {
		return minTurnRadius;
	}
	
	// NOTE: Currently the steer method locks this SteeringPilot into one proprietary LEGO robot design.
	// Tach values for left and right should be in constructor.
	// Should be able to use this class with a variety of steering robots.
	// NOTE: Doesn't actually have variable turn radius. Just minTurnRadius for now.
	// Note: Perhaps it should return the actual radius/arc it achieves, in case can't do the one it is called to do.
	// Although this might really screw things up for the algorithm. Shouldn't necessarily attempt arc it wasn't asked to perform.
	// Perhaps it should check if radius is < minRadius, then throw exception or return failed if it can't do it.
	/**
	 * Positive radius = left turn
	 * Negative radius = right turn
	 */
	private double steer(double radius) {
		if(radius == Double.POSITIVE_INFINITY) {
			this.steeringMotor.rotateTo(0);
			return Double.POSITIVE_INFINITY;
		} else if(radius > 0) {
			this.steeringMotor.rotateTo(minLeft);
			return getMinRadius();
		} else { // if(radius <= 0)
			this.steeringMotor.rotateTo(minRight);
			return -getMinRadius();
		}
	}
	
	public void arcForward(double turnRadius) {
		 arc(turnRadius, Double.POSITIVE_INFINITY, true);
	}
	
	public void arcBackward(double turnRadius) {
		arc(turnRadius, Double.NEGATIVE_INFINITY, true);
	}
	
	public void arc(double turnRadius, double arcAngle) throws IllegalArgumentException {
		if(turnRadius == 0) throw new IllegalArgumentException("SteeringPilot can't do zero radius turns."); // Can't turn in one spot
		 arc(turnRadius, arcAngle, false);
	}

	public void arc(double turnRadius, double arcAngle, boolean immediateReturn) {
		double distance = Move.convertAngleToDistance((float)arcAngle, (float)turnRadius);
		 travelArc(turnRadius, (float)distance, immediateReturn);
	}

	public void setMinRadius(double minTurnRadius) {
		this.minTurnRadius = minTurnRadius;
	}

	public void travelArc(double turnRadius, double distance) {
		travelArc(turnRadius, distance, false);
	}

	// TODO: Currently the DifferentialPilot goes forward if radius is negative. This goes backwards.
	public void travelArc(double turnRadius, double distance, boolean immediateReturn) throws IllegalArgumentException {
		
		// Hack here because JVM causes extra decimals for Math.abs function?
		double diff = this.getMinRadius() - Math.abs(turnRadius); 
		if(diff > 0.1) throw new IllegalArgumentException("Turn radius can't be less than " + this.getMinRadius());
		
		// 1. Check if moving. If so, call stop.
		if(isMoving) stop();
		
		// 2. Change wheel steering:
		double actualRadius = steer(turnRadius);
		
		// 3 Create new Move object:
		double angle = Move.convertDistanceToAngle((float)distance, (float)actualRadius);
		moveEvent = new Move((float)distance, (float)angle, true);
		
		
		// TODO: This if() block is a temporary kludge due to Motor.rotate() bug with Integer.MIN_VALUE:
		// Remove this if Roger changes Motor.rotate().
		if((distance == Double.NEGATIVE_INFINITY) | (distance == Double.POSITIVE_INFINITY)) {
			driveMotor.backward();
			//return moveEvent;
		}
		
		// 4. Start moving
		// Convert Float infinity to Integer maximum value.
		int tachos = (int)((distance * 360) / (driveWheelDiameter * Math.PI));
		driveMotor.rotate(tachos, immediateReturn);
		
		//return moveEvent;
	}
	
	public void backward() {
		travel(Double.NEGATIVE_INFINITY, true);
	}

	public void forward() {
		travel(Double.POSITIVE_INFINITY, true);
	}

	public double getMaxTravelSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	// TODO: This method should indicate it is not live speed. Such as getSpeedSetting(), setSpeedSetting()
	// TODO: Many methods in MoveController have no documentation and unit specification, incl. this.
	public double getTravelSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getMovementIncrement() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setTravelSpeed(double speed) {
		// TODO This should set the motor speed for the drive motor, perhaps also calculates based on wheel diameter?
		
	}

	public void stop() {
		// 1. Check if moving. If not, return?
//		if(!isMoving()) return false; // Should return no movement? Or moveEvent? Null might be appropriate.
		
		// 2. Get instance of moveEvent here. Used to check when rotationStopped() completes
		Move oldMove = moveEvent;
		
		// 3. Stop motor
		driveMotor.stop();
		
		// 4. Compare oldMove with moveEvent, only proceed when it changes
		while(oldMove == moveEvent) {Thread.yield();}
		
		// 5. Return newly created moveEvent
		//return moveEvent;
	}

	public void travel(double distance) {
		 travel(distance, false);
	}

	public void travel(double distance, boolean immediateReturn) {
		travelArc(Double.POSITIVE_INFINITY, distance, immediateReturn);
	}

	public void addMoveListener(MoveListener listener) {
		this.listener = listener;		
	}

	public Move getMovement() {
		// TODO This is probably supposed to provide the movement that has occurred since starting? (No Javadocs for this method makes it hard to figure out how to implement this method.)
		return null;
	}

	public void rotationStarted(RegulatedMotor motor, int tachoCount, boolean stall, long timeStamp) {
		isMoving = true;
		oldTacho = tachoCount;
		
		// Notify MoveListener
		if(listener != null) {
			listener.moveStarted(moveEvent, this);
		}
	}

	public void rotationStopped(RegulatedMotor motor, int tachoCount,boolean stall, long timeStamp) {
		isMoving = false;
		int tachoTotal = tachoCount - oldTacho ;
		float distance = (float)((tachoTotal/360f) * Math.PI * driveWheelDiameter);
		
		float angle = Move.convertDistanceToAngle(distance, moveEvent.getArcRadius()); 
		
		moveEvent = new Move(distance ,angle, isMoving);
		
		// Notify MoveListener
		if(listener != null) {
			listener.moveStopped(moveEvent, this);
		}
		
	}
}