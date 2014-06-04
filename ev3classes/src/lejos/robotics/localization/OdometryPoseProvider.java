package lejos.robotics.localization;

import lejos.robotics.SampleProvider;
import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;

/**
 * <p>A PoseProvider keeps track of the robot {@link lejos.robotics.navigation.Pose}.
 * It does this using odometry (dead reckoning)
 * data contained in a {@link lejos.robotics.navigation.Move}, which is  supplied by a {@link
 *lejos.robotics.navigation.MoveProvider}. When the PoseProivder  is constructed, it registers
 * as listener with its MoveProvider,
 */

public class OdometryPoseProvider implements PoseProvider, MoveListener, SampleProvider
{

  private float x = 0, y = 0, heading = 0;
  private float angle0, distance0;
  MoveProvider mp;
  boolean current = true;

  /**
   *Allocates a new OdometryPoseProivder and registers it  with the  MovePovider as a listener.
   */
  public OdometryPoseProvider(MoveProvider mp)
  {
    mp.addMoveListener(this);
  }

  /**
   * returns  a new pose that represents the current location and heading of the robot.
   * If called while the robot is moving, the PoseProvider will get updated odometry
   * data from its MoveProvider
   * @return pose
   */
  public synchronized Pose getPose()
  {
    if (!current )
    {
      updatePose(mp.getMovement());
    }
    return new Pose(x, y, heading);
  }

  /**
   * called by a MoveProvider when movement starts
   * @param move - the event that just started
   * @param mp the MoveProvider that called this method
   */
  public synchronized void moveStarted(Move move, MoveProvider mp)
  {
    angle0 = 0;
    distance0 = 0;
    current = false;
    this.mp = mp;
  }
  
  public synchronized void   setPose(Pose aPose )
  {
    setPosition(aPose.getLocation());
    setHeading(aPose.getHeading());
  }
  
  /**
   * called by a MoveProvider when movement ends
   * @param move - the event that just started
   * @param mp
   */
  public void moveStopped(Move move, MoveProvider mp)
  {
    updatePose(move);
  }

  /*
   * Update the pose with the incremental movement that has occurred since the
   * movementStarted 
   */
  private synchronized void  updatePose(Move event)
  {
    float angle = event.getAngleTurned() - angle0;
    float distance = event.getDistanceTraveled() - distance0;
    double dx = 0, dy = 0;
    double headingRad = (Math.toRadians(heading));

    if (event.getMoveType() == Move.MoveType.TRAVEL   || Math.abs(angle)<0.2f)
    {
      dx = (distance) * (float) Math.cos(headingRad);
      dy = (distance) * (float) Math.sin(headingRad);
    }
    else if(event.getMoveType() == Move.MoveType.ARC)
    {
      double turnRad = Math.toRadians(angle);
      double radius = distance / turnRad;
      dy = radius * (Math.cos(headingRad) - Math.cos(headingRad + turnRad));
      dx = radius * (Math.sin(headingRad + turnRad) - Math.sin(headingRad));
    }
    x += dx;
    y += dy;
    heading = normalize(heading + angle); // keep angle between -180 and 180
    angle0 = event.getAngleTurned();
    distance0 = event.getDistanceTraveled();
    current = !event.isMoving();
  }

  /*
   * returns equivalent angle between -180 and +180
   */
  private float normalize(float angle)
  {
    float a = angle;
    while (a > 180)a -= 360;
    while (a < -180) a += 360;
    return a;
  }

  private void setPosition(Point p)
  {
    x = p.x;
    y = p.y;
    current = true;
  }

  private void setHeading(float heading)
  {
    this.heading = heading;
    current = true;
  }

	@Override
	public int sampleSize() {
		return 3;
	}
	
	@Override
	public void fetchSample(float[] sample, int offset) {
	    if (!current )
	    {
	      updatePose(mp.getMovement());
	    }
	    sample[offset+0] = x;
	    sample[offset+1] = y;
	    sample[offset+2] = heading;
	}
}
