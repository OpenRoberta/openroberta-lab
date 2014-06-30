package lejos.hardware.lcd;

import lejos.hardware.BrickFinder;
import lejos.utility.Delay;

/**
 * Provide access to the EV3 LCD display
 *
 */
//public class LCD extends JPanel
public class LCD
{
    public static final int SCREEN_WIDTH = 178;
    public static final int SCREEN_HEIGHT = 128;
    public static final int NOOF_CHARS = 96;
    public static final int FONT_WIDTH = 10;
    public static final int FONT_HEIGHT = 16;
    public static final int CELL_WIDTH = FONT_WIDTH;
    public static final int CELL_HEIGHT = FONT_HEIGHT;
    public static final int DISPLAY_CHAR_WIDTH = SCREEN_WIDTH / CELL_WIDTH;
    public static final int DISPLAY_CHAR_DEPTH = SCREEN_HEIGHT / CELL_HEIGHT;
    
    /**
     * Common raster operations for use with bitBlt
     */
    public static final int ROP_CLEAR = 0x00000000;
    public static final int ROP_AND = 0xff000000;
    public static final int ROP_ANDREVERSE = 0xff00ff00;
    public static final int ROP_COPY = 0x0000ff00;
    public static final int ROP_ANDINVERTED = 0xffff0000;
    public static final int ROP_NOOP = 0x00ff0000;
    public static final int ROP_XOR = 0x00ffff00;
    public static final int ROP_OR = 0xffffff00;
    public static final int ROP_NOR = 0xffffffff;
    public static final int ROP_EQUIV = 0x00ffffff;
    public static final int ROP_INVERT = 0x00ff00ff;
    public static final int ROP_ORREVERSE = 0xffff00ff;
    public static final int ROP_COPYINVERTED = 0x0000ffff;
    public static final int ROP_ORINVERTED = 0xff00ffff;
    public static final int ROP_NAND = 0xff0000ff;
    public static final int ROP_SET = 0x000000ff;

    protected static TextLCD t = BrickFinder.getDefault().getTextLCD();
    protected static GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();

    private LCD() {
    }

    /**
     * Standard two input BitBlt function with the LCD display as the
     * destination. Supports standard raster ops and
     * overlapping images. Images are held in native leJOS/Lego format.
     * @param src byte array containing the source image
     * @param sw Width of the source image
     * @param sh Height of the source image
     * @param sx X position to start the copy from
     * @param sy Y Position to start the copy from
     * @param dx X destination
     * @param dy Y destination
     * @param w width of the area to copy
     * @param h height of the area to copy
     * @param rop raster operation.
     */
    public static void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx, int dy, int w, int h, int rop)
    {
        g.bitBlt(src, sw, sh, sx, sy, dx, dy, w, h, rop);
    }

    /**
     * Draw a single char on the LCD at specified x,y co-ordinate.
     * @param c Character to display
     * @param x X location
     * @param y Y location
     */
    public static void drawChar(char c, int x, int y)
    {
        t.drawChar(c, x, y);
    }

    public static void clearDisplay()
    {
        clear();
    }

    /**
     * Display an optionally inverted string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     * @param inverted if true the string is displayed inverted.
     */
    public static void drawString(String str, int x, int y, boolean inverted)
    {
       t.drawString(str, x, y, inverted);
    }

    /**
     * Display a string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public static void drawString(String str, int x, int y)
    {
    	t.drawString(str, x, y);
    }

    /**
     * Display an int on the LCD at specified x,y co-ordinate.
     *
     * @param i The value to display.
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public static void drawInt(int i, int x, int y)
    {
        t.drawInt(i, x, y);
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
    public static void drawInt(int i, int places, int x, int y)
    {
        t.drawInt(i, places, x, y);
    }

    /**
     * Start the process of updating the display. This will always return
     * immediately after starting the refresh process.
     */
    public static void asyncRefresh()
    {
        t.refresh();
    }

    /**
     * Obtain the system time when the current display refresh operation will
     * be complete. Not that this may be in the past.
     * @return the system time in ms when the refresh will be complete.
     */
    public static long getRefreshCompleteTime()
    {
        return System.currentTimeMillis();
    }

    /**
     * Wait for the current refresh cycle to complete.
     */
    public static void asyncRefreshWait()
    {
        long waitTime = getRefreshCompleteTime() -  System.currentTimeMillis();
        if (waitTime > 0)
            Delay.msDelay(waitTime);
    }

    /**
     * Refresh the display. If auto refresh is off, this method will wait until
     * the display refresh has completed. If auto refresh is on it will return
     * immediately.
     */
    public static void refresh()
    {
        t.refresh();
    }

    /**
     * Clear the display.
     */
    public static void clear()
    {
        t.clear();
    }
    
    /**
     * Provide access to the LCD display frame buffer. Allows both the firmware
     * and Java to make changes.
     * @return byte array that is the frame buffer.
     */
    public static byte[] getDisplay()
    {
        return t.getDisplay();
    }

    /**
     * Provide access to the LCD system font. Allows both the firmware
     * and Java to share the same font bitmaps.
     * @return byte array that is the frame buffer.
     */
    public static byte[] getSystemFont()
    {
        return Font.getDefaultFont().glyphs;
    }

    /**
     * Set the period used to perform automatic refreshing of the display.
     * A period of 0 disables the refresh.
     * @param period time in ms
     * @return the previous refresh period.
     */
    public static int setAutoRefreshPeriod(int period)
    {
        return 0;
    }

    /**
     * Turn on/off the automatic refresh of the LCD display. At system startup
     * auto refresh is on.
     * @param on true to enable, false to disable
     */
    public static void setAutoRefresh(boolean on)
    {
        g.setAutoRefresh(on);
    }
    
    /**
     * Method to set a pixel on the screen.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param color the pixel color (0 = white, 1 = black)
     */
    public static void setPixel(int x, int y, int color)
    {
        g.setPixel(x, y, color);
    }
    
    /**
     * Method to get a pixel from the screen.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the pixel color (0 = white, 1 = black)
     */
    public static int getPixel(int x, int y) {
        return g.getPixel(x, y);
    }

    /**
     * Standard two input BitBlt function. Supports standard raster ops and
     * overlapping images. Images are held in native leJOS/Lego format.
     * @param src byte array containing the source image
     * @param sw Width of the source image
     * @param sh Height of the source image
     * @param sx X position to start the copy from
     * @param sy Y Position to start the copy from
     * @param dst byte array containing the destination image
     * @param dw Width of the destination image
     * @param dh Height of the destination image
     * @param dx X destination
     * @param dy Y destination
     * @param w width of the area to copy
     * @param h height of the area to copy
     * @param rop raster operation.
     */
    public static void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte dst[], int dw, int dh, int dx, int dy, int w, int h, int rop)
    {
    	g.bitBlt(src, sw, sh, sx, sy, dst, dw, dh, dx, dy, w, h, rop);
    }     
    
    /**
     * Scrolls the screen up one text line
     *
     */
    public static void scroll()
    {
        t.scroll();
    }
    
    /**
     * Clear a contiguous set of characters
     * @param x the x character coordinate
     * @param y the y character coordinate
     * @param n the number of characters
     */
    public static void clear(int x, int y, int n) {
        t.clear();
    }
    
    /**
     * Clear an LCD display row
     * @param y the row to clear
     */
    public static void clear(int y) {
        t.clear(y);
    }

    /**
     * Set the LCD contrast.
     * @param contrast 0 blank 0x60 full on
     */
    public static void setContrast(int contrast)
    {
        // Not implemented
    }
    
    public static byte[] getHWDisplay() {
    	return t.getHWDisplay();
    }
}

