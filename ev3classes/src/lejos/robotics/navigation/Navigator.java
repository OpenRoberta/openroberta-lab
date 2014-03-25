package lejos.robotics.navigation;

import java.util.ArrayList;

import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.pathfinding.Path;

/**
 * This class controls a robot to traverse a Path,  a sequence of  {@link  lejos.robotics.navigation.Waypoint}s.
 * It's default mode for a new path is continuous movement (no stopping at waypoints)  but see also {@link #singleStep(boolean)}.  To interrupt the path traversal,  call stop().
 *  It uses  an inner class running its own thread to issue movement commands to its
 * {@link lejos.robotics.navigation.MoveController},
 * which can be either a  {@link lejos.robotics.navigation.DifferentialPilot}
 * or {@link lejos.robotics.navigation.SteeringPilot}.
 * It also uses a {@link lejos.robotics.localization.PoseProvider}
 * Calls its {@link lejos.robotics.navigation.NavigationListener}s
 * when  a Waypoint is reached or the robot stops.
 * This class has only one blocking method: {@link #waitForStop()} .
 * @author Roger Glassey
 */
public class Navigator implements WaypointListener
{

   /**
    * Allocates a Navigator object,  using pilot that implements the ArcMoveController interface. 
    * @param pilot
    */
   public Navigator(MoveController pilot)
   {
      this(pilot, null);
   }

   /**
    * Allocates a Navigator object using a pilot and a custom poseProvider, rather than the default
    * OdometryPoseProvider.  
    * @param pilot  the pilot 
    * @param poseProvider  the custom PoseProvider
    */
   public Navigator(MoveController pilot, PoseProvider poseProvider)
   {
      _pilot = pilot;
      if (poseProvider == null)
         this.poseProvider = new OdometryPoseProvider(_pilot);
      else
         this.poseProvider = poseProvider;
      _radius = (_pilot instanceof ArcMoveController ? ((ArcMoveController) _pilot).getMinRadius() : 0);
      _nav = new Nav();
      _nav.setDaemon(true);
      _nav.start();
   }

   /**
    * Sets  the PoseProvider after construction of the Navigator
    * @param aProvider  the PoseProvider
    */
   public void setPoseProvider(PoseProvider aProvider)
   {
      poseProvider = aProvider;
   }

   /**
    * Adds a NavigationListener that is informed when a the robot stops or 
    * reaches a WayPoint.
    * @param listener  the NavitationListener
    */
   public void addNavigationListener(NavigationListener listener)
   {
      _listeners.add(listener);
   }

   /**
    * Returns the PoseProvider
    * @return the PoseProvider
    */
   public PoseProvider getPoseProvider()
   {
      return poseProvider;
   }

   /**
    * Returns the MoveController belonging to this object.
    * @return the pilot
    */
   public MoveController getMoveController()
   {
      return _pilot;
   }

   /**
    * Sets the path that the Navigator will traverse.
    * By default, the  robot will not stop along the way.
    * If the robot is moving when this method is called,  it stops and the current
    * path is replaced by the new one.
    * @param path to be followed.
    */
   public void setPath(Path path)
   {
      if (_keepGoing)
         stop();
      _path = path;
      _singleStep = false;
      _sequenceNr = 0;
   }
   
   /**
    * Clears the current path.
    * If the robot is moving when this method is called, it stops; 
    */
   public void clearPath() {
	   if (_keepGoing)
	         stop();
	   _path.clear();
   }
   
   /**
    * Gets the current path
    * 
    * @return the path
    */
   public Path getPath() {
	   return _path;
   }

   /**
    * Starts the robot traversing the path. This method is non-blocking. 
    * @param path  to be followed.
    */
   public void followPath(Path path)
   {
      _path = path;
      followPath();
   }

   /**
    * Starts the robot traversing the current path. 
    * This method is non-blocking; 
    */
   public void followPath()
   {  
      if (_path.isEmpty())
         return;
      _interrupted = false;
      _keepGoing = true;
//      RConsole.println("navigator followPath called");
   }

   /**
    * Controls whether the robot stops at each Waypoint; applies to the current path only.
    * The robot will move to the next Waypoint if you call {@link #followPath()}.
    * @param yes  if <code>true </code>, the robot stops at each Waypoint.  
    */
   public void singleStep(boolean yes)
   {
      _singleStep = yes;
   }

   /**
    * Starts the robot moving toward the destination.
    * If no path exists, a new one is created consisting of the destination,
    * otherwise the destination is added to the path.  This method is non-blocking, and is 
    * equivalent to <code>{@linkplain #addWaypoint(Waypoint) addWaypoint(destination);}
    * {@linkplain #followPath() followPath();}</code>
    * @param destination  the waypoint to be reached
    */
   public void goTo(Waypoint destination)
   {
      addWaypoint(destination);
      followPath();
 
   }

   /**
    * Starts the  moving toward the destination Waypoint created from 
    * the parameters. 
    * If no path exists, a new one is created,  
    * otherwise the new Waypoint is added to the path.  This method is non-blocking, and is 
    * equivalent to 
     <code>add(float x, float y);   followPath(); </code>
    * @param x  coordinate of the destination
    * @param y  coordinate of the destination
    */
   public void goTo(float x, float y)
   {
      goTo(new Waypoint(x, y));
   }

   /**
    * Starts the  moving toward the destination Waypoint created from 
    * the parameters. 
     * If no path exists, a new one is created,  
    * otherwise the new Waypoint is added to the path.  This method is non-blocking, and is 
    * equivalent to 
     <code>add(float x, float y);   followPath(); </code>
    * @param x coordinate of the destination
    * @param y coordinate of th destination
    * @param heading  desired robot heading at arrival 
    */
   public void goTo(float x, float y, float heading)
   {
      goTo(new Waypoint(x, y, heading));
   }

   /**
    * Rotates the robot to a new absolute heading. For example, rotateTo(0) will line the robot with the 
    * x-axis, while rotateTo(90) lines it with the y-axis. If the robot is currently on the move to a
    * coordinate, this method will not attempt to rotate and it will return false.
    * @param angle The absolute heading to rotate the robot to. Value is 0 to 360.
    * @return true if the rotation happened, false if the robot was moving while this method was called.
    */
   public boolean rotateTo(double angle) {
	   float head = getPoseProvider().getPose().getHeading();
	   double diff = angle - head;
	   while(diff > 180) diff = diff - 360;
	   while(diff < -180) diff = diff + 360;
	   if(isMoving()) return false;
	   if(_pilot instanceof RotateMoveController)
		   ((RotateMoveController) _pilot).rotate(diff);
	   return true;
	   
   }
   
   /**
    * Adds a  Waypoint  to the end of the path. 
    * Call {@link #followPath()} to start moving the along the current path.
    * 
    * @param aWaypoint  to be added
    */
   public void addWaypoint(Waypoint aWaypoint)
   {
	   if(_path.isEmpty())
		   {
		   _sequenceNr = 0;
		   _singleStep = false;
		   }
      _path.add(aWaypoint);
   }

   /**
    * Constructs an new Waypoint from the parameters and adds it to the end of the path.
    * Call {@link #followPath()} to start moving the along the current path.
    * 
    * @param x coordinate of the waypoint
    * @param y coordinate of the waypoint
    */
   public void addWaypoint(float x, float y)
   {
      addWaypoint(new Waypoint(x, y));
   }

   /**
    * Constructs an new Waypoint from the parameters and adds it to the end of the path. 
    * Call {@link #followPath()} to start moving the along the current path.
    * 
    * @param x coordinate of the waypoint
    * @param y coordinate of the waypoint
    * @param heading the heading of the robot when it reaches the waypoint
    */
   public void addWaypoint(float x, float y, float heading)
   {
      addWaypoint(new Waypoint(x, y, heading));
   }

   /**
    * Stops the robot. 
    * The robot will resume its path traversal if you call {@link #followPath()}.
    */
   public void stop()
   {
      _keepGoing = false;
      _pilot.stop();
      _interrupted = true;
      callListeners();
   }

   /**
    * Returns the waypoint to which the robot is presently moving.
    * @return the waypoint ; null if the path is empty.
    */
   public Waypoint getWaypoint()
   {
      if (_path.size() <= 0)
         return null;
      return _path.get(0);
   }

   /** 
    * Returns <code> true </code> if the the final waypoint has been reached 
    * @return  <code> true </code>  if the path is completed
    */
   public boolean pathCompleted()
   {
      return _path.size() == 0;
   }

   /**
    * Waits for the robot  to stop for any reason ;
    * returns <code>true</code> if the robot stopped at the final Waypoint of
    *the  path. 
    * @return   <code> true </code>  if the path is completed
    */
   public boolean waitForStop()
   {
      while (_keepGoing)
         Thread.yield();
      return _path.isEmpty();
   }

   /**
    * Returns <code>true<code> if the robot is moving toward a waypoint.
    * @return  <code>true </code> if moving.
    */
   public boolean isMoving()
   {
      return _keepGoing;
   }
   
   public void pathGenerated() {
		// Currently does nothing	
	}

   private void callListeners()
   {
      if (_listeners != null)
      {
         _pose = poseProvider.getPose();
//            RConsole.println("listener called interrupt"+_interrupted +" done "+_path.isEmpty()+" "+_pose);
         for (NavigationListener l : _listeners)
            if (_interrupted)
               l.pathInterrupted(_destination, _pose, _sequenceNr);
            else
            {
               l.atWaypoint(_destination, _pose, _sequenceNr);
               if (_path.isEmpty())
                  l.pathComplete(_destination, _pose, _sequenceNr);
            }
      }
   }

  /**
   * This inner class runs the thread that processes the waypoint queue
   */
  private class Nav extends Thread
  {
	boolean more = true;

    @Override
	public void run()
    { 
      while (more)
      {
        while (_keepGoing && _path != null && ! _path.isEmpty())
        { 
          _destination = _path.get(0);
          _pose = poseProvider.getPose();
//          RConsole.println("NAV loop begin "+_destination);         
          float destinationRelativeBearing = _pose.relativeBearing(_destination);
          if(!_keepGoing) break;
          if(_radius == 0)  // differential pilot used
          {  
            ((RotateMoveController) _pilot).rotate(destinationRelativeBearing,true); 
            while (_pilot.isMoving() && _keepGoing)Thread.yield(); 
            if(!_keepGoing) break;
          }
          else // begin arc direction change
          {
  			// 1. Get shortest path:
  			Move [] moves;
  			double minRadius = (_pilot instanceof ArcMoveController ? 
                       ((ArcMoveController) _pilot).getMinRadius() : 0);
  			
  			if (_destination.headingRequired)
  			{
  				moves = ArcAlgorithms.getBestPath(poseProvider.getPose(), 
                            (float)minRadius, _destination.getPose(),(float)minRadius);
  			} 
  			else
  			{
  				moves = ArcAlgorithms.getBestPath(poseProvider.getPose(),
                            _destination, (float)minRadius);  				
  			}
  			// 2. Drive the path
  			for(int i=0;i<moves.length;i++) 
  			{
  				((ArcMoveController) _pilot).travelArc(moves[i].getArcRadius(),
                            moves[i].getDistanceTraveled());
  				if (!_keepGoing) break;
  			}
            while (_pilot.isMoving() && _keepGoing)Thread.yield();
          }  // Arc direction change complete
          _pose = poseProvider.getPose();
          if(!_keepGoing) break;
         
          if (_radius == 0) //differential pilot is used
          {
            float distance = _pose.distanceTo(_destination);
            _pilot.travel(distance, true);
            while (_pilot.isMoving() && _keepGoing)Thread.yield(); 
             _pose = poseProvider.getPose();
            if(!_keepGoing) break;
            
            if (_destination.headingRequired) 
            {
            _pose = poseProvider.getPose();
            	_destination.getHeading();
            	((RotateMoveController) _pilot).rotate(_destination.getHeading()
                       - _pose.getHeading());
            }
          }         
          
          if (_keepGoing && ! _path.isEmpty()) 
          { 
//        	  RConsole.println("NAV keep going "+_keepGoing+" pathlength  "+_path.size());
             if(!_interrupted)  //presumably at waypoint     
             { 
//            	 RConsole.println("NAV at waypoint??");
                _path.remove(0);
                _sequenceNr++; 
             }
             callListeners();
             _keepGoing = ! _path.isEmpty();
             if(_singleStep)_keepGoing = false;

//             callListeners();  *** if called here,  sets _keepGoing = true - very strange

             
          }
   
          Thread.yield();
        } // end while keepGoing
        Thread.yield();
      }  // end while more
    }  // end run
  } // end Nav class

  private Nav _nav ;
  private Path _path = new Path();
  /**
   * frequently tested by Nav.run() to break out of primary control loop
   * reset by stop(), and in Nav if _singleStep is set. or end of path is reached
   * set by followPath(xx) and goTo(xx)
   */
  private boolean _keepGoing = false;
  /**
   * if true, causes Nav.run to break whenever  waypoint is reached. 
   */
  private boolean _singleStep = false;
  /** 
   * set by Stop,  reset by followPath() , goTo()
   * used by  Nav.run(), callListeners
   */
  private boolean _interrupted = false;
  private MoveController _pilot;
  private PoseProvider poseProvider;
  private Pose _pose = new Pose();
  private Waypoint _destination;
  private double _radius;
  private int _sequenceNr;
  private ArrayList<NavigationListener> _listeners = new ArrayList<NavigationListener>();
  
}
