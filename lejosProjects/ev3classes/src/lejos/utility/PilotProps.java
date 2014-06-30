package lejos.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

/**
 * Configuration class for Differential Pilot.
 * 
 * @author Lawrie Griffiths
 * 
 */
public class PilotProps extends Properties
{
	public static final String PERSISTENT_FILENAME = "pilot.props";

	public static final String KEY_WHEELDIAMETER = "wheelDiameter";
	public static final String KEY_TRACKWIDTH = "trackWidth";
	public static final String KEY_LEFTMOTOR = "leftMotor";
	public static final String KEY_RIGHTMOTOR = "rightMotor";
	public static final String KEY_REVERSE = "reverse";

	public void loadPersistentValues() throws IOException
	{
		File f = new File(PERSISTENT_FILENAME);
		if (!f.exists())
			return;
		
		FileInputStream fis = new FileInputStream(f);
		try
		{
			this.load(fis);
		}
		finally
		{
			fis.close();
		}
	}

	public void storePersistentValues() throws IOException
	{
		File f = new File(PERSISTENT_FILENAME);
		FileOutputStream fos = new FileOutputStream(f);
		try
		{
			this.store(fos, null);
		}
		finally
		{
			fos.close();
		}
	}

	/**
	 * Utility method to get Motor instance from string (A, B or C)
	 */
	public static RegulatedMotor getMotor(String motor)
	{
		if (motor.equals("A"))
			return Motor.A;
		else if (motor.equals("B"))
			return Motor.B;
		else if (motor.equals("C"))
			return Motor.C;
		else
			return null;
	}
}
