package lejos.hardware.lcd;

public interface TextLCD extends CommonLCD {
	
    /**
     * Draw a single char on the LCD at specified x,y co-ordinate.
     * @param c Character to display
     * @param x X location
     * @param y Y location
     */
    public void drawChar(char c, int x, int y);
    
    /**
     * Display an optionally inverted string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     * @param inverted if true the string is displayed inverted.
     */
    public void drawString(String str, int x, int y, boolean inverted);
    
    /**
     * Display a string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public void drawString(String str, int x, int y);
    
    /**
     * Display an int on the LCD at specified x,y co-ordinate.
     *
     * @param i The value to display.
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public void drawInt(int i, int x, int y);
    
    /**
     * Display an in on the LCD at x,y with leading spaces to occupy at least the number
     * of characters specified by the places parameter.
     *
     * @param i The value to display
     * @param places number of places to use to display the value
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public void drawInt(int i, int places, int x, int y);
    
    /**
     * Clear a contiguous set of characters
     * @param x the x character coordinate
     * @param y the y character coordinate
     * @param n the number of characters
     */
    public void clear(int x, int y, int n);
    
    /**
     * Clear an LCD display row
     * @param y the row to clear
     */
    public void clear(int y);
    
    /**
     * Scrolls the screen up one text line
     */
    public void scroll();
    
    /**
     * Get the current font
     */
    public Font getFont();
    
    /**
     * Get the width of the screen in characters
     */
    public int getTextWidth();
    
    /**
     * Get the height of the screen in characters
     */
    public int getTextHeight();

}
