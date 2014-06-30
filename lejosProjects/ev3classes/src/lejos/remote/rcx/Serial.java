package lejos.remote.rcx;

import lejos.hardware.device.RCXLink;
import lejos.hardware.port.I2CPort;
import lejos.utility.Delay;

/**
 * Emulation of the RCX Serial class with Mindsensors NRLINK adapter.
 * 
 * @author Lawrie Griffiths
 * 
 */
public class Serial {
	private static RCXLink link;
	private static byte[] buf1 = new byte[1];
	private static byte[] packet = new byte[32];
	private static boolean gotOpcode = false;
	private static boolean gotPacket = false;
	private static boolean skipping = false;
	private static int paramsRead;
	private static int paramsRequired;
	private static int checkSum;
		
	private Serial() {		
	}
	
	/**
	 * Set the sensor port
	 * 
	 * @param port the sensor port the link is connected to
	 */
	public static void setPort(I2CPort port) {
		link = new RCXLink(port);
		link.setDefaultSpeed();
		link.flush();
	}

	/**
	 * Read an assembled packet. NRLink only seems to read
	 * one byte at a time reliably, and does not
	 * return zero bytes. 
	 * 
	 * @param aBuffer the buffer to return the packet into
	 * @return the number of bytes in the packet
	 */
	public static int readPacket (byte[] aBuffer) {
		if (!gotPacket) return 0;
		gotPacket = false;
		gotOpcode = false;
		for(int i=0;i<paramsRequired+1;i++) aBuffer[i] = packet[i];
		return paramsRequired+1;
	}
	
	/**
	 * Test if a packet is available
	 * 
	 * @return true iff a packiet is available
	 */
	public static boolean isPacketAvailable() {
		if (gotPacket) return true;
		
		int available = link.bytesAvailable();
		
		// After a failure, skip to next header
		
		if (skipping) {
			while(available> 0) {
				readByte(buf1);
				//LCD.drawInt(buf1[0] & 0xFF, 4, 8, 7);
				//LCD.refresh();
				Delay.msDelay(50);
				available--;
				if (headerByte(buf1[0])) {
					skipping = false;
					break;
				}
			}
			if (skipping) return false;
			gotOpcode = false;
			available = link.bytesAvailable();
		}
		
		// If we don't have the opcode yet, skip header bytes
		
		if (!gotOpcode) {
			byte op = 0;
			
			// skip header bytes, and check complement 
			// for candidate opcode.
			
			while (available > 1) { // Need 2 bytes
				readByte(buf1);
				available--;
				op = buf1[0];
				if (!headerByte(op)) {
					readByte(buf1);
					available--;
					if (complement(buf1[0], op)) {
						gotOpcode = true;
						break;
					} else {					
						// Skip to header if complement check failed				
						skipping = true;
						return false;
					}
				}
			}
			
			// If we have the op code, calculate number of parameters
			// and start the checksum
			
			if (!gotOpcode) return false;			
			available = link.bytesAvailable();
			packet[0] = op;
			int pq = op & 0x7;
			paramsRequired = 0;
			if (pq > 1 && pq < 6) paramsRequired = pq; 
			if (pq == 7) paramsRequired = 1;
			paramsRead = 0;
			checkSum = (op & 0xFF);
			//LCD.drawInt(opCode & 0xFF,4, 0,6);
			//LCD.drawInt(paramsRequired,4, 0,7);
			//LCD.refresh();
			//try {Thread.sleep(500);} catch (InterruptedException e) {}
		}
		
		// If we don't have all the parameters, get them
		// and check the complements
		
        if (paramsRead < paramsRequired) {
        	while (available > 1) {
    			readByte(buf1);
    			available--;
    			byte param = buf1[0];
    			readByte(buf1);
    			available--;
    			if (complement(buf1[0], param)) {
    				paramsRead++;
    				checkSum += (param & 0xFF);
    				packet[paramsRead] = param;
    			} else {
    				skipping = true;
    				//LCD.drawInt(param &0xFF, 4,  0, 4);
    				//LCD.drawInt(buf1[0] & 0xFF, 4, 8, 4);
    				//LCD.refresh();
    				return false;
    			}
    			if (paramsRead == paramsRequired) break;
        	}
        	if (paramsRead != paramsRequired) return false;
        	available = link.bytesAvailable();
        }

        // Check the checksum and its complement
        
		if (available > 1) {
			readByte(buf1);
			available--;
			byte checkDigit = buf1[0];
   			readByte(buf1);
			available--;
			//LCD.drawInt(checkDigit &0xFF, 4,  0, 5);
			//LCD.drawInt(checkSum & 0xFF, 4, 8, 5);
			//LCD.refresh();
			if (complement(buf1[0], checkDigit) &&
			    (checkDigit & 0xFF) == (checkSum & 0xFF)) {
				gotPacket = true;
			} else {
				// Skip to header if complement check
				// or checksum fails
				skipping = true;
			}
		}

		return gotPacket;
	}
	
	/**
	 * Send a packet
	 * 
	 * @param aBuffer the buffer containing the packet
	 * @param aOffset the offset in the buffer - must be zero
	 * @param aLen the length of the packet
	 * @return true iff the packet was successfully sent
	 */
	public static boolean sendPacket (byte[] aBuffer, int aOffset, int aLen) {
		sleep();
		link.defineAndRun(aBuffer, aLen); // assumes offset 0
		sleep();
		return true;
	}

	/**
	 * Set long range
	 *
	 */
	public static void setRangeLong() {
		link.setRangeLong();
	}
	 
	/**
	 * Set short range
	 * 
	 */
	public static void setRangeShort() {
		 link.setRangeLong();
	}
	 
	/**
	 * Reset the link - null
	 *
	 */
	public static void resetSerial() {
	}
	 
	/**
	 * Wait until the packet is sent - null
	 *
	 */
	public static void waitTillSent() {	 
	}
	 
	/**
	 * Get the RCXLink object associated with the Serial class
	 * 
	 * @return the link
	 */
	public static RCXLink getLink() {
		 return link;
	}

	private static void sleep() {
        Delay.msDelay(100);
	}
	
	private static boolean headerByte(byte b) {
		return(b == 0x55 || b == (byte) 0xFF || b == 0x00);
		
	}
	
	private static void readByte(byte [] b) {
		b[0] = 0;
		link.readBytes(b);
	}
	
	private static boolean complement(byte b1, byte b2) {
		return ((b1 &0xFF) + (b2 & 0xFF) == 0xFF);
	}
}
