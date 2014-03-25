package lejos.utility;

/**
 * Tools for manipulating numbers in little-endian and big-endian encodings
 */
public class EndianTools
{
	public static long decodeLongBE(byte[] b, int off)
	{
		return ((long) decodeIntBE(b, off) << 32) | (decodeIntBE(b, off + 4) & 0xFFFFFFFFL);
	}

	public static long decodeUIntBE(byte[] b, int off)
	{
		return decodeIntBE(b, off) & 0xFFFFFFFFL;
	}

	public static int decodeUShortBE(byte[] b, int off)
	{
		return decodeShortBE(b, off) & 0xFFFF;
	}

	public static int decodeIntBE(byte[] b, int off)
	{
		return (b[off] << 24) | ((b[off + 1] & 0xFF) << 16)
			| ((b[off + 2] & 0xFF) << 8) | (b[off + 3] & 0xFF);
	}

	public static short decodeShortBE(byte[] b, int off)
	{
		return (short) ((b[off] << 8) | (b[off + 1] & 0xFF));
	}
	
	public static long decodeLongLE(byte[] b, int off)
	{
		return (decodeIntLE(b, off) & 0xFFFFFFFFL) | ((long) decodeIntLE(b, off + 4) << 32);
	}

	public static long decodeUIntLE(byte[] b, int off)
	{
		return decodeIntLE(b, off) & 0xFFFFFFFFL;
	}

	public static int decodeUShortLE(byte[] b, int off)
	{
		return decodeShortLE(b, off) & 0xFFFF;
	}

	public static int decodeIntLE(byte[] b, int off)
	{
		return (b[off] & 0xFF) | ((b[off + 1] & 0xFF) << 8)
			| ((b[off + 2] & 0xFF) << 16) | (b[off + 3] << 24);
	}
	
	public static short decodeShortLE(byte[] b, int off)
	{
		return (short)((b[off] & 0xFF) | (b[off + 1] << 8));
	}

	public static void encodeLongBE(long v, byte[] b, int off)
	{
		encodeIntBE((int)(v >>> 32), b, off);
		encodeIntBE((int)v, b, off+4);
	}

	public static void encodeIntBE(int v, byte[] b, int off)
	{
		b[off] = (byte)(v >>> 24);
		b[off+1] = (byte)(v >>> 16);
		b[off+2] = (byte)(v >>> 8);
		b[off+3] = (byte)v;
	}

	public static void encodeShortBE(int v, byte[] b, int off)
	{
		b[off] = (byte)(v >>> 8);
		b[off+1] = (byte)v;
	}

	public static void encodeLongLE(long v, byte[] b, int off)
	{
		encodeIntLE((int)v, b, off);
		encodeIntLE((int)(v >>> 32), b, off+4);
	}

	public static void encodeIntLE(int v, byte[] b, int off)
	{
		b[off] = (byte)v;
		b[off+1] = (byte)(v >>> 8);
		b[off+2] = (byte)(v >>> 16);
		b[off+3] = (byte)(v >>> 24);
	}

	public static void encodeShortLE(int v, byte[] b, int off)
	{
		b[off] = (byte)v;
		b[off+1] = (byte)(v >>> 8);
	}

}
