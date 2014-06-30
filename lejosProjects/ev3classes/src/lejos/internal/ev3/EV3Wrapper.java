package lejos.internal.ev3;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCDOutputStream;
import lejos.hardware.lcd.TextLCD;
import lejos.internal.ev3.EV3LCDManager.LCDLayer;
import lejos.utility.Delay;

public class EV3Wrapper implements UncaughtExceptionHandler {

    static LCDLayer systemLayer;
    static EV3LCDManager manager = EV3LCDManager.getLocalLCDManager();
    static PrintStream origErr;
    
	public static void main(String[] args) throws Exception {
		Thread.setDefaultUncaughtExceptionHandler(new EV3Wrapper());
		// Force LCD layer to be created
        LocalEV3.get().getTextLCD();
		// Create extra LCD layers for redirected output, exceptions and our use
		LocalEV3.get().getTextLCD();
        manager.newLayer("STDOUT");
        manager.newLayer("EXCEPTION");
        systemLayer = manager.newLayer("SYSTEM");
        // redirect standard I/O streams
		TextLCD stdOut = new EV3TextLCD("STDOUT");
		OutputStream lcdOut = new LCDOutputStream(stdOut);
		origErr = System.err;
		System.setOut(new RedirectStream(System.out, lcdOut));
		System.setErr(new RedirectStream(System.err, lcdOut));
		// make sure everything can be seen
		switchToLayer("*");
		// allow switching
        new LCDLayerSwitcher().start();
        // run the original class
		invokeClass(args[0], new String[0]);
	}
	
	private static void invokeClass(String name, String[] args) 
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
	    Class<?> c = Class.forName(name);
	    Method m = c.getMethod("main", new Class[] { args.getClass() });
	    m.setAccessible(true);
	    int mods = m.getModifiers();
	    
	    if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
	        !Modifier.isPublic(mods)) {
	        throw new NoSuchMethodException("main");
	    }
	    
	    try {
	        m.invoke(null, new Object[] { args });
	    } catch (IllegalAccessException e) {
	        // This should not happen, as we have disabled access checks
	    }
	}
	
	private static void switchToLayer(String name)
	{
	    LCDLayer[] layers = manager.getLayers();
	    // Special case "*" all layers on
	    if (name.equals("*"))
	    {
	        for(LCDLayer l: layers)
	            l.setVisible(true);
	        return;
	    }

	    // turn off all layers
	    for(LCDLayer l: layers)
	        l.setVisible(false);
	    // find the layer and enable it
	    for(LCDLayer l: layers)
	        if (l.getName().equals(name))
	            l.setVisible(true);	        
	}

	@Override
	public void uncaughtException(Thread th, Throwable t) {
		Sound.buzz();
	    TextLCD lcd = new EV3TextLCD("EXCEPTION", Font.getSmallFont());
		switchToLayer("EXCEPTION");
		// Get rid of invocation exception
	    if (t.getCause() != null) t = t.getCause();
	    // Send stack trace to the menu so it goes to EV3Console etc.
		t.printStackTrace(origErr);
	    int offset = 0;
	    while (true)
	    {
    		lcd.clear();
    		lcd.drawString("Uncaught exception:", offset, 0);
    		lcd.drawString(t.getClass().getName(), offset, 2);
    		if (t.getMessage() != null) lcd.drawString(t.getMessage(), offset, 3);		
    		
    		if (t.getCause() != null) {
    			lcd.drawString("Caused by:", offset, 5);
    			lcd.drawString(t.getCause().toString(), offset, 6);
    		}
    		
    		StackTraceElement[] trace = t.getStackTrace();
    		for(int i=0;i<7 && i < trace.length ;i++) lcd.drawString(trace[i].toString(), offset, 8+i);
    		
    		lcd.refresh();
    		int id = Button.waitForAnyEvent();
    		if (id == Button.ID_ESCAPE) break;
    		if (id == Button.ID_LEFT) offset += 5;
    		if (id == Button.ID_RIGHT)offset -= 5;
    		if (offset > 0) offset = 0;
	    }
	}

    static class LCDLayerSwitcher extends Thread {
        
        public LCDLayerSwitcher()
        {
            setDaemon(true);            
        }
        /**
         * Background thread which provides automatic screen updates
         */
        public void run()
        {
            TextLCD systemLCD = new EV3TextLCD("SYSTEM", Font.getLargeFont());
            int curLayer = -1;
            String layerName = "*";
            Keys keys = LocalEV3.get().getKeys();
            while (true)
            {
                if (keys.getButtons() == (Keys.ID_LEFT|Keys.ID_RIGHT))
                {
                    LCDLayer[] layers = manager.getLayers();
                    curLayer++;
                    if (curLayer < layers.length && layers[curLayer] == systemLayer)
                        // skip system layer
                        curLayer++;
                    if (curLayer >= layers.length)
                    {
                        // special case all layers
                        curLayer = -1;
                        layerName = "*";                        
                    }
                    else
                        layerName = layers[curLayer].getName();
                    // Display the layer name for a short period
                    systemLCD.clear();
                    systemLCD.drawString(layerName, (systemLCD.getTextWidth()-layerName.length() + 1)/2, 1);
                    switchToLayer("SYSTEM");
                    Delay.msDelay(2000);
                    systemLCD.clear();
                    switchToLayer(layerName);
                }
                Delay.msDelay(100);
            }
        }
    }

	static class RedirectStream extends PrintStream {
		PrintStream orig, lcd;
		
		public RedirectStream(PrintStream orig, OutputStream os) {
			super(os);
			this.orig = orig;
		}
		
		@Override
		public void write(int x) {
			super.write(x);
			orig.write(x);
			orig.flush();
		}
		
		@Override
		public void write(byte[] b, int o, int l) {
			super.write(b,o,l);
			orig.write(b,o,l);
			orig.flush();
		}
		
		@Override
		public void close() {
			super.close();
			orig.close();
		}
		
		@Override
		public void flush() {
			super.flush();
			orig.flush();
		}
	}
}
