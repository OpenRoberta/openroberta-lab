package lejos.robotics;

import lejos.hardware.Sound;
import lejos.robotics.RangeReadings;
import lejos.robotics.RangeReading;


/**
 * Software abstraction of a light sensor rotating in a horizontal plane, driven by a motor.
 * Records the  raw light value and bearing of beacons seen during a scan,  
 * This class assumes that the motor tacho count corresponds to the angle of the  sensor  in the navigational plane. <br>
 * After a forward scan is complete, the scan is repeated in the reverse direction
 * and the data are averaged.  This averages out the gear backlash error.   
 * The results of a scan are returned as a set of RangeReadings, not a good name
 * but this class does have enough fields to do the job.   If used in MCL however,
 * a different class name should be used because the algorithm for calculation of particle
 * weights is very different if the basic is angles rather than ranges.
 * @author Roger Glassey
 *
 */
public class LightScanner
{

   protected RegulatedMotor head;
   LightDetector eye;
   private int _numReadings;
   private RangeReadings _readings;
   /**
    * distance to found objects; set by scanTo();
    */
   private boolean _scanning = false;
   /**
    * used by ScanTo() - identify a light beacon - value hardware dependent. 
    */
   private int _lightMin = 420;
   /**
    * used by ScanTo() when light value drops below this, beacon is passed. - value hardware dependent. 
    */
   private int _background = 380;
   /**
    * speed of head while scanning for beacons.  Used by scanLight()
    */
   private int lightSpeed = 100;
   /**
    * direction of scanner movement 
    */
   private int _direction = 1;

   /**
    * number of beacons found so far
    */
   /**
    * Specify the hardware for this object.  Light values less than background 
    * are ignored.  When a light value larger than minBeaconLight is sensed, it 
    * is within the field of view of the light sensor.  As scanning continues,
    * when the light value drops below background,  it is assumed that the beacon
    * has been passed, and the angle at which the maximum occurred is the angle
    * of the beacon.
    * @param headMotor
    * @param lightDetector 
    * @param minBeaconLight
    * @param background 
    */
   public LightScanner(RegulatedMotor headMotor, LightDetector lightDetector,
           int minBeaconLight, int background)
   {
      head = headMotor;
      head.setSpeed(lightSpeed);
      eye = lightDetector;
      _background = background;
      _lightMin = minBeaconLight;
   }

   public void setSpeed(int speed)
   {
      head.setSpeed(speed);
   }

   public void halt()
   {
      _scanning = false;
      head.stop();
   }

   /**
    * Perform a scan  for a number of  light beacons within the arc defined by the start 
    * and end angles in the specified direction.   
    * Scan in both directions and use the average of the angles.
    * Assumes the scanner hardware is built so that increasing tacho count of the 
    * motor corresponds to increasing angle in the navigation plane. 
    * @param startAngle of the arc
    * @param endAngle of the arc
    * @param direction of the scan.  +1 = increasing tacho count of motor. 
    * @param numReadings
    * @return the set of range readings, containing the light intensity and angle of each beacon
    */
   public RangeReadings scanLight(float startAngle, float endAngle, int direction,
           int numReadings)
   {
      _numReadings = numReadings;
      // working arrays to hold data
      int[] intensity = new int[numReadings];
      int[] bearing = new int[numReadings];
      int start = (int) startAngle;
      int end = (int) endAngle;

      for (int i = 0; i < numReadings; i++)  // initialize data
      {
         intensity[i] = 0;
         bearing[i] = 0;
      }
      int dir = direction;
      int arc = normalize((int) (endAngle - startAngle), dir);  // length of scan
      boolean beacon = false;
      int foundCount = 0;
      int indx = 0;  // where data gets stored in working arrays
      int pass = 1; // forward (first) pass along arc
      _scanning = true;
      int light = 0;
      head.setSpeed(600);
      head.rotateTo(start);
      head.setSpeed(lightSpeed);
      head.rotate(arc, true);
      int k = 0;
      while (_scanning && head.isMoving())
      {
         light = eye.getNormalizedLightValue();
         if (!beacon && light > _lightMin)//seeing beacon
         {
            beacon = true;
            intensity[indx] = light;
         }
         if (beacon)
         {
            if (light > intensity[indx]) 
            {
               intensity[indx] = light;
               bearing[indx] = head.getTachoCount();
            }
            if (light < _background) // past the beacon          
            {
               Sound.playTone(800 + 200 * indx, 300);
               beacon = false;
               foundCount++;
               if (pass == 1)
               {
                  if (foundCount == _numReadings)
                  {
                     head.stop();
                     forwardComplete(_readings, intensity, bearing);
                     head.rotate(-arc, true);
                     pass = -1;
                  } else
                     indx++;

               } else
               {
                  if (foundCount == 2 * _numReadings)
                  {
                     head.stop();
                     backComplete(_readings, intensity, bearing);
                     _scanning = false;
                  } else
                     indx--;

               }// light < backaground
            }// end past beacon
            if (!head.isMoving() && _scanning)
            {
               _readings.set(0, new RangeReading(-1, -1)); //incomplete readings
            }

         }// end while scanning

      }
      head.setSpeed(700);
      head.rotateTo(0, true);
      return _readings;
   }
/**
    * helper method for scanLight() ; params will be altered by this method
    * @param readings
    * @param intensity
    * @param bearing 
    */
   private void forwardComplete(RangeReadings readings, int[] intensity, int[] bearing)
   {
      readings.clear();
      head.stop();
      for (int i = 0; i < _numReadings; i++)  // store data in readings
      {
         RangeReading r = new RangeReading(bearing[i], intensity[i]);
         readings.add(r);
         intensity[i] = 0; // initialize for next pass
      }
   }  // count too small

   /**
    * helper method for scanLight() ; params will be altered by this method
    * @param readings
    * @param intensity
    * @param bearing 
    */
   private void backComplete(RangeReadings readings, int[] intensity, int[] bearing)
   {
      RangeReading reading;
      for (int i = 0; i < _numReadings; i++)  // store average values in readings;
      { 
         reading = readings.get(i);
         float avgLight = (reading.getRange() + intensity[i]) / 2;
         float avgBearing = (reading.getAngle() + bearing[i]) / 2;
         if (avgBearing < -180)  // keep angles between -180 and 180
            avgBearing += 360;
         if (avgBearing > 180)
            avgBearing -= 360;
         readings.set(i, new RangeReading(avgBearing, avgLight));
      }
      for (RangeReading r : readings)
      {
         System.out.println("angle" + r.getAngle() + " light " + r.getRange());
      }
   }

   private int normalize(int arc, int dir)
   {
      if (dir > 0) // normalize art between 0 and 360
      {
         while (arc < 0) arc += 360;
         while (arc > 360)   arc -= 360;
         
      } else   // dir <0  normalize arc between -360 and 0 
      {
         while (arc > 0)  arc -= 360;         
         while (arc < - 360) arc += 360;          
      }
      return arc;
   }
   
   public RegulatedMotor getMotor() { return head;}
}