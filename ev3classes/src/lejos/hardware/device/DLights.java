package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

/**
 * The DLights class drives up to four dLights on a single sensor port.
 * <p>
 * The dLight is a RGB color LED connected to a sensor port. 
 * Up to four dLights can be daisy chained on a single port.
 * The individual dLights are identified within this class by a sequence number (1-4). 
 * All dLights can be addressed simultaniously as a group by using sequence number 0.
 * <p>
 * The DLights class supports two different color schemes for instructing the dLights; RGB and HSL.
 * It supports just RGB for querying the dLights
 * 
 * @author Aswin Bouwmeester
 * 
 */
public class DLights {

	private LED[]							dLights				= new LED[5];
	private final int[]				address				= { 0xe0, 0x04, 0x14, 0x24, 0x34 };

	/**
	 * Constructor for DLights.<p> Nb. It also enables HIGH-SPEED I2C on this port.
	 * 
	 * @param port
	 *          The I2CPort that the dLights are attached to.
	 */
	public DLights(I2CPort port) {
		for (int i = 0; i < 5; i++)
			dLights[i] = new LED(port, address[i]);
	}

	public DLights(Port port) {
    for (int i = 0; i < 5; i++)
      dLights[i] = new LED(port, address[i]);
  }

  /**
	 * Returns the I2C driver for a single dLight.
	 * <p>
	 * Nb. For ordinary use there is no need to use the I2C driver for a single
	 * dLight that this method returns. The method and the DLight instance it
	 * returns are nly useful if one needs to access the dLight directly over I2C.
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (1-4). Please note that the group number 0 is invalid for this method.
	 * @return the I2C driver for a single dLight
	 */
	public DLight getDLight(int lightNo) {
		if (1>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		return dLights[lightNo];
	}

	/**
	 * Enables the dLight. <p>
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 */
	public void enable(int lightNo) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].enable();
	}

	/**
	 * Disables the dLight. The light will be off, no matter what values for color are sent to the dLight.
	 * <p> Values for color and blinking pattern are not overwritten and will still be in effect after enabling the dLight again.
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 */
	public void disable(int lightNo) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].disable();
	}
	
	/** Queries the status of the dLight
	 * @param lightNo (1-4). Please note that the group number 0 is invalid for this method.
	 * @return
	 * True, the dLight is enabled. False, the dLight is off.
	 */
	public boolean isEnabled(int lightNo) {
		if (1>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		return dLights[lightNo].isEnabled();
	}

	/**
	 * Changes the color of the LED according to specified RGB color. Each of the
	 * color values should be between 0 (fully off) and 255 (fully on).
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int lightNo, int red, int green, int blue) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].setColor(red, green, blue);
	}

	/**
	 * Changes the color of the LED according to specified RGB color. Each of the
	 * color values should be between 0 (fully off) and 255 (fully on).
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 * @param rgb
	 * an integer array containing RGB colors
	 */
	public void setColor(int lightNo, int[] rgb) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].setColor(rgb);
	}

	
	/**
	 * Changes the color of the LED according to specified HSL color.
	 * 
	 * @param hue
	 *          The hue value in the range of 0-360
	 * @param saturation
	 *          The saturation value in the range of 0-100
	 * @param luminosity
	 *          The saturation luminosity value in the range of 0-100
	 */
	public void setHSLColor(int lightNo, int hue, int saturation, int luminosity) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].setHSLColor(hue, saturation, luminosity);
	}

	/**
	 * Specifies the blinking pattern of the LED. Blinnking mode should be enabled
	 * for the pattern to be in effect.
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 * @param seconds
	 *          The total time of a blinking cycle (in seconds)
	 * @param percentageOn
	 *          The percentage of the time the LED is on within a blinking cycle
	 */
	public void setBlinkingPattern(int lightNo, float seconds, int percentageOn) {
		if (0>lightNo || lightNo>4 || seconds>10.0f || seconds < 0.04f || percentageOn<0 ||percentageOn>100)
			throw new IllegalArgumentException();
		dLights[lightNo].setBlinkingPattern(seconds, percentageOn);
	}

	/**
	 * Enables blinking pattern. The blinking pattern should be set with the
	 * SetBlinkingPattern method.
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 */
	public void enableBlinking(int lightNo) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].enableBlinking();
	}

	/**
	 * Disables blinking. The blinking pattern itself remains in memory.
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 */
	public void disableBlinking(int lightNo) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].disableBlinking();
	}
	
	/** Queries the blinking status of the dLight
	 * @param lightNo (1-4). Please note that the group number 0 is invalid for this method.
	 * @return
	 * True, blinking is enabled. False, blinking is disabled.
	 */
	public boolean isBlinkingEnabled(int lightNo) {
		if (1>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		return dLights[lightNo].isBlinkingEnabled();
	}


	/**
	 * Returns the RGB color of the LED Each of the color values is between 0
	 * (fully off) and 255 (fully on).
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (1-4). Please note that the group number 0 is invalid for this method.
	 * @param rgb
	 *          the RGB color of the LED
	 */
	public void getColor(int lightNo, int[] rgb) {
		if (1>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].getColor(rgb);
	}

	/**
	 * Sets the PWM value of the external LED driver of the dLight. Each dLight
	 * has two leads broken out that can be used to drive an external LED.
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (0-4).
	 * @param value
	 *          The values should be between 0 (fully off) and 255 (fully on).
	 */
	public void setExternalLED(int lightNo, int value) {
		if (0>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		dLights[lightNo].setExternalLED(value);
	}

	/**
	 * Gets the PWM value of the external LED driver of the dLight. Each dLight
	 * has two leads broken out that can be used to drive an external LED.
	 * 
	 * @param lightNo
	 *          The sequence number of the dLight (1-4). Please note that the group number 0 is invalid for this method.
	 * @return The return value is between 0 (fully off) and 255 (fully on).
	 */
	public int getExternalLED(int lightNo) {
		if (1>lightNo || lightNo>4)
			throw new IllegalArgumentException();
		return dLights[lightNo].getExternalLED();
	}

	/**
	 * This is the I2C driver for a single (or group of) dLights. If this class
	 * represents a group then all dLights respond to it's methods.
	 * 
	 * @author Aswin
	 * 
	 */
	private class LED extends I2CSensor implements DLight {
		// Registers
		private static final int	MODE1					= 0x00;
		private static final int	MODE2					= 0x01;
		private static final int	COLOR					= 0x02;
		private static final int	EXTERNAL			= 0x05;
		private static final int	GRPPWM				= 0x06;
		private static final int	GRPFREQ				= 0x07;
		private static final int	LEDOUT				= 0x08;
		private static final int	AUTOINCREMENT	= 0x80;

		// Common register values
		private static final byte	MODE1MASK			= 0x01;
		private static final byte	SLEEPMODE			= 0x10;
		private static final byte	MODE2MASK			= 0x25;
		private static final byte	BLINKINGOFF		= (byte) 0xaa;
		private static final byte	BLINKINGON		= (byte) 0xff;


		private byte[]	buf	= new byte[4];

		protected void init()
		{
            setRegister(MODE1, MODE1MASK);
            setRegister(MODE2, MODE2MASK);
            setRegister(LEDOUT, BLINKINGOFF);
            setColor(0, 0, 0);
            setExternalLED(0);
            setRegister(GRPPWM, 0xff);
            setRegister(GRPFREQ, 0);
		    
		}
		
        protected LED(I2CPort port, int address) {
            super(port, address);
            init();
        }

        protected LED(Port port, int address) {
            super(port, address);
            init();
        }

		public void disable() {
			setRegister(MODE1, MODE1MASK + SLEEPMODE);
		}

		public void enable() {
			setRegister(MODE1, MODE1MASK);
		}

		/**
		 * Wrapper for sendData that cast register value to byte
		 * 
		 * @param n
		 * @param v
		 */
		private void setRegister(int n, int v) {
			buf[0] = (byte) v;
			sendData(n, buf, 1);
		}

		public void setColor(int r, int g, int b) {
			buf[0] = (byte) (r % 256);
			buf[1] = (byte) (g % 256);
			buf[2] = (byte) (b % 256);
			sendData(COLOR + AUTOINCREMENT, buf, 3);
		}

		public void setColor(int[] rgb) {
			for (int i=0;i<3;i++)
			buf[i] = (byte) (rgb[i] % 256);
			sendData(COLOR + AUTOINCREMENT, buf, 3);
		}

		
		public void setHSLColor(int hue, int saturation, int luminosity) {
			float _hue = (hue % 360) / 360f;
			float _saturation = (saturation % 101) / 100f;
			float _luminosity = (luminosity % 101) / 100f;
			double v;
			double r, g, b;

			r = _luminosity; // default to gray
			g = _luminosity;
			b = _luminosity;
			v = (_luminosity <= 0.5) ? (_luminosity * (1.0 + _saturation)) : (_luminosity + _saturation - _luminosity * _saturation);
			if (v > 0) {
				double m;
				double sv;
				int sextant;
				double fract, vsf, mid1, mid2;

				m = _luminosity + _luminosity - v;
				sv = (v - m) / v;
				_hue *= 6.0;
				sextant = (int) _hue;
				fract = _hue - (double) sextant;
				vsf = v * sv * fract;
				mid1 = m + vsf;
				mid2 = v - vsf;
				switch (sextant) {
					case 0:
						r = v;
						g = mid1;
						b = m;
						break;
					case 1:
						r = mid2;
						g = v;
						b = m;
						break;
					case 2:
						r = m;
						g = v;
						b = mid1;
						break;
					case 3:
						r = m;
						g = mid2;
						b = v;
						break;
					case 4:
						r = mid1;
						g = m;
						b = v;
						break;
					case 5:
						r = v;
						g = m;
						b = mid2;
						break;
				}
			}
			setColor((int) (r * 255), (int) (g * 255), (int) (b * 255));
		}

		public void setExternalLED(int w) {
			buf[0] = (byte) (w);
			sendData(EXTERNAL, buf, 1);
		}

		public void setBlinkingPattern(float seconds, int percentageOn) {
			int gdc = (int) ((255.0 * percentageOn) / 100.0);
			int gfrq = (int) (seconds * 24 - 1);
			buf[0] = (byte) gdc;
			buf[1] = (byte) gfrq;
			sendData(GRPPWM + AUTOINCREMENT, buf, 2);
		}

		public void enableBlinking() {
			setRegister(LEDOUT, BLINKINGON);
		}

		public void disableBlinking() {
			setRegister(LEDOUT, BLINKINGOFF);
		}

		public int getExternalLED() {
			getData(EXTERNAL, buf, 1);
			return buf[0];
		}

		public void getColor(int[] rgb) {
			getData(COLOR, buf, 3);
			for (int i = 0; i < 3; i++)
				rgb[i] = buf[i];
		}

		public boolean isEnabled() {
			getData(MODE1,buf,1);
			if (buf[0]==MODE1MASK) return true;
			return false;
		}

		public boolean isBlinkingEnabled() {
			getData(LEDOUT,buf,1);
			if (buf[0]==BLINKINGON) return true;
			return false;
		}

	}

}
