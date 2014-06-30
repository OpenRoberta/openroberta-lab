package lejos.robotics.pathfinding;

import java.util.Collection;

/**
 * A navigation mesh is a set of nodes covering a map area which represent branching pathways for the vehicle
 * to move from one location to another. This interface is used by classes which build navigation meshes. 
 * 
 * @author BB
 */
public interface NavigationMesh {
	// TODO: Consider making it abstract with common methods if any develop?
	// TODO: Consider Self generating nodes inherit this?
	
	/**
	 * Adds a node to this set and connects it with a number of neighboring nodes. If it is unable to find any
	 * neighbors it will return 0. This might occur because the node is outside of the bounded area of the map. 
	 * @param node The unconnected node to add to this mesh. Will be connected with others in the set.
	 * @param neighbors The maximum number of neighbors to attempt to connect with.
	 * @return the number of neighboring nodes it was able to connect with
	 */
	public int addNode(Node node, int neighbors);
	
	/**
	 * Removes a node from the set and removes any existing connections with its neighbors.
	 * Note: There is no guarantee it is disconnecting from only nodes in this mesh. This method will disconnect
	 * the node from all the nodes registered as neighbors.
	 * @param node The node to remove.
	 * @return Returns true if the node was removed, false if it did not exist in this set.
	 */
	public boolean removeNode(Node node);
	
	/**
	 * Attempts to connect two nodes together by adding them as neighbors. If map data exists for this
	 * NavigationMesh, it will check the map data to see if the connection intersects or comes too close
	 * to map geometry. If it does they will not be connected and this method returns false.
	 * @param node1
	 * @param node2
	 * @return Boolean value, true if the nodes were connected successfully, false if they could not connect.
	 */
	public boolean connect(Node node1, Node node2);
	
	/**
	 * Disconnects two nodes by removing them as neighbors. If they were not previously connected it returns false. 
	 * @param node1
	 * @param node2
	 * @return Returns false if they were not previously connected.
	 */
	public boolean disconnect(Node node1, Node node2);
	
	// Note: Not used by NodePathFinder but seemed like a conspicuously absent method. 
	/**
	 * Returns a collection of all nodes within this navigation mesh.
	 * @return A Collection of Nodes.
	 */
	public Collection <Node> getMesh();
	
	// Note: Not used by NodePathFinder but seems useful for GUIs and such when parameters changed.
	/**
	 * Throws away the previous set of nodes and recalculates them all. If any setting were changed, such as
	 * the spacing between nodes, then it will recalculate them with the new settings.  
	 */
	public void regenerate();
	
}
