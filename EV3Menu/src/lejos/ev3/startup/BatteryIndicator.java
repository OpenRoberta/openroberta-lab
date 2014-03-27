package lejos.ev3.startup;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

/**
 * Draws a battery and the name of the NXT.
 */
public class BatteryIndicator
{
    // Battery state information
    private static final int STD_MIN = 6100;
    private static final int STD_OK = 6500;
    private static final int STD_MAX = 8000;
    private static final int RECHARGE_MIN = 7100;
    private static final int RECHARGE_OK = 7200;
    private static final int RECHARGE_MAX = 8200;
    
	private static final int HISTORY_SIZE = 2000 / Config.ANIM_DELAY;
	private static final int WINDOW_TOLERANCE = 10;
    
    private int levelMin;
    private int levelOk;
    private int levelHigh;
    private boolean isOk;
    
	private final int[] history = new int[HISTORY_SIZE];
	private int windowcenter;
	private int historyindex;
	private int historysum;
	
	private byte[] title;
	private byte[] default_title;
	private String titleString;
	
	private boolean rechargeable = false;
	
	private TextLCD lcd = LocalEV3.get().getTextLCD();
	
    public BatteryIndicator()
    {
    	if (rechargeable)
    	{
    		this.levelMin = RECHARGE_MIN;
    		this.levelOk = RECHARGE_OK;
    		this.levelHigh = RECHARGE_MAX;
    	}
    	else
    	{
    		this.levelMin = STD_MIN;
    		this.levelOk = STD_OK;
    		this.levelHigh = STD_MAX;
    	}
    	
    	int val = LocalEV3.ev3.getPower().getVoltageMilliVolt();
		windowcenter = val;
		historysum = val * HISTORY_SIZE;
		for (int i = 0; i < HISTORY_SIZE; i++)
			history[i] = val;
    }
    
    public synchronized void setDefaultTitle(String title)
    {
    	titleString = "";
    	byte[] o = this.default_title;
    	byte[] b = Utils.textToBytes(title);
    	this.default_title = b;
    	if (this.title == o)
    	{
    		this.title = b;
    	}
    }
    
    public synchronized void setTitle(String title)
    {
    	titleString = title;
   		this.title = (title == null) ? default_title : Utils.textToBytes(title);
    }
    
    private int getLevel()
    {
    	int val = LocalEV3.ev3.getPower().getVoltageMilliVolt();
    	
		historysum += val - history[historyindex];
		history[historyindex] = val;
		historyindex = (historyindex + 1) % HISTORY_SIZE;
		int average = (historysum + HISTORY_SIZE/2) / HISTORY_SIZE;
		
		int diff = average - windowcenter;
		if (diff < -WINDOW_TOLERANCE || diff > WINDOW_TOLERANCE)
			windowcenter += diff / 2;
    	
		return windowcenter;
    }

    /**
     * Display the battery icon.
     */
    public synchronized void draw(long time, byte[] buf)
    {
    	int level = getLevel();
    	
        if (level <= levelMin)
        {
        	isOk = false;
        	level = levelMin;
        }
        if (level >= levelOk)
        	isOk = true;
        if (level > levelHigh)
        	level = levelHigh;
        
        if (titleString == null) titleString = "";
        
        lcd.drawString(titleString, 8 - (titleString.length()/2), 0);

        if (isOk || (time % (2*Config.ICON_BATTERY_BLINK)) < Config.ICON_BATTERY_BLINK)
        {
            lcd.drawInt((level - level % 1000) / 1000, 0, 0);
            lcd.drawString(".", 1, 0);
            lcd.drawInt((level % 1000) / 100, 2, 0);

        }
    }
}