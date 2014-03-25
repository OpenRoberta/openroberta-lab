package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

import java.util.ArrayList;

/**
 * Abstraction for a  Lattebox NXT Extension Kit with  Lattebox 10-Axis Servo Kit
 * http://www.lattebox.com
 * UML: http://www.juanantonio.info/p_research/robotics/lejos/nxj/lattebox/LatteboxNXTeKit.png
 *
 * @author Juan Antonio Brenha Moral
 * 
 */
public class NXTe  extends I2CSensor{
	//LSC
	private ArrayList<LSC> arrLSC;
	private final int MAXIMUM_LSC = 4;
	
	//Exception handling
	private final String ERROR_SERVO_DEFINITION =  "Error with Servo Controller definition";
	private final String ERROR_SPI_CONFIGURATION = "Error in SPI Configuration";
	
	//I2C
	private I2CPort portConnected;
	private final byte SPI_PORT[] = {0x01,0x02,0x04,0x08};//SPI Ports where you connect LSC
	public static final byte NXTE_ADDRESS = 0x50;
	private final byte REGISTER_IIC = (byte)0xF0;//NXTe IIC address

	private void init(I2CPort port)
	{
        portConnected = port;
        
        arrLSC = new ArrayList<LSC>();
        

        this.sendData((int)this.REGISTER_IIC, (byte)0x0c);
	    
	}
	/**
	 * Constructor
	 * 
	 * @param port
	 */
    public NXTe(I2CPort port){
        super(port, NXTE_ADDRESS);
        init(this.port);
    }
    
    public NXTe(Port port){
        super(port, NXTE_ADDRESS, TYPE_LOWSPEED_9V);
        init(this.port);
    }
	
	/**
	 * Add a LSC, Lattebox Servo Controller
	 * 
	 * @param SPI_PORT
	 * @throws Exception
	 */
	public void addLSC(int SPI_PORT) throws ArrayIndexOutOfBoundsException{
		if(arrLSC.size() <= MAXIMUM_LSC){
			LSC LSCObj = new LSC(this.portConnected,this.SPI_PORT[SPI_PORT]);
			arrLSC.add(LSCObj);
		}else{
			//throw new ArrayIndexOutOfBoundsException(ERROR_SERVO_DEFINITION);
			throw new ArrayIndexOutOfBoundsException();
		}		
	}	
	
	/**
	 * Get a LSC, Lattebox Servo Controller
	 * 
	 * @param index in the array
	 * @return the LSC object
	 */
	public LSC getLSC(int index){
		return arrLSC.get(index);
	}
}