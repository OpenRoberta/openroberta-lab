package lejos.robotics;

import lejos.robotics.navigation.RotateMoveController;
import lejos.utility.Delay;

/**
 * @author Roger Glassey
 */
public class FixedRangeScanner implements RangeScanner
{
  public FixedRangeScanner(RotateMoveController aPilot, RangeFinder rangeFinder)
  {
    pilot = aPilot;
    this.rangeFinder = rangeFinder;;
  }

  /**
   * Return a set of range readings determined taken at the relative bearings
   * defined in the angles  array;
   * The robot rotates back to its original heading at the end.
   * @return the set of range readings
   */
  public RangeReadings getRangeValues()
  {
   if (readings == null || readings.getNumReadings() != angles.length)
    {
      readings = new RangeReadings(angles.length);
    }
//    RConsole.println("angles "+ angles[0]+" "+angles[1]);
    for (int i = 0; i < angles.length; i++)
    {
      float angle;
      if(i == 0 )angle = angles[0];
      else angle = angles[i]-angles[i-1];
      pilot.rotate(normalize(angle));
      Delay.msDelay(50);
      float range = rangeFinder.getRange() + ZERO;
      if (range > MAX_RELIABLE_RANGE_READING)
      {
        range = -1;
      }
      readings.setRange(i, angles[i], range);
    }
    pilot.rotate(normalize(-angles[angles.length -1]));
    return readings;
    }

  /**
   *
   * @param angleSet
   */
  public void setAngles(float [] angleSet)
  {
    angles = angleSet;
  }

  private float normalize(float angle )
  {
    if(angle < -180) angle += 360;
    if(angle > 180 )angle -= 360;
    return angle;
  }

  /**
 * returns the rangeFinder - allows other objects to get a range value.
 * @return the range finder
 */
  public RangeFinder getRangeFinder()
  {
    return rangeFinder;
  }

  protected RangeFinder rangeFinder;
  protected RotateMoveController pilot;
  protected float[] angles;
  protected final int MAX_RELIABLE_RANGE_READING = 180;
  protected final int ZERO = 2;// correction of sensor zero
  protected RangeReadings readings;;
}

