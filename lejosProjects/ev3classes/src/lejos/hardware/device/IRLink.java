package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;
import lejos.remote.rcx.Opcode;

import java.util.*;

/**
 * Supports for HiTechnic NXT IRLink Sensor (NIL1046) IRLink.
 * 
 * @author Lawrie Griffiths
 *
 */
public class IRLink extends I2CSensor implements Opcode, IRTransmitter {
	
	/*
	 * Documentation: http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NIL1046
	 * 
	 * ProductId: "HiTechnc"
	 * SensorType: "IRLink"
	 * (confirmed for version " V1.2")
	 */
	
	//Registers
	private final static byte TX_BUFFER = 0x40; // 40 to 4C
	private final static byte TX_BUFFER_LEN = 0x4D;
	private final static byte TX_MODE = 0x4E;
	private final static byte TX_BUFFER_FLAG = 0x4F;
	
	private final static byte TX_MAX_BUFFER_LEN = 13;
	
	// IRLink transmission modes
	private final static byte TX_MODE_RCX = 0;
	private final static byte TX_MODE_TRAIN = 1;
	private final static byte TX_MODE_PF = 2;
	
	// PF Modes
	public final static byte PF_MODE_COMBO_DIRECT = 1;
	
	// IR PF signal encoding parameters
	private final byte MAX_BITS = TX_MAX_BUFFER_LEN * 8;
	private final byte STOP_START_PAUSE = 7;
	private final byte LOW_BIT_PAUSE = 2;
	private final byte HIGH_BIT_PAUSE = 4;
	
	// PF motor operations
	public static final byte PF_FLOAT = 0;
	public static final byte PF_FORWARD = 1;
	public static final byte PF_BACKWARD = 2;
	public static final byte PF_BRAKE = 3;
	
	// RCX Remote operation code
	public static int RCX_REMOTE_BEEP  = 0x8000;
	public static int RCX_REMOTE_STOP  = 0x4000;
	public static int RCX_REMOTE_P5    = 0x2000;
	public static int RCX_REMOTE_P4    = 0x1000;
	public static int RCX_REMOTE_P3    = 0x0800;
	public static int RCX_REMOTE_P2    = 0x0400;
	public static int RCX_REMOTE_P1    = 0x0200;
	public static int RCX_REMOTE_C_BWD = 0x0100;
	public static int RCX_REMOTE_B_BWD = 0x0080;
	public static int RCX_REMOTE_A_BWD = 0x0040;
	public static int RCX_REMOTE_C_FWD = 0x0020;
	public static int RCX_REMOTE_B_FWD = 0x0010;
	public static int RCX_REMOTE_A_FWD = 0x0008;
	public static int RCX_REMOTE_MSG3  = 0x0004;
	public static int RCX_REMOTE_MSG2  = 0x0002;
	public static int RCX_REMOTE_MSG1  = 0x0001;
	
	private byte toggle = 0;
	
	private BitSet bits = new BitSet(MAX_BITS);
	private int nextBit = 0;

    public IRLink(I2CPort port) {
        super(port);
    }
    
    public IRLink(Port port) {
        super(port);
    }
    
	/**
	 * Send commands to both motors.
	 * Uses PF Combo direct mode.
	 * 
	 * @param channel the channel number (0-3)
	 * @param opA Motor A operation
	 * @param opB Motor B operation
	 */
	public void sendPFComboDirect(int channel, int opA, int opB) {
		sendPFCommand(channel, PF_MODE_COMBO_DIRECT, opB << 2 | opA);
	}

	private void sendPFCommand(int channel, int mode, int data) {
		byte nibble1 = (byte) ((toggle << 3) | channel);
		byte lrc = (byte) (0xF ^ nibble1 ^ mode ^ data);
		int pfData = (nibble1 << 12) | (mode << 8) | (data << 4) | lrc;

		clearBits();
		nextBit = 0;
		setBit(STOP_START_PAUSE); // Start
		for(int i=15;i>=0;i--) {
			setBit(((pfData >> i) & 1) == 0 ? LOW_BIT_PAUSE : HIGH_BIT_PAUSE);
		}
		setBit(STOP_START_PAUSE); // Stop
		toggle ^= 1;
		byte [] pfCommand = new byte[16];
		
		for(int i =0;i<MAX_BITS;i++) {
			boolean bit = bits.get(i);
			int byteIndex = i/8;
			int bitVal = (bit ? 1 : 0);
			pfCommand[byteIndex] |= (bitVal << (7 - i%8));
		}
		
		pfCommand[13] = TX_MAX_BUFFER_LEN;
	    pfCommand[14] = TX_MODE_PF;
	    pfCommand[15] = 1;

		sendData(TX_BUFFER, pfCommand, TX_MAX_BUFFER_LEN+3);
	}
	
	private void setBit(int pause) {
		bits.set(nextBit++);
		nextBit += pause;
	}
	
	private void clearBits() {
		for(int i=0;i<MAX_BITS;i++) bits.clear(i);
	}

	/*
	 * Send up to 8 bytes to the RCX
	 */
	private void sendBytes8(byte[] data, int len) {
		byte[] buf = new byte[len+3];
		int register = TX_BUFFER_LEN - len;
		
		// Copy data up to the byte before TX_BUFFER_LEN
		for(int i=0;i<len;i++) buf[i] = data[i];
		buf[len] = (byte) len;
		buf[len+1] = TX_MODE_RCX;
		
		// Trigger the transmission
		buf[len+2] = (byte) 1;
		sendData(register,buf, len+3);
	}
	
	public void sendBytes(byte[] data, int len) {
		byte[] buf = new byte[8];
		int bufLen = 0;
		
		// Split data into 8 byte chunks and send it
		for(int i=0; i< (len-1)/8 + 1;i++) {
			bufLen = 0;
			for(int j=i*8;j<(i+1)*8 && j < len;j++) {
				buf[j % 8] = data[j];
				bufLen++;
			}
			sendBytes8(buf, bufLen);
			
			// Wait 5 milliseconds per byte for the transmission to complete
			try {
				Thread.sleep(bufLen * 5);
			} catch (InterruptedException e) {}
		}
	}
	
	public void sendPacket(byte[] data) {
		byte[] packet = new byte[data.length*2+5];
		int checksum = 0;
		
		// Header
		packet[0] = (byte) 0x55;
		packet[1] = (byte) 0xff;
		packet[2] = (byte) 0x00;
		
		//Data
		for(int i=0;i<data.length;i++) {
			packet[2*i + 3] = data[i];
			checksum += data[i];
			//Complement
			packet[2*i + 4] = (byte) (0xff - (data[i] & 0xff));
		}
		
		// Checksum
		checksum &= 0xff;
		packet[2*data.length+3] = (byte) checksum;
		packet[2*data.length+4] = (byte)(255 - checksum);
		
		// Send the packet
		sendBytes(packet,packet.length);
	}
	
	public void sendRemoteCommand(int msg) {
		byte[] buf = new byte[3];
		buf[0] = OPCODE_REMOTE_COMMAND;
		buf[1] = (byte) (msg >> 8);
		buf[2] = (byte) (msg & 0xFF);
		sendPacket(buf);
	}
	
	public void runProgram(int programNumber) {
		sendRemoteCommand(RCX_REMOTE_P1 << (programNumber -1));
	}
	
	public void beep() {
		sendRemoteCommand(RCX_REMOTE_BEEP);
	}
	
	public void stopAllPrograms() {
		sendRemoteCommand(RCX_REMOTE_STOP);		
	}
	
	public void forwardStep(int motor) {
		sendRemoteCommand(RCX_REMOTE_A_FWD << motor);
	}
	
	public void backwardStep(int motor) {
		sendRemoteCommand(RCX_REMOTE_A_BWD << motor);
	}
}
