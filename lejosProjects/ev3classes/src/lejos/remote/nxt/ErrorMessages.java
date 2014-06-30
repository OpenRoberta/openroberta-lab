package lejos.remote.nxt;

/**
 * Error messages that can be returned after a call to the NXT brick.
 * e.g. The return value comes from method calls like Motor.backward(), 
 * SoundSensor.playTone(), etc... Actual values are only returned if you enable 
 * validation in the NXT class using NXT.setValidation().
 * 
 * @author <a href="mailto:bbagnall@mts.net">Brian Bagnall</a> 
 *
 */	
public class ErrorMessages {
	
	private ErrorMessages() {
		// class cannot be instantiated
	}
	
	// leJOS specific error codes, TODO delete and replace with standard codes 
	public static final byte NOT_IMPLEMENTED = (byte) 0xFD;
	public static final byte DIRECTORY_FULL = (byte) 0xFC;

	// Direct command errors:
	public static final byte PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS = 0x20;
	public static final byte SPECIFIED_MAILBOX_QUEUE_IS_EMPTY = 0x40;
	/** Request failed (i.e. specified file not found) */
	public static final byte REQUEST_FAILED = (byte)0xBD;
	public static final byte UNKNOWN_COMMAND_OPCODE = (byte)0xBE;
	public static final byte INSANE_PACKET = (byte)0xBF;
	public static final byte DATA_CONTAINS_OUT_OF_RANGE_VALUES = (byte)0xC0;
	public static final byte COMMUNICATION_BUS_ERROR = (byte)0xDD;
	public static final byte NO_FREE_MEMORY_IN_COMMUNICATION_BUFFER = (byte)0xDE;
	/** Specified channel/connection is not valid */
	public static final byte SPECIFIED_CHANNEL_CONNECTION_IS_NOT_VALID = (byte)0xDF;
	/** Specified channel/connection not configured or busy */
	public static final byte SPECIFIED_CHANNEL_CONNECTION_NOT_CONFIGURED_OR_BUSY = (byte)0xE0;
	public static final byte NO_ACTIVE_PROGRAM = (byte)0xEC;
	public static final byte ILLEGAL_SIZE_SPECIFIED = (byte)0xED;
	public static final byte ILLEGAL_MAILBOX_QUEUE_ID_SPECIFIED = (byte)0xEE;
	public static final byte ATTEMPTED_TO_ACCESS_INVALID_FIELD_OF_A_STRUCTURE = (byte)0xEF;
	public static final byte BAD_INPUT_OR_OUTPUT_SPECIFIED = (byte)0xF0;
	public static final byte INSUFFICIENT_MEMORY_AVAILABLE = (byte)0xFB;
	public static final byte BAD_ARGUMENTS = (byte)0xFF;
	
	// System command errors:
	public static final byte SUCCESS = 0x00;
	public static final byte NO_MORE_HANDLES = (byte)0x81;
	public static final byte NO_SPACE = (byte)0x82;
	public static final byte NO_MORE_FILES = (byte)0x83;
	public static final byte END_OF_FILE_EXPECTED = (byte)0x84;
	public static final byte END_OF_FILE = (byte)0x85;
	public static final byte NOT_A_LINEAR_FILE = (byte)0x86;
	public static final byte FILE_NOT_FOUND = (byte)0x87;
	public static final byte HANDLE_ALREADY_CLOSED = (byte)0x88;
	public static final byte NO_LINEAR_SPACE = (byte)0x89;
	public static final byte UNDEFINED_ERROR = (byte)0x8A;
	public static final byte FILE_IS_BUSY = (byte)0x8B;
	public static final byte NO_WRITE_BUFFERS = (byte)0x8C;
	public static final byte APPEND_NOT_POSSIBLE = (byte)0x8D;
	public static final byte FILE_IS_FULL = (byte)0x8E;
	public static final byte FILE_EXISTS = (byte)0x8F;
	public static final byte MODULE_NOT_FOUND = (byte)0x90;
	public static final byte OUT_OF_BOUNDARY = (byte)0x91;
	public static final byte ILLEGAL_FILE_NAME = (byte)0x92;
	public static final byte ILLEGAL_HANDLE = (byte)0x93;
	
	public static String lcpErrorToString(byte error)
	{
		String s;
		switch (error)
		{
			// leJOS specific codes:
			case DIRECTORY_FULL:
				s = "Directory full";
				break;
			case NOT_IMPLEMENTED:
				s = "Not implemented";
				break;
			
			// direct command errors:
			case PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS:
				s = "Pending communication transaction in progress";
				break;
			case SPECIFIED_MAILBOX_QUEUE_IS_EMPTY:
				s = "Specified mailbox is empty";
				break;
			case REQUEST_FAILED:
				s = "Request failed";
				break;
			case UNKNOWN_COMMAND_OPCODE:
				s = "Unknown command opcode";
				break;
			case INSANE_PACKET:
				s = "Insane packet";
				break;
			case DATA_CONTAINS_OUT_OF_RANGE_VALUES:
				s = "Data contains out-of-range values";
				break;
			case COMMUNICATION_BUS_ERROR:
				s = "Communication bus error";
				break;
			case NO_FREE_MEMORY_IN_COMMUNICATION_BUFFER:
				s = "No free memory in communication buffer";
				break;
			case SPECIFIED_CHANNEL_CONNECTION_IS_NOT_VALID:
				s = "Specified channel/configuration is not valid";
				break;
			case SPECIFIED_CHANNEL_CONNECTION_NOT_CONFIGURED_OR_BUSY:
				s = "Specified channel/configuration not configured or busy";
				break;
			case NO_ACTIVE_PROGRAM:
				s = "No active program";
				break;
			case ILLEGAL_SIZE_SPECIFIED:
				s = "Illegal size specified";
				break;
			case ILLEGAL_MAILBOX_QUEUE_ID_SPECIFIED:
				s = "Illegal mailbox queue ID specified";
				break;
			case ATTEMPTED_TO_ACCESS_INVALID_FIELD_OF_A_STRUCTURE:
				s = "Attempted to access invalid field of a structure";
				break;
			case BAD_INPUT_OR_OUTPUT_SPECIFIED:
				s = "Bad input or output specified";
				break;
			case INSUFFICIENT_MEMORY_AVAILABLE:
				s = "Insufficient memory available";
				break;
			case BAD_ARGUMENTS:
				s = "Bad arguments";
				break;
		
			// system command errors:
			case NO_MORE_HANDLES:
				s = "No more handles";
				break;
			case NO_SPACE:
				s = "No space";
				break;
			case NO_MORE_FILES:
				s = "No more files";
				break;
			case END_OF_FILE_EXPECTED:
				s = "End of file expected";
				break;
			case END_OF_FILE:
				s = "End of file";
				break;
			case NOT_A_LINEAR_FILE:
				s = "Not a linear file";
				break;
			case FILE_NOT_FOUND:
				s = "File not found";
				break;
			case HANDLE_ALREADY_CLOSED:
				s = "Handle already closed";
				break;
			case NO_LINEAR_SPACE:
				s = "No linear space";
				break;
			case UNDEFINED_ERROR:
				s = "Undefined error";
				break;
			case FILE_IS_BUSY:
				s = "File is busy";
				break;
			case NO_WRITE_BUFFERS:
				s = "No write buffers";
				break;
			case APPEND_NOT_POSSIBLE:
				s = "Append not possible";
				break;
			case FILE_IS_FULL:
				s = "File is full";
				break;
			case FILE_EXISTS:
				s = "File exists";
				break;
			case MODULE_NOT_FOUND:
				s = "Module not found";
				break;
			case OUT_OF_BOUNDARY:
				s = "Out of boundary";
				break;
			case ILLEGAL_FILE_NAME:
				s = "Illegal file name";
				break;
			case ILLEGAL_HANDLE:
				s = "Illegal handle";
				break;
			default:
				s = "Unknown error code ("+(error & 0xFF)+")";
				break;
		}
		return s;
	}
}
