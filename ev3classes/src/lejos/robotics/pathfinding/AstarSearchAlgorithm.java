package lejos.robotics.pathfinding;

import java.util.*;
import lejos.robotics.navigation.Waypoint;

// TODO: This works, but this code keeps the Node properties right in the Node object. The same Node set
// (aka Navigation Mesh) might conceivably (probably) be used repeatedly for many different searches. So things
// like setPredecessor() and setG_Score() should be temporary, not part of Node object?

/**
 * This is an implementation of the A* search algorithm. Typically this object would be instantiated and then used
 * in a NodePathFinder constructor, along with a set of connected nodes.
 * @see lejos.robotics.pathfinding.NodePathFinder
 * @author BB
 * 
 */
public class AstarSearchAlgorithm implements SearchAlgorithm{
	/*
	 * DEVELOPER NOTES: This algorithm was adapted from pseudocode in the Wikipedia article:
	 * http://en.wikipedia.org/wiki/A*_search_algorithm
	 */
	
	private static final String STRING_NAME = "A*";
	
	int main_loop = 0; // TODO: DELETE ME
	int neighbor_loop = 0;
	
	public Path findPath(Node start, Node goal) {
		
		//long startNanoT = System.nanoTime();
				
		ArrayList <Node> closedset = new ArrayList<Node>(); // The set of nodes already evaluated. Empty at start.     
		ArrayList <Node> openset = new ArrayList<Node>(); // The set of tentative nodes to be evaluated. 
		openset.add(start); // openset contains startNode at start.
		//ArrayList <Node> path = new ArrayList<Node>(); // came_from := the empty map // The map of navigated nodes.
		start.setG_Score(0); // Distance from start along optimal path. Zero by definition since at start. g(start)
		//start.setH_Score((float)Point2D.distance(start.x, start.y, goal.x, goal.y)); // ORIGINAL h(start)
		start.setH_Score(start.calculateH(goal)); // NEW2
		
		while (!openset.isEmpty()) {
			Node x = getLowestCost(openset); // get the node in openset having the lowest f_score[] value

			main_loop++; // TODO: DELETE ME
			
			if(x == goal) {
				Path final_path = new Path();
				
				//long totalNanoT = System.nanoTime() - startNanoT;
				//long reconNanoT = System.nanoTime();
				reconstructPath(goal, start, final_path);
				//long totalReconT = System.nanoTime() - reconNanoT;
				//System.out.println("Path time " + (totalNanoT/1000000D) + " ms");
				//System.out.println("Recon time " + (totalReconT/1000000D) + " ms");
				//System.out.println("MAIN LOOPS: " + main_loop);
				//System.out.println("NEIGHB LOOPS: " + neighbor_loop);
				//Button.ESCAPE.waitForPressAndRelease();
				return final_path;
			}
			openset.remove(x); // remove x from openset
			closedset.add(x); // add x to closedset

			// TODO: Any mem saving/speedup not using iter? Could use array. Saves about 33 ms total, not really worth it yet?
			Collection <Node> yColl = x.getNeighbors();
			Iterator <Node> yIter = yColl.iterator();

			while(yIter.hasNext()) { // for each y in neighbor_nodes(x)
				neighbor_loop++; // TODO: DELETE ME
				Node y = yIter.next();
				if(closedset.contains(y)) continue;  // if y in closedset already, go to next one

				//float tentative_g_score = x.getG_Score() + (float)Point2D.distance(x.x, x.y, y.x, y.y); // ORIGINAL g_score[x] + dist_between(x,y)
				float tentative_g_score = x.getG_Score() + x.calculateG(y); // NEW2
				boolean tentative_is_better = false;

				if (!openset.contains(y)) { // if y not in openset
					openset.add(y); // add y to openset
					tentative_is_better = true;
				} else if(tentative_g_score < y.getG_Score()) { // if tentative_g_score < g_score[y]
					tentative_is_better = true;
				} else
					tentative_is_better = false;

				if (tentative_is_better) {
					y.setPredecessor(x); // came_from[y] := x
				}

				y.setG_Score(tentative_g_score);
				y.setH_Score(y.calculateH(goal)); // NEW2
				//y.setH_Score((float)Point2D.distance(y.x, y.y, goal.x, goal.y)); // ORIGINAL heuristic_estimate_of_distance(y, goal)
				// Update(closedset,y)  This might mean update the values of y in closedset and openset? Unneeded I assume.
				// Update(openset,y)  
			} // while yIter.hasNext()
		} // while main loop
		return null; // returns null if fails to find a  continuous path.
	}

	/**
	 * Finds the node within a set of neighbors with the least cost (potentially shortest distance to goal). 
	 * @return The node with the least cost.
	 */
	private static Node getLowestCost(Collection <Node> nodeSet) {
		/* TODO: This method has potential for optimization. Called very frequently. Probably best to 
		 * move this method to Node (not NavigationMesh), then can optimize individually based on mesh 
		 * type (e.g. integer grid or float navigation mesh). Mesh would probably be preferable, but 
		 * SearchAlgorithm has no access to the mesh, just individual nodes. Perhaps alternate findPath()
		 * or set method for accepting mesh? (float or int is optimization concern) */ 
		Iterator <Node> nodeIterator = nodeSet.iterator();
		Node best = nodeIterator.next();
		while(nodeIterator.hasNext()) {
			Node cur = nodeIterator.next();
			if(cur.getF_Score() < best.getF_Score())
				best = cur;
		}
		return best;
	}
	
	/**
	 * Given the current node and the start node, this method retraces the completed path. It relies
	 * on Node.getPredecessor() to backtrack using recursion. 
	 * 
	 * @param current_node
	 * @param start
	 * @param path The path output by this algorithm.
	 */
	private static final void reconstructPath(Node current_node, Node start, Collection <Waypoint> path){
		if(current_node != start)
			reconstructPath(current_node.getPredecessor(), start, path);
		path.add(new Waypoint(current_node.x, current_node.y));
		return;
	}
	
	public String toString() {
		return STRING_NAME;
	}
}