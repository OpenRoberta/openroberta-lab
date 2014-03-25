package lejos.internal.ev3;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.TextLCD;

public class EV3TextLCD extends EV3LCD implements TextLCD {
	private Font font;
	
	public EV3TextLCD(Font font) {
		super();
		this.font = font;
	}
	
	public EV3TextLCD() {
		this(Font.getDefaultFont());
	}

    /**
     * Draw a single char on the LCD at specified x,y co-ordinate.
     * @param c Character to display
     * @param x X location
     * @param y Y location
     */
    public void drawChar(char c, int x, int y)
    {
        bitBlt(font.glyphs, font.width * font.glyphCount, font.height, font.width * (c-32), 0, x * font.glyphWidth, y * font.height, font.width, font.height, ROP_COPY);
    }

    /**
     * Display a string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public void drawString(String str, int x, int y)
    {
        char[] strData = str.toCharArray();
        // Draw the background rect
        bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, x * font.glyphWidth, y * font.height, strData.length * font.glyphWidth, font.height, ROP_CLEAR);
        // and the characters
        for (int i = 0; (i < strData.length); i++)
            bitBlt(font.glyphs, font.width * font.glyphCount, font.height, font.width * (strData[i]-32), 0, (x + i) * font.glyphWidth, y * font.height, font.width, font.height, ROP_COPY);
    }
    

    /**
     * Display an int on the LCD at specified x,y co-ordinate.
     *
     * @param i The value to display.
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public void drawInt(int i, int x, int y)
    {
        drawString(Integer.toString(i), x, y);
    }

    /**
     * Display an in on the LCD at x,y with leading spaces to occupy at least the number
     * of characters specified by the places parameter.
     *
     * @param i The value to display
     * @param places number of places to use to display the value
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public void drawInt(int i, int places, int x, int y)
    {
        drawString(String.format("%"+places+"d", i), x, y);
    }
	
    /**
     * Display an optionally inverted string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     * @param inverted if true the string is displayed inverted.
     */
    public void drawString(String str, int x, int y, boolean inverted)
    {
        if (inverted)
        {
            char[] strData = str.toCharArray();
            // Draw the background rect
            bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, x * font.glyphWidth, y * font.height, strData.length * font.glyphWidth, font.height, ROP_SET);
            // and the characters
            for (int i = 0; (i < strData.length); i++)
                bitBlt(font.glyphs, font.width * font.glyphCount, font.height, font.width * (strData[i]-32), 0, (x + i) * font.glyphWidth, y * font.height, font.width, font.height, ROP_COPYINVERTED);
        } else
            drawString(str, x, y);
    }
    
    /**
     * Scrolls the screen up one text line
     *
     */
    public void scroll()
    {
        bitBlt(displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, 0, font.height,
                0, 0, SCREEN_WIDTH, SCREEN_HEIGHT - font.height, ROP_COPY);
        bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 0, SCREEN_HEIGHT - font.height,
                SCREEN_WIDTH, font.height, ROP_CLEAR);
    }
    
    
    /**
     * Clear a contiguous set of characters
     * @param x the x character coordinate
     * @param y the y character coordinate
     * @param n the number of characters
     */
    public void clear(int x, int y, int n) {
        bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, x * font.glyphWidth, y * font.height,
                n * font.glyphWidth, font.height, ROP_CLEAR);
    }
    
    /**
     * Clear an LCD display row
     * @param y the row to clear
     */
    public void clear(int y) {
        bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 0, y * font.height,
                SCREEN_WIDTH, font.height, ROP_CLEAR);      
    }

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public int getTextWidth() {
		return getWidth() / font.width;
	}

	@Override
	public int getTextHeight() {
		return getHeight() / font.height;
	}

}
