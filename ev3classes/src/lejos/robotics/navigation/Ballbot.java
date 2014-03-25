package lejos.robotics.navigation;

import lejos.hardware.lcd.LCD;
import lejos.hardware.Sound;
import lejos.robotics.EncoderMotor;
import lejos.robotics.Gyroscope;

/**
 * <p>This class dynamically stabilizes a ballbot type of robot. The ballbot robot uses two motors to drive a ball
 * with a similar configuration to a mechanical mouse.</p>
 * 
 * <p>A ballbot needs good motors, sensors, a grippy ball/rollers (wheels) and should be as symmetrical as possible in all
 * directions (like a circle) to optimize stability. If using a billiard ball or LEGO ball, it is recommended
 * to apply a layer of rubber glue to the tires to add grip.</p> 
 * 
 * <p>To start the robot balancing:
 * <li>1. Run the program. You will be prompted to lay it down.
 * <li>2. Lay it down (orientation doesn't matter). When it detects it is not moving it will automatically calibrate the gyro sensors.
 * <li>3. When the beeping begins, stand it up on the ball so it is vertically balanced.
 * <li>4. When the beeping stops, let go and it will begin balancing on its own.</p>
 * 
 * <p><i>This code is based on the Segoway class.</p>
 * 
 * @author BB
 *
 */
public class Ballbot extends Thread { // TODO: Thread should be a private inner class, especially given that Ballbot constructor creates two instances of Ballbot threads.
	
	/* Developer notes:
	 * TODO: The Ballbot and Segoway classes could share a lot of the same balance code. Each Ballbot thread uses one motor, 
	 * not two. But most of the code is the same. Possibly extend Segoway and override some methods in new 
	 * Ballbot class.
	 * 
	 * TODO: Get rid of platform dependencies.
	*/
	
	// Motors and gyro:
	private Gyroscope gyro; 
	protected EncoderMotor my_motor;
	//protected EncoderMotor right_motor; // TODO
	
	//=====================================================================
	// Balancing constants
	//
	// These are the constants used to maintain balance.
	//=====================================================================
	
	/** 
	 * Loop wait time.  WAIT_TIME is the time in ms passed to the Wait command.
	 * NOTE: Balance control loop only takes 1.128 MS in leJOS NXJ to execute. 
	 */
	private static final int WAIT_TIME = 6; // originally 8
	
	// These are the main four balance constants, only the gyro
	// constants are relative to the wheel size.  KPOS and KSPEED
	// are self-relative to the wheel size.
	private static final double KGYROANGLE = 7.5;
	private static final double KGYROSPEED = 1.15;
	private static final double KPOS = 0.07;
	private static final double KSPEED = 0.1;

	/**
	 * This constant aids in drive control. When the robot starts moving because of user control,
	 * this constant helps get the robot leaning in the right direction.  Similarly, it helps 
	 * bring robot to a stop when stopping.
	 */
	private static final double KDRIVE = -0.02;

	/**
	 * Power differential used for steering based on difference of target steering and actual motor difference.
	 */
	private static final double KSTEER = 0.25;

	/**
	 * Gyro offset control
	 * The gyro sensor will drift with time.  This constant is used in a simple long term averaging
	 * to adjust for this drift. Every time through the loop, the current gyro sensor value is
	 * averaged into the gyro offset weighted according to this constant.
	 */
	private static final double EMAOFFSET = 0.0005;

	/** 
	 * If robot power is saturated (over +/- 100) for over this time limit then 
	 * robot must have fallen.  In milliseconds.
	 */
	private static final double TIME_FALL_LIMIT = 500; // originally 1000

	//---------------------------------------------------------------------

	/**
	 * This constant is in degrees/second for maximum speed.  Note that position 
	 * and speed are measured as the sum of the two motors, in other words, 600 
	 * would actually be 300 degrees/second for each motor.
	 */
	private static final double CONTROL_SPEED  = 600.0;

	//=====================================================================
	// Global variables
	//=====================================================================
	
	// These two xxControlDrive variables are used to control the movement of the robot. Both
	// are in degrees/second:	
	/**
	 * motorControlDrive is the target speed for the sum of the two motors
	 * in degrees per second.
	 */
	private double motorControlDrive = 0.0;
	
	/**
	 * motorControlSteer is the target change in difference for two motors
	 * in degrees per second.
	 */
	private double motorControlSteer = 0.0;

	/**
	 * This global contains the target motor differential, essentially, which 
	 * way the robot should be pointing.  This value is updated every time through 
	 * the balance loop based on motorControlSteer.
	 */
	private double motorDiffTarget = 0.0;

	/**
	 * Time that robot first starts to balance.  Used to calculate tInterval.
	 */
	private long tCalcStart;

	/**
	 * tInterval is the time, in seconds, for each iteration of the balance loop.
	 */
	private double tInterval;

	/**
	 * ratioWheel stores the relative wheel size compared to a standard NXT 1.0 wheel.
	 * RCX 2.0 wheel has ratio of 0.7 while large RCX wheel is 1.4.
	 */
	private double ratioWheel;

	// Gyro globals
	private double gOffset;
	private double gAngleGlobal = 0;

	// Motor globals
	private double motorPos = 0;
	private long mrcSum = 0, mrcSumPrev;
	private long motorDiff;
	private long mrcDeltaP3 = 0;
	private long mrcDeltaP2 = 0;
	private long mrcDeltaP1 = 0;

	/**
	 * Creates an instance of the Ballbot, prompts the user to lay Ballbot flat for gyro calibration,
	 * then begins self-balancing thread. Wheel diameter is used in balancing equations.
	 *  
	 *  <li>NXT 1.0 wheels = 5.6 cm
	 *  <li>NXT 2.0 wheels = 4.32 cm
	 *  
	 * @param left The left motor. An unregulated motor.
	 * @param right The right motor. An unregulated motor.
	 * @param gyro A HiTechnic gyro sensor
	 * @param wheelDiameter diameter of wheel, preferably use cm (printed on side of LEGO tires in mm)
	 */
	private Ballbot(EncoderMotor motor, Gyroscope gyro, double wheelDiameter) { // TODO: Wheel diam will be unnecessary?
		this.my_motor = motor;
		
		this.gyro = gyro;
		this.ratioWheel = wheelDiameter/5.6; // Original algorithm was tuned for 5.6 cm NXT 1.0 wheels.
		
		// Took out 50 ms delay here.
		
		// Get the initial gyro offset
		getGyroOffset();
	}

	/**
	 * 
	 * @param xMotor The first motor, such as an NXTMotor.
	 * @param xGyro The gyro accompanying xMotor. Monitors the x-axis
	 * @param yMotor The second motor, such as an NXTMotor.
	 * @param yGyro The gyro accompanying xMotor. Monitors the y-axis
	 * @param rollerDiameter The diameter of the motorized rollers. Usually NXT 2.0 wheels (4.32 cm)
	 */
	public Ballbot(EncoderMotor xMotor, Gyroscope xGyro, EncoderMotor yMotor, Gyroscope yGyro, double rollerDiameter) {
		threadx = new Ballbot(xMotor, xGyro, rollerDiameter);
		thready = new Ballbot(yMotor, yGyro, rollerDiameter);
		
		// Play warning beep sequence before balance starts
		Ballbot.startBeeps();
		
		// Start balance threads
		threadx.start();
		try {
			Thread.sleep(WAIT_TIME/2); // Stagger threads to give thread execution more space.
		} catch (InterruptedException e) {}
		thready.start();
		
	}
	
	private Ballbot threadx;
	private Ballbot thready;
	
	/**
	 * Number of offset samples to average when calculating gyro offset.
	 */
	private static final int OFFSET_SAMPLES = 100;

	/**
	 * This function returns a suitable initial gyro offset.  It takes
	 * 100 gyro samples over a time of 1/2 second and averages them to
	 * get the offset.  It also check the max and min during that time
	 * and if the difference is larger than one it rejects the data and
	 * gets another set of samples.
	 */
	private void getGyroOffset() {
		
		LCD.clear();
		LCD.drawString("NXJ Ballbot",0,1);

		LCD.drawString("Lay robot down", 0, 4);
		LCD.drawString("flat to get gyro", 0, 5);
		LCD.drawString("offset.", 0, 6);

		gyro.recalibrateOffset();
	}

	/**
	 * Warn user the Ballbot is about to start balancing. 
	 */
	private static void startBeeps() {
		LCD.clear();
		LCD.drawString("leJOS NXJ Ballbot", 0, 1);
		LCD.drawString("Balance in", 0, 3);

		// Play warning beep sequence to indicate balance about to start
		for (int c=8; c>=0;c--) {
			LCD.drawInt(c, 5, 4);
			Sound.playTone(440,100);
			try { Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * Get the data from the gyro. 
	 * Fills the pass by reference gyroSpeed and gyroAngle based on updated information from the Gyro Sensor.
	 * Maintains an automatically adjusted gyro offset as well as the integrated gyro angle.
	 * 
	 */
	private void updateGyroData() {
		float gyroRaw;

		gyroRaw = gyro.getAngularVelocity();
		gOffset = EMAOFFSET * gyroRaw + (1-EMAOFFSET) * gOffset;
		gyroSpeed = gyroRaw - gOffset; // Angular velocity (degrees/sec)

		gAngleGlobal += gyroSpeed*tInterval;
		gyroAngle = gAngleGlobal; // Absolute angle (degrees)
	}

	/**
	 * Keeps track of wheel position with both motors.
	 */
	private void updateMotorData() {
		long mrcLeft, mrcDelta;

		// Keep track of motor position and speed
		mrcLeft = my_motor.getTachoCount();
		//mrcRight = right_motor.getTachoCount();

		// Maintain previous mrcSum so that delta can be calculated and get
		// new mrcSum and Diff values
		mrcSumPrev = mrcSum;
		mrcSum = mrcLeft + mrcLeft; // TODO: I'm sidestepping these two lines by using mrcLeft twice
		motorDiff = mrcLeft - mrcLeft;

		// mrcDetla is the change int sum of the motor encoders, update
		// motorPos based on this detla
		mrcDelta = mrcSum - mrcSumPrev;
		motorPos += mrcDelta;

		// motorSpeed is based on the average of the last four delta's.
		motorSpeed = (mrcDelta+mrcDeltaP1+mrcDeltaP2+mrcDeltaP3)/(4*tInterval);

		// Shift the latest mrcDelta into the previous three saved delta values
		mrcDeltaP3 = mrcDeltaP2;
		mrcDeltaP2 = mrcDeltaP1;
		mrcDeltaP1 = mrcDelta;
	}

	/** 
	 * Global variables used to control the amount of power to apply to each wheel.
	 * Updated by the steerControl() method.
	 */
	private int powerLeft, powerRight; // originally local variables

	/**
	 * This function determines the left and right motor power that should
	 * be used based on the balance power and the steering control.
	 */
	private void steerControl(int power) {
		int powerSteer;

		// Update the target motor difference based on the user steering
		// control value.
		motorDiffTarget += motorControlSteer * tInterval;

		// Determine the proportionate power differential to be used based
		// on the difference between the target motor difference and the
		// actual motor difference.
		powerSteer = (int)(KSTEER * (motorDiffTarget - motorDiff));

		// Apply the power steering value with the main power value to
		// get the left and right power values.
		powerLeft = power + powerSteer;
		powerRight = power - powerSteer;

		// Limit the power to motor power range -100 to 100
		if (powerLeft > 100)   powerLeft = 100;
		if (powerLeft < -100)  powerLeft = -100;

		// Limit the power to motor power range -100 to 100
		if (powerRight > 100)  powerRight = 100;
		if (powerRight < -100) powerRight = -100;
	}

	/**
	 * Calculate the interval time from one iteration of the loop to the next.
	 * Note that first time through, cLoop is 0, and has not gone through
	 * the body of the loop yet.  Use it to save the start time.
	 * After the first iteration, take the average time and convert it to
	 * seconds for use as interval time.
	 */
	private void calcInterval(long cLoop) {
		if (cLoop == 0) {
			// First time through, set an initial tInterval time and
			// record start time
			tInterval = 0.0055;
			tCalcStart = System.currentTimeMillis();
		} else {
			// Take average of number of times through the loop and
			// use for interval time.
			tInterval = (System.currentTimeMillis() - tCalcStart)/(cLoop*1000.0);
		}
	}

	private double gyroSpeed, gyroAngle; // originally local variables
	private double motorSpeed; // originally local variable

	//---------------------------------------------------------------------
	// 
	// This is the main balance thread for the robot.
	//
	// Robot is assumed to start leaning on a wall.  The first thing it
	// does is take multiple samples of the gyro sensor to establish and
	// initial gyro offset.
	//
	// After an initial gyro offset is established, the robot backs up
	// against the wall until it falls forward, when it detects the
	// forward fall, it start the balance loop.
	//
	// The main state variables are:
	// gyroAngle  This is the angle of the robot, it is the results of
	//            integrating on the gyro value.
	//            Units: degrees
	// gyroSpeed  The value from the Gyro Sensor after offset subtracted
	//            Units: degrees/second
	// motorPos   This is the motor position used for balancing.
	//            Note that this variable has two sources of input:
	//             Change in motor position based on the sum of
	//             MotorRotationCount of the two motors,
	//            and,
	//             forced movement based on user driving the robot.
	//            Units: degrees (sum of the two motors)
	// motorSpeed This is the speed of the wheels of the robot based on the
	//            motor encoders.
	//            Units: degrees/second (sum of the two motors)
	//
	// From these state variables, the power to the motors is determined
	// by this linear equation:
	//     power = KGYROSPEED * gyro +
	//             KGYROANGLE * gyroAngle +
	//             KPOS       * motorPos +
	//             KSPEED     * motorSpeed;
	//
	public void run() {

		int power;
		long tMotorPosOK;
		long cLoop = 0;
		
		LCD.clear();
		LCD.drawString("leJOS NXJ Ballbot", 0, 1);
		LCD.drawString("Balancing", 0, 4);

		tMotorPosOK = System.currentTimeMillis();

		// Reset the motors to make sure we start at a zero position
		my_motor.resetTachoCount();
		//right_motor.resetTachoCount();

		// NOTE: This balance control loop only takes 1.128 MS to execute each loop in leJOS NXJ.
		while(true) {
			calcInterval(cLoop++);

			updateGyroData();

			updateMotorData();

			// Apply the drive control value to the motor position to get robot to move.
			motorPos -= motorControlDrive * tInterval;

			// This is the main balancing equation
			power = (int)((KGYROSPEED * gyroSpeed +               // Deg/Sec from Gyro sensor
					KGYROANGLE * gyroAngle) / ratioWheel + // Deg from integral of gyro
					KPOS       * motorPos +                 // From MotorRotaionCount of both motors
					KDRIVE     * motorControlDrive +        // To improve start/stop performance
					KSPEED     * motorSpeed);                // Motor speed in Deg/Sec

			if (Math.abs(power) < 100)
				tMotorPosOK = System.currentTimeMillis();

			steerControl(power); // Movement control. Not used for balancing.

			// Apply the power values to the motors
			// NOTE: It would be easier/faster to use MotorPort.controlMotorById(), but it needs to be public.
			my_motor.setPower(Math.abs(powerLeft));
			//right_motor.setPower(Math.abs(powerRight));

			if(powerLeft > 0) my_motor.forward(); 
			else my_motor.backward();

			//if(powerRight > 0) right_motor.forward(); 
			//else right_motor.backward();

			// Check if robot has fallen by detecting that motorPos is being limited
			// for an extended amount of time.
			if ((System.currentTimeMillis() - tMotorPosOK) > TIME_FALL_LIMIT) break;
			
			try {Thread.sleep(WAIT_TIME);} catch (InterruptedException e) {}
		} // end of while() loop
		
		my_motor.flt();
		//right_motor.flt();

		Sound.beepSequenceUp();
		LCD.drawString("Oops... I fell", 0, 4);
		LCD.drawString("tInt ms:", 0, 8);
		LCD.drawInt((int)tInterval*1000, 9, 8);
	} // END OF BALANCING THREAD CODE

	/**
	 * This method does not actually 
	 * apply direct power to the wheels. Control is filtered through to each wheel, allowing the robot to 
	 * drive forward/backward and make turns. Higher values are faster. Negative values cause the wheel
	 * to rotate backwards. Values between -200 and 200 are good. If values are too high it can make the
	 * robot balance unstable.
	 * 
	 * @param impulse_power The relative control power to the wheel. -200 to 200 are good numbers.
	 * 
	 */
	
	private void wheelDriver(int impulse_power) {
		// Set control Drive and Steer.  Both these values are in motor degree/second
		motorControlDrive = (impulse_power + impulse_power) * CONTROL_SPEED / 200.0;
	}
	
	/**
	 * <p>Causes movement along either the xaxis or y axis. Normally power for each of these values is
	 * zero in order to keep the ballbot roughly stationary.</p>
	 * 
	 * <p>This method does not actually 
	 * apply direct power to the roller wheels. Control is filtered through to each wheel, allowing the robot to 
	 * move. Higher values are faster. Negative values cause movement in the opposite direction. Values between -200 
	 * and 200 are acceptable. If values are too high it can make the robot balance unstable. Try starting with values 
	 * around 10 or so. A ballbot needs good motors, sensors, a grippy ball/roller and be extremely symmetrical if you 
	 * want to use higher values. </p>
	 * 
	 * @param x_axisPower
	 * @param y_axisPower
	 */
	public void impulseMove(int x_axisPower, int y_axisPower) {
		threadx.wheelDriver(x_axisPower);
		thready.wheelDriver(y_axisPower);
	}	
}