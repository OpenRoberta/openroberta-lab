package lejos.robotics.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;

import lejos.robotics.navigation.*;

/**
 * This path finder class uses one of the common search algorithms (e.g. A*) and a navigation mesh (e.g. grid) and
 * uses them to find a path around map geometry.
 *  
 * @author BB
 *
 */
public class NodePathFinder implements PathFinder{

	private ArrayList<WaypointListener> listeners;
	private SearchAlgorithm alg;
	private NavigationMesh mesh = null;
	
	/**
	 * Instantiates a NodePathFinder object using a specified algorithm. Ideally this class would work with
	 * self propagating nodes, but it currently is not able to. 
	 * @param alg
	 */
	private NodePathFinder(SearchAlgorithm alg) {
		// TODO: This method is not really valid (hence why market private for now) because the
		// algorithm will currently not work without a mesh.
		setSearchAlgorithm(alg);
	}
	
	/**
	 * Instantiates a NodePathFinder object using a specified algorithm. The supplied mesh is used
	 * to add and remove nodes (start and goal) when requesting a path.   
	 * @param alg The search algorithm.
	 * @param mesh The navigation mesh is a set of nodes in various configurations (e.g. grid).
	 */
	public NodePathFinder(SearchAlgorithm alg, NavigationMesh mesh) {
		this(alg);
		setNavMesh(mesh);
	}
	
	/**
	 * Method for changing the navigation mesh after this has been instantiated.
	 * @param mesh
	 */
	public void setNavMesh(NavigationMesh mesh) {
		this.mesh = mesh;
	}
	
	/**
	 * Method for changing the search algorithm after this has been instantiated.
	 * @param alg
	 */
	public void setSearchAlgorithm(SearchAlgorithm alg) {
		this.alg = alg;
	}
	
	public void addListener(WaypointListener wpl) {
		if(listeners == null )listeners = new ArrayList<WaypointListener>();
		listeners.add(wpl);
	}

	public Path findRoute(Pose start, Waypoint goal)
			throws DestinationUnreachableException {
		// Step 1: Make nodes out of start and destination
		// TODO: Big problem: These nodes will not be linked to anything if no mesh was given!
		Node startNode = new Node(start.getX(), start.getY());
		Node goalNode = new Node((float)goal.getX(), (float)goal.getY());
		
		// Step 2: If Mesh is not null, add them to set? 
		if(mesh != null) {
			mesh.addNode(startNode, 4);
			mesh.addNode(goalNode, 4);
		}
		// Step 3: Use alg to find path.
		Path path = alg.findPath(startNode, goalNode);
		if(path == null) throw new DestinationUnreachableException();
		
		// Step 4: If mesh is not null, remove them from set?
		if(mesh != null) {
			mesh.removeNode(startNode);
			mesh.removeNode(goalNode);
		}
		
		return path;
	}

	public void startPathFinding(Pose start, Waypoint end) {
		Path solution = null;
		try {
			solution = findRoute(start, end);
		} catch (DestinationUnreachableException e) {
			// TODO: Call pathComplete() on all listeners with false if unable to find route.
			// l.pathComplete(false); // Code should be below. Mark boolean success as false here.
		}
		if(listeners != null) { 
			for(WaypointListener l : listeners) {
				Iterator<Waypoint> iterator = solution.iterator(); // TODO: If solution null, this can crash here.
				while(iterator.hasNext()) {
					l.addWaypoint(iterator.next());
				}
				l.pathGenerated();
			}
		}
	}
}