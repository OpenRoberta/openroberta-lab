package lejos.robotics.navigation;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.DirectionFinder;
import lejos.utility.Delay;

/**
 * A Pilot that keeps track of direction using a DirectionFinder.
 * @deprecated  This class will disappear in NXJ version 1.0. Compass should be added to a PoseProvider. 
 * @see lejos.robotics.localization.PoseProvider#getPose()
 */
// TODO: Note @deprecated message above, I'm not sure PoseProvider is exactly right place to point users to yet.
// Need to explain this more when we are sure how this will replace CompassPilot. - BB

@Deprecated
public class CompassPilot extends DifferentialPilot
{

  protected DirectionFinder compass;
  protected Regulator regulator = new Regulator(); // inner regulator for thread
  protected float _heading; // desired heading
  protected float _estimatedHeading = 0; //estimated heading
  protected boolean _traveling = false; // state variable used by regulator
  protected float _distance; // set by travel()  used by r egulator to stop
  protected byte _direction;// direction of travel = sign of _distance
  protected float _heading0 = 0;// heading when rotate immediate is called
 

  /**
   *returns returns  true if the robot is travelling for a specific distance;
   **/
  public boolean isTraveling()
  {
    return _traveling;
  }

  /**
   * Allocates a CompasPilot object, and sets the physical parameters of the NXT robot. <br>
   *  Assumes  Motor.forward() causes the robot to move forward);
   * Parameters
   * @param compass :  a compass sensor;
   * @param wheelDiameter Diameter of the tire, in any convenient units.  (The diameter in mm is usually printed on the tire).
   * @param trackWidth Distance between center of right tire and center of left tire, in same units as wheelDiameter
   * @param leftMotor
   * @param rightMotor
   */
  public CompassPilot(DirectionFinder compass, float wheelDiameter,
          float trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor)
  {
    this(compass, wheelDiameter, trackWidth, leftMotor, rightMotor, false);
  }

  /**
   * Allocates a CompasPilot object, and sets the physical parameters of the NXT robot. <br>
   *  Assumes  Motor.forward() causes the robot to move forward);
   * Parameters
   * @param compass :  a compass sensor;
   * @param wheelDiameter Diameter of the tire, in any convenient units.  (The diameter in mm is usually printed on the tire).
   * @param trackWidth Distance between center of right tire and center of left tire, in same units as wheelDiameter
   * @param leftMotor
   * @param rightMotor
   * @param reverse if true of motor.forward() drives the robot backwards
   */
  public CompassPilot(DirectionFinder compass, float wheelDiameter,
          float trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor, boolean reverse)
  {
    super(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
    this.compass = compass;
    regulator.setDaemon(true);
    regulator.start();

  }

  /**
   * Return the compass
   * @return the compass
   */
  public DirectionFinder getCompass()
  {
    return compass;
  }

  /**
   * Returns the change in robot heading since the last call of reset()
   * normalized to be within -180 and _180 degrees
   */
  public float getAngleIncrement()
  {
    return normalize(getCompassHeading() - _heading0);
  }

  /**
   * Returns  direction of desired robot facing
   */
  public float getHeading()
  {
    return _estimatedHeading;
  }

  /**
   * Method returns the current compass heading
   * @return Compass heading in degrees.
   */
  public float getCompassHeading()
  {
    return normalize(compass.getDegreesCartesian());
  }

  /**
   * sets  direction of desired robot facing in degrees
   */
  public void setHeading(float angle)
  {
    _heading = angle;
  }

  /**
   * Rotates the robot 360 degrees while calibrating the compass
   * resets compass zero to heading at end of calibration
   */
  public synchronized void calibrate()
  {
    setRotateSpeed(50);
    compass.startCalibration();
    super.rotate(360, false);
    compass.stopCalibration();

  }

  public void resetCartesianZero()
  {
    compass.resetCartesianZero();
    _heading = 0;
  }

  /**
   * Determines the difference between actual compass direction and desired  heading in degrees
   * @return error (in degrees)
   */
  public float getHeadingError()
  {
    float err = compass.getDegreesCartesian() - _heading;
    // Handles the wrap-around problem:
    return normalize(err);

  }


  /**
   * Moves the NXT robot a specific distance. A positive value moves it forwards and
   * a negative value moves it backwards. The robot steers to maintain its compass heading.
   * @param distance The positive or negative distance to move the robot, same units as _wheelDiameter
   * @param immediateReturn iff true, the method returns immediately.
   */
  public void travel(float distance, boolean immediateReturn)
  {
    movementStart();
    _type = Move.MoveType.TRAVEL;
    super.travel(distance,true);
    _distance = distance;
     _direction = 1;
     if(_distance < 0 ) _direction = -1;
    _traveling = true;
    if (immediateReturn)
    {
      return;
    }
    while (isMoving())
    {
      Thread.yield(); // regulator will call stop when distance is reached
    }

  }

  /**
   * Moves the NXT robot a specific distance;<br>
   * A positive distance causes forward motion;  negative distance  moves backward.
   * Robot steers to maintain its compass heading;
   * @param  distance of robot movement. Unit of measure for distance must be same as wheelDiameter and trackWidth
   **/
  public void  travel(float distance)
  {
    travel(distance, false);
  }

  /**
   * robot rotates to the specified compass heading;
   * @param  immediateReturn  - if true, method returns immediately.
   * Robot stops when specified angle is reached or when stop() is called
   */
  public void  rotate(float angle, boolean immediateReturn)
  {
    movementStart();
    _type = Move.MoveType.ROTATE;
    float heading0 = getCompassHeading();
    super.rotate(angle, immediateReturn); // takes care of movement start
    if (immediateReturn) return;
    _heading = normalize(_heading +angle);
    _traveling = false;
    float error = getHeadingError();
    while (Math.abs(error) > 2)
    {
      super.rotate(-error, false);
      error = getHeadingError();
    }
    _heading0 = heading0;//needed for currect angle increment
  }

  /**
   * Rotates the  NXT robot through a specific angle; Rotates left if angle is positive, right if negative,
   * Returns when angle is reached.
   * Wheels turn in opposite directions producing a  zero radius turn.
   * @param angle  degrees. Positive angle rotates to the left (clockwise); negative to the right. <br>Requires correct values for wheel diameter and track width.
   */
  public void rotate(float angle)
  {
     rotate(angle, false);
  }

  public void reset()
  {
    _left.resetTachoCount();
    _right.resetTachoCount();
    _heading0 = getCompassHeading();
    super.reset();
  }

  // methods required to give regulator access to Pilot superclass
  protected void stopNow()
  {
    stop();
  }

  /**
   * Stops the robot soon after the method is executed. (It takes time for the motors
   * to slow to a halt)
   */
  public void stop()
  {
    super.stop();
    _traveling = false;
    while (isMoving())
    {
      super.stop();
      Thread.yield();
    }
  }


  protected float normalize(float angle)
  {
    while (angle > 180)angle -= 360;
    while (angle < -180)angle += 360;
    return angle;
  }

  /**
   * inner class to regulate heading during travel move
   * Proportional control of steering ; error is an average of heading change
   * from tacho counts and from compass change
   * @author Roger Glassey
   */
  class Regulator extends Thread
  {

    public void run()
    {
      while (true)
      {
        while (!_traveling)
        {
          Thread.yield();
        }
        {  // travel started
          float toGo =  _distance;  // reamining trave distance
          float gain = -3f * _direction;
          float error = 0;
          float e0 = 0;
          float incr0 = 0;
         _estimatedHeading = _heading0;
        do // travel in progress
          {
           // use weighted average of heading from tacho count and compass
          // weights should be based on  variance of compass error and  tacho count error
            float incr = getAngleIncrement();
            _estimatedHeading += (incr - incr0); //change in heading from tacho counts
            incr0 =  incr;           
             _estimatedHeading = normalize( 0.5f *normalize(compass.getDegreesCartesian())  + 0.5f*_estimatedHeading);
            error = normalize( _estimatedHeading - _heading);
           toGo =(_distance - getMovementIncrement());                        
            if(Math.abs(error - e0) > 2)  //only steer if large change in error > 2 deg
            {
              steerPrep(gain * error);
              e0 = error;
            }
            Delay.msDelay(12);  // another arbitrary constant
           Thread.yield();
          } while (Math.abs(toGo) > 3 );
         // travel completed (almost)
          int delta = Math.round(toGo*_leftDegPerDistance);
          _left.rotate(delta,true);
          delta = Math.round(toGo*_rightDegPerDistance);
          _outside.rotate(delta);
          while(_left.isMoving())Thread.yield();
          _traveling = false;
        }
        
      }
    }
  }
}
