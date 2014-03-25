package lejos.robotics.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import lejos.robotics.Calibrate;
import lejos.robotics.SampleProvider;

public abstract class AbstractCalibrationFilter extends AbstractFilter implements Calibrate{
  public class CalibrationFileException extends RuntimeException {


    /**
     * 
     */
    private static final long serialVersionUID = -4292630590012678509L;

    public CalibrationFileException(String string) {
      super(string);
    }

  }

  protected LowPassFilter          lowPassFilter      = null;
  protected float[]                min;
  protected float[]                max;
  protected float[]                sum;
  protected boolean                calibrating = false;
  
  private final static String DIRECTORY = "/home/root/sensorCalibration/";
  private final static String EXT       = ".cal";
  private final Properties    props     = new Properties();
  
  protected int numberOfSamplesInCalibration;
  private float timeConstant=0;

  


  public AbstractCalibrationFilter(SampleProvider source) {
    super(source);
    min=new float[sampleSize];
    max=new float[sampleSize];
    sum=new float[sampleSize];
  }
  
  
  
  /**
   * Fetches a sample from the sensor and updates array with minimum and maximum values when
   * the calibration process is running.
   */
  public void fetchSample(float[] dst, int off) {
    if (!calibrating) {
      source.fetchSample(dst, off);
    }
    else {
      lowPassFilter.fetchSample(dst, off);
      numberOfSamplesInCalibration++;
      for (int i = 0; i < sampleSize; i++) {
        if (min[i] > dst[i + off])
          min[i] = dst[i + off];
        if (max[i] < dst[i + off])
          max[i] = dst[i + off];
        sum[i]+=dst[i+off];
      }
    }
  }
  
  /** Sets the time constant for the lowpass filter that is used when calibrating. <br>
   * A value of zero will effectivly disable the lowpass filter. 
   * Higher values will remove more noise from the signal and give better results, especially when calibraating for scale. 
   * The downside of higher timeConstants is that calibrating takes more time. 
   * @param timeConstant
   * between 0 and 1
   */
  public void setTimeConstant(float timeConstant) {
    this.timeConstant=timeConstant;
  }
  
  /**
   * Starts a calibration proces. Resets collected minimum and maximum values.
   * After starting calibration new minimum and maximum values are calculated on
   * each fetched sample. From this calibration parameters can be calculated.
   */
  
  public void startCalibration() {
    lowPassFilter = new LowPassFilter(source, timeConstant);
    calibrating = true;
    numberOfSamplesInCalibration=0;
    for (int i = 0; i < sampleSize; i++) {
      min[i] = Float.MAX_VALUE;
      max[i] = Float.MIN_VALUE;
      sum[i]=0;
    }
  }

  /**
   * Halts the process of updating calibration parameters.
   */
  public void stopCalibration() {
    calibrating = false;
  }
  
  /**
   * Halts the process of updating calibration parameters.
   */
  public void suspendCalibration() {
    calibrating = false;
  }

  /**
   * Resumes the process of updating calibration parameters after a stop.
   */
  public void resumeCalibration() {
    calibrating = true;
  }


  
  /*
   * Methods involved in loading and storing 
   * calibration parameters on the file system
   */
  
  private File getFile(String filename) {
    return new File(DIRECTORY + filename + EXT);
  }

  protected void load(String filename) {
    props.clear();
    try {
      File f = getFile(filename);
      if (f.exists()) {
        FileInputStream in = new FileInputStream(f);
        props.load(in);
        in.close();
        if (!props.getProperty("type").equals(this.toString())) 
          throw new CalibrationFileException("Invalid Calibration file. Wrong type for filter.");
        if (Integer.parseInt(props.getProperty("sampleSize"))!=sampleSize) 
          throw new CalibrationFileException("Invalid Calibration file. Sample size does not match.");
      }
      else throw new CalibrationFileException("Calibration file "+filename+" does not exist");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void store(String filename) {
    try {
      new File(DIRECTORY).mkdir();
      File f = getFile(filename);
      f.createNewFile();
      FileOutputStream out = new FileOutputStream(f);
      props.setProperty("sampleSize", Integer.toString(sampleSize));
      props.setProperty("type", this.toString());
      
      props.store(out, "Parameters for sensor calibration");
      out.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  

  protected float[] getPropertyArray(String key) {
    String raw = props.getProperty(key);
    StringTokenizer tokenizer = new StringTokenizer(raw, " ");
    int n = tokenizer.countTokens();
    float[] values = new float[n];
    for (int i = 0; i < n; i++) {
      values[i] = Float.parseFloat(tokenizer.nextToken());
    }
    return values;
  }

  protected void setPropertyArray(String key, float[] values) {
    StringBuilder builder = new StringBuilder();
    int n = values.length;
    for (int i = 0; i < n; i++) {
      if (i != 0)
        builder.append(" ");
      builder.append(values[i]);
    }
    props.setProperty(key, builder.toString());
  }
  
  protected void setProperty(String key, float value) {
    props.setProperty(key, Float.toString(value));
  }

  protected float getProperty(String key) {
    return Float.parseFloat(props.getProperty(key));
  }

  
}
