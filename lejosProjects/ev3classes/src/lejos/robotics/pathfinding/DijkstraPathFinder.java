package lejos.robotics.pathfinding;

import lejos.robotics.geometry.*;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.navigation.WaypointListener;

import java.util.*;

/**
 * This class calculates the shortest path from a starting point to a finish point.
 * while avoiding obstacles that are represented as a set of straight lines.
 * The path passes through the end points of some of these lines, which is where the
 * changes of direction occur.  Since the robot is not point, the lines representing
 * the obstacles should be lengthened so the actual robot will miss the actual obstacles.
 * Use the lengthenLines() method to do this.
 * Uses the A* algorithm, a variant of the Dijkstra shortest path algorithm
 * It uses the Node inner class for its internal representation of points.
 *
 * @author Roger Glassey
 */
public class DijkstraPathFinder implements PathFinder
{



public DijkstraPathFinder(LineMap map)
{
  setMap(map);
}
/**
 * Finds the shortest path from start to finish using the map (or collection of lines)
 * in the constructor.
 * @param start  the initial robot pose
 * @param finish the final robot location
 * @return the shortest route
 * @throws DestinationUnreachableException  if, for example, you nave not called setMap();
 */
  public Path findRoute(Pose start, Waypoint finish) throws DestinationUnreachableException
  {
    return findPath(start.getLocation(), finish, _map);
  }

  /**
 * Finds the shortest path from start to finish using the map (or collection of lines)
 * in the constructor.
 * @param start  the initial robot pose
 * @param finish the final robot location
 * @param theMap  the LineMap of obstacles
 * @return the shortest route
 * @throws DestinationUnreachableException  if, for example, you nave not called setMap();
 */
  public Path findRoute(Pose start, Waypoint finish, LineMap theMap) throws DestinationUnreachableException
  {
    setMap(theMap);
    return findPath(start.getLocation(), finish, _map);
  }
  /**
   * finds the shortest path between start  and finish Points whild avoiding the obstacles
   * in the map
   * @param start : the beginning of the path
   * @param finish : the destination
   * @param theMap  that contains the obstacles
   * @return an array list of waypoints.  If no path exists, returns null
   */
  
  private Path findPath(Point start, Point finish, ArrayList<Line> theMap)throws DestinationUnreachableException
  {
    _map = theMap;
    initialize(); // in case this method has already been called before
    Node source = new Node(start);
    Node destination = new Node(finish);  // current destination
    source.setSourceDistance(0);
    _reached.add(source);
    _candidate.add(destination);
    Node from;  // current start node
    Node dest;  // current destination node
    int index = _candidate.size()-1;  //index of current destination in candidate set
    boolean failed = false;

    while (_candidate.size() > 0 && !failed)
    {
      _count++;
      // get destination from inCandidateSet set
      dest = _candidate.get(index);
      from = getBest(dest);
      float distance = from.getDistance(dest);
      if (distance >= BIG)  // dest is known to be blocked  from best node in  _reached
      {
        index--;  // try another temporary start node
        failed = index < 0; // tried the whole stack.
      } else
      { // dest is not known to be blocked  from  best  reached node
        if (segmentBlocked(from, dest))
        { // this method call possibly created and added new nodes to the _candidate set
          from.block(dest);//  Record dest as not directly reachable
          index = _candidate.size() - 1;  // search from top  from top of stack
        } else  // not blocked  so dest node has is  reached
        {
          if (distance < .05f) // essentially same node as best node in _reached,
          { // so will not be a separate way point in the route
            dest.setPredecessor(from.getPredecessor());
            dest.setSourceDistance(from.getSourceDistance());
          } else
          {
            dest.setPredecessor(from); // allows backtracking to recover the path
            dest.setSourceDistance(from.getSourceDistance() + from.getDistance(dest));
          }
          // move dest from _candidate to _reached
          _reached.add(dest);
          _candidate.remove(dest);  // pop the stack
          index = _candidate.size() - 1;
        } // end else  dest not blocked

      } // end else dest not previously blocked
    }// end while
    if (failed)
    {
      throw new DestinationUnreachableException();
//      return null;
    }
    return getRoute(destination);
  }

 public void setMap(ArrayList<Line> theMap)
 {
   _map = theMap;
 }
     
 public void setMap(LineMap theMap)
 {
     Line [] lines = theMap.getLines();
    for(int i = 0; i < lines.length; i++)
    _map.add(lines[i]);
 }


 /**
  * lengthens all the lines in the map by delta at each end
  * @param delta   added to each end of each  line
  */
 public void lengthenLines( float delta)
 {
   for (Line line : _map)
   {
     line.lengthen(delta);
   }
 }
  protected void initialize()
  {
    _reached = new ArrayList<Node>();
    _candidate = new ArrayList<Node>();
  }

  /**
   * helper method for findPath(). Determines if the straight line segment 
   * crosses a line on the map.
   * Side effect: creates nodes at the end of the blocking line and adds them to the _candidate set
   * @param from the  beginning of the line segment
   * @param theDest the end of the line segment
   * @return  true if the segment is blocked
   */
  protected boolean segmentBlocked(final Node from, final Node theDest)
  {
    Node to = new Node(theDest.getLocation()); // alias the destination
    Node n1 = null; // one end of the blocking line
    Node n2 = null; // other end of the blocking line
    Line line = null; // the line connecting  from node   with to node
    Point intersection; // point where the segment crosses the blocking line
    boolean blocked = false;
    Line segment = new Line(from.getX(), from.getY(),
            to.getX(), to.getY());
    for (Line l : _map)// test ever line in the map to see if it blocks the segment
    {
      intersection = l.intersectsAt(segment);
      if (intersection != null && !from.atEndOfLine(l) && !to.atEndOfLine(l))
      {  //segment is legal if it starts or ends at an end point of the line
        line = l;
        blocked = true;
      }// nodes at end of the line
    }
    if (blocked)  // add end points of the blocking segment to  inCandidateSet set
    {
      Point p1 =  line.getP1();
      Point  p2 = line.getP2();
      n1 = new Node((float)p1.getX(),(float)p1.getY());
      if(!inReachedSet(n1) &&!inCandidateSet(n1))
      {
        n1.setSourceDistance(from.getSourceDistance() + from.getDistance(n1));
        _candidate.add(n1);
      }
       n2 = new Node((float)p2.getX(),(float)p2.getY());
       if(!inReachedSet(n2) && !inCandidateSet(n2))
      {
        n2.setSourceDistance(from.getSourceDistance() + from.getDistance(n2));
        _candidate.add(n2);
      }
    }
    return blocked;
  }

  /**
   * Helper method for findPath() <br>
   * returns the  node in  the Reached set, whose distance from the start node plus
   * its straight line distance to the destination is the minimum.
   * @param currentDestination : the current destination node, (in the Candidate set)
   * @return the node the node which could be the last node in the shortest path
   */
  protected  Node getBest(Node currentDestination)
  {
    Node best = _reached.get(0);
    float minDist = best._sourceDistance + best.getDistance(currentDestination);
    for (Node n : _reached)
    {
      float d = n._sourceDistance + n.getDistance(currentDestination);
      if (d < minDist)
      {
        minDist = d;
        best = n;
      }
    }
    return best;
  }


  /**
   * helper method for findPath; check if aNode is in the set of reached nodes
   * @param aNode
   * @return true if aNode has been reached already
   */
  protected boolean inReachedSet(final Node  aNode)
  {
    boolean found = false;
    for (Node n : _reached)
    {
      found = aNode.getLocation().equals(n.getLocation());
      if (found) break;
    }
    return found;
  }
  
/**
   * helper method for findPath; check if aNode is in the set of candidate nodes
   * @param aNode
   * @return true if aNode has been reached already
   */
  protected boolean inCandidateSet(final Node aNode)
  {
    boolean found = false;
    for (Node n : _candidate)
    {
      found = aNode.getLocation().equals(n.getLocation());
      if (found) break;
    }
    return found;
  }

  /**
   * helper method for find path() <br>
   * calculates the route backtracking through predecessor chain
   * @param destination
   * @return the route of the shortest path
   */
protected  Path getRoute(Node destination)
{
    Path route = new Path();
    Node n = destination;
    Waypoint  w ;
    do {  // add waypoints to route as push down stack
      w = new Waypoint(n.getLocation());
      route.add(0, w);
      n = n.getPredecessor();
    } while (n != null);
    return route;
}
  protected ArrayList<Line> getMap()
  {
   return _map;
  }
  public int getIterationCount(){ return _count;}

  public int getNodeCount(){return _reached.size();}

  public void addListener(WaypointListener wpl) {
    if(listeners == null )listeners = new ArrayList<WaypointListener>();
    listeners.add(wpl);
  }
  
  public void startPathFinding(Pose start, Waypoint end) {
	  Collection<Waypoint> solution = null;
	  try {
		  solution = findPath(start.getLocation(), end, _map);
	  } catch (DestinationUnreachableException e) {
		  // TODO Not sure how to handle this.
		  e.printStackTrace();
	  }
	  if(listeners != null) { 
		  for(WaypointListener l : listeners) {
			  Iterator<Waypoint> iterator = solution.iterator(); 
			  while(iterator.hasNext()) {
				  l.addWaypoint(iterator.next());
			  }
			  l.pathGenerated();
		  }
	  }
  }

  //***********  instance variables in ShortestPathFinder *******************
  private ArrayList<WaypointListener> listeners ;
  
  protected    int _count =  0;
  /**
   * set by segmentBlocked() used by findPath()
   */
  protected boolean _blocked = false;

  private static final float BIG = 999999999;

  /**
   * the set of nodes that are candidates for being in the shortest path, but
   * whose distance from the start node is not yet known
   */
  protected ArrayList<Node> _candidate = new ArrayList<Node>();

  /**
   * the set of nodes that are candidates for being in the shortest path, and
   * whose distance from the start node is known
   */
 protected  ArrayList<Node> _reached = new ArrayList<Node>();
  /**  
   * The map of the obstacles
   */
  protected  ArrayList<Line> _map = new ArrayList<Line>();


//************Begin definition of Node class  **********************
 protected class Node
{
  public Node(Point p)
  {
    _p = p;
  }
  public Node(float x, float y)
  {
    this(new Point(x,y));
  }


/**
 * test if this Node is one of the ends of  theLine
 * @param theLine  endpoints to check
 * @return true if this node is an end of the line
 */
  public boolean atEndOfLine(Line theLine)
  {
    return _p.equals(theLine.getP1()) || _p.equals(theLine.getP2());
  }
  /**
   * set the distance of this Node from the source
   * @param theDistance
   */
  public void setSourceDistance(float theDistance)
  {
    _sourceDistance = theDistance;
  }
  /**
   * return the shortest path length to this node from the start node
   * @return shortest distance
   */
  public float getSourceDistance(){return _sourceDistance;}

  /**
   * get the straight line distance from this node to aPoint
   * @param aPoint
   * @return the distance
   */
  public float getDistance(Point aPoint)
  {

    return (float)_p.distance(aPoint);
  }

  /**
   * return the straight distance from this node to aNode
   * @param aNode
   * @return the distance
   */
  public float getDistance(Node aNode)
  {
    if(_blocked.indexOf(aNode) > -1) return BIG;
    else return getDistance(aNode.getLocation());
  }

  /**
   * return the location of this node
   * @return the location
   */
 public Point getLocation()
 {
   return _p;
 }

 /**
  * add aNode to list of nodes not a neighbour of this Node
  * @param aNode
  */
 public void block(Node aNode)
 {
   _blocked.add(aNode);
 }

 /**
  * set the predecessor of this node in the shortest path from the start node
  * @param thePredecessor
  */
 public void setPredecessor(Node thePredecessor)
 {_predecessor = thePredecessor;}
 /**
  * get the predecessor of this node in the shortest path from the start
  * @return the predecessor node
  */
 public Node getPredecessor() { return _predecessor;}

 /**
  * get the X coordinate of this node
  * @return X coordinate
  */
  public float getX(){return (float)_p.getX();}
  /**
   * get the Y coordinate of thes Node
   * @return Y coordinate
   */
  public float getY(){return (float)_p.getY();}
  public String toString(){return " "+getX()+" , "+getY()+" ";}
  protected Point _p;
  protected float _sourceDistance;
  protected Node _predecessor;
  public ArrayList<Node> _blocked = new ArrayList<Node>();
  private float BIG = 100000;
 }
// ****************   end Node class ****************************

} 

