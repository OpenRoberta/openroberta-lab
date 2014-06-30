package lejos.remote.nxt;

/**
 * LEGO Communication Protocol constants.
 *
 */
public interface NXTProtocol {
	// Command types constants. Indicates type of packet being sent or received.
	public static final byte DIRECT_COMMAND_REPLY = 0x00;
	public static final byte SYSTEM_COMMAND_REPLY = 0x01;
	public static final byte REPLY_COMMAND = 0x02;
	public static final byte DIRECT_COMMAND_NOREPLY = (byte)0x80; // Avoids ~100ms latency
	public static final byte SYSTEM_COMMAND_NOREPLY = (byte)0x81; // Avoids ~100ms latency
	
	// System Commands:
	public static final byte OPEN_READ = (byte)0x80;
	public static final byte OPEN_WRITE = (byte)0x81;
	public static final byte READ = (byte)0x82;
	public static final byte WRITE = (byte)0x83;
	public static final byte CLOSE = (byte)0x84;
	public static final byte DELETE = (byte)0x85;
	public static final byte FIND_FIRST = (byte)0x86;
	public static final byte FIND_NEXT = (byte)0x87;
	public static final byte GET_FIRMWARE_VERSION = (byte)0x88;
	public static final byte OPEN_WRITE_LINEAR = (byte)0x89;
	public static final byte OPEN_READ_LINEAR = (byte)0x8A;
	public static final byte OPEN_WRITE_DATA = (byte)0x8B;
	public static final byte OPEN_APPEND_DATA = (byte)0x8C;
	// Many commands could be hidden between 0x8D and 0x96!
	public static final byte BOOT = (byte)0x97;
	public static final byte SET_BRICK_NAME = (byte)0x98;
	// public static final byte MYSTERY_COMMAND = (byte)0x99;
	// public static final byte MYSTERY_COMMAND = (byte)0x9A;
	public static final byte GET_DEVICE_INFO = (byte)0x9B;
	// commands could be hidden here...
	public static final byte DELETE_USER_FLASH = (byte)0xA0;
	public static final byte POLL_LENGTH = (byte)0xA1;
	public static final byte POLL = (byte)0xA2;
	
	// Poll constants:
	public static final byte POLL_BUFFER = (byte)0x00;
	public static final byte HIGH_SPEED_BUFFER = (byte)0x01;
		
	// Direct Commands
	public static final byte START_PROGRAM = 0x00;
	public static final byte STOP_PROGRAM = 0x01;
	public static final byte PLAY_SOUND_FILE = 0x02;
	public static final byte PLAY_TONE = 0x03;
	public static final byte SET_OUTPUT_STATE = 0x04;
	public static final byte SET_INPUT_MODE = 0x05;
	public static final byte GET_OUTPUT_STATE = 0x06;
	public static final byte GET_INPUT_VALUES = 0x07;
	public static final byte RESET_SCALED_INPUT_VALUE = 0x08;
	public static final byte MESSAGE_WRITE = 0x09;
	public static final byte RESET_MOTOR_POSITION = 0x0A;	
	public static final byte GET_BATTERY_LEVEL = 0x0B;
	public static final byte STOP_SOUND_PLAYBACK = 0x0C;
	public static final byte KEEP_ALIVE = 0x0D;
	public static final byte LS_GET_STATUS = 0x0E;
	public static final byte LS_WRITE = 0x0F;
	public static final byte LS_READ = 0x10;
	public static final byte GET_CURRENT_PROGRAM_NAME = 0x11;
	// public static final byte MYSTERY_OPCODE = 0x12; // ????
	public static final byte MESSAGE_READ = 0x13;
	// public static final byte POSSIBLY_MORE_HIDDEN = 0x14; // ????
	
	// NXJ additions
	// TODO NXJ commands should be direct commands.
	// At the moment, the NXJ commands are system commands. However, the LEGO firmware
	// does not respond with an error code to unknown system commands. Instead, it seems to
	// return the reply of the last successful command or something similar.
	// Hence, the NXJ opcodes should be declared as direct commands. The LEGO firmware will
	// reply with error 0xBE (Unknown command opcode) for all direct command
	// opcodes from 0x22 to 0xff (tested with Firmware 1.31). I suggest the usage of
	// opcodes >= 0x80.
	public static final byte NXJ_DISCONNECT = 0x20; 
	public static final byte NXJ_DEFRAG = 0x21;
	public static final byte NXJ_SET_DEFAULT_PROGRAM = 0x22;
	public static final byte NXJ_SET_SLEEP_TIME = 0x23;
	public static final byte NXJ_SET_VOLUME = 0x24;
	public static final byte NXJ_SET_KEY_CLICK_VOLUME = 0x25;
	public static final byte NXJ_SET_AUTO_RUN = 0x26;
	public static final byte NXJ_GET_VERSION = 0x27;
	public static final byte NXJ_GET_DEFAULT_PROGRAM = 0x28;
	public static final byte NXJ_GET_SLEEP_TIME = 0x29;
	public static final byte NXJ_GET_VOLUME = 0x2A;
	public static final byte NXJ_GET_KEY_CLICK_VOLUME = 0x2B;
	public static final byte NXJ_GET_AUTO_RUN = 0x2C;
    public static final byte NXJ_PACKET_MODE = (byte)0xff;
		
	// Output state constants 
	// "Mode":
	/** Turn on the specified motor */
	public static final byte MOTORON = 0x01;
	/** Use run/brake instead of run/float in PWM */
	public static final byte BRAKE = 0x02;
	/** Turns on the regulation */
	public static final byte REGULATED = 0x04; 

	// "Regulation Mode":
	/** No regulation will be enabled */
	public static final byte REGULATION_MODE_IDLE = 0x00;
	/** Power control will be enabled on specified output */
	public static final byte REGULATION_MODE_MOTOR_SPEED = 0x01;
	/** Synchronization will be enabled (Needs enabled on two output) */
	public static final byte REGULATION_MODE_MOTOR_SYNC = 0x02; 

	// "RunState":
	/** Output will be idle */
	public static final byte MOTOR_RUN_STATE_IDLE = 0x00;
	/** Output will ramp-up */
	public static final byte MOTOR_RUN_STATE_RAMPUP = 0x10;	
	/** Output will be running */
	public static final byte MOTOR_RUN_STATE_RUNNING = 0x20; 
	/** Output will ramp-down */
	public static final byte MOTOR_RUN_STATE_RAMPDOWN = 0x40;
	
	// Input Mode Constants
	// "Port Type":
	/**  */
	public static final byte NO_SENSOR = 0x00;
	/**  */
	public static final byte SWITCH = 0x01;
	/**  */
	public static final byte TEMPERATURE = 0x02;
	/**  */
	public static final byte REFLECTION = 0x03;
	/**  */
	public static final byte ANGLE = 0x04;
	/**  */
	public static final byte LIGHT_ACTIVE = 0x05;
	/**  */
	public static final byte LIGHT_INACTIVE = 0x06;
	/**  */
	public static final byte SOUND_DB = 0x07;
	/**  */
	public static final byte SOUND_DBA = 0x08;
	/**  */
	public static final byte CUSTOM = 0x09;
	/**  */
	public static final byte LOWSPEED = 0x0A;
	/**  */
	public static final byte LOWSPEED_9V = 0x0B;
	/**  */
	public static final byte NO_OF_SENSOR_TYPES = 0x0C;

	// "Port Mode":
	/**  */
	public static final byte RAWMODE = 0x00;
	/**  */
	public static final byte BOOLEANMODE = 0x20;
	/**  */
	public static final byte TRANSITIONCNTMODE = 0x40;
	/**  */
	public static final byte PERIODCOUNTERMODE = 0x60;
	/**  */
	public static final byte PCTFULLSCALEMODE = (byte)0x80;
	/**  */
	public static final byte CELSIUSMODE = (byte)0xA0;
	/**  */
	public static final byte FAHRENHEITMODE = (byte)0xC0;
	/**  */
	public static final byte ANGLESTEPSMODE = (byte)0xE0;
	/**  */
	public static final byte SLOPEMASK = 0x1F;
	/**  */
	public static final byte MODEMASK = (byte)0xE0;
}


