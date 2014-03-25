package lejos.robotics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Transmittable {
	public void dumpObject(DataOutputStream dos) throws IOException;
	
	public void loadObject(DataInputStream dis) throws IOException;
}
