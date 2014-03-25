package lejos.robotics.localization;

import java.util.Random;
import lejos.robotics.*;
import lejos.robotics.geometry.*;
import lejos.robotics.mapping.RangeMap;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;

/**
 * Represents a particle for the particle filtering algorithm. The state of the
 * particle is the pose, which represents a possible pose of the robot.
 * 
 * The weight for a particle is set by taking a set of theoretical range readings using a
 * map of the environment, and comparing these ranges with those taken by the
 * robot. The weight represents the relative probability that the robot has this
 * pose. Weights are from 0 to 1.
 * 
 * @author Lawrie Griffiths
 * 
 */
public class MCLParticle {
  private static Random rand = new Random();
 
  // Instance variables (kept to minimum to allow maximum number of particles)
  private Pose pose;
  private float weight = 1;
  private  static  boolean debug = false;

  public static void setDebug(boolean yes)
  {
    debug = yes;
  }
  /**
   * Create a particle with a specific pose
   * 
   * @param pose the pose
   */
  public MCLParticle(Pose pose) {
    this.pose = pose;
  }

  /**
   * Set the weight for this particle
   * 
   * @param weight the weight of this particle
   */
  public void setWeight(float weight) {
    this.weight = weight;
  }

  /**
   * Return the weight of this particle
   * 
   * @return the weight
   */
  public float getWeight() {
    return weight;
  }

  /**
   * Return the pose of this particle
   * 
   * @return the pose
   */
  public Pose getPose() {
    return pose;
  }

  /**
   * Calculate the weight for this particle by comparing its readings with the
   * robot's readings
   * 
   * @param rr Robot readings
   */
  public void calculateWeight(RangeReadings rr, RangeMap map, float divisor) {
    weight = 1;
    Pose tempPose = new Pose();
    tempPose.setLocation(pose.getLocation());
    for (int i = 0; i < rr.getNumReadings(); i++) {
        if(!map.inside(tempPose.getLocation()))
        {
            weight = 0;
            return;
        }
      float angle = rr.getAngle(i);
      tempPose.setHeading(pose.getHeading() + angle);      
      float robotReading = rr.getRange(i);
      float range = map.range(tempPose);
      if (range < 0) {
    	  weight = 0;
          if(debug) System.out.println("zero wt"+tempPose);
    	  return;
      }
      float diff = robotReading - range;
      weight *= (float) Math.exp(-(diff * diff) / divisor);
    }
  }

  /**
   * Get a specific reading
   * 
   * @param i the index of the reading
   * @return the reading
   */
  public float getReading(int i, RangeReadings rr, RangeMap map) {
    Pose tempPose = new Pose();
    tempPose.setLocation(pose.getLocation());
    tempPose.setHeading(pose.getHeading() + rr.getAngle(i));
    return map.range(tempPose);    
  }
  
  public RangeReadings getReadings(RangeReadings rr, RangeMap map) {
	  RangeReadings pr = new RangeReadings(rr.getNumReadings());
	  
      for(int i=0;i<rr.getNumReadings();i++) {
     	 pr.setRange(i,rr.getAngle(i), getReading(i, rr, map));
       }
	  return pr;
  }

  /**
   * Apply the robot's move to the particle with a bit of random noise.
   * Only works for rotate or travel movements.
   * 
   * @param move the robot's move
   */
  public void applyMove(Move move, float distanceNoiseFactor, float angleNoiseFactor) {
    float ym = (move.getDistanceTraveled() * ((float) Math.sin(Math.toRadians(pose.getHeading()))));
    float xm = (move.getDistanceTraveled() * ((float) Math.cos(Math.toRadians(pose.getHeading()))));

    pose.setLocation(new Point((float) (pose.getX() + xm + (distanceNoiseFactor * xm * rand.nextGaussian())),
                     (float) (pose.getY() + ym + (distanceNoiseFactor * ym * rand.nextGaussian()))));
    pose.setHeading((float) (pose.getHeading() + move.getAngleTurned()
        + (angleNoiseFactor  * rand.nextGaussian())));
    pose.setHeading((float) ((int) (pose.getHeading() + 0.5f) % 360));
  }
}
