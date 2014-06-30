package lejos.hardware.device;

import lejos.hardware.motor.RCXMotor;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;
import lejos.remote.rcx.Opcode;
import lejos.remote.rcx.RCXRemoteMotorPort;
import lejos.utility.Delay;

/**
 * Supports Mindsensors NRLink RCX IR adapter.
 * 
 * @author Lawrie Griffiths <lawrie.griffiths@ntlworld.com>
 *
 */
public class RCXLink extends I2CSensor implements Opcode, IRTransmitter {
	
	private byte[] buf = new byte[4];
	
	public RCXMotor A = new RCXMotor(new RCXRemoteMotorPort(this,0));
	public RCXMotor B = new RCXMotor(new RCXRemoteMotorPort(this,1));
	public RCXMotor C = new RCXMotor(new RCXRemoteMotorPort(this,2));

	private final static byte SLOW_SPEED = 0x44; // Default 2400 baud
	private final static byte FLUSH = 0x46; // Flush the FIFO buffer
	private final static byte HIGH_SPEED = 0x48; // 4800 baudprivate final static byte LONG_RANGE = 0x4C;
	private final static byte SHORT_RANGE = 0x53;
	private final static byte LONG_RANGE = 0x4c;
	private final static byte TRANSMIT_RAW_MACRO = 0x55; // Transmit Unassembled raw macro data;
	private final static byte COMMAND = 0x41;
	private final static byte RUN = 0x52;
	private final static byte ARPA_ON = 0x4E;
	private final static byte ARPA_OFF = 0x4F;
	private final static byte STATUS_REG = 0x41;
	private final static byte RX_DATA_LEN = 0x40;
	private final static byte TX_DATA_LEN = 0x40;
	private final static byte RX_DATA = 0x42;
	private final static byte TX_DATA = 0x42;
	
	// ROM Macro Definitions:
	public final static byte SHORT_RANGE_IR = 0x01;
	public final static byte LONG_RANGE_IR = 0x04;
	public final static byte POWER_OFF_RCX = 0x07;
	public final static byte RUN_PROGRAM_1 = 0x09;
	public final static byte RUN_PROGRAM_2 = 0x0D;
	public final static byte RUN_PROGRAM_3 = 0x11;
	public final static byte RUN_PROGRAM_4 = 0x15;
	public final static byte RUN_PROGRAM_5 = 0x19;
	public final static byte STOP_ALL_PROGRAMS = 0x1D;
	public final static byte MOTOR_A_FORWARD = 0x21;
	public final static byte MOTOR_A_REVERSED = 0x25;
	public final static byte MOTOR_B_FORWARD = 0x29;
	public final static byte MOTOR_B_REVERSED = 0x2D;
	public final static byte MOTOR_C_FORWARD = 0x31;
	public final static byte MOTOR_C_REVERSED = 0x35;
	/**
	 * NOTE: The BEEP macro is unreliable. 
     * It works once, and then needs another command executed
     * before it works again.
	 */
	public final static byte BEEP = 0x39;	
		
	public final static int EEPROM_BUFFER = 0x78;
	public final static int DELAY = 10;
	
    public RCXLink(I2CPort port) {
        super(port);
    }

    public RCXLink(Port port) {
        super(port);
    }

	public void runMacro(int addr) {
		buf[0] = RUN;
		buf[1] = (byte) addr;
		
		sendData(COMMAND, buf, 2);
	}
	
	public void beep() {
		runMacro(BEEP);
	}
	
	public void runProgram(int programNumber) {
		runMacro(RUN_PROGRAM_1 + ((programNumber-1)*4));
	}
	
	public void forwardStep(int id) { // RCX Remote Command
		runMacro(MOTOR_A_FORWARD + (id*8));
	}
	
	public void backwardStep(int id) { // RCX Remote Command
		runMacro(MOTOR_A_REVERSED + (id*8));
	}
	
	public void setRCXRangeShort() {
		runMacro(SHORT_RANGE_IR);
	}
	
	public void setRCXRangeLong() {
		runMacro(LONG_RANGE_IR);
	}
	
	public void powerOff() {
		runMacro(POWER_OFF_RCX);
	}
	
	public void stopAllPrograms() {
		runMacro(STOP_ALL_PROGRAMS);
	}
	
	public void flush() {
		sendData(COMMAND, FLUSH);
	}
	
	public void setDefaultSpeed() {
		sendData(COMMAND, SLOW_SPEED);
	}
	
	public void setHighSpeed() {
		sendData(COMMAND, HIGH_SPEED);
	}
	
	public void setRangeLong() {
		sendData(COMMAND, LONG_RANGE);
	}

	public void setRangeShort() {
		sendData(COMMAND, SHORT_RANGE);
	}
	
	public void setAPDAOn() {
		sendData(COMMAND, ARPA_ON);
	}
	
	public void setAPDAOff() {

		sendData(COMMAND, ARPA_OFF);
	}
	
	public void defineMacro(int addr, byte[] macro) {
		sendData((byte) addr,(byte) macro.length);
		sleep();
		sendData((byte) addr+1, macro, macro.length);
	}
	
	public int getStatus() {
		getData(STATUS_REG, buf, 1);
		return buf[0] & 0xFF;
	}
	
	public int bytesAvailable() {
		getData(RX_DATA_LEN, buf, 1);
		return buf[0] & 0xFF;
	}
	
	public void ping() {
		buf[0] = OPCODE_ALIVE;
		defineAndRun(buf,1);
	}
	
	public void sendF7(int msg) {
		buf[0] = OPCODE_SET_MESSAGE;
		buf[1] = (byte) (msg & 0xFF);
		defineAndRun(buf,2);
	}
	
	public void sendPacket(byte [] packet) {
		defineAndRun(packet, packet.length);
	}
	
	public void sendRemoteCommand(int msg) {
		buf[0] = OPCODE_REMOTE_COMMAND;
		buf[1] = (byte) (msg >> 8);
		buf[2] = (byte) (msg & 0xFF);
		defineAndRun(buf,3);
	}
	
	public void setMotorPower(int id, int power) { 
		buf[0] = OPCODE_SET_MOTOR_POWER;
		buf[1] = (byte) (1 << id);
		buf[2] = 2;
		buf[3] = (byte) power;
		defineMacro(EEPROM_BUFFER, buf); // Bug: sendData cannot send more than 3 bytes
		sleep();
		sendData(EEPROM_BUFFER+4, (byte) power);
		sleep();
		runMacro(EEPROM_BUFFER);
	}
	
	public void stopMotor(int id) {
		buf[0] = OPCODE_SET_MOTOR_ON_OFF;
		buf[1] = (byte) ((1 << id) | 0x40);
		defineAndRun(buf,2);
	}
	
	public void startMotor(int id) {
		buf[0] = OPCODE_SET_MOTOR_ON_OFF;
		buf[1] = (byte) ((1 << id) | 0x80);
		defineAndRun(buf,2);
	}
	
	public void fltMotor(int id) {
		buf[0] = OPCODE_SET_MOTOR_ON_OFF;
		buf[1] = (byte) (1 << id);
		defineAndRun(buf,2);
	}
	
	public void forward(int id) {
		buf[0] = OPCODE_SET_MOTOR_DIRECTION;
		buf[1] = (byte) ((1 << id) | 0x80);
		defineAndRun(buf,2);
	}
	
	public void backward(int id) {
		buf[0] = OPCODE_SET_MOTOR_DIRECTION;
		buf[1] = (byte) (1 << id);
		defineAndRun(buf,2);
	}
	
	public void setRawMode() {
		sendData(COMMAND, TRANSMIT_RAW_MACRO);
	}
	
	public void sendBytes(byte[] data, int len) {
		sendData(TX_DATA,data,len);
		sleep();
		sendData(TX_DATA_LEN, (byte) len);
	}
	
	public int readBytes(byte [] data) {
		getData(RX_DATA_LEN,buf,1);
		int numBytes = buf[0];
		if (numBytes > 0) {
			if (numBytes > data.length) numBytes = data.length;
			sleep();
			getData(RX_DATA,data,numBytes);
		}
		return numBytes;
	}
	
	private void sleep() {
        Delay.msDelay(DELAY);
	}
	
	public void defineAndRun(byte[] macro, int len) {
		sendData(EEPROM_BUFFER,(byte) len);
		sleep();
		sendData(EEPROM_BUFFER+1, macro, len);
		sleep();		
		runMacro(EEPROM_BUFFER);
	}
}
