package lejos.robotics.localization;

import lejos.robotics.mapping.RangeMap;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.RangeReadings;
import lejos.robotics.RangeScanner;
import lejos.robotics.Transmittable;

import java.awt.Rectangle;
import lejos.robotics.localization.PoseProvider;
import java.io.*;
import java.util.ArrayList;

/**
 * Maintains an estimate of the robot pose using sensor data.  It uses Monte Carlo
 * Localization  (See section 8.3 of "Probabilistic Robotics" by Thrun et al. <br>
 * Uses a {@link MCLParticleSet} to represent the probability distribution  of the
 * estimated pose.
 * It uses a {@link lejos.robotics.navigation.MoveProvider} to supply odometry
 * data whenever  a movement is completed,
 * from which the {@link lejos.robotics.navigation.Pose} of each particle is updated.
 * It then uses a {@link  lejos.robotics.RangeScanner} to provide
 * {@link lejos.robotics.RangeReadings}  which are used, together with the
 * {@link lejos.robotics.mapping.RangeMap} to calculate the
 * probability weight of  each {@link MCLParticle} .
 * @author Lawrie Griffiths and Roger Glassey
 */

public class MCLPoseProvider implements PoseProvider, MoveListener, Transmittable
{

  private MCLParticleSet particles;
  private RangeScanner scanner;
  private RangeMap map;
  private int numParticles;
  private float _x, _y, _heading;
  private float minX, maxX, minY, maxY;
  private double varX, varY, varH;
  private boolean updated;
  private Updater updater = new Updater();
  private int border;
  private boolean debug = false;
  private boolean busy = false;
  private float BIG_FLOAT = 1000000f;
  private RangeReadings readings;
  private boolean lost = false;
  private boolean incomplete = true;

  /**
   * Allocates a new MCLPoseProvider.
   * @param mp - the MoveProivder
   * @param scanner - the RangeScanner
   * @param map - the RangeMap
   * @param numParticles number of particles
   * @param border of the map
   */
  public MCLPoseProvider(MoveProvider mp, RangeScanner scanner,
          RangeMap map, int numParticles, int border)
  {
    this.numParticles = numParticles;
    this.border = border;
    if (numParticles > 0 && map != null) {
    	particles = new MCLParticleSet(map, numParticles, border);
    }
    this.scanner = scanner;
    this.map = map;
    if (mp != null) mp.addMoveListener(this);
    updated = false;
    updater.start();
  }
  
  /**
   * Constructor for use on PC 
   * @param map the RangeMap
   * @param numParticles the numbers of particles
   * @param border of the map
   */
  public MCLPoseProvider(RangeMap map, int numParticles, int border) {
	this.numParticles = numParticles;
	this.border = border;
	this.map = map;
	updated = false;
  }
  
  /**
   * Associates a map with the MCLPoseProvider 
   * (for example a map send from the PC).
   * 
   * @param map the RangeMap
   */
  public void setMap(RangeMap map) {
	this.map = map;
  }
  
  /**
   * Generates an  initial particle set in a circular normal distribution, centered
   * on aPose.
   * @param aPose - center of the cloud
   * @param radiusNoise - standard deviation of the radius of the cloud
   * @param headingNoise - standard deviation of the heading;
   */
  public void setInitialPose(Pose aPose, float radiusNoise, float headingNoise)
  {
    _x = aPose.getX();
    _y = aPose.getY();
    _heading = aPose.getHeading();
    particles = new MCLParticleSet(map, numParticles, aPose, radiusNoise, headingNoise);
  }

  /**
   *  Generates an  initial particle set  using the range readings.  The particles
   *  have a significant probability weight given the readings.
   * @param readings
   * @param sigma  range reading noise standard deviation.
   */
  public void setInitialPose(RangeReadings readings,float sigma)
  {
    if(debug) System.out.println("MCLPP set Initial pose called ");
    float minWeight = 0.3f;
    particles = new MCLParticleSet(map, numParticles,border,readings, 2*sigma*sigma,minWeight  );
    updated = true; 
  }

  /**
   * Set debugging on or off
   * @param on true = on, false = off
   */
  public void setDebug(boolean on) {
	debug = on;
  }

 /**
  * set the initial pose cloud with radius noise 1 and heading noise 1
  */
  public void setPose(Pose aPose)
  {
    setInitialPose(aPose, 1, 1);
    updated = true;
  }

  /**
   * Returns the particle set
   * @return the particle set
   */
  public MCLParticleSet getParticles()
  {
    return particles;
  }
  
  /**
   * Get the current range readings
   * 
   * @return the RangeReadings object
   */
  public RangeReadings getReadings() {
	  return readings;
  }
  
  /**
   * Associate a particle set with the MCLPoseProvider
   * (e.g. particles sent from the PC)
   * 
   * @param particles the particle set
   */
  public void setParticles(MCLParticleSet particles) {
	this.particles = particles;
	numParticles = particles.numParticles();
  }

  /**
   * Generate a new particle set, uniformly distributed within the map, and
   * uniformly distributed heading.
   */
  public void generateParticles()
  {
    particles = new MCLParticleSet(map, numParticles, border);
  }
  
  /**
  Required by MoveListener interface; does nothing
   */
  public void moveStarted(Move event, MoveProvider mp) { updated = false;}

  /**
   * Required by MoveListener interface. The pose of each particle is updated
   * using the odometry data of the Move object.
   * @param event the move  just completed
   * @param mp the MoveProvider
   */
  public void moveStopped(Move event, MoveProvider mp)
  {
	if (debug) System.out.println("MCL move stopped");
    updated = false;
    updater.moveStopped(event);
  }

  /**
   * 
   * Calls range scanner to get range readings, calculates the probabilities
   * of each particle from the range  readings and the map and calls resample(()
   *
   * @return true if update was successful
   */
  public boolean  update()
  {
	// if(updated) return true;
    if(debug)System.out.println("MCLPP update called ");
    updated = false;
    if (scanner == null)
    {
      return false;
    }
    readings = scanner.getRangeValues();
    incomplete = readings.incomplete();
    //    if(debug) System.out.println("mcl Update: range readings " + readings.getNumReadings());
    if (incomplete  )
    {
       return false;
    }
    return update(readings);
  }
  
  /**
   * Calculates particle weights from readings, then resamples the particle set;
   * @param readings
   * @return true if update was successful.
   */
  @SuppressWarnings("hiding")
  public boolean update(RangeReadings readings)
  {
    if(debug)System.out.println("MCLPP update readings called ");
    updated = false;
    incomplete = readings.incomplete();
    
    if (incomplete) {
      return false;
    }
        
    if(debug)System.out.println("update readings incomplete "+incomplete);
    boolean goodPose = false;

    goodPose = particles.calculateWeights(readings, map);
    if (debug) System.out.println(" max Weight " + particles.getMaxWeight()+
            " Good pose "+goodPose);

    if (!goodPose) {
      if (debug)  System.out.println("Sensor data improbable from this pose ");
      return false;
    }
    
    goodPose = particles.resample();
    updated = goodPose;
    
    if (debug) {
      if (goodPose) System.out.println("Resample done");
      else System.out.println("Resample failed");
    }
    
    return goodPose;
  }
  
  /**
   * Returns update success flag
   * @return true if update is successful
  */
  public boolean isUpdated() {return updated;}

  /**
   * returns lost status - all particles have very low probability weights
   * @return true if robot is lost
   */
  public boolean isLost() { return lost; }

  /**
   * returns range scanner failure status
   * @return true if range readings are incomplete
   */
  public boolean incompleteRanges() { return incomplete;}

  /**
   * Returns the difference between max X and min X
   * @return the difference between min and max X
   */
  public float getXRange()
  {
    return getMaxX() - getMinX();
  }

  /**
   * Return difference between max Y and min Y
   * @return difference between max and min Y
   */
  public float getYRange()
  {
    return getMaxY() - getMinY();
  }

  /**
   * Returns the best best estimate of the current pose;
   * @return the estimated pose
   */
  public Pose getPose()
  {
   if(debug) System.out.println("Mcl call update; updated? "+updated
              +" busy "+busy);
    if (!updated)
    {
      while(busy){
    	if(debug) System.out.println("MCL Busy");
    	Thread.yield();
      }
      if(debug) System.out.println("Mcl call update; updated? "+updated);
      if(!updated)update();
    }
    estimatePose();
    return new Pose(_x, _y, _heading);
  }
  
  public Pose getEstimatedPose() {
	return new Pose(_x, _y, _heading);
  }

  /**
   * Estimate pose from weighted average of the particles
   * Calculate statistics
   */
  public void estimatePose()
  {
    float totalWeights = 0;
    float estimatedX = 0;
    float estimatedY = 0;
    float estimatedAngle = 0;
    varX = 0;
    varY = 0;
    varH = 0;
    minX = BIG_FLOAT;
    minY = BIG_FLOAT;
    maxX = -BIG_FLOAT;
    maxY = -BIG_FLOAT;

    for (int i = 0; i < numParticles; i++)
    {
      Pose p = particles.getParticle(i).getPose();
      float x = p.getX();
      float y = p.getY();
      //float weight = particles.getParticle(i).getWeight();
      float weight = 1; // weight is historic at this point, as resample has been done
      estimatedX += (x * weight);
      varX += (x * x * weight);
      estimatedY += (y * weight);
      varY += (y * y * weight);
      float head = p.getHeading();
      estimatedAngle += (head * weight);
      varH += (head * head * weight);
      totalWeights += weight;

      if (x < minX)  minX = x;

      if (x > maxX)maxX = x;
      if (y < minY)minY = y;
      if (y > maxY)   maxY = y;
    }

    estimatedX /= totalWeights;
    varX /= totalWeights;
    varX -= (estimatedX * estimatedX);
    estimatedY /= totalWeights;
    varY /= totalWeights;
    varY -= (estimatedY * estimatedY);
    estimatedAngle /= totalWeights;
    varH /= totalWeights;
    varH -= (estimatedAngle * estimatedAngle);
    
    // Normalize angle
    while (estimatedAngle > 180) estimatedAngle -= 360;
    while (estimatedAngle < -180) estimatedAngle += 360;
    
    _x = estimatedX;
    _y = estimatedY;
    _heading = estimatedAngle;
  }
  
  /**
   * Returns most recent range readings
   * @return the range readings
   */
  public RangeReadings getRangeReadings()
  {
    return readings;
  }
  /**
   * Returns the minimum rectangle enclosing all the particles
   * @return rectangle : the minimum rectangle enclosing all the particles
   */
  public Rectangle getErrorRect()
  {
    return new Rectangle((int) minX, (int) minY,
            (int) (maxX - minX), (int) (maxY - minY));
  }

  /**
   * Returns the maximum value of  X in the particle set
   * @return   max X
   */
  public float getMaxX()
  {
    return maxX;
  }

  /**
   * Returns the minimum value of   X in the particle set;
   * @return minimum X
   */
  public float getMinX()
  {
    return minX;
  }

  /**
   * Returns the maximum value of Y in the particle set;
   * @return max y
   */
  public float getMaxY()
  {
    return maxY;
  }

  /**
   * Returns the minimum value of Y in the particle set;
   * @return minimum Y
   */
  public float getMinY()
  {
    return minY;
  }

  /**
   * Returns the standard deviation of the X values in the particle set;
   * @return sigma X
   */
  public float getSigmaX()
  {
    return (float) Math.sqrt(varX);
  }

  /**
   * Returns the standard deviation of the Y values in the particle set;
   * @return sigma Y
   */
  public float getSigmaY()
  {
    return (float) Math.sqrt(varY);
  }

  /**
   * Returns the standard deviation of the heading values in the particle set;
   * @return sigma heading
   */
  public float getSigmaHeading()
  {
    return (float) Math.sqrt(varH);
  }
  
  /**
   * Returns the range scanner
   * @return the range scanner
   */
  public RangeScanner getScanner()
  {
	return scanner;
  }

  /**
   * Dump the serialized estimate of pose to a data output stream
   * @param dos the data output stream
   * @throws IOException
   */
  public void dumpObject(DataOutputStream dos) throws IOException
  {
    dos.writeFloat(_x);
    dos.writeFloat(_y);
    dos.writeFloat(_heading);
    dos.writeFloat(minX);
    dos.writeFloat(maxX);
    dos.writeFloat(minY);
    dos.writeFloat(maxY);
    dos.writeFloat((float) varX);
    dos.writeFloat((float) varY);
    dos.writeFloat((float) varH);
    dos.flush();
  }

  /**
   * Load serialized estimated pose from a data input stream
   * @param dis the data input stream
   * @throws IOException
   */
  public void loadObject(DataInputStream dis) throws IOException
  {
    _x = dis.readFloat();
    _y = dis.readFloat();
    _heading = dis.readFloat();
    minX = dis.readFloat();
    maxX = dis.readFloat();
    minY = dis.readFloat();
    maxY = dis.readFloat();
    varX = dis.readFloat();
    varY = dis.readFloat();
    varH = dis.readFloat();
    if (debug) System.out.println("Estimate = " + minX + " , " + maxX + " , " + minY + " , " + maxY);
  }

  /**
   * returns true if particle weights are being updated. The  robot should not move
   * while this is happening otherwise the prediction from odometry data may
   * introduce errors into the updating.
   * @return  true if weight update is in progress.
   */
  public boolean isBusy() { return busy;}

  /**
   * Predicts particle pose from odometry data.
   */
  class Updater extends Thread
  {
    boolean keepGoing = true;
    ArrayList<Move> events = new ArrayList<Move>();

    public void moveStopped(Move theEvent)
    {
      events.add(theEvent);
    }

    @Override
	public void run()
    {
      while (keepGoing)
      {
        while(!events.isEmpty())
        {
          if(debug) System.out.println("Updater move stop "+events.get(0).getMoveType());
          busy = true;

          particles.applyMove(events.get(0));      
          if(debug) System.out.println("applied move ");
          events.remove(0);
        }
        busy = false;
        Thread.yield();
      }
    }  // end run()
  }// end class Updater
}
