package lejos.remote.nxt;

import java.io.UnsupportedEncodingException;

/**
 * Methods to encode and decode ASCIIZ.
 * ASCIIZ is ASCII terminated by Zero.
 * 
 */
public class AsciizCodec {
	private static final String CHARSET = "US-ASCII";

	private AsciizCodec() {
	}

	/**
	 * Encode a string as ASCIIZ
	 * @param str the string
	 * @return the ASCIIZ-encoded byte array
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encode(String str) throws UnsupportedEncodingException {
		// Encode string with ASCII charset
		byte[] ascii = str.getBytes(CHARSET);

		// Append Zero byte

		byte[] asciiz = new byte[ascii.length + 1];
		System.arraycopy(ascii, 0, asciiz, 0, ascii.length);
		System.arraycopy(new byte[] { 0x00 }, 0, asciiz, asciiz.length - 1, 1);

		return asciiz;
	}

	/**
	 * Convert an ASCIIZ byte array to a string
	 * 
	 * @param bytes the byte array
	 * @return the String
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(final byte[] bytes) throws UnsupportedEncodingException {
		byte lastByte = bytes[bytes.length - 1];
		if (lastByte != 0x00) {
			throw new UnsupportedEncodingException(
					"Last byte of ASCIIZ encoded string must be Zero");
		}

		// Remove Last byte
		byte[] ascii = new byte[bytes.length - 1];
		System.arraycopy(bytes, 0, ascii, 0, bytes.length - 1);

		// Decode bytes with ASCII charset
		return new String(ascii, CHARSET);
	}
}
