package lejos.utility;

import lejos.robotics.DirectionFinder;
import lejos.robotics.Gyroscope;

/**
 * Implementation of the <code>DirectionFinder</code> interface that integrates repeated rate-of-turn readings from a 
 * <code>{@link GyroSensor}</code>
 * into a continuously updated heading. This class is very similar to <code>{@link CompassMindSensor}</code>, 
 * except that the direction returned does not convey true heading (north, south, etc) but rather
 * relative heading change since the last time <code>setDegrees()</code> or <code>resetCartesianZero()</code> was called.
 * @see GyroSensor
 * @author Brent Gardner
 * @author Kirk P. Thompson
 */
public class GyroDirectionFinder implements DirectionFinder
{
    private float cartesianCalibrate = 0;
    private float heading = 0;
    private float acceleration;
    private boolean calibrating = false;
    private Regulator reg = new Regulator();
    private Gyroscope gyro;

    /** Creates and initializes a new <code>GyroDirectionFinder</code> using passed <code>GyroSensor</code> 
     * @param gyro A <code>{@link GyroSensor}</code> instance
     * @see GyroSensor
     */
    public GyroDirectionFinder(Gyroscope gyro) {
        this(gyro, false);
    }

    /** Creates and initializes a new <code>GyroDirectionFinder</code> using passed <code>GyroSensor</code> and does
     * the <code>GyroSensor.recalibrateOffset()</code> method.
     * @param gyro A <code>{@link GyroSensor}</code> instance
     * @see GyroSensor#recalibrateOffset()
     * @see #startCalibration
     */
    public GyroDirectionFinder(Gyroscope gyro, boolean calibrate) {
        this.gyro = gyro;
        reg.start();
        if(calibrate == false) return;

        // Optional calibration
        startCalibration();
    }

    /**
     * Resets the current heading to a desired value.
     * @see #getDegrees
     */
    public void setDegrees(float heading) {
        this.heading = heading;
    }

    /**
     * Returns the directional heading in degrees. Includes "winding",
     * so the value could be greater than 360 or less than 0
     * if the robot has done multiple rotations since the last call to <code>resetCartesianZero()</code>.
     * @return Heading in degrees.
     * @see #setDegrees
     */
    public float getDegrees() {
        return heading;
    }

    /**
     * Returns the current rate-of-turn in degrees/second, as read by the <code>GyroSensor</code> instance passed in the constructor.
     * @return Angular velocity in degrees.
     * @see GyroSensor
     */
    public float getAngularVelocity() {
        return gyro.getAngularVelocity();
    }

    /**
     * Returns the current rate at which the angular velocity is increasing or decreasing in degrees-per-second, per second.
     * @return Angular acceleration in degrees-per-second per second.
     */
    public float getAngularAcceleration() {
        return acceleration;
    }

    /**
     * Returns the current rate-of-turn in degrees, as read by the <code>GyroSensor</code>.
     * @return Heading in degrees.
     */
    public float getDegreesCartesian() {
        return cartesianCalibrate - getDegrees();
    }

    /**
     * Resets the current heading to a desired value.
     */
    public void setDegreesCartesian(float heading) {
        this.heading = cartesianCalibrate - heading;
    }

    /**
     * Resets the current heading to zero.
     */
    public void resetCartesianZero() {
        cartesianCalibrate = getDegrees();
    }

    /**
     * Find offset/bias of gyro while at rest (<u>ensure it is at rest</u>). This is done by calling the <code>recalibrateOffset()</code> method of 
     * the <code>GyroSensor</code> instance passed in the constructor. This takes 3 seconds.
     * 
     * @see GyroSensor#recalibrateOffset()
     */
    public void startCalibration() {
        calibrating = true;
        Delay.msDelay(2600);
    }

    /**
     * NO FUNCTIONALITY EQUIVALENT for <code>GyroSensor</code> so implemented just to satisfy the <code>DirectionFinder</code> interface. 
     * Does nothing.
     */
    public void stopCalibration() {
        calibrating = false;
    }

    /**
     * This is the private thread class that is used to continously integrate successive readings from the gyro
     */
    private class Regulator extends Thread {
        protected Regulator() {
            setDaemon(true);
        }

        @Override
        public void run() {
            float lastDegreesPerSecond = 0F;
            float degreesPerSecond, secondsSinceLastReading;
            long lastUpdate;
            long now = System.currentTimeMillis();
            while (true) {
                Delay.msDelay(5);
                lastUpdate = now;
                now = System.currentTimeMillis();
                
                degreesPerSecond=gyro.getAngularVelocity();

                // Calibration flagged...
                if (calibrating) {
                    gyro.recalibrateOffset(); // 5 seconds consumed here
                    calibrating = false;
                }
 
                // reduce "perceived" drift since the sensor resolution is 1 deg/sec. This will increase error...
                // Comment or remove if this behavior is undesired. I don't know if Brent required a wandering value but
                // doing this presents better to the human observer (no perceived drift). KPT 4/7/11
                if (Math.abs(degreesPerSecond)<1.0f) degreesPerSecond=0.0f; 

                // Integration
                secondsSinceLastReading = (now - lastUpdate) * .001f;
                heading += degreesPerSecond * secondsSinceLastReading;
                acceleration = (degreesPerSecond - lastDegreesPerSecond) / secondsSinceLastReading;
                lastDegreesPerSecond = degreesPerSecond;
            }
        }
    }
    
}