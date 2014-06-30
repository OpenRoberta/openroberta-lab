package lejos.hardware.device;

import lejos.hardware.motor.NXTMotor;
import lejos.hardware.port.Port;
import lejos.robotics.EncoderMotor;
import lejos.robotics.LinearActuator;

/** A Linear Actuator class that provides blocking and non-blocking move actions with stall detection. Developed for the 
 * Firgelli L12-NXT-50 and L12-NXT-100
 * but may work for others. These linear actuators are self contained units which include an electric motor and encoder. They will push 
 * up to 25N and move at up to 12 mm/sec unloaded. 
 * <p>
 * This class in not thread-safe and it is up to the caller to properly handle synchronization when calling its methods in a 
 * multithreaded software implementation.
 * <p>
 * This is not an endorsement: For informational purposes, see <a href="http://www.firgelli.com">www.firgelli.com.</a> for details on the actuators.
 * @author Kirk P. Thompson
 * 
 */
public class LnrActrFirgelliNXT implements LinearActuator{
//    private static final int MIN_POWER = 30;
    
    private EncoderMotor encoderMotor;
    private volatile int motorPower =0;
    private volatile int tick_wait; // this is calculated in setPower() to fit the power setting. Variable because lower powers move it slower.
    private volatile boolean isMoveCommand = false;
    private volatile boolean isStalled=false;
    private Thread actuator;
    private volatile boolean dirExtend = true;
    private volatile int distanceTicks;
    private volatile boolean killCurrentAction=false;
    private Object synchObj1 = new Object();
    private volatile int tachoCount=0;
    
    /**Create a <code>LnrActrFirgelliNXT</code> instance.
     * Use this constructor to assign an instance of <code>EncoderMotor</code> used to drive the actuater motor. This constructor
     * allows any motor class that implements the <code>EncoderMotor</code> interface to drive the actuator. You must instantiate
     * the <code>EncoderMotor</code>-type motor before passing it to this constructor.
     * <p>
     * When an instance of the MMXMotor class is used, the motor power curve is much different that with a NXTMotor (due to 
     * a different PWM output?) . The speed
     * peaks out at about 20% power so in effect, the speed control granularity is coarser.
     * <p>
     * The default power at instantiation is 100%.
     * @param encoderMotor A motor instance of type <code>EncoderMotor</code> which will drive the actuator
     * @see lejos.hardware.motor.NXTMotor
     * @see MMXMotor
     * @see EncoderMotor
     */
    public LnrActrFirgelliNXT(EncoderMotor encoderMotor) {
        this.encoderMotor=encoderMotor;
        this.encoderMotor.flt();
        
        setPower(100); 
        this.actuator = new Thread(new Actuator());
        this.actuator.setDaemon(true);
        this.actuator.start();
        doWait(100); 
    }

    /** Convenience constructor that creates an instance of a <code>NXTMotor</code> using the specified motor port. This instance is then
     * used to drive the actuator motor.
     * <p>
     * The default power at instantiation is 100%.
     * @param port The motor port that the linear actuator is attached to.
     * @see lejos.hardware.port.MotorPort
     * @see NXTMotor
     */
    public LnrActrFirgelliNXT(Port port) {
        this(new NXTMotor(port));
    }
    
    /**Sets the power for the actuator. This is called before the <code>move()</code> or <code>moveTo()</code> method is called 
     * to set the power.
     * Using lower power values and pushing/pulling
     * an excessive load may cause a stall and in this case, stall detection will stop the current actuator action and 
     * set the stalled condition flag.
     * <p>
     * The default power value on instantiation is 100%.
     * @param power power setting: 0-100%
     * @see LinearActuator#move(int,boolean)
     * @see #isStalled
     */
    public void setPower(int power){
        power=Math.abs(power);
        this.motorPower = (power>100)?100:power;
        this.encoderMotor.setPower(this.motorPower);
        
        // calc encoder tick/ms based on my testing. y=mm/sec, x=power + 10%
        if (this.encoderMotor instanceof MMXMotor) {
            this.tick_wait = (int)(500/(0.4396f * this.motorPower + 3.8962f));
        } else {
            this.tick_wait = (int)(500/(0.116f * this.motorPower - 0.5605f)) ;
        }
        
        // ~12 mm/s = ~40 ms/encoder tick
        if (this.tick_wait<40) this.tick_wait = 40;
        // ~2.5 mm/s = ~200 ms/encoder tick
        if (this.tick_wait>200) this.tick_wait = 200;
        // add 10% for unit manufacturing tolerance variance
        this.tick_wait = (int)(this.tick_wait * 1.1f);
    }
          
    /**
    * Returns the current actuator motor power setting. 
    * @return current power 0-100%
    */
    public int getPower() {
        return this.motorPower;
    }
 
    /**Returns true if the actuator is in motion.
     * @return true if the actuator is in motion.
     */
    public boolean isMoving() {
        return this.isMoveCommand; 
    }

    /**Returns true if a <code>move()</code> or <code>moveTo()</code> order ended due to a motor stall. This behaves 
     * like a latch where the 
     * reset of the stall status is done on a new <code>move()</code> or <code>moveTo()</code> order. 
     * @return <code>true</code> if actuator motor stalled during a movement order. <code>false</code> otherwise.
     * @see LinearActuator#move(int,boolean)
     */
    public boolean isStalled() {
        return this.isStalled; 
    }
    
    
    /**Causes the actuator to move <code>distance</code> in encoder ticks. The <code>distance</code> is relative to the actuator 
     * shaft position at the time of calling this method. 
     * Positive values extend the actuator shaft while negative values retract it. 
     * The Firgelli L12-NXT-50 & 100 use 0.5 mm/encoder tick. eg: 200 ticks=100 mm. 
     * <p>
     * Stall detection stops the actuator in the event of a stall condition to help prevent damage to the actuator.
     * <P>
     * If <code>immediateReturn</code> is <code>true</code>, this method returns immediately (does not block) and the actuator stops when the
     * stroke <code>distance</code> is met [or a stall is detected]. If another <code>move</code> action is called before the 
     * stroke
     * distance is reached, the current actuator action is cancelled and the new action is initiated.
     * <p>
     * If the stroke <code>distance</code> specified exceeds the maximum 
     * stroke length (fully extended or retracted against an end stop), stall detection will stop the action. It is advisable 
     * not to extend or retract to the 
     * stop as this is hard on the actuator. If you must go all the way to an end stop and rely on stall detection to stop the
     * action, use a lower power setting.
     * 
     * @param distance The Stroke distance in encoder ticks. 
     * @param immediateReturn Set to <code>true</code> to cause the method to immediately return while the action is executed in
     * the background. 
     * <code>false</code> will block until the action is completed, whether successfully or stalled.
     * @see #setPower
     * @see #stop
     * @see #getTachoCount
     * @see #moveTo(int,boolean)
     */
    public synchronized void move(int distance, boolean immediateReturn ){
        // set globals
        this.dirExtend=distance>=0;
        this.distanceTicks = Math.abs(distance);
         // initiate the action
        doAction(immediateReturn);
    }

    /**Causes the actuator to move to absolute <code>position</code> in encoder ticks. The <code>position</code> of the actuator
     * shaft on startup or when set by <code>resetTachoCount()</code> is zero.
     * @param position The absolute shaft position in encoder ticks.
     * @param immediateReturn Set to <code>true</code> to cause the method to immediately return while the action is executed in
     * the background. 
     * @see #move(int,boolean)
     * @see #resetTachoCount
     */
    public void moveTo(int position, boolean immediateReturn ){
        int distance = position - this.tachoCount;
        move(distance, immediateReturn);
    }
    
    // only called by move()
    private void doAction(boolean immediateReturn){
        // If we already have an active command, signal it to cease and wait until cleared
        if (this.isMoveCommand) {
            this.killCurrentAction=true;
            synchronized(this.synchObj1){
                while (this.isMoveCommand) {
                    try {
                        this.synchObj1.wait();
                    } catch (InterruptedException e) {
                        ; //ignore
                    }
                }
            }
        }
        // initiate the action by waking up the actuator thread to do the action
        this.killCurrentAction=false; // ensure state baseline
        synchronized (this.actuator) {
            // set state to indicate an action is in effect
            this.isMoveCommand=true;
            this.isStalled=false;
            this.actuator.notify();
        }
        
        // if told to block, wait until the actuator thread completes its current task. When done, it will do a notify() to wake 
        // us up here.
        if (!immediateReturn) {
            synchronized(this.synchObj1){
                while (!this.killCurrentAction) {
                    try {
                        this.synchObj1.wait();
                    } catch (InterruptedException e) {
                        ; // ignore
                    }
                }
            }
        }
    }
    
    /**This thread does the actuator control
     */
    private class Actuator implements Runnable{
        private static final int STALL_COUNT = 3; 
        
        public void run() {
            while(true) {
                // wait until triggered to do an actuation
                synchronized (LnrActrFirgelliNXT.this.actuator) {
                    while (true){
                        try {
                            LnrActrFirgelliNXT.this.actuator.wait();
                            if (LnrActrFirgelliNXT.this.isMoveCommand) break;
                        } catch (InterruptedException e) {
                            ; // do nothing and continue
                        }
                    }
                }
                
                // this blocks. When finished, toExtent() will reset this.isMoveCommand, etc. w/ call to stop()
                toExtent(); 
                
                // wake up any wait in doAction()
                synchronized(LnrActrFirgelliNXT.this.synchObj1){
                    LnrActrFirgelliNXT.this.synchObj1.notify();
                }
            }
        }
        
        // starts the motor and waits until move is completed or interrupted with the this.killCurrentAction
        // flag which in effect, causes the thread wait/block until next command is issued (this.isMoveCommand set to true)
        private void toExtent() {
            int power = LnrActrFirgelliNXT.this.motorPower;
            int tacho=0;
            int temptacho;
            
            LnrActrFirgelliNXT.this.encoderMotor.resetTachoCount();
            
            // initiate the actuator action
            if (LnrActrFirgelliNXT.this.dirExtend) {
                LnrActrFirgelliNXT.this.encoderMotor.forward(); 
            } else {
                LnrActrFirgelliNXT.this.encoderMotor.backward(); 
            }
            
            // wait until the actuator shaft starts moving (with a time limit)
            int begTacho=LnrActrFirgelliNXT.this.encoderMotor.getTachoCount();
            long begTime = System.currentTimeMillis();
            while (!LnrActrFirgelliNXT.this.killCurrentAction&&(begTacho==LnrActrFirgelliNXT.this.encoderMotor.getTachoCount())) {
                doWait(LnrActrFirgelliNXT.this.tick_wait/2);
                // kill the move and exit if it takes too long to start moving  
                if ((System.currentTimeMillis()-begTime)>LnrActrFirgelliNXT.this.tick_wait*6) {
                    LnrActrFirgelliNXT.this.isStalled=true;
                    LnrActrFirgelliNXT.this.killCurrentAction=true; // will cause loop below to immediately finish
                    break;
                }
            }
            
            // monitor the move and stop when stalled or action completes
            begTime = System.currentTimeMillis();
            temptacho=LnrActrFirgelliNXT.this.tachoCount;
            while (!LnrActrFirgelliNXT.this.killCurrentAction) {
                // Stall check. if no tacho change...
                if (begTacho==LnrActrFirgelliNXT.this.encoderMotor.getTachoCount()) {
                    // ...and we exceed STALL_COUNT wait periods and have been commanded to move, it probably means we have stalled
                    if (System.currentTimeMillis()- begTime>LnrActrFirgelliNXT.this.tick_wait*STALL_COUNT) {
                        LnrActrFirgelliNXT.this.isStalled=true;
                        break;
                    }
                } else {
                    // The tacho is moving, get the current point and time for next comparision
                    begTacho=LnrActrFirgelliNXT.this.encoderMotor.getTachoCount();
                    begTime = System.currentTimeMillis();
                }
                // calc abs tacho
                tacho = LnrActrFirgelliNXT.this.encoderMotor.getTachoCount();
                LnrActrFirgelliNXT.this.tachoCount = temptacho - tacho;
                tacho=Math.abs(tacho);
                
                 // reduce speed when near destination when at higher speeds
                if (LnrActrFirgelliNXT.this.distanceTicks-tacho<=4&&power>80) LnrActrFirgelliNXT.this.encoderMotor.setPower(70);
                // exit loop if destination is reached
                if (tacho>=LnrActrFirgelliNXT.this.distanceTicks) break;
                // if power changed during this run.... (only when immediateReturn=true)
                if (power!=LnrActrFirgelliNXT.this.motorPower) {
                    power = LnrActrFirgelliNXT.this.motorPower;
                    LnrActrFirgelliNXT.this.encoderMotor.setPower(power);
                }
                
                if (LnrActrFirgelliNXT.this.killCurrentAction) break;
                doWait(LnrActrFirgelliNXT.this.tick_wait/2);
            }
            
            // stop the motor
            LnrActrFirgelliNXT.this.encoderMotor.stop(); 
            stop(); // potentially redundant state-setting when user calls stop()
            LnrActrFirgelliNXT.this.tachoCount=temptacho-LnrActrFirgelliNXT.this.encoderMotor.getTachoCount();
            
            // set the power back (if changed)
            if (LnrActrFirgelliNXT.this.distanceTicks-tacho<=4&&power>80) 
                LnrActrFirgelliNXT.this.encoderMotor.setPower(LnrActrFirgelliNXT.this.motorPower);
        }
    }
    
    private static void doWait(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            ; // do nothing
        }
    }

    /**Immediately stop any current actuator action.
     * @see LinearActuator#move(int,boolean)
     */
    public void stop() {
        this.killCurrentAction = true;
        this.isMoveCommand = false; 
        
    }

    /** Returns the absolute tachometer (encoder) position of the actuator shaft. The zero position of the actuator shaft is where 
     * <code>resetTachoCount()</code> was last called or the position of the shaft when instantiated. 
     * <p>
     * The Firgelli L12-NXT-50 & 100 use 0.5 mm/encoder tick. eg: 200 ticks=100 mm. 
     * 
     * @return tachometer count in encoder ticks.
     * @see #resetTachoCount
     */
    public int getTachoCount() {
       return this.tachoCount;
    }
    
    /**Resets the tachometer (encoder) count to zero at the current actuator shaft position.
     * @see #getTachoCount
     */
    public void resetTachoCount() {
         this.tachoCount=0;
    }
    
}

