package lejos.robotics;

import lejos.utility.Delay;

/**
 * Implementation of RangeScanner with a rotating ultrasonic sensor or other range finder
 * @author Roger Glassey
 */
public class RotatingRangeScanner implements RangeScanner
{

  /**
   * The constructor specifies the motor and range finder used
   * @param head the motor that rotates the sensor
   * @param rangeFinder the range finder
   */
  public RotatingRangeScanner(RegulatedMotor head, RangeFinder rangeFinder)
  {
	  this(head,rangeFinder,1);
  }
  
  public RotatingRangeScanner(RegulatedMotor head, RangeFinder rangeFinder, int gearRatio) {
	  this.head = head;
	  this.rangeFinder = rangeFinder;
	  this.gearRatio = gearRatio;
	  head.resetTachoCount();
  }
  
  /**
   * Set the gear ratio
   * 
   * @param gearRatio the gear ratio
   */
  public void setGearRatio(int gearRatio) 
  {
	  this.gearRatio = gearRatio;
  }
  
  /**
   * Set the head motor
   * 
   * @param motor the head motor
   */
  public void setHeadMotor(RegulatedMotor motor) {
	  head = motor;
  }
  
  /**
   * Returns a set of Range Readings taken the angles specified.
   * @return the set of range values
   */
  public RangeReadings getRangeValues()
  {
    if (readings == null || readings.getNumReadings() != angles.length)
    {
      readings = new RangeReadings(angles.length);
    }

    for (int i = 0; i < angles.length; i++)
    {
      head.rotateTo(((int) angles[i]) * gearRatio);
      Delay.msDelay(50);
      float range = rangeFinder.getRange() + ZERO;
      if (range > MAX_RELIABLE_RANGE_READING)
      {
        range = -1;
      }
      readings.setRange(i, angles[i], range);
    }
    head.rotateTo(0);
    return readings;
  }

  /**
   * set the angles to be used by the getRangeValues() method
   * @param angles
   */
  public void setAngles(float[] angles)
  {
    this.angles = angles.clone();
  }

/**
 * returns the rangeFinder - allows other objects to get a range value.
 * @return the range finder
 */
  public RangeFinder getRangeFinder()
  {
    return rangeFinder;
  }

  protected final int MAX_RELIABLE_RANGE_READING = 180;
  protected final int ZERO = 2;// correction of sensor zero
  protected RangeReadings readings;
  protected RangeFinder rangeFinder;
  protected RegulatedMotor head;
  protected float[] angles ={0,90};// default
  protected int gearRatio;
}
