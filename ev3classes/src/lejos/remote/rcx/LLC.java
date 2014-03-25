package lejos.remote.rcx;

import lejos.hardware.device.RCXLink;
import lejos.hardware.port.I2CPort;

/**
 * Emulates RCX LLC class using the RCXLink class.
 * 
 **/
public class LLC {
  private static RCXLink link;
  private static byte[] data = new byte[1];
  
  private LLC() {
  }

  /** 
   * Initialize LLC and set port
   **/
  public static void init(I2CPort port){
	  link = new RCXLink(port);
	  link.setDefaultSpeed();
	  link.flush();
  }
  
  /** 
   * Initialize LLC an
   **/
  public static void init() {
	  link.setDefaultSpeed();
	  link.flush();
  }
  
  /** 
   * Set sensor port
   **/
  public static void setPort(I2CPort port) {
	  link = new RCXLink(port);
  }

  /**
   * read a single byte, if available
   * @return the byte read, or -1 if no byte is available
   **/
  public static int read() {
	  int numBytes = link.bytesAvailable();	  
	  if (numBytes == 0) return -1;
	  data[0] = 0;
	  link.readBytes(data);
	  return (data[0] & 0xFF);
  }

  /**
   * Write raw bytes
   * 
   * @param buf the bytes to write
   * @param len the number of bytes
   */
  private static void write(byte [] buf, int len) {
	  link.sendBytes(buf, len);
  }

  /**
   * Indicate whether the last send is still active
   * @return true if still sending, else false
   **/
  public static boolean isSending() {
	  return false;
  }

  /**
   * Return the error status of the last send
   * @return true if still sending, else false
   **/
  public static boolean isSendError() {
	  return false;
  }

  /**
   * Send a number of bytes and wait for completion of transmission
   * @param buf the array of bytes to send
   * @param len the number of bytes to send
   * @return true if the send is successful, else false
   **/
  public static boolean sendBytes(byte [] buf, int len) {
	  LLC.write(buf, len);
	  return true;
  }

  /**
   * wait a little while for a byte to become available
   * @return the byte received, or -1 if no byte available
   **/
  public static int receive() {
	  int r;
	  for(int i=0;i<10;i++) {
		  r = LLC.read();
		  if (r >= 0) return r; 
		  Thread.yield();
	  }
	  return -1;
  }

  /**
   * Sets long range transmision.
   */
  public static void setRangeLong()
  {
	  link.setRangeLong();  
  }

  /**
   * Sets short range transmision.
   */
  public static void setRangeShort()
  {
	  link.setRangeLong();	  
  }
  
  /**
   * Return the RCXLink object associated with LLC
   * 
   * @return the RCXLink used
   */
  public static RCXLink getLink() {
	  return link;
  }
}



