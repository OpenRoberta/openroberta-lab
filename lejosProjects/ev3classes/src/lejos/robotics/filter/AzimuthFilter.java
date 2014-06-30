package lejos.robotics.filter;
/**
 * This class fails to build at the moment (because there is no code. The following dummy class allows it to build
 * while we decide what to do wuth it!
 * @author andy
 *
 */
public class AzimuthFilter
{
    
}
//
//import lejos.robotics.SampleProvider;
//
///**
// * This SampleProvider converts magnetic field strengths from a magnetometer
// * (triaxis compass sensor) into azimuth.<br>
// * Azimuth (expressed in radians) is the angle between North and the X-axis of
// * the magnetometer.
// * <p>
// * To apply tilt correction specify an accelerometer in the constructor of this
// * class.
// * 
// * @author Aswin
// * 
// */
//public class AzimuthFilter extends AbstractFilter {
//  float[]        m             = new float[3];
//  SampleProvider accelerometer = null;
//  float[]        t;
//  Vector3f       magneto, accel, west;
//
//  /**
//   * Constructor for the Azimuth filter without tilt correction.
//   * <p>
//   * 
//   * @param source
//   *          SampleProvider that provides magnetic field strength over X, Y and
//   *          Z
//   */
//  public AzimuthFilter(SampleProvider source) {
//    super(source);
//    sampleSize = 1;
//  }
//
//  /**
//   * Constructor for the Azimuth filter with tilt correction.
//   * 
//   * @param source
//   *          SampleProvider that provides magnetic field strength over X, Y and
//   *          Z
//   * @param accelerometer
//   *          ampleProvider that provides acceleration over X, Y and Z
//   */
//  public AzimuthFilter(SampleProvider source, SampleProvider accelerometer) {
//    this(source);
//    this.accelerometer = accelerometer;
//    t = new float[3];
//    magneto = new Vector3f();
//    accel = new Vector3f();
//    west = new Vector3f();
//  }
//
//  @Override
//  public void fetchSample(float[] sample, int offset) {
//    double direction;
//    source.fetchSample(m, 0);
//    if (accelerometer == null) {
//      direction = Math.atan2(m[1], m[0]);
//    }
//    else {
//      /**
//       * Apply tilt correction. Tilt correction is based on the fact that the
//       * cross product of two vectors gives a vector perpendicular to the other
//       * two vectors. A vector perpendicular to the gravity vector (down) and
//       * the magnetic field vector (north) points east/west.
//       */
//      accelerometer.fetchSample(t, 0);
//      magneto.set(m);
//      accel.set(t);
//      west.cross(magneto, accel);
//      direction = Math.atan2(west.y, west.x) + .5 * Math.PI;
//    }
//    if (direction < 0)
//      direction += 2 * Math.PI;
//    sample[offset] = (float) direction;
//  }
//
//}
