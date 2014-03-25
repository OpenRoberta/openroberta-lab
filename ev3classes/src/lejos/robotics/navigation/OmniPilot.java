package lejos.robotics.navigation;

import java.util.ArrayList;

import lejos.hardware.Power;
import lejos.robotics.Gyroscope;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;
import lejos.utility.Matrix;

/*
 *          
 *       central wheel 
 * 			   |
 * 			   |
 *     120�    |    120�
 *            / \
 *           /   \         
 *          /120� \
 *   left wheel   right wheel
 * 
 * wi is the i-th wheel angular speed in [rad/s]
 * Vx and Vy are the components of the speed vector expressed in the robot local reference frame
 * thetadot is the robot angular speed
 * [Vx, Vy, thetadot]' = kMatrix*[w1, w2, w3]' 
 * [w1, w2, w3]' = ikMatrix*[Vx, Vy, thetadot]'	
 * [dx, dy, dh]' = kMatrix*[dr1, dr2, dr3]'
 */

/**
 * <p>Use the OmniPilot class to control holonomic vehicles with three omnidirectional wheels 
 * that allow the robot to move in any direction without changing heading. 
 * The robot can also spin while driving straight, and perform any kind of maneuvre the other steering and differential drive vehicles can do.
 * The odometry is computed by this class directly. 
 * For the class to work properly, take care to design the robot symmetrically, so that the three wheel axes meet in the center of the robot.</p>
 * @author Daniele Benedettelli
 * 
 */
public class OmniPilot implements ArcRotateMoveController, RegulatedMotorListener {
    
    private Pose pose = new Pose(); // TODO: Technically this variable should be removed. Navigator handles Pose.
	private float wheelBase = 7.0f; // units
	private float wheelDiameter = 4.6f; // units
	private double[][] ikPars;
	private double[][] kPars;
	private Matrix ikMatrix; // inverse kinematics matrix
	private Matrix kMatrix; // direct kinematics matrix
	private float linearSpeed = 10; //units/s
	private boolean reverse = false; // true when linearSpeed is negative
	private float speedVectorDirection = 0;
	private float angularSpeed = 0; // deg/s
	private final RegulatedMotor motor1; // central motor
	private final RegulatedMotor motor2; // left motor
	private final RegulatedMotor motor3; // right motor
	private int motor1Speed = 0; //deg/s
	private int motor2Speed = 0; //deg/s
	private int motor3Speed = 0; //deg/s
	private Odometer odo = new Odometer();
	private boolean spinningMode = false; 
	private float spinLinSpeed = 0; // units/s
	private float spinAngSpeed = 0; // deg/s
	private float spinTravelDirection = 0; // deg
	private Power battery;
	
	private double minTurnRadius = 0; // This vehicle can turn withgout moving therefore minimum turn radius = 0
	
	private Gyroscope gyro;
	
	private boolean gyroEnabled = false;
	
	/**
	 * MoveListeners to notify when a move is started or stopped.
	 */
	private ArrayList<MoveListener> listeners= new ArrayList<MoveListener>();
	
	/**
	 * Instantiates a new omnidirectional pilot.
	 * This class also keeps track of the odometry
	 * Express the distances in the units you prefer (mm, in, cm ...)
	 * 
	 * @param wheelDistanceFromCenter the wheel distance from center
	 * @param wheelDiameter the wheel diameter 
	 * @param centralMotor the central motor
	 * @param CW120degMotor the motor at 120 degrees clockwise from front
	 * @param CCW120degMotor the motor at 120 degrees counter-clockwise from front
	 * @param centralWheelFrontal if true, the central wheel frontal else it is facing back
	 * @param motorReverse if motors are mounted reversed
	 */
	public OmniPilot (float wheelDistanceFromCenter, float wheelDiameter, 
					RegulatedMotor centralMotor, RegulatedMotor CW120degMotor, RegulatedMotor CCW120degMotor,  
					boolean centralWheelFrontal, boolean motorReverse,
					Power battery) {
		this.wheelBase = wheelDistanceFromCenter;
		this.wheelDiameter = wheelDiameter;
		this.motor1 = centralMotor;
		this.motor2 = CCW120degMotor;
		this.motor3 = CW120degMotor;
		this.battery = battery;
		motor1.addListener(this);
		motor2.addListener(this);
		motor3.addListener(this);
		initMatrices(centralWheelFrontal, motorReverse);
	    odo.setDaemon(true);
//	    odo.setPriority(6);
	    odo.start();
	}
	
	/**
	 * Instantiates a new omnidirectional pilot.
	 * This class also keeps track of the odometry
	 * Express the distances in the units you prefer (mm, in, cm ...)
	 * This constructor allows you to add a cruizcore gyro for accurate odometry and spinning
	 *
	 * @param wheelDistanceFromCenter the wheel distance from center
	 * @param wheelDiameter the wheel diameter 
	 * @param centralMotor the central motor
	 * @param CW120degMotor the motor at 120 degrees clockwise from front
	 * @param CCW120degMotor the motor at 120 degrees counter-clockwise from front
	 * @param centralWheelFrontal if true, the central wheel frontal else it is facing back
	 * @param motorReverse if motors are mounted reversed
	 * @param gyro the gyroscope
	 */
	public OmniPilot(float wheelDistanceFromCenter, float wheelDiameter, 
			RegulatedMotor centralMotor, RegulatedMotor CW120degMotor, RegulatedMotor CCW120degMotor,  
			boolean centralWheelFrontal, boolean motorReverse, 
			Power battery, Gyroscope gyro) {
		this(wheelDistanceFromCenter, wheelDiameter,centralMotor, CW120degMotor, CCW120degMotor,  
				centralWheelFrontal, motorReverse, battery);
		this.gyro = gyro;
//		gyro = new CruizcoreGyro(gyroPort), I2CPort.HIGH_SPEED);
//		gyro = new CruizcoreGyro(gyroPort, I2CPort.LEGO_MODE);
		gyro.reset();
		
		gyroEnabled = true;
	}
	
/*	
	private void showMatrices() {
		LCD.clear();
		System.out.println("ikM row 1 ");
		System.out.println((float)ikMatrix.get(0, 0));
		System.out.println((float)ikMatrix.get(0, 1));
		System.out.println((float)ikMatrix.get(0, 2));
		Button.waitForPress();
		System.out.println("ikM row 2 ");
		System.out.println((float)ikMatrix.get(1, 0));
		System.out.println((float)ikMatrix.get(1, 1));
		System.out.println((float)ikMatrix.get(1, 2));
		Button.waitForPress();
		System.out.println("ikM row 3 ");
		System.out.println((float)ikMatrix.get(2, 0));
		System.out.println((float)ikMatrix.get(2, 1));
		System.out.println((float)ikMatrix.get(2, 2));		
		Button.waitForPress();
		LCD.clear();
		System.out.println("kM row 1 ");
		System.out.println((float)kMatrix.get(0, 0));
		System.out.println((float)kMatrix.get(0, 1));
		System.out.println((float)kMatrix.get(0, 2));
		Button.waitForPress();
		System.out.println("kM row 2 ");
		System.out.println((float)kMatrix.get(1, 0));
		System.out.println((float)kMatrix.get(1, 1));
		System.out.println((float)kMatrix.get(1, 2));
		Button.waitForPress();
		System.out.println("kM row 3 ");
		System.out.println((float)kMatrix.get(2, 0));
		System.out.println((float)kMatrix.get(2, 1));
		System.out.println((float)kMatrix.get(2, 2));		
		Button.waitForPress();	
	}
*/
	
	/**
 * Inits the matrices.
 *
 * @param centralWheelForward the central wheel forward
 * @param motorReverse the motor reverse
 */
private void initMatrices(boolean centralWheelForward, boolean motorReverse) {
		ikPars = new double[3][3];
		kPars = new double[3][3];
		int centralFwd = centralWheelForward? 1: -1;
		int rev = motorReverse? -1: 1;
		// front/back motor row
		ikPars[0][0] = 0;
		ikPars[0][1] = -1*centralFwd;
		ikPars[0][2] = -wheelBase;
		// left motor row
		ikPars[1][0] = Math.sqrt(3)*centralFwd/2;
		ikPars[1][1] = 0.5*centralFwd;
		ikPars[1][2] = -wheelBase;
		// right motor row
		ikPars[2][0] = -Math.sqrt(3)*centralFwd/2;
		ikPars[2][1] = 0.5*centralFwd;
		ikPars[2][2] = -wheelBase;
		ikMatrix = new Matrix(ikPars,3,3);
		ikMatrix.timesEquals(rev*2/wheelDiameter);
		
		// front/back motor column
		kPars[0][0] = 0;
		kPars[1][0] = -2*centralFwd;
		kPars[2][0] = -1/wheelBase;
		// left motor column
		kPars[0][1] = Math.sqrt(3)*centralFwd;
		kPars[1][1] = 1*centralFwd;
		kPars[2][1] = -1/wheelBase;
		// right motor column
		kPars[0][2] = -Math.sqrt(3)*centralFwd;
		kPars[1][2] = 1*centralFwd;
		kPars[2][2] = -1/wheelBase;
		kMatrix = new Matrix(kPars,3,3);
		kMatrix.timesEquals(rev*wheelDiameter/6);	
	}	
	
	
	/**
	 * Sets the speed.
	 *
	 * @param linSpeed the lin speed
	 * @param dir the dir
	 * @param angSpeed the ang speed
	 */
	private void setSpeed(double linSpeed, double dir, double angSpeed) {
		double ang = Math.toRadians(dir);
		double angspd = Math.toRadians(angSpeed);
		double[] spd = {linSpeed*Math.cos(ang), linSpeed*Math.sin(ang), angspd};
		Matrix speeds = new Matrix(spd, 3);
		Matrix commands = ikMatrix.times(speeds);
		
		motor1Speed = (int) Math.toDegrees(commands.get(0, 0));
		motor2Speed = (int) Math.toDegrees(commands.get(1, 0));
		motor3Speed = (int) Math.toDegrees(commands.get(2, 0));
//		LCD.drawString("Vx "+(float)spd[0]+"  ", 0, 0);
//		LCD.drawString("Vy "+(float)spd[1]+"  ", 0, 1);
//		LCD.drawString("ang spd "+angSpeed+"  ", 0, 2);
//		LCD.drawString("w1 "+motor1Speed+" deg/s ", 0, 3);
//		LCD.drawString("w2 "+motor2Speed+" deg/s ", 0, 4);
//		LCD.drawString("w3 "+motor3Speed+" deg/s ", 0, 5);
//		Button.waitForPress();
		motor1.setSpeed(motor1Speed);
		motor2.setSpeed(motor2Speed);
		motor3.setSpeed(motor3Speed);
	}
	
	/**
	 * Sets the acceleration.
	 *
	 * @param accel the new acceleration
	 */
	public void setAcceleration(int accel) {
		this.motor1.setAcceleration(accel);
		this.motor2.setAcceleration(accel);
		this.motor3.setAcceleration(accel);
	}
	
	/**
	 * Start motors.
	 */
	private synchronized void  startMotors() {
		if (motor1Speed>0) 
			motor1.forward(); 
		else 
			motor1.backward();
		if (motor2Speed>0) 
			motor2.forward(); 
		else motor2.backward();
		if (motor3Speed>0) 
			motor3.forward(); 
		else motor3.backward();	
	}
	
	/**
	 * Coast. TODO: Probably delete this method?
	 */
	private synchronized void coast() {
		motor1.flt();
		motor2.flt();
		motor3.flt();
		spinningMode = false;
	}
	
	public synchronized void forward() {
		spinningMode = false;
		setSpeed(linearSpeed, 0, angularSpeed);
		startMotors();
	}

	public synchronized void backward() {
		spinningMode = false;
		setSpeed(linearSpeed, 180, angularSpeed);
		startMotors();
	}
	
	/**
	 * This method causes the robot to move in a direction while keeping the front of the robot pointed in the current direction it is facing. 
	 *
	 * @param linSpeed the lin speed
	 * @param direction the direction relative to the current direction the robot is facing
	 */
	public synchronized void moveStraight(float linSpeed, int direction) {
//		float dir = linSpeed>0? direction : direction+180;
		spinningMode = false;
		setSpeed(linSpeed, direction, 0);
		startMotors();
	}
	
	/**
	 * Causes the robot to spin while moving along a linear path. This method is similar to {@link OmniPilot#moveStraight(float, int)}
	 * except the robot will spin instead of holding the robot in the current direction.
	 *
	 * @param linSpeed the linear speed [units/s]
	 * @param angSpeed the angular speed [deg/s]
	 * @param direction the direction [deg]
	 */
	public synchronized void spinningMove(float linSpeed, int angSpeed, int direction) {
		spinLinSpeed = linSpeed;
		spinAngSpeed = angSpeed;
		spinTravelDirection = direction;
		spinningMode = true;
//		setSpeed(spinLinSpeed, spinTravelDirection-pose.getHeading(), spinAngSpeed);
//		startMotors();
	}
	
	public synchronized void stop() {
		motor1.stop();
		motor2.stop();
		motor3.stop();
		spinningMode = false;
	}

	public boolean isMoving() {
		return motor1.isMoving() || motor2.isMoving() || motor3.isMoving();
	}

	public void setTravelSpeed(double speed) {
		linearSpeed = Math.abs((float)speed);
		reverse = speed<0;
	}

	public double getTravelSpeed() {
		return linearSpeed;
	}
	
	/**
	 * Sets the move direction. This value is then used by subsequent calls to {@link OmniPilot#steer(float)} (all three overloaded methods).
	 *
	 * @param dir the new move direction
	 */
	public void setMoveDirection(int dir) {
		speedVectorDirection = dir;
	}
	
	/**
	 * Gets the move direction.
	 *
	 * @return the move direction
	 */
	public float getMoveDirection() {
		return speedVectorDirection;
	}

	public double getMaxTravelSpeed() {
		// it is generally assumed, that the maximum accurate speed of Motor is
		// 100 degree/second * Voltage
		double maxRadSec = Math.toRadians(battery.getVoltage()*100f);
		double[] spd = {0, maxRadSec, -maxRadSec};
		Matrix wheelSpeeds = new Matrix(spd, 3);		
		Matrix robotSpeeds = kMatrix.times(wheelSpeeds);
		return (float) Math.sqrt(robotSpeeds.get(0,0)*robotSpeeds.get(0,0) + 
				robotSpeeds.get(1,0)*robotSpeeds.get(1,0)); 
		// max degree/second divided by degree/unit = unit/second
	}

	public void setRotateSpeed(double speed) {
		angularSpeed = (float)speed;
	}
	
	public double getRotateSpeed() {
		return angularSpeed;
	}

	public double getRotateMaxSpeed() {
		// it is generally assumed, that the maximum accurate speed of Motor is
		// 100 degree/second * Voltage
		double maxRadSec = Math.toRadians(battery.getVoltage()*100f);
		Matrix wheelSpeeds = new Matrix(3, 1, maxRadSec);
		Matrix robotSpeeds = kMatrix.times(wheelSpeeds);
		return (float) Math.abs(Math.toDegrees(robotSpeeds.get(2,0)));
		// max degree/second divided by degree/unit = unit/second
	}

	private Move.MoveType previousMoveType = null;
	private float previousDistance = 0;
	private float previousAngle = 0;

	/**
	 * Move.
	 *
	 * @param distance the distance
	 * @param direction the direction
	 * @param angle the angle
	 * @param immediateReturn the immediate return
	 */
	private void move(final double distance, double direction, final double angle, boolean immediateReturn) {
		// Notify MoveListeners that a new move has begun.
		
		if(distance != 0 & angle == 0) previousMoveType = Move.MoveType.TRAVEL;
		else if(distance == 0 & angle != 0) previousMoveType = Move.MoveType.ROTATE;
		else if(distance != 0 & angle != 0) previousMoveType = Move.MoveType.ARC;
		else previousMoveType = Move.MoveType.STOP;
		
		for(MoveListener ml:listeners) 
			ml.moveStarted(new Move(previousMoveType, (float)distance, (float)angle, true), this);
		
		spinningMode = false;
		double[] dsp = {distance*Math.cos(Math.toRadians(direction)), distance*Math.sin(Math.toRadians(direction)), Math.toRadians(angle)};
		Matrix displacement = new Matrix(dsp, 3);
		Matrix distances = ikMatrix.times(displacement);
		int d1 = (int) Math.toDegrees(distances.get(0,0));
		int d2 = (int) Math.toDegrees(distances.get(1,0));
		int d3 = (int) Math.toDegrees(distances.get(2,0));
		
		if (angle==0) {
			setSpeed(linearSpeed, direction, 0);
		}
		if (distance==0) {
			setSpeed(0, 0, angularSpeed);
		}
//		LCD.drawString("dx "+(float)dsp[0]+"  ", 0, 0);
//		LCD.drawString("dy "+(float)dsp[1]+"  ", 0, 1);
//		LCD.drawString("d1 "+d1+" deg ", 0, 3);
//		LCD.drawString("d2 "+d2+" deg ", 0, 4);
//		LCD.drawString("d3 "+d3+" deg ", 0, 5);
//		Button.waitForPress();
		motor1.rotate(d1, true);
		motor2.rotate(d2, true);
		motor3.rotate(d3, immediateReturn);

		if (!immediateReturn)
			while (isMoving())
				Thread.yield();
	}
	
	public void travel(double distance) {
		travel(distance, 0, false);
	}

	/**
	 * This method causes the robot to travel in a linear path, similar to other travel() methods, except you can specify
	 * which direction to move (relative to the current robot heading).
	 *  
	 * <b>NOTE: This method is not part of the MoveController interface.</b>
	 * @param distance
	 * @param direction
	 */
	public void travel(double distance, double direction) {
		travel(distance, direction, false);
	}
	
	public void travel(double distance, boolean immediateReturn) {
		travel(distance, 0, immediateReturn);
	}
	
	/**
	 * This method causes the robot to travel in a linear path, similar to other travel() methods, except you can specify
	 * which direction to move (relative to the current robot heading).
	 * 
	 * <b>NOTE: This method is not part of the MoveController interface.</b>
	 * @param distance
	 * @param direction
	 * @param immediateReturn
	 */
	public void travel(double distance, double direction, boolean immediateReturn) {
		move(distance, direction, 0, immediateReturn);
	}

	public void rotate(double angle) {
		rotate(angle, false);
	}

	public void rotate(double angle, boolean immediateReturn) {
		if (angularSpeed==0) angularSpeed = 90;
		move(0, 0, angle, immediateReturn);
	}


	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	private float getAngle() {
//		Sound.playTone(2000, 10);
		int t1 = motor1.getTachoCount();
		int t2 = motor2.getTachoCount();
		int t3 = motor3.getTachoCount();
		double[] dsp = {Math.toRadians(t1),Math.toRadians(t2),Math.toRadians(t3)};
		Matrix displacement = new Matrix(dsp, 3);
		Matrix distances = kMatrix.times(displacement);
		float ang = (float) Math.toDegrees(distances.get(2,0));
//		LCD.drawString(t1+" "+t2+" "+t3, 0, 5);
//		LCD.drawString("a "+ang+"   ", 0, 6);
		return ang;
	}

	/**
	 * Gets the travel distance since last tacho reset.
	 *
	 * @return the travel distance
	 */
	private float getTravelDistance() {
		int t1 = motor1.getTachoCount();
		int t2 = motor2.getTachoCount();
		int t3 = motor3.getTachoCount();
		double[] dsp = {Math.toRadians(t1),Math.toRadians(t2),Math.toRadians(t3)};
		Matrix displacement = new Matrix(dsp, 3);
		Matrix distances = kMatrix.times(displacement);
		float d = (float) Math.sqrt(distances.get(0,0)*distances.get(0,0) +	distances.get(1,0)*distances.get(1,0)); //TODO is this correct for omni odometry?
//		LCD.drawString(t1+" "+t2+" "+t3+"      ", 0, 5);
//		LCD.drawString("d "+d+"   ", 0, 7);
		return d;
	}

	/**
	 * Steer.
	 *
	 * @param turnRate the turn rate
	 */
	public void steer(float turnRate) {
		float angSpeed = (float)(turnRate*getRotateMaxSpeed()/200);
		float dir = reverse? speedVectorDirection : speedVectorDirection+180;
		spinningMode = false;
		setSpeed(linearSpeed, dir, angSpeed);
		startMotors();
	}

	/**
	 * Steer.
	 *
	 * @param linSpeed the lin speed
	 * @param angSpeed the ang speed
	 */
	public void steer(float linSpeed, float angSpeed) {
//		float dir = linSpeed>0? speedVectorDirection : speedVectorDirection+180;
		spinningMode = false;
		setSpeed(linSpeed, speedVectorDirection, angSpeed);
		startMotors();
	}

	/**
	 * Steer.
	 *
	 * @param turnRate the turn rate
	 * @param angle the angle
	 * @param immediateReturn the immediate return
	 */
	public void steer(float turnRate, float angle, boolean immediateReturn) {
		angularSpeed = (float)(turnRate*getRotateMaxSpeed()/200);
//		LCD.drawString("spd "+angularSpeed+" deg/s ", 0, 0);	
		float radius = (float) (linearSpeed/Math.toRadians(angularSpeed));
		arc(radius,angle,immediateReturn);
	}
	
	public void travelArc(double radius, double distance) {
		travelArc(radius, distance, false);
	}

	public void travelArc(double radius, double distance, boolean immediateReturn) {
		travelArc(radius, distance, 0, false);
	}
	
	/**
	 * This method moves the robot in an arc, similar to the other {@link OmniPilot#travelArc(double, double)} methods,
	 * except you can choose any of the 360 degree directions relative to the current heading (0) of the robot, while keeping
	 * the heading of the robot pointed in the same direction during the move. 
	 * 
	 * <b>NOTE: This method is not part of the MoveController interface.</b>
	 * @param radius
	 * @param distance
	 * @param direction
	 */
	public void travelArc(double radius, double distance, float direction) {
		travelArc(radius, distance, direction, false);
	}
	
	/**
	 * This method moves the robot in an arc, similar to the other {@link OmniPilot#travelArc(double, double)} methods,
	 * except you can choose any of the 360 degree directions relative to the current heading (0) of the robot, while keeping
	 * the heading of the robot pointed in the same direction during the move. 
	 * 
	 * <b>NOTE: This method is not part of the MoveController interface.</b>
	 * @param radius
	 * @param distance
	 * @param direction
	 * @param immediateReturn
	 */
	public void travelArc(double radius, double distance, float direction,  boolean immediateReturn) {
		float angle = (float) ((distance * 180) / (Math.PI * radius));
		arc(radius, angle, direction, immediateReturn);
	}

	public void arc(double radius, double angle) {
		arc(radius, angle, 0, false);
	}
	
	/**
	 * This method moves the robot in an arc, similar to the other {@link OmniPilot#arc(double, double)} methods,
	 * except you can choose any of the 360 degree directions relative to the current heading (0) of the robot, while keeping
	 * the heading of the robot pointed in the same direction during the move. 
	 * 
	 * <b>NOTE: This method is not part of the MoveController interface.</b>
	 * @param radius
	 * @param angle
	 * @param direction
	 */
	public void arc(double radius, double angle, double direction) {
		arc(radius, angle, direction, false);
	}
		
	public void arc(double radius, double angle, boolean immediateReturn) {
		arc(radius,angle,0,immediateReturn);
	}
	
	/**
	 * This method moves the robot in an arc, similar to the other {@link OmniPilot#arc(double, double)} methods,
	 * except you can choose any of the 360 degree directions relative to the current heading (0) of the robot, while keeping
	 * the heading of the robot pointed in the same direction during the move. 
	 * 
	 * <b>NOTE: This method is not part of the MoveController interface.</b>
	 * @param radius
	 * @param angle
	 * @param direction
	 * @param immediateReturn
	 */
	public void arc(double radius, double angle, double direction, boolean immediateReturn) {
		
		float angSpeed = (float) Math.toDegrees(linearSpeed/radius);
		spinningMode = false;
		setSpeed(linearSpeed, direction, angSpeed);
		if (Float.isInfinite((float)angle)) {
			startMotors();
		} else {
			float distance = (float) (Math.toRadians(angle)*radius);
//			LCD.drawString("R "+radius+" units ", 0, 0);
//			LCD.drawString("A "+angle+" deg ", 0, 1);
//			LCD.drawString("D "+distance+" units ", 0, 2);
//			Button.waitForPress();
			move(distance,direction, angle, immediateReturn);
		}
	}


	/**
	 * Reset all tacho counts. TODO: Delete this method? Unused by any other method or class.
	 */
	public void reset() {
		motor1.resetTachoCount();
		motor2.resetTachoCount();
		motor3.resetTachoCount();
		odo.reset();
	}

	/**
	 * Sets drive motor speed.
	 *
	 * @param speed the new speed
	 * @deprecated in 0.8, use setRotateSpeed() and setTravelSpeed(). The method was deprecated, as this it requires knowledge
	 * of the robots physical construction, which this interface should hide!
	 */
	@Deprecated
	public void setSpeed(int speed) {
		setTravelSpeed(speed);
	}

	private class Odometer extends Thread {
		
		private int t1old = 0;
		
		private int t2old = 0;
		
		private int t3old = 0;
		
		private boolean keepRunning = true;
		
		private int period = 10; //ms
		
		//private boolean displayPose = false;
		
		//private int displayLine = 0;
		
		/**
		 * Stop the odometry thread.
		 */
		public void shutDown() {
			keepRunning = false;
		}
		
		@Override
		public void run() {
			long tick = period + System.currentTimeMillis(); 
			while(keepRunning) {
               if(System.currentTimeMillis()>= tick) { // simulate timer
                  tick += period;
                  updatePose();
                  if (gyroEnabled) {
                	  pose.setHeading(gyro.getAngle()/100.0f);
                  }
                  if (spinningMode) {
                	  setSpeed(spinLinSpeed, spinTravelDirection-pose.getHeading(), spinAngSpeed);
                	  startMotors();
                	  //LCD.drawString("t:"+tick, 0, 0);
                  } 
               } else {
            	   Delay.msDelay(5);
               }
			} 
		}
		
		/**
		 * Reset odometry.
		 */
		public void reset() {
			synchronized (pose) {
				pose.setLocation(new Point(0, 0));
				pose.setHeading(0);
				gyro.reset();
			}
		}

		private void updatePose()
		{
//			Sound.playTone(1000, 5);
			int t1 = motor1.getTachoCount();
			int t2 = motor2.getTachoCount();
			int t3 = motor3.getTachoCount();
			double[] angles = {Math.toRadians(t1-t1old),Math.toRadians(t2-t2old),Math.toRadians(t3-t3old)};
			Matrix wheelTachos = new Matrix(angles, 3);
			Matrix localDeltaPose = kMatrix.times(wheelTachos);
			float dXL = (float) localDeltaPose.get(0,0);
			float dYL = (float) localDeltaPose.get(1,0);
			float dH = (float) localDeltaPose.get(2,0);
			
	
			float H = (float) Math.toRadians(pose.getHeading());
			float dX = (float) (Math.cos(H)*dXL - Math.sin(H)*dYL);
			float dY = (float) (Math.sin(H)*dXL + Math.cos(H)*dYL);
			pose.translate(dX, dY);
			pose.rotateUpdate((float)Math.toDegrees(dH));
		    t1old = t1;
		    t2old = t2;
		    t3old = t3;
		    /*
		    if (displayPose) {
		    	LCD.drawString("X: "+ Math.round(pose.getX()*1000f)/1000f+ "      ", 0, displayLine);
		    	LCD.drawString("Y: "+ Math.round(pose.getY()*1000f)/1000f+ "      ", 0, displayLine+1);
		    	LCD.drawString("H: "+ Math.round(pose.getHeading()*1000f)/1000f+ " deg     ", 0, displayLine+2);
		    }*/
		}
	}

	
	public void arcBackward(double radius) {
		arc(radius, Double.NEGATIVE_INFINITY, true);
	}

	public void arcForward(double radius) {
		arc(radius, Double.POSITIVE_INFINITY, true);
	}

	public double getMinRadius() {
		return minTurnRadius;
	}

	public void setMinRadius(double radius) {
		minTurnRadius = radius;
	}

	public void addMoveListener(MoveListener listener) {
		listeners.add(listener);
	}

	public Move getMovement() {
		return new Move(previousMoveType, getTravelDistance() - previousDistance, getAngle() - previousAngle, isMoving());
	}

	public void rotationStarted(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
		// Do nothing. private move() method handles notifications. 
	}
	
	/**
	 * Notify the MoveListeners when a move is completed.
	 */
	public void rotationStopped(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
		if(!motor1.isMoving() && !motor2.isMoving() && !motor3.isMoving()) {
			float newDistance = getTravelDistance();
			float newAngle = getAngle();
			Move finalMove = new Move(previousMoveType, newDistance - previousDistance, newAngle - previousAngle, isMoving());
			for(MoveListener ml:listeners) 
				ml.moveStopped(finalMove, this);
			
			//this.reset(); // THIS CAUSES AN EXCEPTION. Will subtract previous values instead.
			previousDistance = newDistance;
			previousAngle = newAngle;
		}
	}
}
