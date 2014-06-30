package lejos.robotics.pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents a Node which can be connected to other neighboring nodes. Node sets can be searched using
 * search algorithms. Typically the search algorithm only requires one starting node and one goal node. It assumes these
 * nodes are linked by intermediate nodes.
 * @author BB
 * @see lejos.robotics.pathfinding.SearchAlgorithm
 */
public class Node  {
	
	/**
	 * This constant is multiplied with the float coordinate to get an integer for faster internal
	 * computations. A multiplier of 100 gives the equivalent of 2 decimal places (1.2345 = 123)
	 */
	static final int MULTIPLIER = 100;
	
	/**
	 * The x coordinate of this node.
	 */
	public float x;
	
	/**
	 * The y coordinate of this node.
	 */
	public float y;
	
	/**
	 * The cumulative distance from the start node to this node.
	 */
	private float g_score = 0;
	
	/*
	 * The estimated distance to the goal node from this node. Distance "as the crow flies"
	 */
	private float h_score = 0;
	
	/**
	 * The predecessor node used by A* search algorithm to mark off the previous node in the search tree.
	 */
	private Node cameFrom = null;
	
	/**
	 * List of neighbors to this node.
	 */
	private ArrayList <Node> neighbors = new ArrayList<Node>();
	
	/**
	 * Creates a new instance of a node.
	 * @param x The x coordinate of this node.
	 * @param y The y coordinate of this node.
	 */
	public Node(float x, float y) {
		this.x = x; 
		this.y = y;
	}
	
	/**
	 * Returns all the neighbors which this node is connected to. 
	 * @return The collection of all neighboring nodes.
	 */
	public Collection <Node> getNeighbors() {
		return neighbors;
	}
	
	/**
	 * Indicates the number of neighbors (nodes connected to this node).
	 * @return int Number of neighbors.
	 */
	public int neighbors() {
		return neighbors.size();
	}
	
	/**
	 * Adds a neighboring node to this node, connecting them together. Note: You must make sure to add this node
	 * to the neighbor, and also add the neighbor to this node. This method doesn't do both.
	 * @param neighbor The neighboring node to connect with.
	 * @return Returns false if the neighbor already existed, or if you try to add this node to itself as a neighbor.
	 */
	public boolean addNeighbor(Node neighbor) {
		// TODO: OPTION - Maybe code here should add each other as neighbors?
		// Check to make sure same isn't added twice. (Assuming ArrayList doesn't do this already?)
		if(neighbors.contains(neighbor)) return false;
		// Check to make sure doesn't add itself
		if(neighbor == this) return false;
		neighbors.add(neighbor);
		return true;
	}
	
	/**
	 * Removes a node from this node as neighbors, effectively disconnecting them. Note: You have to remove
	 * this node from the neighbor, and also remove the neighbor from this node. This method doesn't do both.	
	 * @param neighbor The neighboring node to disconnect from.
	 * @return Returns false if the neighbor did not previously exist as a neighbor.
	 */
	public boolean removeNeighbor(Node neighbor) {
		// TODO: Maybe code here should remove each other as neighbors?
		return neighbors.remove(neighbor);
	}
	
	/**
	 * Method used by A* to calculate search score. The H score is the estimated distance to the goal node from this node.
	 * It can either be distance "as the crow flies" or in the case of a grid navigation mesh, the minimum number
	 * of grid spaces to get to the goal node (x squares horizontally + y squares vertically from goal).
	 * NOTE: There is no getH_score() because the A* algorithm only needs to set this value, not retrieve it.
	 * @param h
	 */
	protected void setH_Score(float h) {
		h_score = h;
	}
	
	/**
	 * Calculates the distance to a neighbor node. This method is used to optimize the algorithm.
	 * @param neighbor
	 * @return the distance to neighbor
	 */
	protected float calculateG(Node neighbor) {
		return (float)Point2D.distance(this.x, this.y, neighbor.x, neighbor.y);
	}
	
	/**
	 * Calculates the distance to the goal node. This method is used to optimize the algorithm.
	 * @param goal
	 * @return the distance to goal
	 */
	protected float calculateH(Node goal) {
		return calculateG(goal);
	}
	
	/**
	 * Method used by A* to calculate search score. The G score is the cumulative distance from the start node to this node.
	 * @param g
	 */
	protected void setG_Score(float g) {
		g_score = g;
	}
	
	/**
	 * Method used by A* to calculate search score. The G score is the cumulative distance from the start node to this node.
	 * @return the search score
	 */
	protected float getG_Score(){
		return g_score;
	}
		
	/**
	 * Method used by A* to calculate search score. You can't set FScore because it is 
	 * derived internally by adding the gscore and hscore.
	 */
	protected float getF_Score(){
		return g_score + h_score;
	}
	
	/**
	 * Used by A* search. Stores the node that the search came from prior to this node. 
	 * @return the predecessor node
	 */
	protected Node getPredecessor() {
		return cameFrom;
	}
	
	/**
	 * Used by A* search. Stores the node that the search came from prior to this node.
	 * @param orig
	 */
	protected void setPredecessor(Node orig) {
		cameFrom = orig;
	}
}