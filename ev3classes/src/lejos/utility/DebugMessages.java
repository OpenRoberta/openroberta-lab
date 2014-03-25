package lejos.utility;
import lejos.hardware.lcd.LCD;

/**
 * This class has been developed to use it in case of you have
 * to tests leJOS programs and you need to show in NXT Display data
 * but you don't need to design a User Interface.
 * 
 * This class is very useful to debug algorithms in your NXT brick.
 * 
 * @author Juan Antonio Brenha Moral
 *
 */
public class DebugMessages {
	private int lineCounter = 0;//Used to know in what row LCD is showing the messages
	private final int maximumLCDLines = 7;//NXT Brick has a LCD with 7 lines
	private int LCDLines = maximumLCDLines;//By default, Debug Messages show messages in maximumLCDLines
	private int delayMS = 250;//By default, the value to establish a delay
	private boolean delayEnabled = false;//By default, DebugMessages show messages without any delay
	
	/*
	 * Constructors
	 */
	
	/*
	 * Constructor with default features
	 */
	public DebugMessages(){
		//Empty
	}

	/**
	 * Constructor which the user establish in what line start showing messages
	 * 
	 * @param init
	 */
	public DebugMessages(int init){
		lineCounter = init;
	}
	
	/*
	 * Getters & Setters
	 */

	/**
	 * Set the number of lines to show in the screen.
	 * 
	 * @param lines
	 */
	public void setLCDLines(int lines){
		if(lines <= maximumLCDLines){
			LCDLines = lines;
		}
	}
	
	/**
	 * Enable/Disabled if you need to show output with delay
	 * 
	 * @param de
	 */
	public void setDelayEnabled(boolean de){
		delayEnabled = de;
	}

	/**
	 * Set the delay measured in MS.
	 * 
	 * @param dMs
	 */	
	public void setDelay(int dMs){
		delayMS = dMs;
	}

	/*
	 * Public Methods
	 */
	
	/**
	 * Show in NXT Screen a message
	 * 
	 * @param message
	 */
	public void echo(String message){
		if(lineCounter > LCDLines){
			lineCounter = 0;
			LCD.clear();			
		}else{
			LCD.drawString(message, 0, lineCounter);
			LCD.refresh();
			lineCounter++;
		}
		if(delayEnabled){
			try {Thread.sleep(delayMS);} catch (Exception e) {}
		}		
	}
	
	/**
	 * Show in NXT Screen a message
	 * 
	 * @param message
	 */	
	public void echo(int message){
		if(lineCounter > LCDLines){
			lineCounter = 0;
			LCD.clear();
		}else{
			LCD.drawInt(message, 0, lineCounter);
			LCD.refresh();			
			lineCounter++;
		}
		if(delayEnabled){
			try {Thread.sleep(delayMS);} catch (Exception e) {}
		}
	}
	

	/**
	 * Clear LCD
	 */
	public void clear(){
		LCD.clear();
		lineCounter = 0;

		if(delayEnabled){
			try {Thread.sleep(delayMS);} catch (Exception e) {}
		}
	}
}
