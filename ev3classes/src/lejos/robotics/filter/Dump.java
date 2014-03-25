package lejos.robotics.filter;

import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

public class Dump extends AbstractFilter {
  String format="%7.3f";

  public Dump(SampleProvider source) {
    super(source);
  }
 
  public Dump(SampleProvider source, String format) {
    super(source);
    this.format=format;
  }
 
  
  public void fetchSample(float[] sample, int offset) {
    String f;
  super.fetchSample(sample,offset);
  LCD.clear();
  for (int i=0;i<Math.min(sampleSize,LCD.DISPLAY_CHAR_DEPTH);i++) { 
    f=String.format(format,sample[i+offset]);
    System.out.print(f);
    System.out.print(' ');
    LCD.drawString(f, 0, i);
  }
  System.out.println();
  LCD.refresh();

  }

}
