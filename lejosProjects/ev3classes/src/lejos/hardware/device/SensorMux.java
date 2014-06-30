package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * @author 3DGamer (LeJOS Forum User)
 * @author Juan Antonio Brenha Moral
 * @author Xander Soldaat
 */
public class SensorMux extends I2CSensor {
   
	private final int STATUS_BATTERY_BIT = 1;
	private final byte digitalRegisters[] = {0x40,0x50,0x60,0x70};
	private final byte analogRegisters[] = {0x36,0x38,0x3A,0x3C};
	
    /**
     * Constructor
     * @param port the port
     */
    public SensorMux(I2CPort port) {
        super(port, 0x10);
    }
   
    /**
     * Constructor
     * @param port the port
     */
    public SensorMux(Port port) {
        super(port, 0x10, TYPE_LOWSPEED_9V);
    }
   
   /**
    * Return the type
    * @return the type
    */
   public int getType() {
   	   byte[] buf = new byte[1];
	   getData(0x23,buf,1);
	   return buf[0] & 0xff;
   }

   /**
    * Method used to get if battery status is low.
    * @return true if the battery is low, else false
    */
   public boolean isBatteryLow(){
	  byte[] buf = new byte[1];
	  getData(0x21,buf,1);
	  return ((buf[0]&STATUS_BATTERY_BIT)!=0);
   }
   
   /**
    * This method return the value from a digital sensor.
    * Currently, SMux supports Ultrasonic Sensors
    * 
    * @param channel the index of the channel
    * @return the digital value 
    */
   private int getDigitalValue(int channel){
	   byte[] buf = new byte[1];
	   byte register = digitalRegisters[channel-1];
	   getData(register,buf,1);
	   int digitalValue = buf[0] & 0xff;
	   return digitalValue;
   }
   
   /**
    * This method return the value from a analogic sensor.
    * Currently, SMux supports Touch sensor and Sound sensor
    * 
    * @param channel the index of the channel
    * @return the analog value
    */
   private int getAnalogValue(int channel){
	   byte[] buf = new byte[1];
	   byte register = analogRegisters[channel-1];
	   getData(register,buf,1);
	   int analogValue = buf[0] & 0xff;
	   return analogValue;
   }
   
   /**
    * This method is necessary to execute to connect sensors on it
    */
   public void configurate(){

	     //Set the SMUX in halted mode: 0x10 0x20 0x00
	     sendData(0x20, (byte)0x00);

	     // Wait 50 ms for SMUX to clean up
	     try{Thread.sleep(50);}catch(Exception e){}

	     //# Send auto-scan command: 0x10 0x20 0x01
	     sendData(0x20, (byte)0x01);

	     //# wait 500ms for auto-scan to complete
	     try{Thread.sleep(600);}catch(Exception e){}

	     //# Set the SMUX in normal mode: 0x10 0x20 0x02
	     sendData(0x20, (byte)0x02);
	}
   
   /**
    * Method used to use a US with the sensor and get the distances
    * @param channel the index of the channel
    * @return the distance
    */
   public int getDistance(int channel){
	   int distance = 0;
	   distance = getDigitalValue(channel);	   
	   return distance;
   }
   
   /**
    * Method used to manage a Touch Sensor
    * 
    * @param channel the index of the channel
    * @return true if the sensor is presed, else false
    */
   public int isPressed(int channel){
	   int pressed = 0;
	   pressed = getAnalogValue(channel);
	   if(pressed == 255){
		   pressed = 0;
	   }else{
		   pressed  = 1;
	   }
	   return pressed;
   }
   
   /**
    * Method used to receive data from a Sound Sensor
    * 
    * @param channel the index of the channel
    * @return the value
    */
   public int readValue(int channel){
	   int value = 0;
	   value = getAnalogValue(channel);
	   value = ((1023 - value) * 100/ 1023);
	   return value;
   }
} 
