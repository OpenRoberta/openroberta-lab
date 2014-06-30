package lejos.remote.ev3;

import java.io.Serializable;

public class EV3Reply implements Serializable {
	private static final long serialVersionUID = 6213598677277301069L;
	
	public String[] names;
	public String value;
	
	public boolean result;
	public int reply;
	public float floatReply;

	public byte[] contents;
	
	public String name;
	public float[] floats;
	public short[] shorts;
	
	public double doubleReply;
	
	public Exception e;

}
