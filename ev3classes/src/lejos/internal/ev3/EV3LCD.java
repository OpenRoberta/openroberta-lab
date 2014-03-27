package lejos.internal.ev3;

import lejos.hardware.lcd.CommonLCD;
import lejos.internal.io.NativeDevice;
import lejos.utility.Delay;

import com.sun.jna.Pointer;

/**
 * Provide access to the EV3 LCD display
 *
 */
public class EV3LCD implements CommonLCD
{
    public static final int SCREEN_WIDTH = 178;
    public static final int SCREEN_HEIGHT = 128;
    public static final int DEFAULT_REFRESH_PERIOD = 250;
    
    private static boolean autoRefresh = true;

    protected final static int HW_MEM_WIDTH = ((SCREEN_WIDTH + 31)/32)*4; // width of HW Buffer in bytes
    protected final static int SCREEN_MEM_WIDTH = (SCREEN_WIDTH +7)/8; // width of leJOS screen buffer in bytes
    protected final static int LCD_BUFFER_LENGTH = HW_MEM_WIDTH*SCREEN_HEIGHT;
    protected static byte[] displayBuf = new byte[LCD_BUFFER_LENGTH];
    protected NativeDevice dev = new NativeDevice("/dev/fb0");
    protected Pointer lcd = dev.mmap(LCD_BUFFER_LENGTH);
        
    protected byte [] hwBuffer = new byte[LCD_BUFFER_LENGTH];
    private static LCDUpdate updateThread;

    public EV3LCD() {
    	if (updateThread == null) {
    		updateThread = new LCDUpdate();
            updateThread.setDaemon(true);
            updateThread.start();
    	}
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
    public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx, int dy, int w, int h, int rop)
    {
        bitBlt(src, sw, sh, sx, sy, displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, dx, dy, w, h, rop);
    }

    public void clearDisplay()
    {
        clear();
    }

    /**
     * Start the process of updating the display. This will always return
     * immediately after starting the refresh process.
     */
    public void asyncRefresh()
    {
        update(displayBuf);
    }

    /**
     * Obtain the system time when the current display refresh operation will
     * be complete. Not that this may be in the past.
     * @return the system time in ms when the refresh will be complete.
     */
    public long getRefreshCompleteTime()
    {
        return System.currentTimeMillis();
    }

    /**
     * Wait for the current refresh cycle to complete.
     */
    public void asyncRefreshWait()
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
    public void refresh()
    {
        asyncRefresh();
        if (!autoRefresh)
            asyncRefreshWait();
    }

    /**
     * Clear the display.
     */
    public void clear()
    {
        bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, ROP_CLEAR);
    }
    
    /**
     * Provide access to the LCD display frame buffer. Allows both the firmware
     * and Java to make changes.
     * @return byte array that is the frame buffer.
     */
    public byte[] getDisplay()
    {
        return displayBuf;
    }

    /**
     * Set the period used to perform automatic refreshing of the display.
     * A period of 0 disables the refresh.
     * @param period time in ms
     * @return the previous refresh period.
     */
    public int setAutoRefreshPeriod(int period)
    {
        return 0;
    }

    /**
     * Turn on/off the automatic refresh of the LCD display. At system startup
     * auto refresh is on.
     * @param on true to enable, false to disable
     */
    public void setAutoRefresh(boolean on)
    {
        setAutoRefreshPeriod((on ? DEFAULT_REFRESH_PERIOD : 0));
        autoRefresh = on;
    }
    
    /**
     * Method to set a pixel on the screen.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param color the pixel color (0 = white, 1 = black)
     */
    public void setPixel(int x, int y, int color)
    {
        bitBlt(displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, x, y, 1, 1,(color == 1 ? ROP_SET : ROP_CLEAR));
    }
    
    /**
     * Method to get a pixel from the screen.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the pixel color (0 = white, 1 = black)
     */
    public int getPixel(int x, int y) {
        if (x < 0 || x >= SCREEN_WIDTH || y < 0 || y >= SCREEN_HEIGHT) return 0; 
        int bit = (x & 0x7);
        //int index = (y)*SCREEN_WIDTH + x;
        int index = (y)*HW_MEM_WIDTH + x;
        return ((displayBuf[index] >> bit) & 1);
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
    public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte dst[], int dw, int dh, int dx, int dy, int w, int h, int rop)
    {
        /* This is a partial implementation of the BitBlt algorithm. It provides a
         * complete set of raster operations and handles partial and fully aligned
         * images correctly. Overlapping source and destination images is also 
         * supported. It does not performing mirroring. The code was converted
         * from an initial Java implementation and has not been optimized for C.
         * The general mechanism is to perform the block copy with Y as the inner
         * loop (because on the display the bits are packed y-wise into a byte). We
         * perform the various rop cases by reducing the operation to a series of
         * AND and XOR operations. Each step is controlled by a byte in the rop code.
         * This mechanism is based upon that used in the X Windows system server.
         */
        // Clip to source and destination
        int trim;
        if (dx < 0)
        {
          trim = -dx;
          dx = 0;
          sx += trim;
          w -= trim;
        }
        if (dy < 0)
        {
          trim = -dy;
          dy = 0;
          sy += trim;
          h -= trim;
        }
        if (sx < 0 || sy < 0) return;
        if (dx + w > dw) w = dw - dx;
        if (sx + w > sw) w = sw - sx;
        if (w <= 0) return;
        if (dy + h > dh) h = dh - dy;
        if (sy + h > sh) h = sh - sy;
        if (h <= 0) return;
        // Setup initial parameters and check for overlapping copy
        int xinc = 1;
        int yinc = 1;
        byte firstBit = 1;
        if (src == dst)
        {
          // If copy overlaps we use reverse direction
          if (dy > sy)
          {
            sy = sy + h - 1;
            dy = dy + h - 1;
            yinc = -1;
          }
          if (dx > sx)
          {
            firstBit = (byte)0x80;
            xinc = -1;
            sx = sx + w - 1;
            dx = dx + w - 1;
          }
        }
        if (src == null)
            src = dst;
        int swb = (sw+7)/8;
        int dwb = (dw+7)/8;
        if (src == displayBuf)
            swb = HW_MEM_WIDTH;
        if (dst == displayBuf)
            dwb = HW_MEM_WIDTH;
        int inStart = sy*swb;
        int outStart = dy*dwb;
        byte inStartBit = (byte)(1 << (sx & 0x7));
        byte outStartBit = (byte)(1 << (dx & 0x7));
        dwb *= yinc;
        swb *= yinc;
        // Extract rop sub-fields
        byte ca1 = (byte)(rop >> 24);
        byte cx1 = (byte)(rop >> 16);
        byte ca2 = (byte)(rop >> 8);
        byte cx2 = (byte) rop;
        boolean noDst = (ca1 == 0 && cx1 == 0);
        int ycnt;
        // Check for byte aligned case and optimise for it
        if (w >= 8 && inStartBit == firstBit && outStartBit == firstBit)
        {
          int ix = sx/8;
          int ox = dx/8;
          int byteCnt = w/8;
          ycnt = h;
          while (ycnt-- > 0)
          {
            int inIndex = inStart + ix;
            int outIndex = outStart + ox;
            int cnt = byteCnt;
            while(cnt-- > 0)
            {
              if (noDst)
                dst[outIndex] = (byte)((src[inIndex] & ca2)^cx2);            
              else
              {
                byte inVal = src[inIndex];
                dst[outIndex] = (byte)((dst[outIndex] & ((inVal & ca1)^cx1)) ^ ((inVal & ca2)^cx2));
              }
              outIndex += xinc;
              inIndex += xinc;
            }
            ix += swb;
            ox += dwb;
          }
          // Do we have a final non byte multiple to do?
          w &= 0x7;
          if (w == 0) 
          {
              //if (dst == displayBuf)
                  //update(displayBuf);
              return;
          }
          //inStart = sy*swb;
          //outStart = dy*dwb;
          sx += byteCnt*8;
          dx += byteCnt*8;
        }
        // General non byte aligned case
        int ix = sx/8;
        int ox = dx/8;
        ycnt = h;
        while(ycnt-- > 0)
        {
          int inIndex = inStart + ix;
          byte inBit = inStartBit;
          byte inVal = src[inIndex];
          byte inAnd = (byte)((inVal & ca1)^cx1);
          byte inXor = (byte)((inVal & ca2)^cx2);
          int outIndex = outStart + ox;
          byte outBit = outStartBit;
          byte outPixels = dst[outIndex];
          int cnt = w;
          while(true)
          {
            if (noDst)
            {
              if ((inXor & inBit) != 0)
                outPixels |= outBit;
              else
                outPixels &= ~outBit;
            }
            else
            {
              byte resBit = (byte)((outPixels & ((inAnd & inBit) != 0 ? outBit : 0))^((inXor & inBit) != 0 ? outBit : 0));
              outPixels = (byte)((outPixels & ~outBit) | resBit);
            }
            if (--cnt <= 0) break;
            if (xinc > 0)
            {
              inBit <<= 1;
              outBit <<= 1;
            }
            else
            {
              inBit >>= 1;
              outBit >>= 1;
            }
            if (inBit == 0)
            {
              inBit = firstBit;
              inIndex += xinc;
              inVal = src[inIndex];
              inAnd = (byte)((inVal & ca1)^cx1);
              inXor = (byte)((inVal & ca2)^cx2);
            }
            if (outBit == 0)
            {
              dst[outIndex] = outPixels;
              outBit = firstBit;
              outIndex += xinc;
              outPixels = dst[outIndex];
            }
          }
          dst[outIndex] = outPixels;
          inStart += swb;
          outStart += dwb;
        }
        //if (dst == displayBuf)
            //update(displayBuf);
      }        

    /**
     * Set the LCD contrast.
     * @param contrast 0 blank 0x60 full on
     */
    public void setContrast(int contrast)
    {
       // Not implemented 
    }

    /**
     * Update the hardware display contents from the internal display bitmap. We 
     * need to convert the data from the simple packed format used by the internal
     * display to that used by the actual LCD controller. See the datasheet for the
     * ST7586S LCD controller for details.
     * @param buffer internal format buffer to display
     */
    protected synchronized void update(byte [] buffer)
    {
        lcd.write(0, buffer, 0, buffer.length);

    }
    
    public byte[] getHWDisplay() {
    	byte[] buffer = new byte[SCREEN_HEIGHT*SCREEN_MEM_WIDTH];
    	byte [] hwBuffer = new byte[LCD_BUFFER_LENGTH];
    	
    	lcd.read(0, hwBuffer, 0, LCD_BUFFER_LENGTH);
    	
        for(int row = 0; row < SCREEN_HEIGHT; row++)
        {
            System.arraycopy(hwBuffer, row*HW_MEM_WIDTH, buffer, row*SCREEN_MEM_WIDTH, SCREEN_MEM_WIDTH);
        }
    	return buffer;
    }
    
    class LCDUpdate extends Thread {
	    /**
	     * Background thread which provides automatic screen updates
	     */
	    public void run()
	    {
	        for(;;)
	        {
	            Delay.msDelay(DEFAULT_REFRESH_PERIOD);
	            if (autoRefresh)
	                update(displayBuf);
	        }
	    }
    }

	@Override
	public int getWidth() {
		return SCREEN_WIDTH;
	}

	@Override
	public int getHeight() {
		return SCREEN_HEIGHT;
	}
}

