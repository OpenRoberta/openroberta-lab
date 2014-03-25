package lejos.hardware.device.tetrix;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

/**
 * HiTechnic Tetrix Motor and Servo Controller factory class used to provide Motor and Servo controller abstractions.
 * These are then used 
 * to obtain motor and servo instances respectively. These abstraction classes are <code>TetrixMotorController</code> and 
 * <code>TetrixServoController</code>.
 * <p>
 * Motor and servo controllers are enumerated starting at the controller connected to one of the NXT's sensor ports and then
 * working outwards along the daisy chain. 4 controllers can be daisy-chained, with a mixture of servo and/or motor controllers.
 * No other sensors can be connected to the daisy-chain.
 * 
 * <p>Code Example:<br>
 * <pre>
 * // Instantiate the factory and get a Motor and servo controller. We assume that there is one of 
 * // each daisy-chained.
 * TetrixControllerFactory cf = new TetrixControllerFactory(SensorPort.S1);
 * TetrixMotorController mc = cf.newMotorController();
 * TetrixServoController sc = cf.newServoController();
 * 
 * // Display the voltage from the motor controller
 * System.out.println("v=" + mc.getVoltage());'
 * 
 * // Get an encoder motor instance. The physical motor (with encoder) is connected to the Motor 2 terminals on the controller
 * TetrixEncoderMotor mot1 = mc.getEncoderMotor(TetrixMotorController.MOTOR_2);
 * 
 * // Start the motor
 * mot1.forward();
 * </pre>
 * 
 * @author Kirk P. Thompson
 * @see TetrixMotorController
 * @see TetrixServoController
 */
public class TetrixControllerFactory  {
    /**
     * Position 1 (nearest to the NXT) in the daisy chain. Use this if only one controller.
     */
    public static final int DAISY_CHAIN_POSITION_1 = 2;
    /**
     * Position 2 in the daisy chain. 
     */
    public static final int DAISY_CHAIN_POSITION_2 = 4;
    /**
     * Position 3 in the daisy chain. 
     */
    public static final int DAISY_CHAIN_POSITION_3 = 6;
    /**
     * Position 3 (furthest from the NXT)in the daisy chain. 
     */
    public static final int DAISY_CHAIN_POSITION_4 = 8;
    
    private static final int MAX_CHAINED_CONTROLLERS=4;
    static final String TETRIX_VENDOR_ID = "HiTechnc";
    static final String TETRIX_MOTORCON_PRODUCT_ID = "MotorCon";
    static final String TETRIX_SERVOCON_PRODUCT_ID = "ServoCon";
    
    private int currentMotorIndex=0;
    private int currentServoIndex=0;
    private Port port;
    private Finder finder;
    
    /**
     * Instantiate a <code>TetrixControllerFactory</code> using the specified NXT sensor port.
     * @param port The NXT sensor port the Tetrix controller is connected to
     */
    public TetrixControllerFactory(Port port){
        this.port=port;
        finder = new Finder(port);
    }
    
    private class Finder extends I2CSensor {
        Finder(Port port) {
            super(port);
        }
        
        /**
         * @param i where to start searching index-wise
         * @param product the product ID string
         * @return the index the product ID was found. -1 if not found or outside MAX_CHAINED_CONTROLLERS bounds
         */
        private int findProduct(int i, String product){
            if (i<0 || i>=MAX_CHAINED_CONTROLLERS) return -1;
            for (;i<MAX_CHAINED_CONTROLLERS;i++) {
                address=(i + 1) * 2;
                if (getVendorID().equalsIgnoreCase(TETRIX_VENDOR_ID) && getProductID().equalsIgnoreCase(product)) return i;
            }
            return -1;
        }
    }
    
    /**
     * Get the next available Tetrix Motor controller. Servo controllers in the daisy-chain (if any) are skipped in the search.
     * <p>
     * Successive controllers in a daisy-chain logically go "outwards" from the controller closest to the NXT as #1 to #4 for each controller
     * in the chain. Once a specific, logical controller has been retrieved using this method, it cannot be retrieved again.
     * <p>
     * A combination of Servo and Motor controllers can be daisy-chained. 
     * @return The next available <code>TetrixMotorController</code> instance.
     * @throws IllegalStateException If no more motor controllers can be returned. If there are no motor controllers
     * in the daisy-chain, this exception is also thrown.
     * @see #newServoController
     */
    public TetrixMotorController newMotorController()
    {
        this.currentMotorIndex = finder.findProduct(this.currentMotorIndex, TETRIX_MOTORCON_PRODUCT_ID);
        if (this.currentMotorIndex<0) throw new IllegalStateException("no motor controllers available");
        TetrixMotorController mc = new TetrixMotorController(finder.getPort(), (this.currentMotorIndex + 1) * 2);
        this.currentMotorIndex++;
        return mc;
    }
    
    /**
     * Get the next available Tetrix Servo controller. Motor controllers in the daisy-chain (if any) are skipped in the search.
     * <p>
     * Successive controllers in a daisy-chain logically go "outwards" from the controller closest to the NXT as #1 to #4 for each controller
     * in the chain. Once a specific, logical controller has been retrieved using this method, it cannot be retrieved again.
     * <p>
     * A combination of Servo and Motor controllers can be daisy-chained.
     * @return The next available <code>TetrixServoController</code> instance.
     * @throws IllegalStateException If no more servo controllers can be returned. If there are no servo controllers
     * in the daisy-chain, this exception is also thrown.
     * @see #newMotorController
     */
    public TetrixServoController newServoController()
    {
        this.currentServoIndex = finder.findProduct(this.currentServoIndex, TETRIX_SERVOCON_PRODUCT_ID);
        if (this.currentServoIndex<0) throw new IllegalStateException("no servo controllers available");
        TetrixServoController sc = new TetrixServoController(finder.getPort(), (this.currentServoIndex + 1) * 2);
        this.currentServoIndex++;
        return sc;
    }
}
