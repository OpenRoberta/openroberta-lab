package lejos.robotics.subsumption;


/**
 * Arbitrator controls which Behavior object will become active in
 * a behavior control system. Make sure to call start() after the 
 * Arbitrator is instantiated.<br>
 *  This class has three major responsibilities: <br> 
 * 1. Determine the highest priority  behavior that returns <b> true </b> to takeControl()<br>   
 * 2. Suppress the active behavior if its priority is less than highest
 * priority. <br>   
 * 3. When the action() method exits, call action() on the Behavior of highest priority.
 * <br>  The Arbitrator assumes that a Behavior is no longer active when action() exits,
 * <br>  therefore it will only call suppress() on the Behavior whose action() method is running.
 * <br>  It can make consecutive calls of action() on the same Behavior.
 * <br>  Requirements for a Behavior:
 * <br>    When suppress() is called, terminate  action() immediately.
 * <br>    When action() exits, the robot is in a safe state (e.g. motors stopped)
 * @see Behavior
 * @author Roger Glassey
 */
public class Arbitrator
{

  private final int NONE = -1;
  private Behavior[] _behavior;
  // highest priority behavior that wants control ; set by start() usec by monitor
  private int _highestPriority = NONE;
  private int _active = NONE; //  active behavior; set by montior, used by start();
  private boolean _returnWhenInactive;
  /**
   * Monitor is an inner class.  It polls the behavior array to find the behavior of hightst
   * priority.  If higher than the active behavior, it calls active.suppress()
   */
  private Monitor monitor;

  /**
   * Allocates an Arbitrator object and initializes it with an array of
   * Behavior objects. The index of a behavior in this array is its priority level, so 
   * the behavior of the largest index has the highest the priority level. 
   * The behaviors in an Arbitrator can not
   * be changed once the arbitrator is initialized.<BR>
   * <B>NOTE:</B> Once the Arbitrator is initialized, the method start() must be
   * called to begin the arbitration.
   * @param behaviorList an array of Behavior objects.
   * @param returnWhenInactive if <B>true</B>, the <B>start()</B> method returns when no Behavior is active.
   */
  public Arbitrator(Behavior[] behaviorList, boolean returnWhenInactive)
  {
    _behavior = behaviorList;
    _returnWhenInactive = returnWhenInactive;
    monitor = new Monitor();
    monitor.setDaemon(true);
  }

  /**
   * Same as Arbitrator(behaviorList, false) Arbitrator start() never exits
   * @param behaviorList An array of Behavior objects.
   */
  public Arbitrator(Behavior[] behaviorList)
  {
    this(behaviorList, false);
  }

  /**
   * This method starts the arbitration of Behaviors and runs an endless loop.  <BR>
   * Note: Arbitrator does not run in a separate thread. The start()
   * method will never return unless <br>1.  no action() method is running  and
   * <br>2. no behavior  takeControl()
   * returns <B> true </B>  and  <br> 3. the <i>returnWhenInacative </i> flag is true,
   */
  public void start()
  {
    monitor.start();
    while (_highestPriority == NONE)
    {
      Thread.yield();//wait for some behavior to take contro                    
    }
    while (true)
    {
      synchronized (monitor)
      {
        if (_highestPriority != NONE)
        {
          _active = _highestPriority;

        } else if (_returnWhenInactive)
        {// no behavior wants to run
          monitor.more = false;//9 shut down monitor thread
          return;
        }
      }// monotor released before action is called
      if (_active != NONE)  //_highestPrioirty could be NONE
      {
        _behavior[_active].action();
        _active = NONE;  // no active behavior at the moment
      }
      Thread.yield();
    }
  }

  /**
   * Finds the highest priority behavior that returns <B>true </B> to takeControl();
   * If this priority is higher than the active behavior, it calls active.suppress().
   * If there is no active behavior, calls suppress() on the most recently acrive behavior.
   */
  private class Monitor extends Thread
  {

    boolean more = true;
    int maxPriority = _behavior.length - 1;

    public void run()
    {
      while (more)
      {
        //FIND HIGHEST PRIORITY BEHAVIOR THAT WANTS CONTROL
        synchronized (this)
        {
           _highestPriority = NONE;
          for (int i = maxPriority; i >= 0; i--)
          {
            if (_behavior[i].takeControl())
            {
              _highestPriority = i;
              break;
            }
          }
          int active = _active;// local copy: avoid out of bounds error in 134
          if (active != NONE && _highestPriority > active)
          {
            _behavior[active].suppress();
          }
        }// end synchronize block - main thread can run now
        Thread.yield();
      }
    }
  }
}
  
