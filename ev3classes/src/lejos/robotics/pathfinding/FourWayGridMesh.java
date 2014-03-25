package lejos.robotics.pathfinding;

import java.util.ArrayList;
import java.util.Collection;

import lejos.robotics.geometry.Line;
import lejos.robotics.geometry.Rectangle;
import lejos.robotics.mapping.LineMap;

/**
 * Generates a grid of nodes. Spacing between the grid nodes and clearance around map geometry can be specified. 
 * This set can be generated once at the beginning of a user program, and the same node set can be used for all 
 * subsequent navigation.
 * @author BB
 *
 */
public class FourWayGridMesh implements NavigationMesh {

	private ArrayList <Node> mesh = null;
	private LineMap map = null;
	private float clearance;
	private float gridspace;
	
	/**
	 * Instantiates a grid mesh of nodes which won't interconnect between any map geometry. Will also keep away
	 * the set parameter from map geometry. Grid spacing is adjustable via the constructor. 
	 * @param map The map containing geometry.
	 * @param gridSpace The size of each grid square.
	 * @param clearance The safety zone between all nodes/connections and the map geometry.
	 */
	public FourWayGridMesh(LineMap map, float gridSpace, float clearance) {
		setMap(map);
		setClearance(clearance);
		setGridSpacing(gridSpace);
		// TODO: OPTION 1: Generate now (predictable) or later (allows changes to be made to map? Nah.) 
		// Maybe generate called here? If called later, someone could  add a node before it was
		// generated and expect it to be connected, which it won't be.
	}
	
	public Collection <Node> getMesh(){
		if(mesh == null) regenerate();
		return mesh;
	}
	
	/**
	 * Change the size of each grid square. NOTE: When grid space value is changed, this class does not regenerate 
	 * the navigation mesh until regenerate() is explicitly called.
	 * @param gridSpace The unit size of each grid square.
	 */
	public void setGridSpacing(float gridSpace) {
		this.gridspace = gridSpace;
	}
	
	/**
	 * Changes the safety zone between all nodes/connections and map geometry. This leaves a margin of error between
	 * potential object collisions and the robot. NOTE: When clearance value is changed, 
	 * this class does not regenerate the navigation mesh until regenerate() is explicitly called.	
	 * @param clearance The safety clearance between nodes/connections and map geometry. 
	 */
	public void setClearance(float clearance) {
		this.clearance = clearance;
	}
	
	/**
	 * Feeds this class a new map. NOTE: When Map is changed, this class does not regenerate the navigation mesh 
	 * until regenerate() is explicitly called. 
	 * @param map The new map data.
	 */
	public void setMap(LineMap map) {
		this.map = map;
	}
	
	public void regenerate() {
		//long startNanoT = System.nanoTime();
		//long startFreeMem = Runtime.getRuntime().freeMemory();
		
		mesh = new ArrayList <Node> ();
		
		// First node is "clearance" from the corner of the map
		Rectangle bounds = map.getBoundingRect();
		
		float startx = bounds.x + clearance;
		float starty = bounds.y + clearance;
		
		float endx = bounds.width + bounds.x - clearance;
		float endy = bounds.height + bounds.y - clearance;
		
		int x_grid_squares = 0;
		int y_grid_squares = 0;
		
		for(float y = starty;y<endy;y+=gridspace) {
			y_grid_squares += 1;
			for(float x = startx;x<endx;x+=gridspace) {
				x_grid_squares += 1;
				mesh.add(new GridNode(x, y, gridspace));
				// TODO: Why not use addNode for each subsequent node?! Because it tries to connect it to others.
			}
		}
		x_grid_squares /= y_grid_squares;
		
		// Start connecting neighbors in upper left, connect to one to right and one down
		Node cur = mesh.get(0);
		for(int i=1;i<mesh.size();i++) {
			Node rightNode = mesh.get(i);
			Node downNode = null;
			int down = i - 1 + x_grid_squares;
			if(down < mesh.size()) {
				downNode = mesh.get(down);
				connect(cur, downNode); // Check if no more down.
			}
			if(i % x_grid_squares != 0) connect(cur, rightNode); // Check if no more to right
			
			cur = rightNode;
		}
		
		// TODO: At this point I could optionally remove nodes that are too close to geometry. Pretty quick. Currently
		// it leaves them in the mesh set unconnected to anything. Probably better that way.
		
		//long totalNanoT = System.nanoTime() - startNanoT;
		//long endFreeMem = Runtime.getRuntime().freeMemory();
		
		//System.out.println("Mesh time " + (totalNanoT/1000000D) + " ms");
		//System.out.print("Free Memory start: " + startFreeMem);
		//System.out.print(" end: " + endFreeMem);
		//System.out.println(" used: " + (startFreeMem - endFreeMem));
	}
	
	public boolean connect(Node node1, Node node2) {
		
		// If there is map data to check against, do it:
		if(map != null) {
			// Check if nodes are within bounding box:
			if(!map.getBoundingRect().contains(node1.x, node1.y)) return false;
			if(!map.getBoundingRect().contains(node2.x, node2.y)) return false;
			
			/* TODO: Might speed things up here for larger maps if you do preliminary check
			   to see if point is within bounding box or something. Less expensive calculation
			   to verify it is worth doing deeper calculation. */
			
			// Now check if connection comes too close to any map geometry:
			Line connection = new Line(node1.x, node1.y, node2.x, node2.y);
			Line [] lines = map.getLines();
			for(int i=0;i<lines.length;i++) {
				if(lines[i].segDist(connection) < clearance)
					return false;
			}
		}
		node1.addNeighbor(node2);
		node2.addNeighbor(node1);
		
		return true;
	}
	
	public boolean disconnect(Node node1, Node node2) {
		// TODO: Return true if nodes were previously connected? Or return void.
		node1.removeNeighbor(node2);
		node2.removeNeighbor(node1);
		return true;
	}
	
	/**
	 * Adds a node to this set and connects it with a number of neighboring nodes. If it is unable to find any
	 * neighbors it will return 0. This might occur because the node is outside of the bounded area of the map. 
	 * Note: The most FourWayGridMesh can connect a node to is four. If you select a number larger than four, a maximum of
	 * four neighbors will be connected with the node. 
	 * @param node The unconnected node to add to this mesh. Will be connected with others in the set.
	 * @param neighbors The maximum number of neighbors to attempt to connect with.
	 * @return the number of neighboring nodes it was able to connect with
	 */
	public int addNode(Node node, int neighbors) {
		if(mesh == null) regenerate();
		
		int total = 0;
		
		// Fact: Only four nodes can logically be within "gridspace" of a node.
		for(int i=0;i<mesh.size();i++) {
			Node cur = mesh.get(i);
			float dif_x = Math.abs(cur.x - node.x);
			if(dif_x <= gridspace) {
				float dif_y = Math.abs(cur.y - node.y);
				if(dif_y <= gridspace) {
					if(connect(node, cur)) total++;
					if(total >= neighbors) {
						mesh.add(node);
						return total;
					}
				}
			}
		}
		mesh.add(node);
		return total;
	}

	public boolean removeNode(Node node) {
		//System.out.print("MAIN NODE ");
		//outputNodeData(node);
		Collection <Node> coll = node.getNeighbors();
		ArrayList <Node> arr = new ArrayList <Node> (coll);
		for(int i=0;i<arr.size();i++) {
			Node neighbor = arr.get(i);
			//System.out.print("NEIGHBOR NODE ");
			//outputNodeData(neighbor);
			neighbor.removeNeighbor(node);
			node.removeNeighbor(neighbor); // Could remove all of them after with one call!
		}
			
		return mesh.remove(node);
	}		
}