package lejos.hardware.lcd;

import java.io.OutputStream;

import lejos.hardware.BrickFinder;

/**
 * A simple output stream that implements console output.
 * It writes to the bottom line of the screen, scrolling the
 * LCD up one line when writing to character position 0, 
 * and starting a new line when the position reaches 16
 * or a new line character is written. 
 * 
 * Used by System.out.println.
 * 
 * @author Lawrie Griffiths
 *
 */
public class LCDOutputStream extends OutputStream {
	private int col = 0;
	private int line = 0;
	
	private TextLCD lcd;
	
	public LCDOutputStream(TextLCD lcd)
	{
	    this.lcd = lcd;
	}
	
	public LCDOutputStream()
	{
	    this(BrickFinder.getDefault().getTextLCD());
	}
	
	@Override
	public void write(int c) {
		char x = (char)(c & 0xFF);
		switch (x)
		{
			case '\f':
				lcd.clear();
				line = 0;
				col = 0;
				break;
			case '\t':
				col = col + 8 - col % 8; 
				break;
			case '\n': 
				incLine();
			case '\r':
				col = 0;
				break;
			default:
				if (col >= lcd.getTextWidth())
				{
					col = 0;
					incLine();
				}
				lcd.drawChar(x, col++, line);
		}
		
	}

	private void incLine() {
		lcd.refresh();
		if (line < lcd.getTextHeight() - 1)
			line++;
		else
			lcd.scroll();
	}
}
