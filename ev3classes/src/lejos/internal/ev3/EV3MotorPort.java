package lejos.internal.ev3;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.internal.io.NativeDevice;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.utility.Delay;

/**
 * Abstraction for an EV3 output port.
 * 
 * TODO: Sort out a better way to do this, or least clean up the magic numbers.
 *
 */
public class EV3MotorPort extends EV3IOPort implements TachoMotorPort {
    static final byte OUTPUT_SET_TYPE = (byte)0xa1;
    static final byte OUTPUT_POWER = (byte)0xa4;
    static final byte OUTPUT_START = (byte)0xa6;
    static final byte OUTPUT_STOP = (byte)0xa3;
    static final byte OUTPUT_CLR_COUNT = (byte)0xb2;
        
    protected static NativeDevice tacho;
    protected static ByteBuffer bbuf;
    protected static IntBuffer ibuf;
    protected static NativeDevice pwm;
    static
    {
        initDeviceIO();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {resetAll();}
        });
    }
    protected int curMode = FLOAT+1; // current mode is unknown
    protected byte[] cmd = new byte[3];
    protected MotorRegulator regulator;

    /**
     * Implementation of a PID based motor regulator that uses a kernel module
     * for the core regulation operations. This mechanism is accessed via the
     * EV3MotorPort class.
     **/
    public class EV3MotorRegulatorKernelModule extends Thread implements MotorRegulator
    {
        static final int NO_LIMIT = 0x7fffffff;
        // Regulator move states
        static final int ST_IDLE = 0;
        static final int ST_STALL = 1;
        static final int ST_HOLD = 2;
        static final int ST_START = 3;
        static final int ST_ACCEL = 4;
        static final int ST_MOVE = 5;
        static final int ST_DECEL = 6;

        protected int zeroTachoCnt;
        protected int limitAngle;
        protected float curPosition;
        protected float curVelocity;
        protected float curCnt;
        protected int curTime;
        protected int curState;
        protected int curSerial;
        protected int curLimit;
        protected float curSpeed;
        protected float curAcc;
        protected boolean curHold;
        protected int stallLimit=50;
        protected int stallTime=1000;
        
        protected byte[] regCmd = new byte[55];


        // state for listener stuff
        boolean started = false;
        RegulatedMotorListener listener;
        RegulatedMotor motor;

        public EV3MotorRegulatorKernelModule(TachoMotorPort p)
        {
            if (p != EV3MotorPort.this)
                throw new IllegalArgumentException("Invlaid port specified");
            // don't wait for the listener thread to finish
            this.setDaemon(true);
        }
        
        // Fixed point routines and constants
        static final int FIX_SCALE = 256;
        
        protected int floatToFix(float f)
        {
            return Math.round(f*FIX_SCALE);
        }
        
        protected int intToFix(int i)
        {
            return i*FIX_SCALE;
        }
        
        protected float FixToFloat(int fix)
        {
            return (float)fix/FIX_SCALE;
        }
        
        protected int FixMult(int a, int b)
        {
            return (a*b)/FIX_SCALE;
        }
        
        protected int FixDiv(int a, int b)
        {
            return (a*FIX_SCALE)/b;
        }
        
        protected int FixRound(int a)
        {
            return (a >= 0 ? (a+FIX_SCALE/2)/FIX_SCALE : (a-FIX_SCALE/2)/FIX_SCALE);
        }

       

        /**
         * pack a value ready to be written to the kernel module
         * @param buf
         * @param offset
         * @param val
         */
        protected void setVal(byte[] buf, int offset, int val)
        {
            buf[offset] = (byte)val;
            buf[offset+1] = (byte)(val >> 8);
            buf[offset+2] = (byte)(val >> 16);
            buf[offset+3] = (byte)(val >> 24);
        }

        /**
         * Set the PID control parameters in the kernel module
         * @param typ
         * @param moveP
         * @param moveI
         * @param moveD
         * @param holdP
         * @param holdI
         * @param holdD
         * @param offset
         * @param deadBand
         */
        public synchronized void setControlParams(int typ, float moveP, float moveI, float moveD, float holdP, float holdI, float holdD, int offset, float deadBand)
        {
            regCmd[0] = OUTPUT_SET_TYPE;
            regCmd[1] = (byte)port;
            regCmd[2] = (byte)typ;
            setVal(regCmd, 3, floatToFix(moveP));
            setVal(regCmd, 7, floatToFix(moveI));
            setVal(regCmd, 11, floatToFix(moveD));
            setVal(regCmd, 15, floatToFix(holdP));
            setVal(regCmd, 19, floatToFix(holdI));
            setVal(regCmd, 23, floatToFix(holdD));
            setVal(regCmd, 27, offset);
            setVal(regCmd, 31, floatToFix(deadBand));
            pwm.write(regCmd, 35);
            
        }


        /**
         * Check to see if the current command is complete and if needed call
         * any listeners.
         */
        protected synchronized void checkComplete()
        {
            if (started && !isMoving())
            {
                started = false;
                if (listener != null)
                    listener.rotationStopped(motor, getTachoCount(), isStalled(), System.currentTimeMillis());
            }
        }

        /**
         * We are starting a new move operation. Handle listeners as required
         */
        protected synchronized void startNewMove()
        {
            if (started)
                checkComplete();
            if (started)
                throw new IllegalStateException("Motor must be stopped");
            started = true;
            if (listener != null)
            {
                listener.rotationStarted(motor, getTachoCount(), false, System.currentTimeMillis());
                notifyAll();
            }
                
        }

        /**
         * Thread to handle listeners.
         */
        public synchronized void run()
        {
            while (true)
            {
                // wait until a move is actually started
                while (!started)
                    try {
                        wait();
                    } catch (InterruptedException e){}
                checkComplete();
                try {
                    wait(5);
                } catch (InterruptedException e){}
            }
        }
        
        /**
         * Start a move using the PID loop in the kernel module
         * @param t1 Time for acceleration phase
         * @param t2 Time for cruise phase
         * @param t3 Time for deceleration phase
         * @param c2 Position (cnt) after acceleration phase
         * @param c3 Position (cnt) after cruise stage
         * @param v1 Velocity at start of acceleration stage
         * @param v2 Velocity after acceleration stage
         * @param a1 Acceleration
         * @param a3 Deceleration
         * @param sl stall limit
         * @param st stall time
         * @param ts Time stamp
         * @param hold What to do after the move
         */
        protected void subMove(int t1, int t2, int t3, float c1, float c2, float c3, float v1, float v2, float a1, float a3, int sl, int st, int ts, boolean hold)
        {
            //System.out.println("t1 " + t1 + " t2 " + t2 + " t3 " + t3 + " c1 " + c1 + " c2 " + c2 + " c3 " + c3 + " v1 " + v1 + " v2 " + v2 + " a1 " + a1 + " a3 " + a3);
            // convert units from /s (i.e 100ms) to be per 1024ms to allow div to be performed by shift
            v1 = (v1/1000f)*1024f;
            v2 = (v2/1000f)*1024f;
            a1 = (((a1/1000f)*1024f)/1000f)*1024f;
            a3 = (((a3/1000f)*1024f)/1000f)*1024f;
            // now start the actual move
            regCmd[0] = OUTPUT_START;
            regCmd[1] = (byte)port;
            setVal(regCmd, 2, t1);
            setVal(regCmd, 6, t2);
            setVal(regCmd, 10, t3);
            setVal(regCmd, 14, floatToFix(c1));
            setVal(regCmd, 18, floatToFix(c2));
            setVal(regCmd, 22, floatToFix(c3));
            setVal(regCmd, 26, floatToFix(v1));
            setVal(regCmd, 30, floatToFix(v2));
            setVal(regCmd, 34, floatToFix(a1));
            setVal(regCmd, 38, floatToFix(a3));
            setVal(regCmd, 42, sl);
            setVal(regCmd, 46, st);
            setVal(regCmd, 50, ts);
            regCmd[54] = (byte) (hold ? 1 : 0);
            // if we are going to move then tell any listeners.
            if ((v1 != 0 || v2 != 0) && ts == 0)
                startNewMove();

            // Get ready to start the move.
            // TODO: Understand what is going on with shared memory. There is something
            // very odd here. We always set the regulator state in the kernel module
            // however for some reason the shared memory as we see it does not always
            // reflect the change straight away. The following code waits for the change
            // to show up!
            int ser = curSerial;
            pwm.write(regCmd, 55);
            while (getSerialNo() == ser)
                Thread.yield();
        }
        
        /**
         * Helper method generate a move by splitting it into three phases, initial
         * acceleration, constant velocity, and final deceleration. We allow for the case
         * were it is not possible to reach the required constant velocity and hence the
         * move becomes triangular rather than trapezoid.   
         * @param curVel Initial velocity
         * @param curPos Initial position
         * @param speed
         * @param acc
         * @param limit
         * @param hold
         */
        protected void genMove(float curVel, float curPos, float curCnt, int curTime, float speed, float acc, int limit, boolean hold)
        {
            // Save current move params we may need these to adjust speed etc.
            curSpeed = speed;
            curHold = hold;
            curAcc = acc;
            curLimit = limit;
            float u2 = curVel*curVel;
            int len = (int)(limit - curPos);
            float v = speed;
            float a1 = acc;
            float a3 = acc;
            //System.out.println("pos " + curPos + " curVel " + curVel + " limit " + limit + " len " + len + " speed " + speed + " hold " + hold);
            if (speed == 0.0)
            {
                // Stop case
                //System.out.println("Stop");
                if (curVel < 0)
                    a3 = -acc;
                int t3 = (int)(1000*(curVel/a3));
                subMove(0, 0, t3, 0, 0, curCnt, 0, curVel, 0, -a3, stallLimit, stallTime, curTime, hold);
                return;
            }
            float v2 = v*v;
            if (Math.abs(limit) == NO_LIMIT)
            {
                // Run forever, no need for deceleration at end
                //System.out.println("Unlimited move");
                if (limit < 0)
                    v = -speed;
                if (v < curVel)
                    a1 = -acc;
                float s1 = (v2 - u2)/(2*a1);
                int t1 = (int)(1000*(v - curVel)/a1);
                subMove(t1, NO_LIMIT, 0, curCnt, curCnt + s1, 0, curVel, v, a1, 0, stallLimit, stallTime, curTime, hold);
                return;
            }
            // We have some sort of target position work out how to get to it
            if (curVel != 0)
            {
                // we need to work out if we can get to the end point in a single move
                if (curVel < 0)
                    a3 = -acc;
                float s3 = (u2)/(2*a3);
                //System.out.println("stop pos " + s3);
                // if final position is less than stop pos we need to reverse direction
                if (len < s3)
                    v = -speed;
                a3 = acc;
            }
            else
                if (len < 0)
                    v = -speed;
            if (v < curVel)
                a1 = -acc;
            if (v < 0)
                a3 = -acc;
            float vmax2 = a3*len + u2/2;
            // can we ever reach target velocity?
            if (vmax2 <= v2)
            {
                // triangular move
                //System.out.println("Triangle");
                if (vmax2 < 0) System.out.println("vmax -ve" + vmax2);
                if (v < 0)
                    v = -(float) Math.sqrt(vmax2);
                else
                    v = (float) Math.sqrt(vmax2);
                float s1 = (vmax2 - u2)/(2*a1);
                int t1 = (int)(1000*(v - curVel)/a1);
                int t2 = t1;
                int t3 = t2 + (int)(1000*(v/a3));
                subMove(t1, t2, t3, curCnt, 0, s1+curCnt, curVel, v, a1, -a3, stallLimit, stallTime, curTime, hold);         
            }
            else
            {
                // trapezoid move
                //System.out.println("Trap");
                float s1 = (v2 - u2)/(2*a1);
                float s3 = (v2)/(2*a3);
                float s2 = len - s1 - s3;
                //System.out.println("s1 " + s1 + " s2 " + s2 + " s3 " + s3);
                int t1 = (int)(1000*(v - curVel)/a1);
                int t2 = t1 + (int)(1000*s2/v);
                int t3 = t2 + (int)(1000*(v/a3));
                //System.out.println("v " + v + " a1 " + a1 + " a3 " + (-a3));
                subMove(t1, t2, t3, curCnt, curCnt+s1, curCnt+s1+s2, curVel, v, a1, -a3, stallLimit, stallTime, curTime, hold);

            }
        }

        /**
         * Waits for the current move operation to complete
         */
        public void waitComplete()
        {
            /*
            int pos = this.getTachoCount();
            while(isMoving())
            {
                Delay.msDelay(1);
                int newPos = this.getTachoCount();
                if (newPos != pos)
                {
                    updateVelocityAndPosition();
                    System.out.println("P: " + newPos + " T " + curPosition + " V " + curVelocity);
                    pos = newPos;
                }
            }*/
            while(isMoving())
                Delay.msDelay(1);
            checkComplete();                
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
        public void newMove(float speed, int acceleration, int limit, boolean hold, boolean waitComplete)
        {
            synchronized(this)
            {
                limitAngle = limit;
                if (Math.abs(limit) != NO_LIMIT)
                    limit += zeroTachoCnt;
                // Ignore repeated commands
                if (!waitComplete && (speed == curSpeed) && (curAcc == acceleration) && (curLimit == limit) && (curHold == hold))
                    return;
                
                updateRegulatorInformation();
                if (curState >= ST_START)
                    // moving already, blend moves
                    genMove(curVelocity, curPosition, curCnt, curTime, speed, acceleration, limit, hold);
                else
                    // not moving, start a new move
                    genMove(curVelocity, curPosition, curCnt, 0, speed, acceleration, limit, hold);
            }
            if (waitComplete)
                waitComplete();
        }

        /**
         * The kernel module updates the shared memory serial number every time
         * a new command is issued. We can use this to wait for the shared mem
         * to be updated. We must do this to ensure that we do not see a move
         * as complete when in fact it may not have even started yet!
         * @return
         */
        protected int getSerialNo()
        {
            synchronized(ibuf)
            {
                return ibuf.get(port*8 + 7);
            }
        }
        /**
         * Grabs the current state of the regulator and stores in class
         * member variables
         */
        protected void updateRegulatorInformation()
        {
            int baseCnt;
            int cnt;
            int vel;
            int time;
            int time2;
            int state;
            int serial;
            //int loopCnt = 0;
            // Check to make sure time is not changed during read
            do {
                // TODO: sort out how to handle JIT issues and shared memory.
                // The problem is that when the JIT compiler gets to work 
                // it ends up seeing the shared memory as a simple array.
                // It is not possible to label this array as volatile so
                // some of the following code is seen as invariant and so can be
                // optimised. Adding the synchronized section seems to help
                // with this but it is not ideal. Need a better solution if possible
                synchronized(ibuf)
                {
                    time = ibuf.get(port*8 + 5);
                    baseCnt = ibuf.get(port*8);
                    cnt = ibuf.get(port*8+1);
                    vel = ibuf.get(port*8+2);
                    state = ibuf.get(port*8 + 4);
                    serial = ibuf.get(port*8 + 7);
                    //loopCnt++;
                    time2 = ibuf.get(port*8 + 6);
                }
            } while (time != time2);
            //if (loopCnt > 1) System.out.println("loop cnt " + loopCnt + " time " + time);
            curCnt = FixToFloat(cnt);
            curPosition = curCnt + baseCnt;
            curVelocity = (FixToFloat(vel)/1024)*1000;
            curTime = time;
            curState = state;
            curSerial = serial;
        }
        
        /**
         * returns the current position from the regulator
         * @return current position in degrees
         */
        public synchronized float getPosition()
        {
            updateRegulatorInformation();
            return curPosition - zeroTachoCnt;
        }

        /**
         * returns the current velocity from the regulator
         * @return velocity in degrees per second
         */
        public synchronized float getCurrentVelocity()
        {
            updateRegulatorInformation();
            return curVelocity;
        }


        /**
         * return the regulator state.
         * @return
         */
        protected int getRegState()
        {
            synchronized(ibuf)
            {
                curState = ibuf.get(port*8 + 4);
                return curState;
            }
        }
        
        public boolean isMoving()
        {
            return getRegState() >= ST_START;
        }
        
        public boolean isStalled()
        {
            return getRegState() == ST_STALL;
        }
                        
        public int getTachoCount()
        {
            return EV3MotorPort.this.getTachoCount() - zeroTachoCnt;
        }
        
        public void resetTachoCount()
        {
            zeroTachoCnt = EV3MotorPort.this.getTachoCount();
        }

        
        public void setStallThreshold(int error, int time)
        {
            this.stallLimit = error;
            this.stallTime = time;
        }


        /**
         * The target speed has been changed. Reflect this change in the
         * regulator.
         * @param newSpeed new target speed.
         */
        public synchronized void adjustSpeed(float newSpeed)
        {
            if (curSpeed != 0 && newSpeed != curSpeed)
            {
                updateRegulatorInformation();
                if (curState >= ST_START && curState <= ST_MOVE)
                    genMove(curVelocity, curPosition, curCnt, curTime, newSpeed, curAcc, curLimit, curHold);
            }
        }

        /**
         * The target acceleration has been changed. Updated the regulator.
         * @param newAcc
         */
        public synchronized void adjustAcceleration(int newAcc)
        {
            if (newAcc != curAcc)
            {
                updateRegulatorInformation();
                if (curState >= ST_START && curState <= ST_MOVE)
                    genMove(curVelocity, curPosition, curCnt, curTime, curSpeed, newAcc, curLimit, curHold);
            }
        }


        @Override
        public void setControlParamaters(int typ, float moveP, float moveI,
                float moveD, float holdP, float holdI, float holdD, int offset)
        {
            setControlParams(typ, moveP, moveI, moveD, holdP, holdI, holdD, offset, 0.5f);
        }



        @Override
        public void addListener(RegulatedMotor motor, RegulatedMotorListener listener)
        {
            this.motor = motor;
            this.listener = listener;
            if (getState() == Thread.State.NEW)
                start();
        }


        @Override
        public RegulatedMotorListener removeListener()
        {
            RegulatedMotorListener old = listener;
            listener = null;
            return old;
        }


        @Override
        public int getLimitAngle()
        {
            return limitAngle;
        }
    }    

    /**
     * Helper method to adjust the requested power
     * @param power
     */
    protected void setPower(int power)
    {
        cmd[0] = OUTPUT_POWER;
        cmd[1] = (byte) port;
        cmd[2] = (byte) power;
        pwm.write(cmd, 3);
    }

    /**
     * Helper method stop the motor
     * @param flt
     */
    protected void stop(boolean flt)
    {
        cmd[0] = OUTPUT_STOP;
        cmd[1] = (byte) port;
        cmd[2] = (byte) (flt ? 0 : 1);
        pwm.write(cmd, 3);

    }
    
    
    /**
     * Low-level method to control a motor. 
     * 
     * @param power power from 0-100
     * @param mode defined in <code>BasicMotorPort</code>. 1=forward, 2=backward, 3=stop, 4=float.
     * @see BasicMotorPort#FORWARD
     * @see BasicMotorPort#BACKWARD
     * @see BasicMotorPort#FLOAT
     * @see BasicMotorPort#STOP
     */
    public synchronized void controlMotor(int power, int mode)
    {
        // Convert lejos power and mode to EV3 power and mode
        if (mode >= STOP)
        {
            power = 0;
            stop(mode == FLOAT);
        }
        else
        {
            if (mode == BACKWARD)
                power = -power;
            setPower(power);
        }
        curMode = mode;
    }


    /**
     * returns tachometer count
     */
    public  int getTachoCount()
    {
        synchronized(ibuf)
        {
            return ibuf.get(port*8 + 3);
        }
    }
    
    
    /**
     *resets the tachometer count to 0;
     */ 
    public synchronized void resetTachoCount()
    {
        cmd[0] = OUTPUT_CLR_COUNT;
        cmd[1] = (byte)port;
        pwm.write(cmd,  2);
    }
    
    public void setPWMMode(int mode)
    {
    }

    /**
     * reset all motor ports by setting the type to be none.
     */
    private static void resetAll()
    {
        byte[] cmd = new byte[35];
        for(int i = 0; i < EV3IOPort.MOTORS; i++)
        {
            cmd[0] = OUTPUT_SET_TYPE;
            cmd[1] = (byte)i;
            cmd[2] = (byte)EV3IOPort.TYPE_NONE;
            pwm.write(cmd, 35);
        }            
    }
    
    
    private static void initDeviceIO()
    {
        tacho = new NativeDevice("/dev/lms_motor");
        bbuf = tacho.mmap(4*8*4).getByteBuffer(0, 4*8*4);
        //System.out.println("direct " + bbuf.isDirect());
        ibuf = bbuf.asIntBuffer();
        pwm = new NativeDevice("/dev/lms_pwm");
        resetAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized MotorRegulator getRegulator()
    {
        if (regulator == null)
            regulator = new EV3MotorRegulatorKernelModule(this);
            //regulator = new JavaMotorRegulator(this);
        return regulator;
    }
}
