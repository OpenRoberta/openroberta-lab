package de.fhg.iais.roberta.codegen.lejos;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import lejos.hardware.Audio;
import lejos.hardware.Keys;
import lejos.hardware.LED;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.EncoderMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltraSSensor;
import de.fhg.iais.roberta.conf.transformer.BrickConfiguration;
import de.fhg.iais.roberta.conf.transformer.HardwareComponent;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Connection class between generated code and leJOS methods
 * 
 * @author dpyka
 */
public class Hal
{

	private final BrickConfiguration brickConfiguration;

	private final EV3 brick;
	private final TextLCD lcd;
	private final GraphicsLCD glcd;
	private final LED led;
	private final Keys keys;
	private final Audio audio;

	private final Map<ActorPort, RegulatedMotor> lejosRegulatedMotorBindings = new TreeMap<>();
	private final Map<ActorPort, EncoderMotor> lejosUnregulatedMotorBindings = new TreeMap<>();

	private final Map<SensorPort, BaseSensor> lejosSensorBindings = new TreeMap<>();
	private final Map<SensorPort, String> lejosSensorModeNames = new TreeMap<>();
	private final Map<SensorPort, SampleProvider> lejosSampleProvider = new TreeMap<>();

	/**
	 * @param brickConfiguration
	 */
	public Hal(BrickConfiguration brickConfiguration)
	{
		this.brickConfiguration = brickConfiguration;
		this.brick = LocalEV3.get();
		// TODO test if needed for Motor/ Sensorport enums from lejos??
		this.brick.setDefault();
		this.lcd = this.brick.getTextLCD();
		this.glcd = this.brick.getGraphicsLCD();
		this.led = this.brick.getLED();
		this.keys = this.brick.getKeys();
		this.audio = this.brick.getAudio();
	}

	/**
	 * instantiate all lejos objects for devices that are needed
	 * 
	 * @param actorPort1
	 * @param actorPort2
	 * @param actorPort3
	 * @param actorPort4
	 * @param sensorPort1
	 * @param sensorPort2
	 * @param sensorPort3
	 * @param sensorPort4
	 */
	public void createDeviceObjectsFromConfiguration(ActorPort actorPort1, ActorPort actorPort2, ActorPort actorPort3,
			ActorPort actorPort4, SensorPort sensorPort1, SensorPort sensorPort2, SensorPort sensorPort3,
			SensorPort sensorPort4)
	{
		createMotorObject(actorPort1);
		createMotorObject(actorPort2);
		createMotorObject(actorPort3);
		createMotorObject(actorPort4);
		createSampleProvider(sensorPort1);
		createSampleProvider(sensorPort2);
		createSampleProvider(sensorPort3);
		createSampleProvider(sensorPort4);
	}

	/**
	 * @param speedPercent
	 * @return degrees per second
	 */
	private int toDegPerSec(int speedPercent)
	{
		return 720 / 100 * speedPercent;
	}

	/**
	 * @param degreesPerSecond
	 * @return speedPercent
	 */
	private int toPercent(int degPerSec)
	{
		return degPerSec * 100 / 720;
	}

	/**
	 * convert rotations count to motor rotation angle<br>
	 * used for unregulated motors
	 * 
	 * @param rotations
	 * @return
	 */
	private int rotationsToAngle(int rotations)
	{
		return rotations * 360;
	}

	/**
	 * @param actorPort
	 */
	private void createMotorObject(ActorPort actorPort)
	{
		HardwareComponent actorType = null;
		Port hardwarePort = null;
		switch (actorPort)
		{
		case A:
			actorType = this.brickConfiguration.getActorA();
			hardwarePort = MotorPort.A;
			break;
		case B:
			actorType = this.brickConfiguration.getActorB();
			hardwarePort = MotorPort.B;
			break;
		case C:
			actorType = this.brickConfiguration.getActorC();
			hardwarePort = MotorPort.C;
			break;
		case D:
			actorType = this.brickConfiguration.getActorD();
			hardwarePort = MotorPort.D;
			break;
		default:
			throw new DbcException("Invalid actor port!");
		}

		switch (actorType)
		{
		case EV3LargeRegulatedMotor:
			RegulatedMotor ev3LargeRegulatedMotor = new EV3LargeRegulatedMotor(hardwarePort);
			this.lejosRegulatedMotorBindings.put(actorPort, ev3LargeRegulatedMotor);
			break;
		case EV3MediumRegulatedMotor:
			RegulatedMotor ev3MediumRegulatedMotor = new EV3MediumRegulatedMotor(hardwarePort);
			this.lejosRegulatedMotorBindings.put(actorPort, ev3MediumRegulatedMotor);
			break;
		case NXTRegulatedMotor:
			RegulatedMotor nxtRegulatedMotor = new NXTRegulatedMotor(hardwarePort);
			this.lejosRegulatedMotorBindings.put(actorPort, nxtRegulatedMotor);
			break;
		case NXTMotor:
			// EV3Motor can be accessed by NXTMotor as unregulated motor too!!!
			EncoderMotor nxtMotor = new NXTMotor(hardwarePort);
			this.lejosUnregulatedMotorBindings.put(actorPort, nxtMotor);
			break;
		default:
			throw new DbcException("Invalid/unsupported actor name!");
		}
	}

	private void createSampleProvider(SensorPort sensorPort)
	{
		HardwareComponent sensorType = null;
		Port hardwarePort = null;
		BaseSensor sensor = null;

		switch (sensorPort)
		{
		case S1:
			sensorType = this.brickConfiguration.getSensor1();
			hardwarePort = lejos.hardware.port.SensorPort.S1;
			break;
		case S2:
			sensorType = this.brickConfiguration.getSensor2();
			hardwarePort = lejos.hardware.port.SensorPort.S2;
			break;
		case S3:
			sensorType = this.brickConfiguration.getSensor3();
			hardwarePort = lejos.hardware.port.SensorPort.S3;
			break;
		case S4:
			sensorType = this.brickConfiguration.getSensor4();
			hardwarePort = lejos.hardware.port.SensorPort.S4;
			break;
		default:
			throw new DbcException("Invalid sensor port!");
		}

		switch (sensorType)
		{
		case EV3ColorSensor:
			sensor = new EV3ColorSensor(hardwarePort);
			break;
		case EV3IRSensor:
			sensor = new EV3IRSensor(hardwarePort);
			break;
		case EV3GyroSensor:
			sensor = new EV3GyroSensor(hardwarePort);
			break;
		case EV3TouchSensor:
			sensor = new EV3TouchSensor(hardwarePort);
			break;
		case EV3UltrasonicSensor:
			sensor = new EV3UltrasonicSensor(hardwarePort);
			break;
		default:
			throw new DbcException("Invalid/unsupported sensor name!");
		}
		this.lejosSensorBindings.put(sensorPort, sensor);

		setSensorMode(sensorPort, sensor, this.brickConfiguration.getSensorModeName(sensorPort));
	}

	/**
	 * Set the mode of a sensor, save name to lejosSensorModeNames treemap
	 * 
	 * @param sensorPort
	 * @param sensor
	 * @param sensorMode
	 */
	private void setSensorMode(SensorPort sensorPort, BaseSensor sensor, String sensorMode)
	{
		// set & save the sampleprovider (^= the mode) of a sensor
		SampleProvider sp = sensor.getMode(sensorMode.toString());
		this.lejosSampleProvider.put(sensorPort, sp);
		// save the sensorModeName (only used for return sensorModeName block!, keep
		// synchronized with sampleprovider treemap)
		this.lejosSensorModeNames.put(sensorPort, sensorMode);
	}

	/**
	 * TODO change to enum instead of string??
	 * 
	 * @param sensorPort
	 * @return
	 */
	private String getSensorModeName(SensorPort sensorPort)
	{
		return this.lejosSensorModeNames.get(sensorPort).toString();
	}

	/**
	 * 
	 * @param sensorPort
	 * @return
	 */
	private BaseSensor getSensor(SensorPort sensorPort)
	{
		return this.lejosSensorBindings.get(sensorPort);
	}

	/**
	 * 
	 * @param sensorPort
	 * @return
	 */
	private SampleProvider getSampleProvider(SensorPort sensorPort)
	{
		return this.lejosSampleProvider.get(sensorPort);
	}

	/**
	 * 
	 * @param actorPort
	 * @return
	 */
	private boolean isRegulated(ActorPort actorPort)
	{
		if (this.lejosRegulatedMotorBindings.containsKey(actorPort))
		{
			return true;
		}
		else if (this.lejosUnregulatedMotorBindings.containsKey(actorPort))
		{
			return false;
		}
		else
		{
			throw new DbcException("Invalid Actor Port!");
		}
	}

	/**
	 * 
	 * @param actorPort
	 * @return
	 */
	private RegulatedMotor getRegulatedMotor(ActorPort actorPort)
	{
		return this.lejosRegulatedMotorBindings.get(actorPort);
	}

	/**
	 * 
	 * @param actorPort
	 * @return
	 */
	private EncoderMotor getUnregulatedMotor(ActorPort actorPort)
	{
		return this.lejosUnregulatedMotorBindings.get(actorPort);
	}

	// --- Aktion Bewegung ---

	/**
	 * @param actorPort
	 * @param speedPercent
	 */
	public void setMotorSpeed(ActorPort actorPort, int speedPercent)
	{
		if (isRegulated(actorPort))
		{
			getRegulatedMotor(actorPort).setSpeed(toDegPerSec(speedPercent));
		}
		else
		{
			getUnregulatedMotor(actorPort).setPower(speedPercent);
		}

	}

	/**
	 * Method does not return until finished!!!<br>
	 * TODO test unregulated part
	 * 
	 * @param actorPort
	 * @param speedPercent
	 * @param rotations
	 */
	public void rotateMotor(ActorPort actorPort, int speedPercent, int rotations)
	{
		if (isRegulated(actorPort))
		{
			getRegulatedMotor(actorPort).setSpeed(toDegPerSec(speedPercent));
			getRegulatedMotor(actorPort).rotate(rotations);
		}
		else
		{
			getUnregulatedMotor(actorPort).forward();
			while (getUnregulatedMotor(actorPort).getTachoCount() < rotationsToAngle(rotations))
			{
				// do nothing
			}
			getUnregulatedMotor(actorPort).stop();
		}
	}

	/**
	 * @param actorPort
	 * @return
	 */
	public int getSpeed(ActorPort actorPort)
	{
		if (isRegulated(actorPort))
		{
			return toPercent(getRegulatedMotor(actorPort).getSpeed());
		}
		else
		{
			return getUnregulatedMotor(actorPort).getPower();
		}
	}

	/**
	 * @param actorPort
	 * @param floating
	 */
	public void stopMotor(ActorPort actorPort, boolean floating)
	{
		if (isRegulated(actorPort))
		{
			if (floating)
			{
				getRegulatedMotor(actorPort).flt();
			}
			else
			{
				getRegulatedMotor(actorPort).stop();
			}
		}
		else
		{
			if (floating)
			{
				getUnregulatedMotor(actorPort).flt();
			}
			else
			{
				getUnregulatedMotor(actorPort).stop();
			}
		}
	}

	// --- END Aktion Bewegung ---
	// --- Aktion Fahren ---

	/**
	 * @param actorPort1
	 *          left motor
	 * @param actorPort2
	 *          right motor
	 * @param direction
	 *          forward or backward
	 * @param speedPercent
	 */
	public void drive(ActorPort actorPort1, ActorPort actorPort2, DriveAction.Direction direction, int speedPercent)
	{
		DifferentialPilot dPilot = new DifferentialPilot(this.brickConfiguration.getWheelDiameter(),
				this.brickConfiguration.getTrackWidth(), getRegulatedMotor(actorPort1), getRegulatedMotor(actorPort2), false);
		dPilot.setRotateSpeed(toDegPerSec(speedPercent));
		switch (direction)
		{
		case FOREWARD:
			dPilot.forward();
			break;
		case BACKWARD:
			dPilot.backward();
			break;
		}
	}

	/**
	 * TODO also implementation for unregulated motors??
	 * 
	 * @param actorPort1
	 *          left motor
	 * @param actorPort2
	 *          right motor
	 * @param direction
	 *          forward or backward
	 * @param speedPercent
	 * @param distance
	 *          in cm
	 */
	public void driveDistance(ActorPort actorPort1, ActorPort actorPort2, DriveAction.Direction direction,
			int speedPercent, int distance)
	{
		DifferentialPilot dPilot = new DifferentialPilot(this.brickConfiguration.getWheelDiameter(),
				this.brickConfiguration.getTrackWidth(), getRegulatedMotor(actorPort1), getRegulatedMotor(actorPort2), false);
		dPilot.setRotateSpeed(toDegPerSec(speedPercent));
		switch (direction)
		{
		case FOREWARD:
			dPilot.travel(distance);
			break;
		case BACKWARD:
			dPilot.travel(-distance);
			break;
		}
	}

	/**
	 * 
	 * @param actorPort1
	 * @param actorPort2
	 */
	public void stop(ActorPort actorPort1, ActorPort actorPort2)
	{
		if (isRegulated(actorPort1) && isRegulated(actorPort2))
		{
			getRegulatedMotor(actorPort1).stop();
			getRegulatedMotor(actorPort2).stop();
		}
		else
		{
			getUnregulatedMotor(actorPort1).stop();
			getUnregulatedMotor(actorPort2).stop();
		}
	}

	/**
	 * @param actorPort1
	 *          left motor
	 * @param actorPort2
	 *          right motor
	 * @param direction
	 *          right or left
	 * @param speedPercent
	 */
	public void rotateDirection(ActorPort actorPort1, ActorPort actorPort2, TurnAction.Direction direction,
			int speedPercent)
	{
		if (isRegulated(actorPort1) && isRegulated(actorPort2))
		{
			DifferentialPilot dPilot = new DifferentialPilot(this.brickConfiguration.getWheelDiameter(),
					this.brickConfiguration.getTrackWidth(), getRegulatedMotor(actorPort1), getRegulatedMotor(actorPort2), false);
			dPilot.setRotateSpeed(toDegPerSec(speedPercent));
			switch (direction)
			{
			case RIGHT:
				dPilot.rotateRight();
				break;
			case LEFT:
				dPilot.rotateLeft();
				break;
			}
		}
		else
		{
			getUnregulatedMotor(actorPort1).setPower(speedPercent);
			getUnregulatedMotor(actorPort2).setPower(speedPercent);
			switch (direction)
			{
			case RIGHT:
				getUnregulatedMotor(actorPort1).forward();
				getUnregulatedMotor(actorPort2).backward();
				break;
			case LEFT:
				getUnregulatedMotor(actorPort1).backward();
				getUnregulatedMotor(actorPort2).forward();
				break;
			}
		}
	}

	/**
	 * TODO conversion from distance to motor rotation in angle<br>
	 * TODO implementation for unregulated motor too?<br>
	 * no regulation necessary for this block (->beate)
	 * 
	 * @param actorPort1
	 *          left motor
	 * @param actorPort2
	 *          right motor
	 * @param direction
	 *          right or left
	 * @param speedPercent
	 * @param distance
	 */
	public void rotateDirectionDistance(ActorPort actorPort1, ActorPort actorPort2, TurnAction.Direction direction,
			int speedPercent, int distance)
	{
		int angle = distance * 0;
		switch (direction)
		{
		case RIGHT:
			getRegulatedMotor(actorPort1).rotate(-angle);
			getRegulatedMotor(actorPort2).rotate(angle);
			break;
		case LEFT:
			getRegulatedMotor(actorPort1).rotate(angle);
			getRegulatedMotor(actorPort2).rotate(-angle);
			break;
		}
	}

	/**
	 * Placeholder!!!<br>
	 * TODO no block for this method so far<br>
	 * 
	 * @param actorPort1
	 * @param actorPort2
	 * @param direction
	 * @param speedPercent
	 * @param angle
	 */
	public void rotateDirectionAngle(ActorPort actorPort1, ActorPort actorPort2, TurnAction.Direction direction,
			int speedPercent, int angle)
	{
		DifferentialPilot dPilot = new DifferentialPilot(this.brickConfiguration.getWheelDiameter(),
				this.brickConfiguration.getTrackWidth(), getRegulatedMotor(actorPort1), getRegulatedMotor(actorPort2), false);
		dPilot.setRotateSpeed(toDegPerSec(speedPercent));
		switch (direction)
		{
		case RIGHT:
			dPilot.rotate(angle, false);
			break;
		case LEFT:
			angle = angle * -1;
			dPilot.rotate(angle, false);
			break;
		}
	}

	// --- END Aktion Fahren ---
	// --- Aktion Anzeige ---

	/**
	 * @param text
	 * @param x
	 * @param y
	 */
	public void drawText(String text, int x, int y)
	{
		lcd.drawString(text, x, y);
	}

	/**
	 * TODO map smiley selection to image<br>
	 * TODO create images
	 * 
	 * @param smiley
	 * @param x
	 * @param y
	 */
	public void drawPicture(int smiley, int x, int y)
	{
		Image image = null;
		switch (smiley)
		{
		case 1:
			// number to image
			break;
		case 2:
			// number to image
			break;
		case 3:
			// number to image
			break;
		case 4:
			// number to image
			break;
		}
		glcd.drawImage(image, x, y, 0);
	}

	public void clearDisplay()
	{
		lcd.clear();
	}

	// --- END Aktion Anzeige ---
	// -- Aktion Klang ---

	/**
	 * @param frequency
	 * @param duration
	 */
	public void playTone(int frequency, int duration)
	{
		audio.playTone(frequency, duration);
	}

	/**
	 * play one out of 4 soundfiles<br>
	 * TODO create sound files + directory reference
	 * 
	 * @param fileNumber
	 */
	public void playFile(int fileNumber)
	{
		switch (fileNumber)
		{
		case 1:
			audio.playSample(new File(""));
			break;
		case 2:
			audio.playSample(new File(""));
			break;
		case 3:
			audio.playSample(new File(""));
			break;
		case 4:
			audio.playSample(new File(""));
			break;
		default:
			throw new DbcException("wrong file number");
		}
	}

	/**
	 * set the volume of the speaker
	 * 
	 * @param volume
	 */
	public void setVolume(int volume)
	{
		audio.setVolume(volume);
	}

	/**
	 * @return volume of the speaker
	 */
	public int getVolume()
	{
		return audio.getVolume();
	}

	// -- END Aktion Klang ---
	// --- Aktion Statusleuchte ---

	/**
	 * TODO enum for blink 3colors x3 modes
	 * 
	 * @param color
	 * @param blink
	 */
	public void ledOn(LightAction.Color color, boolean blink)
	{
		switch (color)
		{
		case GREEN:
			if (blink)
			{
				led.setPattern(4);
			}
			else
			{
				led.setPattern(1);
			}
			break;
		case RED:
			if (blink)
			{
				led.setPattern(5);
			}
			else
			{
				led.setPattern(2);
			}
			break;
		case ORANGE:
			if (blink)
			{
				led.setPattern(6);
			}
			else
			{
				led.setPattern(3);
			}
			break;
		}
	}

	/**
	 * turn off led
	 */
	public void ledOff()
	{
		led.setPattern(0);
	}

	/**
	 * TODO @beate function of the block???
	 */
	public void resetLED()
	{
		led.setPattern(0);
	}

	// --- END Aktion Statusleuchte ---
	// --- Sensoren Berührungssensor ---

	/**
	 * @param sensorPort
	 * @return
	 */
	public boolean isPressed(SensorPort sensorPort)
	{
		// always 1 cell for touch sensor
		float[] sample = new float[getSampleProvider(sensorPort).sampleSize()];
		getSampleProvider(sensorPort).fetchSample(sample, 0);
		if (sample[0] == 1.0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// --- END Sensoren Berührungssensor ---
	// --- Sensoren Ultraschallsensor ---

	/**
	 * TODO change String to enum<br>
	 * set ultrasonic mode to distance or presence
	 * 
	 * @param sensorPort
	 * @param mode
	 */
	public void setUltrasonicSensorMode(SensorPort sensorPort, UltraSSensor.Mode mode)
	{
		setSensorMode(sensorPort, getSensor(sensorPort), mode.toString());
	}

	/**
	 * @param sensorPort
	 * @return
	 */
	public String getUltraSonicModeName(SensorPort sensorPort)
	{
		return getSensorModeName(sensorPort);
	}

	/**
	 * Returns value of Ultrasonic sensor<br>
	 * if mode is set to "distance: returns value in cm<br>
	 * if mode is set to "listen": returns ?? :-D<br>
	 * 
	 * @param sensorPort
	 * @return
	 */
	public int getUltraSonicValue(SensorPort sensorPort)
	{
		float[] sample = new float[getSampleProvider(sensorPort).sampleSize()];
		getSampleProvider(sensorPort).fetchSample(sample, 0);
		if (getSensorModeName(sensorPort).equals(UltraSSensor.Mode.PRESENCE))
		{
			return Math.round(sample[0]);
		}
		else
		// UltraSSensor.Mode.Distance or "default" ^= distance
		{
			return Math.round(sample[0]) * 100; // ^= distance in cm
		}
	}

	// END Sensoren Ultraschallsensor ---
	// --- Sensoren Farbsensor ---

	/**
	 * TODO interpretation/conversion before return
	 * 
	 * @param sensorPort
	 * @return
	 */
	public int getColorValue(SensorPort sensorPort)
	{
		float[] sample = new float[getSampleProvider(sensorPort).sampleSize()];
		getSampleProvider(sensorPort).fetchSample(sample, 0);
		if (getSensorModeName(sensorPort).equals(ColorSensor.Mode.AMBIENTLIGHT))
		{
			return Math.round(sample[0]);
		}
		else if (getSensorModeName(sensorPort).equals(ColorSensor.Mode.COLOUR))
		{
			return Math.round(sample[0]);
		}
		else if (getSensorModeName(sensorPort).equals(ColorSensor.Mode.LIGHT))
		{
			return Math.round(sample[0]);
		}
		else
		{
			throw new DbcException("invalid colorsensor mode");
		}
	}

	// END Sensoren Farbsensor ---
	// --- Sensoren IRSensor ---
	/**
	 * TODO
	 * 
	 * @param sensorPort
	 * @return
	 */
	public int getIRValue(SensorPort sensorPort)
	{
		return 0;
	}

	// END Sensoren IRSensor ---
	// --- Aktorsensor Drehsensor ---
	/**
	 * TODO
	 * 
	 * @param actorPort
	 */
	public void resetMotorTacho(ActorPort actorPort)
	{
		//
	}

	/**
	 * TODO
	 * 
	 * @param actorPort
	 * @return
	 */
	public int getMotorTachoValue(ActorPort actorPort)
	{
		return 0;
	}

	// END Aktorsensor Drehsensor ---
	// --- Sensor Gyrosensor ---
	/**
	 * TODO
	 * 
	 * @param sensorPort
	 * @return
	 */
	public int getGyroValue(SensorPort sensorPort)
	{
		return 0;
	}

	/**
	 * 
	 * @param sensorPort
	 */
	public void resetGyro(SensorPort sensorPort)
	{
		//
	}
	// END Sensor Gyrosensor
}
