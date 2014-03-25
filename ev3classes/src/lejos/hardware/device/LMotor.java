package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.I2CSensor;

/**
 * Generic abstraction to manage RC Servos and DC Motor.
 * LServo and LDCMotor uses inherits from this class
 *
 * @author Juan Antonio Brenha Moral
 *
 */
public class LMotor extends I2CSensor{
	private String name = "";//String to describe any Motor connected to LSC
	protected int LSC_position; //Position where Servo has been plugged
	
	//Servo ID
	private SensorPort portConnected;//What
	protected byte SPI_PORT;//What SPI Port is connected LSC

	public static final int arrMotorUnload[] = {0x01,0x02,0x04,0x08,0x20,0x40,0x80,0x100,0x200};
	public static final int arrMotorLoad[] = {0x3FE,0x3FD,0x3FB,0x3F7,0x3EF,0x3DF,0x3BF,0x37F,0x2FF,0x1FF};
	
    /**
     * Constructor
     * 
     * @param port the port
     * @param location the location 
     * @param name the name of the servo
     * @param SPI_PORT the SPI port
     *  
     */
    public LMotor(I2CPort port, int location, String name, byte SPI_PORT){
        super(port, NXTe.NXTE_ADDRESS);
        this.name = name;
        this.LSC_position = location;
        
        this.SPI_PORT = SPI_PORT;
    }   
    
    /**
     * Constructor
     * 
     * @param port the port
     * @param location the location 
     * @param name the name of the servo
     * @param SPI_PORT the SPI port
     *  
     */
    public LMotor(Port port, int location, String name, byte SPI_PORT){
        super(port, NXTe.NXTE_ADDRESS);
        this.name = name;
        this.LSC_position = location;
        
        this.SPI_PORT = SPI_PORT;
    }   
    
	/**
	 * 
	 * private method to know internal information about 
	 * if the servo is moving
	 * 
	 * @return
	 * 
	 */
	private int readMotion(){
		byte[] bufReadResponse;
		bufReadResponse = new byte[8];
		byte h_byte;
		byte l_byte;
		
		int motion = -1;
		
		//Write OP Code
		sendData((int)this.SPI_PORT, (byte)0x68);
		
		//Read High Byte
		sendData((int)this.SPI_PORT, (byte)0x00);	
		getData((int)this.SPI_PORT, bufReadResponse, 1);
		h_byte = bufReadResponse[0];

		//Read Low Byte
		sendData((int)this.SPI_PORT, (byte)0x00);	
		getData((int)this.SPI_PORT, bufReadResponse, 1);
		l_byte = bufReadResponse[0];
	
		if(l_byte == 0xFF){
			motion =  ((h_byte & 0x07 ) << 8) + 255;
		}else{
			motion = ((h_byte & 0x07 ) << 8)|(l_byte&0xFF);
		}
		return motion;
	}	
	
	/**
	 * Method to know if Servo is moving to a determinated angle
	 * 
	 * @return true iff the servo is moving
	 */
	public boolean isMoving(){
		boolean flag = false;
		if(readMotion() != 0){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * Set a delay in a Motor
	 * 
	 * @param delay the delay
	 */
	public void setDelay(int delay){
		byte h_byte;
		byte l_byte;
		
		int motor = LSC_position;
		h_byte = (byte)0xF0;
		l_byte = (byte)(((motor)<<4) + delay);
	     
		sendData((int)this.SPI_PORT, h_byte);
		sendData((int)this.SPI_PORT, l_byte);
	}
	
	public void unload(){
		byte[] bufReadResponse;
		byte h_byte;
		byte l_byte;		
		
		int channel = 0x00;
		channel = arrMotorUnload[LSC_position];
		
		h_byte = (byte)0xe0; //0xe0 | (0x00 >>(byte)8); //?? 
		l_byte = (byte)channel;
	     
	    //High Byte Write
		sendData((int)this.SPI_PORT, h_byte);

	    //Low Byte Write
		sendData((int)this.SPI_PORT, l_byte);		
	}
	
	/**
	 * Load Servo located in a position X
	 * 
	 */
	public void load(){
		byte h_byte;
		byte l_byte;		
		
		int channel = 0x00;
		channel = arrMotorLoad[LSC_position];
		
		h_byte = (byte)0xe0; //0xe0 | (0x00 >>(byte)8); //?? 
		l_byte = (byte)channel;
	     
	    //High Byte Write
		sendData((int)this.SPI_PORT, h_byte);

	    //Low Byte Write
		sendData((int)this.SPI_PORT, l_byte);		
	}

	/**
	 * Get name from a RC Servo or a DC Motor
	 * 
	 */	
	public String getName(){
		return this.name;
	}

	/**
	 * This class set the Pulse over a RC Servo or a DC Motor
	 * 
	 * @param pulse
	 */
	protected void setPulse(int pulse){
		byte h_byte;
		byte l_byte;
		
		int servo = LSC_position;
		h_byte = (byte)(0x80 | ((servo<<3) | (pulse >>8)));
	    l_byte = (byte)pulse;
		
	    //High Byte Write
		sendData((int)this.SPI_PORT, h_byte);

	    //Low Byte Write
		sendData((int)this.SPI_PORT, l_byte);
	}

	/**
	 * This method return current pulse over a RC Servo or a DC Motor.
	 * This method is used internally by LDCMotor Objects or LServo Objects
	 * to get the speed or angle.
	 * 
	 * @return the pulse
	 */
	protected int getPulse(){
		byte[] bufReadResponse;
		bufReadResponse = new byte[8];
		byte h_byte;
		byte l_byte;		
		
		int servo = LSC_position;
	    //Write OP Code
	    h_byte  = (byte)(servo << 3);
		sendData((int)this.SPI_PORT, h_byte);
		
	    //Read High Byte
	    //I2CBytes(IN_3, bufReadValue, buflen, bufReadResponse);
		sendData((int)this.SPI_PORT, (byte)0x00);
		getData((int)this.SPI_PORT, bufReadResponse, 1);
		
	    h_byte = bufReadResponse[0];
	    
	    //Read Low Byte
		sendData((int)this.SPI_PORT, (byte)0x00);
		getData((int)this.SPI_PORT, bufReadResponse, 1);
	    l_byte = bufReadResponse[0];
	    
	    return  ((h_byte & 0x07 ) << 8) +  (l_byte & 0x00000000FF);
	}
}
