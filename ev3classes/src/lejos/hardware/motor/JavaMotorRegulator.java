package lejos.hardware.motor;

import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.utility.Delay;

/**
 * Java based regulator 
 * regulate velocity; also stop motor at desired rotation angle.
 * This class uses a very simple movement model based on simple linear
 * acceleration. This model is used to generate ideal target positions which
 * are then used to generate error terms between the actual and target position
 * this error term is then used to drive a PID style motor controller to
 * regulate the power supplied to the motor.
 *
 * If new command are issued while a move is in progress, the new command
 * is blended with the current one to provide smooth movement.
 *
 * If the requested speed is not possible then the controller will simply
 * drop move cycles until the motor catches up with the ideal position. If
 * too many consecutive dropped moves are required then the motor is viewed
 * to have stalled and the move is terminated.
 *
 * Once the motor stops, the final position is held using the same PID control
 * mechanism (with slightly different parameters), as that used for movement.
 **/
public class JavaMotorRegulator implements MotorRegulator
{
    // PID constants for move and for hold
    // Old values
    //static final float MOVE_P = 4f;
    //static final float MOVE_I = 0.04f;
    //static final float MOVE_D = 32f;
    // New values
    //static final float MOVE_P = 7f;
    //static final float MOVE_P = 6f;
    //static final float MOVE_I = 0.04f;
    //static final float MOVE_D = 22f;
    //static final float MOVE_P = 4f;
    //static final float MOVE_I = 0.04f;
    //static final float MOVE_D = 10f;
    static final float MOVE_P = 4f;
    static final float MOVE_I = 0.04f;
    static final float MOVE_D = 10f;
    static final float HOLD_P = 2f;
    static final float HOLD_I = 0.02f;
    static final float HOLD_D = 8f;
    
    float moveP;
    float moveI;
    float moveD;
    float holdP;
    float holdI;
    float holdD;
    float basePower = 0; //used to calculate power
    float err1 = 0; // used in smoothing
    float err2 = 0; // used in smoothing
    float curVelocity = 0;
    float baseVelocity = 0;
    float baseCnt = 0;
    float curCnt = 0;
    float curAcc = 0;
    float curStopAcc = 0;
    float curTargetVelocity = 0;
    int curLimit = NO_LIMIT;
    boolean curHold = true;
    float accCnt = 0;
    long baseTime = 0;
    long now = 0;
    long accTime = 0;
    boolean moving = false;
    boolean pending = false;
    boolean checkLimit = false;
    float newSpeed = 0;
    int newAcceleration = 0;
    int newLimit = 0;
    boolean newHold = true;
    int tachoCnt;
    int zeroTachoCnt;
    public int power;
    int mode;
    boolean active = false;
    RegulatedMotorListener listener;
    RegulatedMotor motor;
    boolean stalled;
    int stallCnt = 0;
    protected int stallLimit = 50;
    protected int stallTime = 1000;
    protected TachoMotorPort tachoPort;
    protected static final Controller cont = new Controller();
    static {
        // Start the single controller thread
        cont.setPriority(Thread.MAX_PRIORITY);
        cont.setDaemon(true);
        // Mark it as system thread, so it won't happen to get suspended during debugging.
        //VM.updateThreadFlags(cont, VM.VM_THREAD_SYSTEM, 0);
        cont.start();
        // Add shutdown handler to stop the motors
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {cont.shutdown();}
        });
    }

    public JavaMotorRegulator(TachoMotorPort p)
    {
        tachoPort = p;
        tachoPort.setPWMMode(TachoMotorPort.PWM_BRAKE);
        reset();
    }
    
    
    public int getTachoCount()
    {
        return tachoPort.getTachoCount() - zeroTachoCnt;
    }
    
    public synchronized void resetTachoCount()
    {
        newMove(0, 1000, NO_LIMIT, false, true);
        zeroTachoCnt = tachoPort.getTachoCount();
        reset();
    }

    public boolean isMoving()
    {
        return moving;
    }
    
    public float getCurrentVelocity()
    {
        return curVelocity;
    }
    
    public void setStallThreshold(int error, int time)
    {
        this.stallLimit = error;
        this.stallTime = time/Controller.UPDATE_PERIOD;
    }
    
    
    /**
     * Update the internal state of the motor.
     * @param velocity
     * @param hold
     * @param stalled
     */
    protected synchronized void updateState(int velocity, boolean hold, boolean stalled)
    {
        if (listener != null)
        {
            if (velocity == 0)
                listener.rotationStopped(motor, getTachoCount(), stalled, System.currentTimeMillis());
            else
                listener.rotationStarted(motor, getTachoCount(), false, System.currentTimeMillis());
        }
    }
    
    @Override
    public void addListener(RegulatedMotor motor, RegulatedMotorListener listener)
    {
        this.motor = motor;
        this.listener = listener;        
    }


    @Override
    public RegulatedMotorListener removeListener()
    {
        RegulatedMotorListener old = listener;
        listener = null;
        return old;
    }



    @Override
    public void setControlParamaters(int typ, float moveP, float moveI,
            float moveD, float holdP, float holdI, float holdD, int offset)
    {
        // Stop the motor if needed
        newMove(0, 1000, NO_LIMIT, false, true);
        this.moveP = moveP;
        this.moveI = moveI;
        this.moveD = moveD;
        this.holdP = holdP;
        this.holdI = holdI;
        this.holdD = holdD;
        reset();
    }


    @Override
    public void waitComplete()
    {
        waitStop();
    }


    @Override
    public int getLimitAngle()
    {
        return curLimit;
    }


    @Override
    public boolean isStalled()
    {
        return stalled;
    }


    /**
     * Reset the tachometer readings
     */
    protected synchronized void reset()
    {
        curCnt = tachoCnt = getTachoCount();
        baseTime = now = System.currentTimeMillis();
    }

    /**
     * Helper method. Start a sub move operation. A sub move consists
     * of acceleration/deceleration to a set velocity and then holding that
     * velocity up to an optional limit point. If a limit point is set this
     * method will be called again to initiate a controlled deceleration
     * to that point
     * @param speed
     * @param acceleration
     * @param limit
     * @param hold
     */
    synchronized private void startSubMove(float speed, float acceleration, int limit, boolean hold)
    {
        float absAcc = Math.abs(acceleration);
        checkLimit = Math.abs(limit) != NO_LIMIT;
        baseTime = now;
        if (!moving && Math.abs(limit - curCnt) < 1.0)
            curTargetVelocity = 0;
        else
            curTargetVelocity = (limit - curCnt >= 0 ? speed : -speed);
        curAcc = curTargetVelocity - curVelocity >= 0 ? absAcc : -absAcc;
        curStopAcc = curTargetVelocity >= 0 ? absAcc : -absAcc;
        accTime = Math.round(((curTargetVelocity - curVelocity) / curAcc) * 1000);
        accCnt = (curVelocity + curTargetVelocity) * accTime / (2 * 1000);
        baseCnt = curCnt;
        baseVelocity = curVelocity;
        curHold = hold;
        curLimit = limit;
        //limitAngle = curLimit; // limitAngle is in outer. KPT 5/13/2011 06:42
        moving = curTargetVelocity != 0 || baseVelocity != 0;
    }

    /**
     * Helper method, if move is currently active wait for it to be
     * completed
     */
    private void waitStop()
    {
        if (moving)
            try
            {
                wait();
            } catch (Exception e)
            {
            }
    }

    /**
     * return the regulations models current position. Ensure that the motor is active
     * if needed.
     * @return the models current position
     */
    synchronized public float getPosition()
    {
        if (!active)
        {
            cont.addMotor(this);
            active = true;
        }
        return curCnt;
        
    }

    /**
     * Initiate a new move and optionally wait for it to complete.
     * If some other move is currently executing then ensure that this move
     * is terminated correctly and then start the new move operation.
     * @param speed
     * @param acceleration
     * @param limit
     * @param hold
     * @param waitComplete
     */
    synchronized public void newMove(float speed, int acceleration, int limit, boolean hold, boolean waitComplete)
    {
        if (!active)
        {
            cont.addMotor(this);
            active = true;
        }
        // ditch any existing pending command
        pending = false;
        // no longer stalled
        stalled = false;
        // Stop moves always happen now
        if (speed == 0)
            startSubMove(0, acceleration, NO_LIMIT, hold);
        else if (!moving)
        {
            // not moving so we start a new move
            startSubMove(speed, acceleration, limit, hold);
            updateState(Math.round(curTargetVelocity), hold, false);
        }
        else
        {
            // we already have a move in progress can we modify it to match
            // the new request? We must ensure that the new move is in the
            // same direction and that any stop will not exceed the current
            // acceleration request.
            float moveLen = limit - curCnt;
            float acc = (curVelocity*curVelocity)/(2*(moveLen));
            if (moveLen*curVelocity >= 0 && Math.abs(acc) <= acceleration)
                startSubMove(speed, acceleration, limit, hold);
            else
            {
                // Save the requested move
                newSpeed = speed;
                newAcceleration = acceleration;
                newLimit = limit;
                newHold = hold;
                pending = true;
                // stop the current move
                startSubMove(0, acceleration, NO_LIMIT, true);
                // If we need to wait for the existing command to end
                if (waitComplete)
                    waitStop();
            }
        }
        if (waitComplete)
            waitStop();
    }

    /**
     * The target speed has been changed. Reflect this change in the
     * regulator.
     * @param newSpeed new target speed.
     */
    public synchronized void adjustSpeed(float newSpeed)
    {
        if (curTargetVelocity != 0)
        {
            startSubMove(newSpeed, curAcc, curLimit, curHold);
        }
        if (pending)
            this.newSpeed = newSpeed;
    }

    /**
     * The target acceleration has been changed. Updated the regulator.
     * @param newAcc
     */
    public synchronized void adjustAcceleration(int newAcc)
    {
        if (curTargetVelocity != 0)
        {
            startSubMove(Math.abs(curTargetVelocity), newAcc, curLimit, curHold);
        }
        if (pending)
            newAcceleration = newAcc;
    }

    /**
     * The move has completed either by the motor stopping or by it stalling
     * @param stalled
     */
    synchronized private void endMove(boolean stalled)
    {
        moving = pending;
        this.stalled = stalled;
        updateState(0, curHold, stalled);
        if (stalled)
        {
            // stalled try and maintain current position
            reset();
            curVelocity = 0;
            stallCnt = 0;
            startSubMove(0, 0, NO_LIMIT, curHold);
        }
        // if we have a new move, go start it
        if (pending)
        {
            pending = false;
            startSubMove(newSpeed, newAcceleration, newLimit, newHold);
            updateState(Math.round(curTargetVelocity), curHold, false);
        }
        notifyAll();
    }

    /**
     * Monitors time and tachoCount to regulate velocity and stop motor rotation at limit angle
     */
    synchronized void regulateMotor(long delta)
    {
        float error;
        now += delta;
        long elapsed = now - baseTime;
        if (moving)
        {
            if (elapsed < accTime)
            {
                // We are still accelerating, calculate new position
                curVelocity = baseVelocity + curAcc * elapsed / (1000);
                curCnt = baseCnt + (baseVelocity + curVelocity) * elapsed / (2 * 1000);
                error = curCnt - tachoCnt;
            } else
            {
                // no longer accelerating, calculate new position
                curVelocity = curTargetVelocity;
                curCnt = baseCnt + accCnt + curVelocity * (elapsed - accTime) / 1000;
                error = curCnt - tachoCnt;
                // Check to see if the move is complete
                if (curTargetVelocity == 0 && (pending || (Math.abs(error) < 2 && elapsed > accTime + 100) || elapsed > accTime + 500))
                {
                    endMove(false);
                }
            }
            // check for stall
            if (Math.abs(error) > stallLimit)
            {
                baseTime += delta;
                if (stallCnt++ > stallTime) endMove(true);
            }
            else
            {
                stallCnt /= 2;
            }
            calcPower(error, MOVE_P, MOVE_I, MOVE_D, (float)delta/Controller.UPDATE_PERIOD);
            // If we have a move limit, check for time to start the deceleration stage
            if (checkLimit)
            {
                float acc = (curVelocity*curVelocity)/(2*(curLimit - curCnt));
                if (curStopAcc/acc < 1.0)
                    startSubMove(0, acc, NO_LIMIT, curHold);
            }
        } else if (curHold)
        {
            // not moving, hold position
            error = curCnt - tachoCnt;
            calcPower(error, HOLD_P, HOLD_I, HOLD_D, (float)delta/Controller.UPDATE_PERIOD);
        }
        else
        {
            // Allow the motor to move freely
            curCnt = tachoCnt;
            power = 0;
            mode = TachoMotorPort.FLOAT;
            active = false;
            cont.removeMotor(this);
        }
    }// end run

    /**
     * helper method for velocity regulation.
     * calculates power from error using double smoothing and PID like
     * control
     * @param error
     */
    private void calcPower(float error, float P, float I, float D, float time)
    {
        // use smoothing to reduce the noise in frequent tacho count readings
        // New values
        err1 = 0.375f * err1 + 0.625f * error;  // fast smoothing
        err2 = 0.75f * err2 + 0.25f * error; // slow smoothing
        // Original values
        //err1 = 0.5f * err1 + 0.5f * error;  // fast smoothing
        //err2 = 0.8f * err2 + 0.2f * error; // slow smoothing
        float newPower = basePower + P * err1 + D * (err1 - err2)/time;
        basePower = basePower + I * (newPower - basePower)*time;
        if (basePower > TachoMotorPort.MAX_POWER)
            basePower = TachoMotorPort.MAX_POWER;
        else if (basePower < -TachoMotorPort.MAX_POWER)
            basePower = -TachoMotorPort.MAX_POWER;
        //newPower = (float) (power*0.75 + newPower*0.25);
        power = (newPower > TachoMotorPort.MAX_POWER ? TachoMotorPort.MAX_POWER : newPower < -TachoMotorPort.MAX_POWER ? -TachoMotorPort.MAX_POWER : Math.round(newPower));

        //mode = (power == 0 ? TachoMotorPort.STOP : TachoMotorPort.FORWARD);
        mode = TachoMotorPort.FORWARD;
    }


    /**
     * This class provides a single thread that drives all of the motor regulation
     * process. Only active motors will be regulated. To try and keep motors
     * as closely synchronized as possible tach counts for all motors are gathered
     * as close as possible to the same time. Similarly new power levels for each
     * motor are also set at the same time.
     */
    protected static class Controller extends Thread
    {
        static final int UPDATE_PERIOD = 4;
        JavaMotorRegulator [] activeMotors = new JavaMotorRegulator[0];
        boolean running = false;
    
        /**
         * Add a motor to the set of active motors.
         * @param m
         */
        synchronized void addMotor(JavaMotorRegulator m)
        {
            JavaMotorRegulator [] newMotors = new JavaMotorRegulator[activeMotors.length+1];
            System.arraycopy(activeMotors, 0, newMotors, 0, activeMotors.length);
            newMotors[activeMotors.length] = m;
            m.reset();
            activeMotors = newMotors;
        }
    
        /**
         * Remove a motor from the set of active motors.
         * @param m
         */
        synchronized void removeMotor(JavaMotorRegulator m)
        {
            m.tachoPort.controlMotor(0, TachoMotorPort.FLOAT);
            JavaMotorRegulator [] newMotors = new JavaMotorRegulator[activeMotors.length-1];
            int j = 0;
            for(int i = 0; i < activeMotors.length; i++)
                if (activeMotors[i] != m)
                    newMotors[j++] = activeMotors[i];
            activeMotors = newMotors;
        }
        
        synchronized void shutdown()
        {
            // Shutdown all of the motors and prevent them from running
            running = false;
            for(JavaMotorRegulator m : activeMotors)
                m.tachoPort.controlMotor(0, TachoMotorPort.FLOAT);
            activeMotors = new JavaMotorRegulator[0];
        }
    
    
        @Override
        public void run()
        {
            running = true;
            long now = System.currentTimeMillis();
            while(running)
            {
                long delta;
                synchronized (this)
                {
                    delta = System.currentTimeMillis() - now;
                    JavaMotorRegulator [] motors = activeMotors;
                    now += delta;
                    for(JavaMotorRegulator m : motors)
                        m.tachoCnt = m.tachoPort.getTachoCount() - m.zeroTachoCnt;
                    for(JavaMotorRegulator m : motors)
                        m.regulateMotor(delta);
                    for(JavaMotorRegulator m : motors)
                        m.tachoPort.controlMotor(m.power, m.mode);
                }
                Delay.msDelay(now + UPDATE_PERIOD - System.currentTimeMillis());
            }   // end keep going loop
        }
    }
}