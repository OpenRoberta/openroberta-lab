package lejos.robotics;

import java.io.*;
import java.util.ArrayList;

/**
 * Represents a set of range readings.
 * 
 * @author Lawrie Griffiths
 */
public class RangeReadings extends ArrayList<RangeReading> implements Transmittable  { 

  
  public RangeReadings(int numReadings) {
    super(numReadings);
    for(int i=0;i<numReadings;i++) add(new RangeReading(0,-1));
  }

  /**
   * Get a specific range reading
   * 
   * @param i the reading index
   * @return the range value
   */
  public float getRange(int i) {
    return get(i).getRange();
  }
  
  /**
   * Get a range reading for a specific angle
   * 
   * @param angle the reading angle
   * @return the range value
   */
  public float getRange(float angle) {
    for(RangeReading r: this) {
    	if (r.getAngle() == angle) return r.getRange();
    }
    return -1f;
  }
  
  /**
   * Get the angle of a specific reading
   * 
   * @param index the index of the reading
   * @return the angle in degrees
   */
  public float getAngle(int index) {
	  return  get(index).getAngle();
  }

  /**
   * Return true if the readings are incomplete
   * 
   * @return true iff one of the readings is not valid
   */
  public boolean incomplete() {
    for (RangeReading r: this) {
      if (r.invalidReading()) return true;
    }
    return false;
  }
  
  /**
   * Get the number of readings in a set
   */
  public int getNumReadings() {
    return size();
  }
  
  /**
   * Set the range reading
   * 
   * @param index the index of the reading in the set
   * @param angle the angle of the reading relative to the robot heading
   * @param range the range reading
   */
  public void setRange(int index, float angle, float range) {
	  set(index, new RangeReading(angle, range));
  }
  
  /**
   * Dump the readings to a DataOutputStream
   * @param dos the stream
   * @throws IOException
   */
  public void dumpObject(DataOutputStream dos) throws IOException {
	dos.writeInt(size());
    for (RangeReading r: this) {
      dos.writeFloat(r.getAngle());
      dos.writeFloat(r.getRange());
    }
    dos.flush();
  }
  
  /**
   * Load the readings from a DataInputStream
   * @param dis the stream
   * @throws IOException
   */
  public void loadObject(DataInputStream dis) throws IOException {
	int numReadings = dis.readInt();
	this.clear();
    for (int i = 0; i < getNumReadings(); i++) {
      add(new RangeReading(dis.readFloat(),dis.readFloat()));
    }        
  }
  
  /**
   * Print the range readings on standard out
   */
  public void printReadings() {
	int index = 0;
    for (RangeReading r: this) {
      System.out.println("Range " + index + " = " + 
    		  (r.invalidReading() ? "Invalid" : r.getRange()));
      index++;
    }        
  }
}

