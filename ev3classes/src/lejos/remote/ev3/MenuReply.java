package lejos.remote.ev3;

import java.io.Serializable;

public class MenuReply implements Serializable {
	private static final long serialVersionUID = -7200416724089157694L;
	public String[] names;
	public String value;
	
	public boolean result;
	public int reply;

	public byte[] contents;
}
