package lejos.hardware.ev3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.Keys;
import lejos.hardware.LED;
import lejos.hardware.Power;
import lejos.hardware.Bluetooth;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.LocalWifiDevice;
import lejos.hardware.Wifi;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.internal.ev3.EV3Audio;
import lejos.internal.ev3.EV3DeviceManager;
import lejos.internal.ev3.EV3GraphicsLCD;
import lejos.internal.ev3.EV3Key;
import lejos.internal.ev3.EV3Keys;
import lejos.internal.ev3.EV3LCDManager;
import lejos.internal.ev3.EV3LED;
import lejos.internal.ev3.EV3Port;
import lejos.internal.ev3.EV3Battery;
import lejos.internal.ev3.EV3TextLCD;

/**
 * This class represents the local instance of an EV3 device. It can be used to
 * obtain access to the various system resources (Sensors, Motors etc.).
 * @author andy
 *
 */
public class LocalEV3 implements EV3
{
    static
    {
        // Check that we have EV3 hardware available
        EV3DeviceManager.getLocalDeviceManager();
    }
    
    public static final int ID_UP = 0x1;
    public static final int ID_ENTER = 0x2;
    public static final int ID_DOWN = 0x4;
    public static final int ID_RIGHT = 0x8;
    public static final int ID_LEFT = 0x10;
    public static final int ID_ESCAPE = 0x20;
    
    public static final LocalEV3 ev3 = new LocalEV3();
    protected EV3LCDManager lcdManager;
    protected final Power battery = new EV3Battery();
    protected final Audio audio = EV3Audio.getAudio();
    protected ArrayList<EV3Port> ports = new ArrayList<EV3Port>();
    protected TextLCD textLCD;
    protected GraphicsLCD graphicsLCD;
    protected EV3Keys keys = new EV3Keys();
    protected final LED led = new EV3LED();
    
    protected final Key enter = new EV3Key(keys, "Enter");
    protected final Key escape = new EV3Key(keys, "Escape");
    protected final Key left = new EV3Key(keys, "Left");
    protected final Key right = new EV3Key(keys, "Right");
    protected final Key up = new EV3Key(keys, "Up");
    protected final Key down = new EV3Key(keys, "Down");
    
    protected final Key[] keyArray = { up, enter, down, right, left,  escape };
    
    private LocalEV3()
    {
        // Create the port objects
        ports.add(new EV3Port("S1", EV3Port.SENSOR_PORT, 0));
        ports.add(new EV3Port("S2", EV3Port.SENSOR_PORT, 1));
        ports.add(new EV3Port("S3", EV3Port.SENSOR_PORT, 2));
        ports.add(new EV3Port("S4", EV3Port.SENSOR_PORT, 3));
        ports.add(new EV3Port("A", EV3Port.MOTOR_PORT, 0));
        ports.add(new EV3Port("B", EV3Port.MOTOR_PORT, 1));
        ports.add(new EV3Port("C", EV3Port.MOTOR_PORT, 2));
        ports.add(new EV3Port("D", EV3Port.MOTOR_PORT, 3));
    }
    
    public static EV3 get()
    {
        return ev3;
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public Port getPort(String portName)
    {
        for(EV3Port p : ports)
            if (p.getName().equals(portName))
                return p;
        throw new IllegalArgumentException("No such port " + portName);
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public Power getPower()
    {
        return battery;
    }
    
    protected void initLCD()
    {
        if (lcdManager == null)
        {
            lcdManager = EV3LCDManager.getLocalLCDManager();
            lcdManager.newLayer("LCD");
        }
    }

	@Override
	public TextLCD getTextLCD() 
	{
        initLCD();
		if (textLCD == null)
		{
		    initLCD();
		    textLCD = new EV3TextLCD("LCD");
		}
		return textLCD;
	}

	@Override
	public GraphicsLCD getGraphicsLCD() 
	{
            initLCD();
		if (graphicsLCD == null) 
		{
		    initLCD();
		    graphicsLCD = new EV3GraphicsLCD("LCD"); 
		}
		return graphicsLCD;
	}

	@Override
	public TextLCD getTextLCD(Font f) 
	{
        initLCD();
		return new EV3TextLCD("LCD", f);
	}
	
    /** {@inheritDoc}
     */    
    @Override
    public Audio getAudio()
    {
        return audio;
    }

	@Override
	public boolean isLocal() 
	{
		return true;
	}

	@Override
	public String getType() 
	{
		return "EV3";
	}

	@Override
	public String getName() 
	{		
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader("/etc/hostname"));
			String name = in.readLine().trim();
			in.close();
			return name;
		} catch (IOException e) 
		{
			return "Not known";
		}
	}

	@Override
	public LocalBTDevice getBluetoothDevice() 
	{
		return Bluetooth.getLocalDevice();
	}

	@Override
	public LocalWifiDevice getWifiDevice() 
	{
		return Wifi.getLocalDevice("wlan0");
	}

	@Override
	public void setDefault() 
	{
		BrickFinder.setDefault(this);		
	}

	@Override
	public Key getKey(String name) 
	{
		return keyArray[EV3Key.getKeyPos(name)];
	}

	@Override
	public LED getLED() 
	{
		return led;
	}

	@Override
	public Keys getKeys() 
	{
		return keys;
	}
}
