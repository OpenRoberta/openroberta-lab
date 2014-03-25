package lejos.remote.nxt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Sends LCP requests to the NXT and receives replies.
 * Uses an object that implements the NXTComm interface 
 * for low-level communication.
 *
 */
public class NXTCommand implements NXTProtocol {
	
	public static final int MAX_FILENAMELENGTH = 20;

	private NXTCommRequest nxtComm = null;
	private boolean verifyCommand = false;
	private boolean open;
	private static final String hexChars = "01234567890abcdef";
	private static final int MAX_BUFFER_SIZE = 58;
	
	//TODO checks whether open==true are missing all over the place

	/**
	 * Create a NXTCommand object. 
	 */
	public NXTCommand(NXTCommRequest nxtComm) {
		this.nxtComm = nxtComm;
		open = true;
	}

	/**
	 * Toggle the verify flag.
	 * 
	 * @param verify true causes all commands to return a response.
	 */
	public void setVerify(boolean verify) {
		verifyCommand = verify;
	}

	/**
	 * Small helper method to send DIRECT COMMAND request to NXT and return
	 * verification result.
	 * 
	 * @param request
	 * @return
	 */
	private byte sendRequest(byte[] request, int replyLen) throws IOException {
		if (!open)
			throw new IOException("NXTCommand is closed");
		
		byte verify = 0; // default of 0 means success
		if (verifyCommand)
			request[0] = DIRECT_COMMAND_REPLY;

		byte[] reply = nxtComm.sendRequest(request,
				(request[0] == DIRECT_COMMAND_REPLY ? replyLen : 0));
		if (request[0] == DIRECT_COMMAND_REPLY) {
			verify = reply[2];
		}
		return verify;
	}

	/**
	 * Small helper method to send a SYSTEM COMMAND request to NXT and return
	 * verification result.
	 * 
	 * @param request the request
	 * @return the status
	 */
	private byte sendSystemRequest(byte[] request, int replyLen) throws IOException {
		if (!open)
			throw new IOException("NXTCommand is closed");
		
		byte verify = 0; // default of 0 means success
		if (verifyCommand)
			request[0] = SYSTEM_COMMAND_REPLY;

		byte[] reply = nxtComm.sendRequest(request,
				(request[0] == SYSTEM_COMMAND_REPLY ? replyLen : 0));
		if (request[0] == SYSTEM_COMMAND_REPLY) {
			verify = reply[2];
		}
		return verify;
	}

	/**
	 * Starts a program already on the NXT.
	 * 
	 * @param fileName the file name
	 * @return the status
	 */
	public byte startProgram(String fileName) throws IOException {
		byte[] request = { DIRECT_COMMAND_NOREPLY, START_PROGRAM };
		request = appendString(request, fileName);
		byte status = sendRequest(request, 22);
        open = false;
		return status;
	}
	
	/**
	 * Forces the currently executing program to stop.
	 * Not implemented by leJOS NXJ.
	 * 
	 * @return Error value
	 */
	public byte stopProgram() throws IOException {
		byte [] request = {DIRECT_COMMAND_NOREPLY, STOP_PROGRAM};
		return sendRequest(request, 3);
	}
	
	/**
	 * Name of current running program.
	 * Does not work with leJOS NXJ. 
	 * 
	 * @return the program name
	 */
	public String getCurrentProgramName() throws IOException {
		byte [] request = {DIRECT_COMMAND_REPLY, GET_CURRENT_PROGRAM_NAME};
		byte [] reply =  nxtComm.sendRequest(request, 23);
		
		return new StringBuffer(new String(reply)).delete(0, 2).toString();
	}

	/**
	 * Opens a file on the NXT for reading. Returns a handle number and file
	 * size, enclosed in a FileInfo object.
	 * 
	 * @param fileName
	 *            e.g. "Woops.wav"
	 * @return fileInfo object giving details of the file
	 */
	public FileInfo openRead(String fileName) throws IOException {
		byte[] request = { SYSTEM_COMMAND_REPLY, OPEN_READ };
		request = appendString(request, fileName); // No padding required
													// apparently
		byte[] reply = nxtComm.sendRequest(request, 8);
		FileInfo fileInfo = new FileInfo(fileName);
		if (reply[2] != ErrorMessages.SUCCESS)
			throw new LCPException(reply[2]);
		if (reply.length == 8) { // Check if all data included in reply
			fileInfo.fileHandle = reply[3];
			fileInfo.fileSize = (0xFF & reply[4]) | ((0xFF & reply[5]) << 8)
					| ((0xFF & reply[6]) << 16) | ((0xFF & reply[7]) << 24);
		}
		return fileInfo;
	}

	/**
	 * Opens a file on the NXT for writing.
	 * 
	 * @param fileName
	 *            e.g. "Woops.wav"
	 *            
	 * @return File Handle number
	 */
	public byte openWrite(String fileName, int size) throws IOException {
		byte[] command = { SYSTEM_COMMAND_REPLY, OPEN_WRITE };
		byte[] asciiFileName = new byte[fileName.length()];
		for (int i = 0; i < fileName.length(); i++)
			asciiFileName[i] = (byte) fileName.charAt(i);
		command = appendBytes(command, asciiFileName);
		byte[] request = new byte[22];
		System.arraycopy(command, 0, request, 0, command.length);
		byte[] fileLength = { (byte) size, (byte) (size >>> 8),
				(byte) (size >>> 16), (byte) (size >>> 24) };
		request = appendBytes(request, fileLength);
		byte[] reply = nxtComm.sendRequest(request, 4);
		if (reply == null || reply.length != 4) {
			throw new IOException("Invalid return from OPEN WRITE");
		} else if (reply[2] != ErrorMessages.SUCCESS) {
			throw new LCPException(reply[2]);
		}
		return reply[3]; // The handle number
	}

	/**
	 * Closes an open file.
	 * 
	 * @param handle
	 *            File handle number.
	 * @return Error code 0 = success
	 * @throws IOException
	 */
	public byte closeFile(byte handle) throws IOException {
		byte[] request = { SYSTEM_COMMAND_NOREPLY, CLOSE, handle };
		return sendSystemRequest(request, 4);
	}

	/**
	 * Delete a file on the NXT
	 * 
	 * @param fileName the name of the file
	 * @return the error code 0 = success
	 * @throws IOException
	 */
	public byte delete(String fileName) throws IOException {
		byte[] request = { SYSTEM_COMMAND_REPLY, DELETE };
		request = appendString(request, fileName);
		return sendSystemRequest(request, 23);
	}

	/**
	 * Find the first file on the NXT.
	 * 
	 * @param wildCard
	 *            [filename].[extension], *.[extension], [filename].*, *.*
	 * @return fileInfo object giving details of the file
	 */
	public FileInfo findFirst(String wildCard) throws IOException {

		byte[] request = { SYSTEM_COMMAND_REPLY, FIND_FIRST };
		request = appendString(request, wildCard);

		byte[] reply = nxtComm.sendRequest(request, 28);
		FileInfo fileInfo = null;
		if (reply[2] == 0 && reply.length == 28) {
			StringBuffer name = new StringBuffer(new String(reply))
					.delete(0, 4);
			int lastPos = name.indexOf("\0");
			if (lastPos < 0 || lastPos > 20) lastPos = 20;
			name.delete(lastPos, name.length());
			fileInfo = new FileInfo(name.toString());
			fileInfo.fileHandle = reply[3];
			fileInfo.fileSize = (0xFF & reply[24]) | ((0xFF & reply[25]) << 8)
					| ((0xFF & reply[26]) << 16) | ((0xFF & reply[27]) << 24);
		}
		return fileInfo;
	}

	/**
	 * Find the next file on the NXT
	 * 
	 * @param handle
	 *            Handle number from the previous found file or from the Find
	 *            First command.
	 * @return fileInfo object giving details of the file
	 */
	public FileInfo findNext(byte handle) throws IOException {

		byte[] request = { SYSTEM_COMMAND_REPLY, FIND_NEXT, handle };

		byte[] reply = nxtComm.sendRequest(request, 28);
		FileInfo fileInfo = null;
		if (reply[2] == 0 && reply.length == 28) {
			StringBuffer name = new StringBuffer(new String(reply))
					.delete(0, 4);
			int lastPos = name.indexOf("\0");
			if (lastPos < 0 || lastPos > 20) lastPos = 20;
			name.delete(lastPos, name.length());
			fileInfo = new FileInfo(name.toString());
			fileInfo.fileHandle = reply[3];
			fileInfo.fileSize = (0xFF & reply[24]) | ((0xFF & reply[25]) << 8)
					| ((0xFF & reply[26]) << 16) | ((0xFF & reply[27]) << 24);
		}
		return fileInfo;
	}

	/**
	 * Helper code to append a string and null terminator at the end of a
	 * command request. 
	 * 
	 * @param command the command
	 * @param str the string to append
	 * @return the concatenated command
	 */
	private byte[] appendString(byte[] command, String str) {
		byte[] buff = new byte[command.length + str.length() + 1];
		for (int i = 0; i < command.length; i++)
			buff[i] = command[i];
		for (int i = 0; i < str.length(); i++)
			buff[command.length + i] = (byte) str.charAt(i);
		buff[command.length + str.length()] = 0;
		return buff;
	}

	/**
	 * Helper method to concatenate two byte arrays
	 * @param array1 the first array (e.g. a request)
	 * @param array2 the second array (e.g. an extra parameter)
	 * 
	 * @return the concatenated array
	 */
	private byte[] appendBytes(byte[] array1, byte[] array2) {
		byte[] array = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, array, 0, array1.length);
		System.arraycopy(array2, 0, array, array1.length, array2.length);
		return array;
	}

	/**
	 * Get the battery reading
	 * 
	 * @return the battery level in millivolts
	 * @throws IOException
	 */
	public int getBatteryLevel() throws IOException {
		byte[] request = { DIRECT_COMMAND_REPLY, GET_BATTERY_LEVEL };
		byte[] reply = nxtComm.sendRequest(request, 5);
		int batteryLevel = (0xFF & reply[3]) | ((0xFF & reply[4]) << 8);
		return batteryLevel;
	}
	
	/**
	 * Call the close() command when your program ends, otherwise you will have
	 * to turn the NXT brick off/on before you run another program.
	 * @deprecated call disconnect, then close the underlying NXTComm
	 */
	@Deprecated
	public void close() throws IOException {
		if (!open) return;
		open = false;
		this.disconnect();
		nxtComm.close();
	}

	/**
	 * Tell the NXT that the connection is aborted.
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		byte[] request = { SYSTEM_COMMAND_REPLY, NXJ_DISCONNECT };
		nxtComm.sendRequest(request, 3); // Tell NXT to disconnect
		
		// like boot(), this should probably mark this NXTCommand as closed
		this.open = false;
	}

	/**
	 * Put the NXT into SAMBA mode, ready to update the firmware.
	 * Marks this NXTCommand object as closed.
	 * Does never never wait for a reply from the NXT.
	 *
	 * @throws IOException
	 */
    public void boot() throws IOException {
		if (!open)
			throw new IOException("NXTCommand is closed");
		
        byte[] request = {SYSTEM_COMMAND_NOREPLY, BOOT};
        request = appendString(request, "Let's dance: SAMBA");
        nxtComm.sendRequest(request, 0);
        // Connection cannot be used after this command so we close it
        open = false;
    }
    
    /**
     * Write data to the file
     * 
     * @param handle the file handle
     * @param data the data to write
     * @return the status value
     * 
     * @throws IOException
     */
	public byte writeFile(byte handle, byte[] data, int offset, int length) throws IOException {
		byte[] command = { SYSTEM_COMMAND_NOREPLY, WRITE, handle };
		int remaining = length;
		int chunkStart = offset;
		while (remaining > 0) {
			int chunkLen = MAX_BUFFER_SIZE;
			if (remaining < chunkLen)
				chunkLen = remaining;
			byte [] request = new byte[chunkLen + 3];
			System.arraycopy(command, 0, request, 0, command.length);
			System.arraycopy(data, chunkStart, request, 3, chunkLen);

			byte status = sendSystemRequest(request, 6);
			if (status != 0)
				return status;
			remaining -= chunkLen;
			chunkStart += chunkLen;
		}
		return 0;
	}
	
	/**
	 * Upload a file to the NXT
	 * 
	 * @param file the file to upload
	 * @param nxtFileName the name of the file on the NXT
	 * @return a message saying how long it took to upload the file
	 * 
	 * @throws IOException
	 */
	public String uploadFile(File file, String nxtFileName) throws IOException {
	    long millis = System.currentTimeMillis();
	    FileInputStream in = new FileInputStream(file);
	    try
	    {
			byte handle = openWrite(nxtFileName, (int) file.length());
			byte[] data = new byte[MAX_BUFFER_SIZE];
			int len;
			while ((len = in.read(data)) > 0)
			{
				writeFile(handle, data, 0, len);
			}
			setVerify(true);
			closeFile(handle);
			return "Upload successful in " + (System.currentTimeMillis() - millis) + " milliseconds";
	    }
	    finally
	    {
	    	in.close();
	    }
	}

	/**
	 * Returns requested number of bytes from a file. File must first be opened
	 * using the openRead() command.
	 * 
	 * @param handle File handle number (from openRead method)
	 * @param data Buffer to which data is written
	 * @param offset Index of first byte to be overwritten
	 * @param length Number of bytes to read
	 * @return number of bytes read
	 */
	public int readFile(byte handle, byte[] data, int offset, int length) throws IOException {
		int remaining = length;
		int chunkStart = offset;
		while (remaining > 0) {
			int chunkLen = MAX_BUFFER_SIZE;
			if (chunkLen > remaining)
				chunkLen = remaining;
			byte[] request = { SYSTEM_COMMAND_REPLY, READ, handle, (byte) chunkLen,
					(byte) (chunkLen >>> 8) };
			byte[] reply1 = nxtComm.sendRequest(request, chunkLen + 6);
			int dataLen = (reply1[4] & 0xFF) + ((reply1[5] & 0xFF) << 8);
			System.arraycopy(reply1, 6, data, chunkStart, dataLen);
			chunkStart += chunkLen;
			remaining -= chunkLen;
		}
		return length;
	}

	/**
	 * A NXJ extension to defrag the file system
	 * 
	 * @return the status byte
	 * @throws IOException
	 */
	public byte defrag() throws IOException {
		byte[] request = { SYSTEM_COMMAND_NOREPLY, NXJ_DEFRAG };
		return sendSystemRequest(request, 3);
	}

	/**
	 * Get the friendly name of the NXT
	 * @return the friendly name
	 * @throws IOException
	 */
	public String getFriendlyName() throws IOException {
		byte[] request = { SYSTEM_COMMAND_REPLY, GET_DEVICE_INFO };
		byte[] reply = nxtComm.sendRequest(request, 33);
		char nameChars[] = new char[16];
		int len = 0;

		for (int i = 0; i < 15 && reply[i + 3] != 0; i++) {
			nameChars[i] = (char) reply[i + 3];
			len++;
		}

		return new String(nameChars, 0, len);
	}

	/**
	 * Set the friendly name of the NXT
	 * 
	 * @param name the friendly name
	 * @return the status byte
	 * @throws IOException
	 */
	public byte setFriendlyName(String name) throws IOException {
		byte[] request = { SYSTEM_COMMAND_NOREPLY, SET_BRICK_NAME };
		request = appendString(request, name);

		return sendSystemRequest(request, 3);
	}

	/**
	 * Get the local address of the NXT.
	 * 
	 * @return the address (used by USB and Bluetooth)
	 * @throws IOException
	 */
	public String getLocalAddress() throws IOException {
		byte[] request = { SYSTEM_COMMAND_REPLY, GET_DEVICE_INFO };
		byte[] reply = nxtComm.sendRequest(request, 33);
		char addrChars[] = new char[14];

		for (int i = 0; i < 7; i++) {
			addrChars[i * 2] = hexChars.charAt((reply[i + 18] >> 4) & 0xF);
			addrChars[i * 2 + 1] = hexChars.charAt(reply[i + 18] & 0xF);
		}
		
		return new String(addrChars);
	}
	
	/**
	 * Get input values for a specific NXT sensor port
	 * 
	 * @param port the port number
	 * @return the InputValues structure
	 * @throws IOException
	 */
	public InputValues getInputValues(int port) throws IOException {
		byte [] request = {DIRECT_COMMAND_REPLY, GET_INPUT_VALUES, (byte)port};
		byte [] reply = nxtComm.sendRequest(request, 16);
		InputValues inputValues = new InputValues();
		inputValues.inputPort = reply[3];
		// 0 is false, 1 is true.
		inputValues.valid = (reply[4] != 0);
		// 0 is false, 1 is true. 
		inputValues.isCalibrated = (reply[5] == 0);
		inputValues.sensorType = reply[6];
		inputValues.sensorMode = reply[7];
		inputValues.rawADValue = (short) ((0xFF & reply[8]) | ((0xFF & reply[9]) << 8));
		inputValues.normalizedADValue = (short) ((0xFF & reply[10]) | ((0xFF & reply[11]) << 8));
		inputValues.scaledValue = (short) ((0xFF & reply[12]) | ((0xFF & reply[13]) << 8));
		inputValues.calibratedValue = (short) ((0xFF & reply[14]) | ((0xFF & reply[15]) << 8));
		
		return inputValues;
	}
	
	/**
	 * Retrieves the current output state for a port.
	 * @param port - 0 to 3
	 * @return OutputState - returns a container object for output state variables.
	 */
	public OutputState getOutputState(int port) throws IOException {
		// !! Needs to check port to verify they are correct ranges.
		byte [] request = {DIRECT_COMMAND_REPLY, GET_OUTPUT_STATE, (byte)port};
		byte [] reply = nxtComm.sendRequest(request,25);

		OutputState outputState = new OutputState(port);
		outputState.status = reply[2];
		outputState.outputPort = reply[3];
		outputState.powerSetpoint = reply[4];
		outputState.mode = reply[5];
		outputState.regulationMode = reply[6];
		outputState.turnRatio = reply[7];
		outputState.runState = reply[8];
		outputState.tachoLimit = (0xFF & reply[9]) | ((0xFF & reply[10]) << 8)| ((0xFF & reply[11]) << 16)| ((0xFF & reply[12]) << 24);
		outputState.tachoCount = (0xFF & reply[13]) | ((0xFF & reply[14]) << 8)| ((0xFF & reply[15]) << 16)| ((0xFF & reply[16]) << 24);
		outputState.blockTachoCount = (0xFF & reply[17]) | ((0xFF & reply[18]) << 8)| ((0xFF & reply[19]) << 16)| ((0xFF & reply[20]) << 24);
		outputState.rotationCount = (0xFF & reply[21]) | ((0xFF & reply[22]) << 8)| ((0xFF & reply[23]) << 16)| ((0xFF & reply[24]) << 24);
		return outputState;
	}
	
	/**
	 * Retrieves tacho count.
	 * @param port - 0 to 3
	 * @return tacho count
	 */
	public int getTachoCount(int port) throws IOException {
		synchronized(this) {
			byte [] request = {DIRECT_COMMAND_REPLY, GET_OUTPUT_STATE, (byte)port};
			byte [] reply = nxtComm.sendRequest(request, 25);
	
			int tachoCount = (0xFF & reply[13]) | ((0xFF & reply[14]) << 8)| ((0xFF & reply[15]) << 16)| ((0xFF & reply[16]) << 24);
			return tachoCount;
		}
	}
	
	/**
	 * Tells the NXT what type of sensor you are using and the mode to operate in.
	 * @param port - 0 to 3
	 * @param sensorType - Enumeration for sensor type (see NXTProtocol) 
	 * @param sensorMode - Enumeration for sensor mode (see NXTProtocol)
	 */
	public byte setInputMode(int port, int sensorType, int sensorMode) throws IOException {
		// !! Needs to check port to verify they are correct ranges.
		byte [] request = {DIRECT_COMMAND_NOREPLY, SET_INPUT_MODE, (byte)port, (byte)sensorType, (byte)sensorMode};
		return sendRequest(request, 3);
	}
	
	/**
	 * Returns the status for an Inter-Integrated Circuit (I2C) sensor (the 
	 * ultrasound sensor) via the Low Speed (LS) data port. The port must first 
	 * be configured to type LOWSPEED or LOWSPEED_9V.
	 * @param port 0-3
	 * @return byte[0] = status, byte[1] = Bytes Ready (count of available bytes to read)
	 */
	public byte [] LSGetStatus(byte port) throws IOException{
		byte [] request = {DIRECT_COMMAND_REPLY, LS_GET_STATUS, port};
		byte [] reply = nxtComm.sendRequest(request,4);
		byte [] returnData = {reply[2], reply[3]}; 
		return returnData;
	}
	
	/**
	 * Reads data from an Inter-Integrated Circuit (I2C) sensor (the 
	 * ultrasound sensor) via the Low Speed (LS) data port. The port must 
	 * first be configured to type LOWSPEED or LOWSPEED_9V.
	 * Data lengths are limited to 16 bytes per command. The response will
	 * also contain 16 bytes, with invalid data padded with zeros.
	 * @param port
	 * @return the response
	 */
	public byte [] LSRead(byte port) throws IOException {
		byte [] request = {DIRECT_COMMAND_REPLY, LS_READ, port};
		byte [] reply = nxtComm.sendRequest(request, 20);
		
		int rxLength = reply[3] & 0xFF;
		if(reply[2] == 0 && rxLength >= 0) {
            byte [] rxData = new byte[rxLength];
			System.arraycopy(reply, 4, rxData, 0, rxLength);
            return rxData;
		}
		return null;
	}
	
	/**
	 * Used to request data from an Inter-Integrated Circuit (I2C) sensor (the 
	 * ultrasound sensor) via the Low Speed (LS) data port. The port must first 
	 * be configured to type  LOWSPEED or LOWSPEED_9V.
	 * Data lengths are limited to 16 bytes per command.
	 * Rx (receive) Data Length MUST be specified in the write
	 * command since reading from the device is done on a 
	 * master-slave basis.
	 * @param txData Transmitted data.
	 * @param rxDataLength Receive data length.
	 * @param port 0-3
	 * @return the status (0 = success)
	 */
	public byte LSWrite(byte port, byte [] txData, byte rxDataLength) throws IOException {
		byte [] request = {DIRECT_COMMAND_NOREPLY, LS_WRITE, port, (byte)txData.length, rxDataLength};
		request = appendBytes(request, txData);
		return sendRequest(request, 3);
	}
	
	/**
	 * Read message.
	 * @param remoteInbox 0-9
	 * @param localInbox 0-9
	 * @param remove True clears the message from the remote inbox.
	 * @return the message as an array of bytes, excluding the trailing null-terminator or null when queue is empty
	 */
	public byte[] messageRead(byte remoteInbox, byte localInbox, boolean remove) throws IOException {
		byte [] request = {DIRECT_COMMAND_REPLY, MESSAGE_READ, remoteInbox, localInbox, (remove ? (byte) 1 : (byte) 0)};
		byte [] reply = nxtComm.sendRequest(request, 64);
		if (reply[2] == ErrorMessages.SPECIFIED_MAILBOX_QUEUE_IS_EMPTY)
			return null;
		this.checkStatusByte(reply);
		int size = reply[4] & 0xFF; //size includes null terminator 
		// check whether length is in range and for null-terminator
		if (size < 1 || size > 5 + reply.length || reply[4+size] != 0)
			throw new LCPException("protocol error");
		byte[] message = new byte[size-1];
		System.arraycopy(reply, 5, message, 0, size-1);
		return message;
	}
	
	private void checkStatusByte(byte[] reply) throws LCPException {
		byte code = reply[2];
		if (code != 0)
			throw new LCPException(code);
	}

	/**
	 * Sends a message to an inbox on the NXT for storage(?)
	 * For future reference, message size must be capped at 59 for USB.
	 * A null terminator is automatically appended and should not be included in the message.
	 * UNTESTED
	 * @param message String to send.
	 * @param inbox Inbox Number 0 - 9
	 * @return the status (0 = success)
	 */
	public byte messageWrite(byte [] message, byte inbox) throws IOException {
		//TODO check range of number, throw exception if message is too large
		int len = message.length;
		byte[] request = new byte[5 + len];
		request[0] = DIRECT_COMMAND_NOREPLY;
		request[1] = MESSAGE_WRITE;
		request[2] = inbox;
		request[3] = (byte)(len+1); // size includes null-terminator
		System.arraycopy(message, 0, request, 4, len);
		request[4+len] = 0;
		return sendRequest(request, 3);
	}
	
	/**
	 * Plays a tone on NXT speaker. If a new tone is sent while the previous tone is playing,
	 * the new tone command will stop the old tone command.
	 * @param frequency - 100 to 2000?
	 * @param duration - In milliseconds.
	 * @return - Returns true if command worked, false if it failed.
	 */
	public byte playTone(int frequency, int duration) throws IOException {
		byte [] request = {DIRECT_COMMAND_NOREPLY, PLAY_TONE, (byte)frequency, (byte)(frequency>>>8), (byte)duration, (byte)(duration>>>8)};
		return sendRequest(request, 3);
	}
	
	public byte playSoundFile(String fileName, boolean repeat) throws IOException {
		
		byte boolVal = 0;
		if(repeat) boolVal = (byte)0xFF; // Convert boolean to number
		
		byte [] request = {DIRECT_COMMAND_NOREPLY, PLAY_SOUND_FILE, boolVal};
		byte[] encFileName = null;
		try {
			encFileName = AsciizCodec.encode(fileName);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Illegal characters in filename");
			return -1;
		}
		request = appendBytes(request, encFileName);
		return sendRequest(request, 3);
	}
	
	/**
	 * Stops sound file playing.
	 * @return the status (0 = success)
	 */
	public byte stopSoundPlayback() throws IOException {
		byte [] request = {DIRECT_COMMAND_NOREPLY, STOP_SOUND_PLAYBACK};
		return sendRequest(request, 3);
	}
	
	/**
	 * Resets either RotationCount or BlockTacho
	 * @param port Output port (0-2)
	 * @param relative TRUE: BlockTacho, FALSE: RotationCount
	 * @return the status (0 = success)
	 */
	public byte resetMotorPosition(int port, boolean relative) throws IOException {
		// !! Needs to check port to verify they are correct ranges.
		// !!! I'm not sure I'm sending boolean properly
		byte boolVal = 0;
		if(relative) boolVal = (byte)0xFF;
		byte [] request = {DIRECT_COMMAND_NOREPLY, RESET_MOTOR_POSITION, (byte)port, boolVal};
		return sendRequest(request, 3);
	}
	
	/**
	 * 
	 * @param port - Output port (0 - 2 or 0xFF for all three)
	 * @param power - Setpoint for power. (-100 to 100)
	 * @param mode - Setting the modes MOTORON, BRAKE, and/or REGULATED. This parameter is a bitfield, so to put it in brake mode and regulated, use BRAKEMODE + REGULATED
	 * @param regulationMode - see NXTProtocol for enumerations 
	 * @param turnRatio - Need two motors? (-100 to 100)
	 * @param runState - see NXTProtocol for enumerations
	 * @param tachoLimit - Number of degrees(?) to rotate before stopping.
	 * @return the status (0 = success)
	 */
	public byte setOutputState(int port, byte power, int mode, int regulationMode, int turnRatio, int runState, int tachoLimit) throws IOException {
		// !! Needs to check port, power to verify they are correct ranges.
		byte [] request = {DIRECT_COMMAND_NOREPLY, SET_OUTPUT_STATE, (byte)port, power, (byte)mode, (byte)regulationMode, (byte)turnRatio, (byte)runState, (byte)tachoLimit, (byte)(tachoLimit>>>8), (byte)(tachoLimit>>>16), (byte)(tachoLimit>>>24)};
		return sendRequest(request, 3);
	}
	
	/**
	 * Gets device information
	 * 
	 * @return a DeviceInfo structure
	 * @throws IOException
	 */
	public DeviceInfo getDeviceInfo() throws IOException {
		// !! Needs to check port to verify they are correct ranges.
		byte [] request = {SYSTEM_COMMAND_REPLY, GET_DEVICE_INFO};
		byte [] reply = nxtComm.sendRequest(request, 33);
		DeviceInfo d = new DeviceInfo();
		d.status = reply[2];
		d.NXTname = new StringBuffer(new String(reply)).delete(18,33).delete(0, 3).toString();
		d.bluetoothAddress = Integer.toHexString(reply[18]) + ":" + Integer.toHexString(reply[19]) + ":" + Integer.toHexString(reply[20]) + ":" + Integer.toHexString(reply[21]) + ":" + Integer.toHexString(reply[22]) + ":" + Integer.toHexString(reply[23]) + ":" + Integer.toHexString(reply[24]);
		d.signalStrength = (0xFF & reply[25]) | ((0xFF & reply[26]) << 8)| ((0xFF & reply[27]) << 16)| ((0xFF & reply[28]) << 24);
		d.freeFlash = (0xFF & reply[29]) | ((0xFF & reply[30]) << 8)| ((0xFF & reply[31]) << 16)| ((0xFF & reply[32]) << 24);
		return d;
	}
	
	/**
	 * Get the fimrware version.
	 * leJOS NXJ returns the version of the LEGO firmware that it emulates,
	 * not its own version number.
	 * 
	 * @return a FirmwareInfo structure.
	 * @throws IOException
	 */
	public FirmwareInfo getFirmwareVersion() throws IOException {
		byte [] request = {SYSTEM_COMMAND_REPLY, GET_FIRMWARE_VERSION};
		byte [] reply = nxtComm.sendRequest(request, 7);
		FirmwareInfo info = new FirmwareInfo();
		info.status = reply[2];
		if(info.status == 0) {
			info.protocolVersion = reply[4] + "." + reply[3];
			info.firmwareVersion = reply[6] + "." + reply[5];
		}
		return info;
	}
	
	/**
	 * Deletes user flash memory.
	 * Not implemented by leJOS NXJ.
	 * @return the status (0 = success)
	 */
	public byte deleteUserFlash() throws IOException {
		byte [] request = {SYSTEM_COMMAND_REPLY, DELETE_USER_FLASH};
		byte [] reply = nxtComm.sendRequest(request, 3);
		return reply[2];
	}
	
	/**
	 * leJOS-specific command to set the default program
	 * 
	 * @param name the default program name
	 * @return the status (0 is success)
	 * @throws IOException
	 */
	public byte setDefaultProgram(String name) throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_SET_DEFAULT_PROGRAM};
		byte[] encName = null;
		try {
			encName = AsciizCodec.encode(name);
		} catch (UnsupportedEncodingException e) {
			return -1;
		}
		request = appendBytes(request, encName);
		byte [] reply = nxtComm.sendRequest(request, 3);
		return reply[2];
	}
	
	/** 
	 * leJOS-specific command to set the master volume level
	 * 
	 * @param volume the master volume level
	 * @return the status (0 = success)
	 * @throws IOException
	 */
	public byte setVolume(byte volume) throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_SET_VOLUME, volume};
		byte [] reply = nxtComm.sendRequest(request, 3);
		return reply[2];
	}

	/** 
	 * leJOS-specific command to set the key click volume level
	 * 
	 * @param volume the key click volume level
	 * @return the status (0 = success)
	 * @throws IOException
	 */
	public byte setKeyClickVolume(byte volume) throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_SET_KEY_CLICK_VOLUME, volume};
		byte [] reply = nxtComm.sendRequest(request, 3);
		return reply[2];
	}
	
	/**
	 * leJOS-specific command to set auto-run on or off
	 * 
	 * @param on true = on, false = off
	 * @return the status (0 = success)
	 * @throws IOException
	 */
	public byte setAutoRun(boolean on) throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_SET_AUTO_RUN, (byte) (on ? 1 : 0)};
		byte [] reply = nxtComm.sendRequest(request, 3);
		return reply[2];		
	}
	
	/**
	 * leJOS-specific command to get the master volume level
	 * 
	 * @return the master volume level
	 * @throws IOException
	 */
	public int getVolume() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_VOLUME};
		byte [] reply = nxtComm.sendRequest(request, 4);
		return reply[3];		
	}
	
	/**
	 * leJOS-specific command to get the master volume level
	 * 
	 * @return the master volume level
	 * @throws IOException
	 */
	public int getKeyClickVolume() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_KEY_CLICK_VOLUME};
		byte [] reply = nxtComm.sendRequest(request, 4);
		return reply[3];		
	}
	
	/**
	 * leJOS-specific command to get the auto run setring
	 * 
	 * @return the auto run setting
	 * @throws IOException
	 */
	public boolean getAutoRun() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_AUTO_RUN};
		byte [] reply = nxtComm.sendRequest(request, 4);
		return (reply[3] == 1);		
	}
	
	/**
	 * leJOS-specific command to get the NXJ firmware version
	 * 
	 * @return a string with major version, minor version, and patch level and revision
	 * @throws IOException
	 */
	public String getNXJFirmwareVersion() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_VERSION};
		byte [] reply = nxtComm.sendRequest(request, 17);
		int revision = (0xFF & reply[6]) | ((0xFF & reply[7]) << 8)| ((0xFF & reply[8]) << 16)| ((0xFF & reply[9]) << 24);
		return reply[3] + "." + reply[4] + "." + reply[5] + "(" + revision + ")";	
	}
	
	/**
	 * leJOS-specific command to get the NXJ start-up menu version
	 * 
	 * @return a string with major version, minor version, patch level and revision
	 * @throws IOException
	 */
	public String getNXJMenuVersion() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_VERSION};
		byte [] reply = nxtComm.sendRequest(request, 17);
		int revision = (0xFF & reply[13]) | ((0xFF & reply[14]) << 8)| ((0xFF & reply[15]) << 16)| ((0xFF & reply[16]) << 24);
		return reply[10] + "." + reply[11] + "." + reply[12] + "(" + revision + ")";	
	}
	
	/**
	 * leJOS-specific command to get the NXJ firmware and menu information
	 * 
	 * @return a NXJFirmwareInfo object containing all the version numbers
	 * @throws IOException
	 */
	public NXJFirmwareInfo getNXJFirmwareInfo() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_VERSION};
		byte [] reply = nxtComm.sendRequest(request, 17);
		NXJFirmwareInfo info = new NXJFirmwareInfo();
		info.firmwareMajorVersion = reply[3];
		info.firmwareMinorVersion = reply[4];
		info.firmwarePatchLevel = reply[5];
		info.firmwareRevision = (0xFF & reply[6]) | ((0xFF & reply[7]) << 8)| ((0xFF & reply[8]) << 16)| ((0xFF & reply[9]) << 24);
		info.menuMajorVersion = reply[10];
		info.menuMinorVersion = reply[11];
		info.menuPatchLevel = reply[12];
		info.menuRevision = (0xFF & reply[13]) | ((0xFF & reply[14]) << 8)| ((0xFF & reply[15]) << 16)| ((0xFF & reply[16]) << 24);
		
		return info;
	}
	
	/**
	 * leJOS-specific method to get the menu sleep time
	 * 
	 * @return the sleep time in seconds
	 * @throws IOException
	 */
	public int getSleepTime() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_SLEEP_TIME};
		byte [] reply = nxtComm.sendRequest(request, 4);
		return reply[3];
	}
	
	/**
	 * leJOS-specific command to get the default program name
	 * 
	 * @return the default program name
	 * @throws IOException
	 */
	public String getDefaultProgram() throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_GET_DEFAULT_PROGRAM};
		byte [] reply = nxtComm.sendRequest(request, 23);
		StringBuffer name =  new StringBuffer(new String(reply)).delete(0, 3);	
		int lastPos = name.indexOf("\0");
		if (lastPos < 0 || lastPos > 20) lastPos = 20;
		name.delete(lastPos, name.length());
		return name.toString();
	}
	
	/**
	 * leJOS-specific command to the the sleep time for the menu
	 * @param seconds the number of seconds before shutdown
	 * @return the status (0 = success)
	 * @throws IOException
	 */
	public byte setSleepTime(byte seconds) throws IOException {
		byte[] request = {SYSTEM_COMMAND_REPLY, NXJ_SET_SLEEP_TIME, seconds};
		byte [] reply = nxtComm.sendRequest(request, 3);
		return reply[2];		
	}
	
	/**
	 * Test is connection is open
	 * 
	 * @return true iff the connection is open
	 */
	public boolean isOpen() {
		return open;
	}
}
