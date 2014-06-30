package lejos.robotics.navigation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.robotics.Transmittable;
import lejos.robotics.geometry.Point;

/**
 * Represents the location and heading(direction angle) of a robot.<br>
 * This class includes  methods for updating the Pose to in response to basic robot
 * movements.
  * It also contains utility  methods for use in navigation, such as the
 * direction and distance to a point from the location of the pose, and also the
 * location of a point at a given distance and direction from the location of the pose.<br>
 * All directions and angles are in degrees and use the standard convention
 * in mathematics: direction 0 is parallel to the X axis, and direction +90 is
 * parallel to the Y axis. <br>
 * @author Roger Glassey
 */
public class Pose implements Transmittable
{
  /**
   * allocate a new Pose at the origin, heading  = 0:the direction  the positive X axis
   */
public Pose()
{
  _location = new Point(0,0);
  _heading = 0;
}
/**
 * Allocate a new pose at location (x,y) with specified heading in degrees.
 * 
 * @param x the X coordinate
 * @param y the Y coordinate
 * @param heading the heading
 */
public Pose(float x, float y, float heading)
{
  _location = new Point(x,y);
  _heading = heading;
}
/**
 * Rotate the heading through the specified angle
 * 
 * @param angle
 */
public void rotateUpdate(float angle)
{
  _heading += angle;
  while(_heading < 180)_heading += 360;
  while(_heading > 180)_heading -= 360;
}

/**
 * Move the specified distance in the direction of current heading.
 * 
 * @param distance to move
 */
public void moveUpdate(float distance)
{
  float x = distance * (float)Math.cos(Math.toRadians(_heading));
  float y = distance * (float)Math.sin(Math.toRadians(_heading));
  _location.translate(x,y);
}
/**
 * Change the x and y coordinates of the pose by adding dx and dy.
 * 
 * @param dx  change in x coordinate
 * @param dy  change in y coordinate
 */
public void translate( float dx, float dy)
{
    _location.translate(dx,dy);
}
/**
 * Sets the pose location and heading to the correct values resulting from travel
 * in a circular arc.  The radius is calculated from the distance and turn angle
 * 
 * @param distance the distance traveled
 * @param turnAngle the angle turned
 */
public void arcUpdate(float distance, float turnAngle)
{
  float dx = 0;
  float dy = 0;
  double heading = (Math.toRadians(_heading));
  if (Math.abs(turnAngle) > .5)
  {
    float turn = (float) Math.toRadians(turnAngle);
    float radius = distance / turn;
    dy = radius * (float) (Math.cos(heading) - Math.cos(heading + turn));
    dx = radius * (float) (Math.sin(heading + turn) - Math.sin(heading));
  } else if (Math.abs(distance) > .01)
  {
    dx = distance * (float) Math.cos(heading);
    dy = distance * (float) Math.sin(heading);
  }
  _location.translate( dx, dy);
  rotateUpdate(turnAngle);
}
/**
 * Returns the angle with respect to the X axis  to <code. destination </code> from the
 * current location of this pose.
 * @param destination
 * @return angle in degrees
 */
public float angleTo(Point destination)
{
  return _location.angleTo(destination);
}
/**
 * Returns the angle to <code>destination</code> relative to the pose heading;
 * @param destination  the target point
 * @return the relative bearing of the destination, between -180 and 180
 */
public float relativeBearing(Point destination)
{
  float bearing = angleTo(destination) - _heading;
  if(bearing < -180)bearing +=360;
  if(bearing > 180)bearing -= 360;
  return bearing;
}
/**
 * Return the distance to the destination

 * @param destination
 * @return  the distance
 */
public float distanceTo(Point destination)
{
  return (float) _location.distance(destination);
}
/**
 * Returns the point at <code> distance </code> from the location of this pose,
 * in the direction  <code>bearing</code> relative to the X axis.
 * @param distance  the distance to the point
 * @param bearing  the true bearing of the point
 *  @return point
 */
public Point pointAt(float distance, float bearing)
{ 
  return _location.pointAt(distance, bearing);
}

/**
 * returns the heading (direction angle) of the Pose
 * 
 * @return the heading
 */
public float getHeading() { return _heading ; }
/**
 * Get the X coordinate
 * 
 * @return the X coordinate
 */
public float getX(){ return (float) _location.getX();}
/**
 * Get the Y coordinate
 * 
 * @return the Y coordinate
 */
public float getY() {return (float)_location.getY();}

/**
 * Get the location as a Point
 * 
 * @return the location as a point
 */
//TODO: Maybe call it getPoint()?
public Point getLocation() { return _location;}

/**
 * Set the location of the pose
 * 
 * @param p the new location
 */
public void setLocation(Point p)
{
  _location = p;
}

/**
 * Sets the location of this pose to a new point at x,y;
 * @param x
 * @param y
 */
public void setLocation(float x, float y)
{
  setLocation(new Point(x,y));
}

public void setHeading(float heading )
{
  _heading = heading;
}
/**
 * return string contains x,y and heading
 * @return x,y,heading
 */
@Override
public String toString()
{
  return("X:"+_location.x+" Y:"+_location.y+" H:"+_heading);
}

public void dumpObject(DataOutputStream dos) throws IOException {
	dos.writeFloat(_location.x);
	dos.writeFloat(_location.y);
	dos.writeFloat(_heading);
	dos.flush();
}

public void loadObject(DataInputStream dis) throws IOException {
	_location = new Point(dis.readFloat(), dis.readFloat());
	_heading = dis.readFloat();
}

protected  Point _location;
protected  float _heading;

}

