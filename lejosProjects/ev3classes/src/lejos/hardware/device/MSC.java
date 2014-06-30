package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

/**
 * This class has been designed to manage the device
 * MSC8, Mindsensors NXT Servo which manages up to 8 RC Servos.
 * 
 * For example, do: 
 * 
 *   <code>msc.servo1.setAngle(angle)</code> 
 *   
 * to set the angle of the servo at location 1.
 * 
 * Many thanks to Luis Bunuel (bunuel66@hotmail.com) in Testing process 
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class MSC extends I2CSensor {
	public static final byte NXTSERVO_ADDRESS = (byte)0xb0;
	public static final byte MSC8_VBATT = 0x41;//The I2C Register to read the battery 

	/**
	 * Servo at location 1
	 */
	public MServo servo1;
	
	/**
	 * Servo at location 2
	 */
	public MServo servo2;
	
	/**
	 * Servo at location 3
	 */
	public MServo servo3;
	
	/**
	 * Servo at location 4
	 */
	public MServo servo4;
	
	/**
	 * Servo at location 5
	 */
	public MServo servo5;
	
	/**
	 * Servo at location 6
	 */
	public MServo servo6;
	
	/**
	 * Servo at location 7
	 */
	public MServo servo7;
	
	/**
	 * Servo at location 8
	 */
	public MServo servo8;
	
	private MServo[] arrServo; //ServoController manages up to 8 RC Servos

	//I2C	
	private I2CPort portConnected;


	private void init(I2CPort port)
	{
        portConnected = port;
        
        servo1 = new MServo(portConnected,1);
        servo2 = new MServo(portConnected,2);
        servo3 = new MServo(portConnected,3);
        servo4 = new MServo(portConnected,4);
        servo5 = new MServo(portConnected,5);
        servo6 = new MServo(portConnected,6);
        servo7 = new MServo(portConnected,7);
        servo8 = new MServo(portConnected,8);
    
        arrServo = new MServo[8];
        arrServo[0] = servo1;
        arrServo[1] = servo2;
        arrServo[2] = servo3;
        arrServo[3] = servo4;
        arrServo[4] = servo5;
        arrServo[5] = servo6;
        arrServo[6] = servo7;
        arrServo[7] = servo8;
	}

	/**
	 * 
	 * Constructor
	 * 
	 * @param port the NXTServo is connected to
	 * 
	 */
    public MSC(Port port){
        super(port);
        this.port.setType(TYPE_LOWSPEED_9V);
        this.setAddress(NXTSERVO_ADDRESS);
    }

    public MSC(I2CPort port){
        super(port);
    }

	/**
	 * Method to get an RC Servo in from the NXTServo
	 * 
	 * @param location location of the servo (from 1 to 8) 
	 * @return the MServo object
	 * 
	 */
	public MServo getServo(int location){
		return arrServo[location-1];
	}

	
	/**
	 * Read the battery voltage data from
	 * NXTServo module (in millivolts)
	 * 
	 * @return the battery voltage in millivolts
	 */
	public int getBattery(){
		byte[] bufReadResponse= new byte[1];
		getData(MSC8_VBATT, bufReadResponse, 1);

		// 37 is calculated from 4700 mv /128
		return(37*(0xFF & bufReadResponse[0]));
	}
}
