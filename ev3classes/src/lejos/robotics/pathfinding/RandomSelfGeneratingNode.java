package lejos.robotics.pathfinding;

import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * This Node is able to randomly generate its own neighbors via the getNeighbors() method. The number of neighbors 
 * and possible distances to the neighbors are determined in the constructor. The first instance of this node is generally
 * the start node used in algorithms. The goal node is just a regular node that is automatically linked to this set
 * when the dynamically created nodes come within range of the goal node.
 * Note: Because the nodes are randomly generated, there is no guarantee they will come within range of the goal node
 * and successfully link up. However, in practice they will always connect if you use enough connections (minimum 3-4) and 
 * a long enough maxDist.
 * @author BB
 *
 */
public class RandomSelfGeneratingNode extends Node {
	
	// TODO: Add ability to prune out paths that come close to map data objects.
	
	/**
	 * firstCall indicates if getNeighbors() has been called yet. false = has been called
	 */
	private boolean firstCall = true;
	
	private int connections;
	private float maxDist;
	
	// TODO: Making the goal node static blows apart the possibility of using two sets of random nodes with different goals.
	private static Node goal = null;
	
	/**
	 * Creates a node that will randomly generate 'connections' number of neighbors when getNeighbors()
	 * is called. These neighbors will be within range of 'maxDist'.
	 * @param x The x coordinate of this node.
	 * @param y The y coordinate of this node.
	 * @param maxDist The maximum x or y distance to create new nodes randomly.
	 * @param connections The number of neighbors to randomly generate and connect with.
	 */
	public RandomSelfGeneratingNode(float x, float y, float maxDist, int connections) {
		super(x, y);
		this.maxDist = maxDist;
		this.connections = connections;
	}
	
	/**
	 * Creates a node that will randomly generate 'connections' number of neighbors when getNeighbors()
	 * is called. These neighbors will be within range of 'maxDist'.
	 * @param x The x coordinate of this node.
	 * @param y The y coordinate of this node.
	 * @param maxDist The maximum x or y distance to create new nodes randomly.
	 * @param connections The number of neighbors to randomly generate and connect with.
	 * @param goal The goal node which is added to this set and connected to any nodes within range.
	 */
	public RandomSelfGeneratingNode(float x, float y, float maxDist, int connections, Node goal) {
		this(x, y, maxDist, connections);
		RandomSelfGeneratingNode.goal = goal;
	}
	
	/** When this method is called the first time, it randomly generates a set of neighbors according to
	 * the parameters in the constructor. It then calls addNeighbor() for each one. The next time getNeighbors() is called
	 * it will return the same set of neighbors it initially generated.
	 * 
	 * Each of these neighbors is a RandomSelfGeneratingNode too, so when their neighbors are requested they will also
	 * self-generate a set of neighbors. 
	 * 
	 * Each random node will also add the "parent" node to its list of neighboring nodes. 
	 * 
	 * If a goal node was added, it checks if the node is within maxDist of the goal node. If it is, both the goal node and
	 * this node add each other as neighbors. 
	 * @return a collection of RandomSelfGeneratingNode objects. 
	 */
	public Collection <Node> getNeighbors() {
		// TODO: When to do the map geometry pruning of these?
		if(firstCall) {
			
			// TODO: Really, if any of the previously generated nodes are in range, they should be connected. Means it would
			// need to scrutinize every previous node generated. Either backtrack using Node.getPredecessor() or keep another
			// list of all nodes generated so far.
			// See if goal node is in range. Yes? Add.
			float goal_dist = (float)Point2D.distance(goal.x, goal.y, this.x, this.y);
			if(goal_dist <= maxDist) {
				this.addNeighbor(goal);
				goal.addNeighbor(this);
			}
			
			int nodes_to_add = connections - super.neighbors();
			for(int i=0;i<nodes_to_add;i++) {
				// TODO: Could use Point.pointAt(distance, angle) when generating random points. Slower.
				// Generate new node with random direction and distance from this one.
				float rand_x = (float)(Math.random() * maxDist);
				float rand_y = (float)(Math.random() * maxDist);
				if(Math.random() < 0.5) rand_x*= -1;
				if(Math.random() < 0.5) rand_y*= -1;
				float new_x = this.x + rand_x;
				float new_y = this.y + rand_y;
				//String new_id = "(" + new_x + ", " + new_y + ")";
				RandomSelfGeneratingNode newNode = new RandomSelfGeneratingNode(new_x, new_y, maxDist, connections);
				
				// Add parent node (this) and add new to this.
				newNode.addNeighbor(this);
				this.addNeighbor(newNode);
				
			}
			firstCall = false;
		}
		return super.getNeighbors();
	}
	
}
