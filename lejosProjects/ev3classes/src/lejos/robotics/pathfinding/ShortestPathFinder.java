package lejos.robotics.pathfinding;

import lejos.robotics.geometry.*;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.navigation.WaypointListener;
import java.util.*;
import lejos.robotics.navigation.Pose;

/**
* This class calculates the shortest path from a starting point to a finish
* point. while avoiding obstacles that are represented as a set of straight
* lines. The path passes through the end points of some of these lines, which
* is where the changes of direction occur. Since the robot is not point, the
* lines representing the obstacles should be lengthened so the actual robot
* will miss the actual obstacles. Use the lengthenLines() method to do this.
* Uses modification of the A* algorithm,which is a a variant of the Dijkstra
* shortest path algorithm. This variant adds nodes needed. It uses the Node
* inner class for its internal representation of points.
* 
* @author Roger Glassey
*/
public class ShortestPathFinder implements PathFinder
{
	public ShortestPathFinder(LineMap map)
	{
		setMap(map);
		System.out.println("ShortestPathFinder");
	}


	/**
	 * Finds the shortest path from start to finish using the map (or collection
	 * of lines) in the constructor.
	 * 
	 * @param start
	 *            the initial robot pose
	 * @param finish
	 *            the final robot location
	 * @return the shortest route
	 * @throws DestinationUnreachableException
	 *             if, for example, you nave not called setMap();
	 */
	public Path findRoute(Pose start, Waypoint finish)
			throws DestinationUnreachableException
	{
		return findPath(start.getLocation(), finish, _map);
	}

	/**
	 * Finds the shortest path from start to finish using the map ( collection
	 * of lines) in the constructor.
	 * 
	 * @param start
	 *            the initial robot pose
	 * @param finish
	 *            the final robot location
	 * @param theMap
	 *            the LineMap of obstacles
	 * @return the shortest route
	 * @throws DestinationUnreachableException
	 *             if, for example, you nave not called setMap();
	 */
	public Path findRoute(Pose start, Waypoint finish, LineMap theMap)
			throws DestinationUnreachableException
	{
		setMap(theMap);
		return findPath(start.getLocation(), finish, _map);
	}



	/**
	 * Finds the shortest path between start and finish Points while avoiding
	 * the obstacles represented by lines in the map
	 * 
	 * @param start
	 *            : the beginning of the path
	 * @param finish
	 *            : the destination
	 * @param theMap
	 *            that contains the obstacles
	 * @return an array list of waypoints. If no path exists, returns null and
	 *         throws an exception.
	 */
	private Path findPath(Point start, Point finish, ArrayList<Line> theMap)
			throws DestinationUnreachableException
	{
		_map = theMap;
		initialize(); // in case this method has already been called before
		Node source = new Node(start);
		_destination = new Node(finish); // current destination
		
		if (_debug)
			System.out.println("Start " + source + " Destination "
					+ _destination);
		source.setSourceDistance(0);
		_reached.add(source);
		_candidate.add(_destination);
		boolean failed = false;
		Node bestCandidate = null;
		Node bestReached = null;
		// The real work is here:
		while (!_reached.contains(_destination) && !failed)
		{
			_count++;
			float minDistance = BIG;
			float distance;// find node closest to source; check all combinations of reached and candidate nodes
			for (Node reached : _reached)
			{
				for (Node candidate : _candidate)
				{
					if (!reached.isBlocked(candidate))// candidate node not already blocked from reached node
					{
						distance = reached._sourceDistance
								+ reached.getDistance(candidate);
						if (minDistance > distance)
						{
							minDistance = distance;
							bestReached = reached;
							bestCandidate = candidate;
						}
					}
				}
			}
			if (_debug)
				System.out.println("bestCandidate " + bestCandidate
						+ " minDist " + minDistance);
			// is the line from bestReached to bestCandidate blocked?  The segmentBlocked method adds nodes
			if (!segmentBlocked(bestReached, bestCandidate))
			{
				{
					bestCandidate.setPredecessor(bestReached); // allows backtrack to recover the path
					bestCandidate.setSourceDistance(minDistance);
				}
				// move bestCandidate from _candidate set to _reached set
				_reached.add(bestCandidate);
				_candidate.remove(bestCandidate);
				if (_debug)
					System.out.println("Moved from candidate to reached "
							+ bestCandidate + " source dist "
							+ bestCandidate.getSourceDistance() + " of "
							+ _candidate.size());
			} else
			{ // line from bestReached to bestCandidate is blocked
				if (_debug)
					System.out.println("bestCandidate " + bestCandidate
							+ " blocked from " + bestReached);
				bestReached.block(bestCandidate);
				bestCandidate.block(bestReached);
			}
			failed = minDistance >=BIG;
		}// end while
		if (failed)
			throw new DestinationUnreachableException();
		if (_debug) 
			{
			 System.out.println("DONE  iteration count = " + _count);
			for (Node n : _reached)System.out.println("Node "+n+"Distance from source" + n.getSourceDistance());
			}
		return getRoute(_destination);
	}

	/**
	 * Helper method for findPath(). Determines if the straight line segment
	 * crosses a line on the map. Side effect: creates nodes at the end of the
	 * blocking line and adds them to the _candidate set
	 * 
	 * @param from
	 *            the beginning of the line segment
	 * @param theDest
	 *            the end of the line segment
	 * @return true if the segment is blocked
	 */
	private boolean segmentBlocked(final Node from, final Node theDest)
	{
		Node to = new Node(theDest.getLocation()); // alias the destination
		Node n1 = null; // one end of the blocking line
		Node n2 = null; // other end of the blocking line
		Line line = null; // the line connecting from node with to node
		Point intersection; // point where the segment crosses the blocking line
		boolean blocked = false;
		Line segment = new Line(from.getX(), from.getY(), to.getX(), to.getY());
		for (Line l : _map)// test every line in the map to see if it blocks the segment
		{
			intersection = l.intersectsAt(segment);
			if (intersection != null && !from.atEndOfLine(l)
					&& !to.atEndOfLine(l))
			{ // segment is not blocked if the intersection point is not a line end. 
				line = l;
				blocked = true;
				break;
			}// nodes at end of the line
		}
		if (blocked) // add end points of the blocking segment to inCandidateSet
						// set
		{
			if (_debug)
				System.out.println("  blocked from " + from + " to " + theDest);
			Point p1 = line.getP1();
			Point p2 = line.getP2();
			n1 = new Node((float) p1.getX(), (float) p1.getY());
			if (!inReachedSet(n1) && !inCandidateSet(n1))
			{
				n1.setSourceDistance(from.getSourceDistance()
						+ from.getDistance(n1));
				_candidate.add(n1);
				if (_debug)
					System.out.println("Candidate add n1 " + n1
							+ " Source Distance " + n1.getSourceDistance());
			}
			n2 = new Node((float) p2.getX(), (float) p2.getY());
			if (!inReachedSet(n2) && !inCandidateSet(n2))
			{
				n2.setSourceDistance(from.getSourceDistance()
						+ from.getDistance(n2));
				_candidate.add(n2);
				if (_debug)
					System.out.println("Candidate add n2 " + n2
							+ " Source Distance " + n2.getSourceDistance());
			}
		}
		return blocked;
	}

	/**
	 * helper method for findPath; check if aNode is in the set of reached nodes
	 * 
	 * @param aNode
	 * @return true if aNode has been reached already
	 */
	private boolean inReachedSet(final Node aNode)
	{
		boolean found = false;
		for (Node n : _reached)
		{
			found = aNode.equals(n);
			if (found)
				break;
		}
		return found;
	}

	/**
	 * helper method for findPath; check if aNode is in the set of candidate
	 * nodes
	 * 
	 * @param aNode
	 * @return true if aNode has been reached already
	 */
	public boolean inCandidateSet(final Node aNode)
	{
		boolean found = false;
		for (Node n : _candidate)
		{
			found = aNode.equals(n);
			if (found)
				break;
		}
		return found;
	}

	/**
	 * helper method for find path() <br>
	 * calculates the route backtracking through predecessor chain
	 * 
	 * @param destination
	 * @return the route of the shortest path
	 */
	private Path getRoute(Node destination)
	{
		Path route = new Path();
		Node n = destination;
		Waypoint w;
		do
		{ // add waypoints to route as push down stack
			w = new Waypoint(n.getLocation());
			route.add(0, w);
			n = n.getPredecessor();
		} while (n != null);
		return route;
	}

	public void setMap(ArrayList<Line> theMap)
	{
		_map = theMap;
	}

	public void setMap(LineMap theMap)
	{
		Line[] lines = theMap.getLines();
		for (int i = 0; i < lines.length; i++)
			_map.add(lines[i]);
	}

	public void setDebug(boolean yes)
	{
		_debug = yes;
	}

	/**
	 * lengthens all the lines in the map by delta at each end
	 * 
	 * @param delta
	 *            added to each end of each line
	 */
	public void lengthenLines(float delta)
	{
		for (Line line : _map)
		{
			line.lengthen(delta);
		}
	}

	private void initialize()
	{
		_reached = new ArrayList<Node>();
		_candidate = new ArrayList<Node>();
	}

	public ArrayList<Line> getMap()
	{
		return _map;
	}

	public int getIterationCount()
	{
		return _count;
	}

	public int getNodeCount()
	{
		return _reached.size();
	}

	public void addListener(WaypointListener wpl)
	{
		if (listeners == null)
			listeners = new ArrayList<WaypointListener>();
		listeners.add(wpl);
	}

	public void startPathFinding(Pose start, Waypoint end)
	{
		Path solution = null;
		try
		{
			solution = findPath(start.getLocation(), end, _map);
		} catch (DestinationUnreachableException e)
		{
			System.out.println("Destinatin "+_destination +" not reachable ");
			return;
		}
		if (listeners != null)
		{
			for (WaypointListener l : listeners)
			{
				Iterator<Waypoint> iterator = solution.iterator();
				while (iterator.hasNext())
				{
					l.addWaypoint(iterator.next());
				}
				l.pathGenerated();
			}
		}
	}
	// *********** instance variables in ShortestPathFinder *******************
	private ArrayList<WaypointListener> listeners;
	private int _count = 0;
	private static final float BIG = 999999999;
	private Node _destination;
	private Node _source;
	/**
	 * the set of nodes that are candidates for being in the shortest path, but
	 * whose distance from the start node is not yet known.
	 */
	private ArrayList<Node> _candidate = new ArrayList<Node>();
	/**
	 * the set of nodes that are candidates for being in the shortest path, and
	 * whose distance from the start node is known
	 */
	private ArrayList<Node> _reached = new ArrayList<Node>();
	/**
	 * The map of the obstacles
	 */
	private ArrayList<Line> _map = new ArrayList<Line>();
	private boolean _debug = false;

	// ************Begin definition of Node class **********************
	public class Node
	{
		public Node(Point p)
		{
			_p = p;
		}

		private Node(float x, float y)
		{
			this(new Point(x, y));
		}

		/**
		 * test if this Node is one of the ends of theLine
		 * 
		 * @param theLine
		 *            endpoints to check
		 * @return true if this node is an end of the line
		 */
		private boolean atEndOfLine(Line theLine)
		{
			return _p.equals(theLine.getP1()) || _p.equals(theLine.getP2());
		}

		/**
		 * Set the distance of this Node from the source
		 * 
		 * @param theDistance
		 */
		private void setSourceDistance(float theDistance)
		{
			_sourceDistance = theDistance;
		}

		/**
		 * Return the shortest path length to this node from the start node
		 * 
		 * @return shortest distance
		 */
		private float getSourceDistance()
		{
			return _sourceDistance;
		}

		/**
		 * Get the straight line distance from this node to aPoint
		 * 
		 * @param aPoint
		 * @return the distance
		 */
		private float getDistance(Point aPoint)
		{
			return (float) _p.distance(aPoint);
		}

		/**
		 * Return the straight line distance from this node to aNode or a big
		 * number if the straight line is known to be blocked
		 * 
		 * @param aNode
		 * @return the distance
		 */
		private float getDistance(Node aNode)
		{
			if (isBlocked(aNode))
				return BIG;
			return getDistance(aNode.getLocation());
		}

		/**
		 * return the location of this node
		 * 
		 * @return the location
		 */
		private Point getLocation()
		{
			return _p;
		}

		/**
		 * add aNode to list of nodes that are not a neighbour of this Node
		 * 
		 * @param aNode
		 */
		private void block(Node aNode)
		{
			_blocked.add(aNode);
		}

		/**
		 * set the predecessor of this node in the shortest path from the start
		 * node
		 * 
		 * @param thePredecessor
		 */
		private void setPredecessor(Node thePredecessor)
		{
			_predecessor = thePredecessor;
		}

		/**
		 * get the predecessor of this node in the shortest path from the start
		 * 
		 * @return the predecessor node
		 */
		private Node getPredecessor()
		{
			return _predecessor;
		}

		/**
		 * get the X coordinate of this node
		 * 
		 * @return X coordinate
		 */
		private float getX()
		{
			return (float) _p.getX();
		}

		/**
		 * get the Y coordinate of the Node
		 * 
		 * @return Y coordinate
		 */
		private float getY()
		{
			return (float) _p.getY();
		}

		public boolean equals(Node n)
		{
			return this._p.equals(n._p);
		}

		public boolean isBlocked(Node aNode)
		{
			boolean blocked = false;
			for (Node n : _blocked)
			{
				blocked = aNode.equals(n);
				if (blocked)
					break;
			}
			return blocked;
		}

		@Override
		public String toString()
		{
			return " " + getX() + " , " + getY() + " ";
		}
		private Point _p; // the location of this SPNode
		private float _sourceDistance;
		private Node _predecessor;
		public ArrayList<Node> _blocked = new ArrayList<Node>();
	}
}
//**************** end Node class ****************************


