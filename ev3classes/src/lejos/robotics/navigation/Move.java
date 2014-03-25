package lejos.robotics.navigation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.robotics.Transmittable;

/**
 * Models a movement performed by a pilot
 * 
 * @author Lawrie Griffiths
 *
 */
public class Move implements Transmittable {
	/**
	 * The type of  movement made in sufficient detail to allow errors
	 * in the movement to be modeled.
	 */
	public enum MoveType {TRAVEL, ROTATE, ARC, STOP}
	private float distanceTraveled, angleTurned;
	private MoveType moveType;
	private float arcRadius = Float.POSITIVE_INFINITY;
	private boolean isMoving;
	private long timeStamp;
	private float travelSpeed, rotateSpeed;
	
	/**
	 * Create a movement object to record a movement made by a pilot.
         * This method automatically calculates the
	 * MoveType based on the data as follows:<br>
	 * <li>(distance NOT 0) AND (angle NOT 0) --> ARC
	 * <li>(distance = 0) AND (angle NOT 0) --> ROTATE
	 * <li>(distance NOT 0) AND (angle = 0) --> TRAVEL
	 * <li>(distance = 0) AND (angle = 0) --> STOP
	 * @param distance the distance traveled in pilot units
	 * @param angle the angle turned in degrees
	 * @param isMoving true iff the movement was created while the robot was moving
	 */
  public Move(float distance, float angle, boolean isMoving)
  {
		this.moveType = Move.calcMoveType(distance, angle);
		this.distanceTraveled = distance;
		this.angleTurned = angle;
		this.isMoving = isMoving;
		// TODO: This works fine, but could use convertDistanceToAngle() instead here?
		if (Math.abs(angle) > 0.5) {
			double turnRad = Math.toRadians(angle);
			arcRadius = (float) (distance / turnRad);
		}
		this.timeStamp = System.currentTimeMillis();
	}

  /**
   * Create a movement object to record a movement made by a pilot.
   * @param type the movement type
   * @param distance the distance traveled in pilot units
   * @param angle the angle turned in degrees
   * @param travelSpeed the travel speed
   * @param rotateSpeed the rotate speed
   * @param isMoving true iff the movement was created while the robot was moving
   */
  public Move(MoveType type, float distance, float angle, float travelSpeed, float rotateSpeed, boolean isMoving)
  {
    this.moveType = type;
    this.distanceTraveled = distance;
    this.angleTurned = angle;
    if (Math.abs(angle) > 0.5) 
    {
		double turnRad = Math.toRadians(angle);
		arcRadius = (float) (distance / turnRad);
	}
    this.travelSpeed = travelSpeed;
    this.rotateSpeed = rotateSpeed;
    this.isMoving = isMoving;
    this.timeStamp = System.currentTimeMillis();
  }

  /**
   * Create a movement object to record a movement made by a pilot.
   * @param type the movement type
   * @param distance the distance traveled in pilot units
   * @param angle the angle turned in degrees
   * @param isMoving true iff the movement was created while the robot was moving
   */
  public Move(MoveType type, float distance, float angle, boolean isMoving)
  {
	  this(type,distance,angle,0f,0f,isMoving);
  }
  

  /**
   * use this method to recycle an existing Move instead of creating a new one
   * @param distance
   * @param angle
   * @param isMoving
   */
  public void setValues(MoveType type, float distance, float angle,boolean isMoving)
  {
    this.moveType = Move.calcMoveType(distance, angle);
	this.distanceTraveled = distance;
	this.angleTurned = angle;
	this.isMoving = isMoving;
	// TODO: This works fine, but could use convertDistanceToAngle() instead here?
	if (Math.abs(angle) > 0.5) 
	{
		double turnRad = Math.toRadians(angle);
		arcRadius = (float) (distance / turnRad);
	}
	this.timeStamp = System.currentTimeMillis();
  }

	/**
	 * Helper method to calculate the MoveType based on distance, angle, radius parameters.
	 * 
	 * @param distance
	 * @param angle
	 * @return
	 */
	private static MoveType calcMoveType(float distance, float angle) {
		if(distance == 0 & angle == 0) return MoveType.STOP;
		else if(distance != 0 & angle == 0) return MoveType.TRAVEL;
		else if(distance == 0 & angle != 0) return MoveType.ROTATE;
		else return MoveType.ARC;
	}
	
	/**
	 * Alternate constructor that uses angle and turn radius instead. Useful for constructing arcs, but it
	 * can't represent a straight line of travel with a set distance (use the other constructor to specify distance).
	 *  This method automatically calculates the MoveType based on the data as follows:<br>
	 * <li>(radius NOT 0) AND (angle NOT 0) --> ARC
	 * <li>(radius = 0) AND (angle NOT 0) --> ROTATE
	 * <li>(radius = 0) AND (angle = 0) --> STOP
	 * <li>(radius = +infinity) AND (angle = 0) --> TRAVEL
	 * <li>NOTE: can't calculate distance based only on angle and radius, therefore distance can't be calculated and will equal NaN)
	 * @param isMoving
	 * @param angle
	 * @param turnRadius
	 */
	public Move(boolean isMoving, float angle, float turnRadius) {
		this.distanceTraveled = Move.convertAngleToDistance(angle, turnRadius);
		this.moveType = Move.calcMoveType(this.distanceTraveled, angle);
		this.angleTurned = angle;
		this.isMoving = isMoving;
		arcRadius = turnRadius;
		this.timeStamp = System.currentTimeMillis();
	}

	/**
	 * Get the distance traveled. This can be in a straight line or an arc path.
	 * 
	 * @return the distance traveled
	 */
	public float getDistanceTraveled() {
		return distanceTraveled;
	}
	
	/**
	 * The time stamp is the system clock at the time the Move object is created. It is set automatically
	 * in the Move constructor using {@link System#currentTimeMillis()} 
	 * @return Time stamp in milliseconds.
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Get the angle turned by a rotate or an arc operation.
	 * 
	 * @return the angle turned
	 */
	public float getAngleTurned() {
		return angleTurned;
	}
	
	/**
	 * Get the type of the movement performed
	 * 
	 * @return the movement type
	 */
	public MoveType getMoveType() {
		return moveType;
	}
	
	/**
	 * Get the radius of the arc
	 * 
	 * @return the radius of the arc
	 */
	public float getArcRadius() {
		return arcRadius;
	}
	
	/**
	 * Get the travel speed
	 * @return the travel speed
	 */
	public float getTravelSpeed() {
		return travelSpeed;
	}
	
	/**
	 * Get the rotate speed
	 * @return the rotate speed
	 */
	public float getRotateSpeed() {
		return rotateSpeed;
	}
		
	/**
	 * Test if move was in progress
	 * 
	 * @return true iff the robot was moving when this Move object was created
	 */
	public boolean isMoving() {
		return isMoving;
	}
	
	/**
	 * Static utility method for converting distance (given turn radius) into angle.
	 * @param distance
	 * @param turnRadius
	 * @return angle
	 */
	public static float convertDistanceToAngle(float distance, float turnRadius){
		return (float)((distance * 360) / (2 * Math.PI * turnRadius));
	}
	
	/**
	 * Static utility method for converting angle (given turn radius) into distance.
	 * @param angle
	 * @param turnRadius
	 * @return distance
	 */
	public static float convertAngleToDistance(float angle, float turnRadius){
		return (float)((angle * 2 * Math.PI * turnRadius) / 360);
	}

	public void dumpObject(DataOutputStream dos) throws IOException {
		dos.writeByte(moveType.ordinal());
		dos.writeFloat(travelSpeed);
		dos.writeFloat(rotateSpeed);
		dos.writeFloat(distanceTraveled);
		dos.writeFloat(angleTurned);
		dos.writeFloat(arcRadius);
		dos.flush();
	}

	public void loadObject(DataInputStream dis) throws IOException {
		moveType = MoveType.values()[dis.readByte()];
		travelSpeed = dis.readFloat();
		rotateSpeed = dis.readFloat();
		distanceTraveled = dis.readFloat();
		angleTurned = dis.readFloat();
		arcRadius = dis.readFloat();
	}
	
	@Override
	public String toString() {
		String s = moveType.name() + " ";
		switch(moveType) {
		case ROTATE: 
			s += angleTurned + " at " + rotateSpeed;
			break;
		case TRAVEL:
			s += distanceTraveled + " at " + travelSpeed;
			break;
		case ARC:
			s += " of " + arcRadius + " for " + angleTurned + "degrees  at " + travelSpeed;
			break;
		}
		return s;
	}
}
