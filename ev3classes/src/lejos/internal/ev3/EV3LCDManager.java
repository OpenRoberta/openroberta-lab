package lejos.internal.ev3;

import java.io.Closeable;
import java.util.ArrayList;

import lejos.internal.io.NativeDevice;
import lejos.utility.Delay;

import com.sun.jna.Pointer;

/**
 * This class provides management of a set of LCD layers.<br>
 * Each layer can be used as the frame buffer for text/graphics output. Layers can be
 * made visible or invisible. If more than one layer is visible the layers will be
 * combined and will overlay each other on the display. Layers are named and this
 * name can be used to access the layer. 
 * @author andy
 *
 */
public class EV3LCDManager
{
    public static final int DEFAULT_REFRESH_PERIOD = 250;
    protected final static int HW_MEM_WIDTH = ((EV3LCD.SCREEN_WIDTH + 31)/32)*4; // width of HW Buffer in bytes
    protected final static int LCD_HW_BUFFER_LENGTH = HW_MEM_WIDTH*EV3LCD.SCREEN_HEIGHT;
    protected NativeDevice dev = new NativeDevice("/dev/fb0");
    protected Pointer lcd = dev.mmap(LCD_HW_BUFFER_LENGTH);
    protected boolean autoRefresh = true;
    protected long refreshTime = 0;
        
    protected byte [] hwBuffer = new byte[LCD_HW_BUFFER_LENGTH];
    private static LCDUpdate updateThread;
    protected static EV3LCDManager localLCDManager = new EV3LCDManager();
    protected ArrayList<LCDLayer> layers = new ArrayList<LCDLayer>();
    protected volatile LCDLayer [] visibleLayers = new LCDLayer[0];
    static
    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {localLCDManager.closeAll();}
        });
    }

    /**
     * The actual layer object. provides a basic container for the frame buffer.
     * @author andy
     *
     */
    static public class LCDLayer implements Closeable
    {
        protected byte [] displayBuf = null;
        protected boolean visible = false;
        protected boolean autoRefresh = true;
        protected String name;
        protected int openCnt = 0;

        /**
         * Create the layer and give it a name
         * @param name the name of the layer
         */
        public LCDLayer(String name)
        {
            this.name = name;
        }

        /**
         * Open the layer making it available for use
         */
        public void open()
        {
            if (openCnt++ == 0)
            {
                displayBuf = new byte[EV3LCD.LCD_BUFFER_LENGTH];
                setVisible(true);
            }
        }

        /**
         * Close the layer, closed layers can not be written to.
         */
        @Override
        public void close()
        {
            if (openCnt <= 0)
                throw new IllegalStateException("layer is not open");
            if (openCnt == 1)
            {
                setVisible(false);
                displayBuf = null;
                openCnt = 0;
            }
        }

        /**
         * Check to see if the layer is open
         * @return
         */
        public boolean isOpen()
        {
            return openCnt > 0;
        }

        /**
         * Set the visibility of the layer. Note that invisible layers can still be updated.
         * @param visible true to make the layer visible, false to hide it
         */
        public void setVisible(boolean visible)
        {
            if (openCnt > 0)
            {
                this.visible = visible;
                localLCDManager.buildVisibleList();
            }
        }

        /**
         * Enable/disable automatic refresh for the layer.
         * @param refresh turn auto refresh on/off
         */
        public void setAutoRefresh(boolean refresh)
        {
            this.autoRefresh = refresh;
            localLCDManager.buildVisibleList();
        }

        /**
         * return the name of the layer
         * @return the layer name
         */
        public String getName()
        {
            return name;
        }

        /**
         * Get the frame buffer
         * @return the array of bytes that represents the frame buffer for this layer
         */
        public byte[] getDisplay()
        {
            return displayBuf;
        }

        /**
         * Update the hardware display.
         */
        public void refresh()
        {
            localLCDManager.update();
        }
        
    }

    /**
     * Iternal class to manage the hardware update
     * @author andy
     *
     */
    class LCDUpdate extends Thread {
        
        public LCDUpdate()
        {
            setDaemon(true);            
        }
        /**
         * Background thread which provides automatic screen updates
         */
        public void run()
        {
            for(;;)
            {
                long now = System.currentTimeMillis();
                if (now >= refreshTime)
                {
                    if (autoRefresh)
                        update();
                    else
                        refreshTime = now + DEFAULT_REFRESH_PERIOD;
                }
                Delay.msDelay(refreshTime - now);
           }
        }
    }

    
    private EV3LCDManager() {
        if (updateThread == null) {
            updateThread = new LCDUpdate();
            updateThread.start();
        }
    }

    /**
     * return the LCD manager object
     * @return the manager
     */
    public static EV3LCDManager getLocalLCDManager()
    {
        return localLCDManager;
    }

    /**
     * Helper function locate the named layer.
     * @param name
     * @return
     */
    protected LCDLayer findLayer(String name)
    {
        for(LCDLayer l : layers)
            if (l.getName().equals(name))
                return l;
        return null;
    }

    /**
     * Return the requested layer.
     * @param name the name of the layer to be located
     * @return The layer
     */
    public synchronized LCDLayer getLayer(String name)
    {
        LCDLayer l = findLayer(name);
        if (l == null)
            throw new IllegalArgumentException("Layer does not exist " + name);
        return l;
    }

    /**
     * return an array containing all of the current layers
     * @return array of layers
     */
    public LCDLayer[] getLayers()
    {
        return layers.toArray(new LCDLayer[layers.size()]);
    }

    /**
     * Create a new layer with the provided name.
     * @param name the layer name
     * @return the layer
     */
    public synchronized LCDLayer newLayer(String name)
    {
        if (findLayer(name) != null)
            throw new IllegalArgumentException("Layer already exists " + name);
        LCDLayer l = new LCDLayer(name);
        layers.add(l);
        return l;
    }

    /**
     * Helper function. Build a list of layers that should be displayed.
     */
    protected synchronized void buildVisibleList()
    {
        // count how many layers are visible
        int visCnt = 0;
        for(LCDLayer l : layers)
            if (l.visible && l.displayBuf != null)
                visCnt++;
        LCDLayer [] vis = new LCDLayer[visCnt];
        // place layers into list
        visCnt = 0;
        autoRefresh = false;
        for(LCDLayer l : layers)
            if (l.visible && l.displayBuf != null)
            {
                vis[visCnt++] = l;
                autoRefresh |= l.autoRefresh;
            }
        // Make the new list active
        visibleLayers = vis; 
        update();
    }

    /**
     * Helper method. Copy all of the visible layers to the HW screen
     */
    protected void update()
    {
        LCDLayer[] vis = visibleLayers;
        if (vis.length > 0)
        {
            // copy first layer to the display
            for(int row = 0; row < EV3LCD.SCREEN_HEIGHT; row++)
                System.arraycopy(vis[0].displayBuf, row*EV3LCD.SCREEN_MEM_WIDTH, hwBuffer, row*HW_MEM_WIDTH, EV3LCD.SCREEN_MEM_WIDTH);
            // now or in any other layers
            for(int i = 1; i < vis.length; i++)
                for(int row = 0; row < EV3LCD.SCREEN_HEIGHT; row++)
                    for(int col = 0; col < EV3LCD.SCREEN_MEM_WIDTH; col++)
                        hwBuffer[row*HW_MEM_WIDTH + col] |= vis[i].displayBuf[row*EV3LCD.SCREEN_MEM_WIDTH + col];
            lcd.write(0, hwBuffer, 0, hwBuffer.length);            
        }
        else
        {
            // nothing to display, clear the screen
            for(int i = 0; i < hwBuffer.length; i++)
                hwBuffer[i] = 0;
        }
        lcd.write(0, hwBuffer, 0, hwBuffer.length);
        refreshTime = System.currentTimeMillis() + DEFAULT_REFRESH_PERIOD;
    }

    /**
     * Close all open layers.
     */
    protected void closeAll()
    {
        for(LCDLayer l : layers)
            if (l.isOpen())
                l.close();        
    }

    /**
     * Return the contents of the hardware display.
     * @return
     */
    public byte[] getHWDisplay() 
    {
        byte[] buffer = new byte[EV3LCD.LCD_BUFFER_LENGTH];
        byte [] hwBuffer = new byte[LCD_HW_BUFFER_LENGTH];
        
        lcd.read(0, hwBuffer, 0, LCD_HW_BUFFER_LENGTH);
        
        for(int row = 0; row < EV3LCD.SCREEN_HEIGHT; row++)
        {
            System.arraycopy(hwBuffer, row*HW_MEM_WIDTH, buffer, row*EV3LCD.SCREEN_MEM_WIDTH, EV3LCD.SCREEN_MEM_WIDTH);
        }
        return buffer;
    }    
}
