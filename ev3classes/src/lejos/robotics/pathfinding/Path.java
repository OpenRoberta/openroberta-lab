package lejos.robotics.pathfinding;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import lejos.robotics.Transmittable;
import lejos.robotics.navigation.Waypoint;

/**
 * Represents a path consisting of an ordered collection of waypoints
 * 
 * @author Lawrie Griffiths
 *
 */
public class Path extends ArrayList<Waypoint> implements Transmittable {

	public void dumpObject(DataOutputStream dos) throws IOException {
		dos.writeInt(this.size());
		for(Waypoint wp: this) {
			wp.dumpObject(dos);
		}
	}

	public void loadObject(DataInputStream dis) throws IOException {
		int n = dis.readInt();
		this.clear();
		for(int i=0;i<n;i++) {
			Waypoint wp = new Waypoint(0,0);
			wp.loadObject(dis);
			add(wp);
		}		
	}
}
