package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.TextLCD;

public class Viewer {
   private static int startX = 0, startY = 0;
   private static final int NUM_LINES = 13, NUM_CHARS = 29;
   private static ArrayList<String> lines = new ArrayList<String>();
   private static TextLCD lcd = BrickFinder.getDefault().getTextLCD(Font.getSmallFont());
   
   public static void view(String fileName) throws IOException {
	   lines.clear();
		InputStream in = null;
		try {
		    in = new FileInputStream(fileName);
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        lines.add(line);
		    }
		    reader.close();
		} catch (IOException x) {
		    System.err.println(x);
		} finally {
		    if (in != null) in.close();
		}
		
		int maxLen = 0;
		for(String line: lines) {
			if (line.length() > maxLen) maxLen = line.length();
		}
		
	    display();
	    
	    for(;;) {

	    	int b = Button.waitForAnyPress();
	    	
		    switch (b) {
		    case Button.ID_ESCAPE:
		    	lines.clear();
		    	return;
		    case Button.ID_UP:
		    	if  (startY > 0) {
		    		startY--;
		    		display();
		    	}
		    	break;
		    case Button.ID_DOWN:
		    	if (startY < lines.size() - NUM_LINES) {
		    		startY++;
		    		display();
		    	}
		    	break;
		    case Button.ID_LEFT:
		    	if  (startX > 0) {
		    		startX--;
		    		display();
		    	}
		    	break;
		    case Button.ID_RIGHT:
		    	if (startX < maxLen - NUM_CHARS) {
		    		startX++;
		    		display();
		    	}
		    	break;
		    }  
	    }   
	}
	
	private static void display() {
		lcd.clear();
		for(int i=0;i<NUM_LINES && startY+i<lines.size();i++) {
			if (lines.get(startY+i).length() > startX)
				lcd.drawString(lines.get(startY+i).substring(startX), 0, 3+i);
		}
	}
}
