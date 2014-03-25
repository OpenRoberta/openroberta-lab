package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.robotics.Tachometer;
import lejos.utility.Delay;

/**
 * Provide access to the Lego RCX Rotation Sensor.
 *
 * The sensor records the direction and degree of rotation. A full rotation
 * will result in a count of +/-16. Thus each count is 22.5 degrees.
 *
 * @author Andy Shaw
 * 
 */
public class RCXRotationSensor extends AnalogSensor implements Tachometer, SensorConstants
{
	/**
	 * The incremental count for one whole rotation (360 degrees).
	 */
    public static final int ONE_ROTATION = 16;
    protected static final int UPDATE_TIME = 2;
    protected int count;
    protected final Reader reader;
    private int speed = 0;
    private long previous_time = System.currentTimeMillis();
    
    /**
     * Create an RCX rotation sensor object attached to the specified port.
     * @param port port, e.g. Port.S1
     */
    public RCXRotationSensor(Port p)
    {
        super(p);
        port.setTypeAndMode(TYPE_ANGLE, MODE_RAW);
        reader = new Reader();
        reader.setDaemon(true);
        reader.setPriority(Thread.MAX_PRIORITY);
        reader.start();
        count = 0;
    }


    /**
     * Returns the current phase of the sensor.
     *
     * The sensor returns four distinct values read by the ADC port. Each value
     * represents a phase in the rotation. The sequence of the phases is:
     * 0 1 3 2 and 0 2 3 1
     * The transition from one phase to another can be used to identify the
     * direction of rotation.
     * @return the current rotation phase.
     */
    protected int getPhase()
    {
        int val = NXTRawIntValue(port.getPin1());
        if (val < 450) return 0;
        if (val < 675) return 1;
        if (val < 911) return 2;
        return 3;
    }

    /**
     * The following table when indexed by [previous phase][current phase]
     * provides the current direction of rotation. Invalid phase combinations
     * result in zero.
     */
    protected static final int [][]inc =   {{0, 1, -1, 0},
                                            {-1, 0, 0, 1},
                                            {1, 0, 0, -1},
                                            {0, -1, 1, 0}};


    protected class Reader extends Thread
    {
        /**
         * Sensor reader thread.
         * Reads the current phase of the sensor and computes the new count.
         * NOTE: There is a problem with this sensor when a read spans the
         * point at which the sensor output changes from one value to another.
         * The result of this can be a "ghost value". For instance if the read
         * occurs when moving from state 2 to state 0 then a false reading of
         * state 1 may be read. To reduce this problem a new state is not
         * accepted until two consecutive reads return the same state.
         */
        public void run()
        {
            int prev = getPhase();
            int cur1 = prev;
            while (true)
            {
                int cur2 = getPhase();
                if (cur1 == cur2)
                {
                    if (cur2 != prev)
                    {
                        synchronized(this)
                        {
                            count += inc[prev][cur2];
                            
                            // TODO: This should probably indicate sign for speed if Motor does too. Also, Javadocs
                            // for interface should also specify whether sign applies for speed.
                            // TODO: This will never report 0 speed! Need some algorithm to realize when it is at 0 speed,
                            // especially when it goes from fast to dead stop.
                         // Estimate speed by calculating time elapsed for every increment
                            int time_elapsed = (int)(System.currentTimeMillis() - previous_time);
                            speed = (360 * 1000) / (time_elapsed * ONE_ROTATION);
                            previous_time = System.currentTimeMillis();
                        }
                        prev = cur2;
                        
                    }
                }
                cur1 = cur2;
                
                Delay.msDelay(UPDATE_TIME);
            }
        }
    }

    /**
	   * Returns the tachometer count.
	   * NOTE: Because the RCX rotation sensor only counts 16 increments for a full rotation, the degree values
	   * are only accurate to +- 22.5 degrees.
	   * @return tachometer count in degrees, in increments of 22.5 degrees (rounded off)
	   */
    public int getTachoCount()
    {
        return (360 * count) / ONE_ROTATION;
    }
    
    /**
     * Returns the raw values from the rotation sensor instead of degrees.
     * A full rotation of 360 degrees results in count increasing by 16. 
     * @return the raw tachometer reading
     */
    public int getRawTachoCount() {
    	return count;
    }

    /**
     * Reset the tacho count to zero.
     */
    public void resetTachoCount()
    {
        synchronized(reader)
        {
            count = 0;
        }
    }

    // TODO: Change to getTachoSpeed
	public int getRotationSpeed() {
		// TODO: Ok, if it has been longer than last delay between pulses, then it should start to 
		// calculate speed based on the time between pulses here in this method. In other words, the 
		// speed value starts working its way towards zero. Might not actually get to 0, but could 
		// choose some arbitrary value to round to zero.
		return speed;
	}
}