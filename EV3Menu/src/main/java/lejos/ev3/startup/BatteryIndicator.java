package lejos.ev3.startup;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.CommonLCD;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.lcd.TextLCD;

/**
 * Draws a battery and the name of the EV3.
 */
public class BatteryIndicator {
    // Battery state information
    private static final int STD_MIN = 6100;
    private static final int STD_OK = 6500;
    private static final int STD_MAX = 8000;
    private static final int RECHARGE_MIN = 7100;
    private static final int RECHARGE_OK = 7200;
    private static final int RECHARGE_MAX = 8200;

    private static final int HISTORY_SIZE = 2000 / Config.ANIM_DELAY;
    private static final int WINDOW_TOLERANCE = 10;

    private static final String ICIWifi =
        "\u0000\u0000\u0000\u0000\u00e0\u0007\u00f8\u001f\u001e\u0078\u0007\u00e0\u0003\u00c0\u00f0\u000f\u0078\u001e\u0018\u0018\u0000\u0000\u0080\u0001\u0080\u0001\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICIUSB =
        "\u0000\u0001\u0080\u0003\u0000\u0009\u0000\u001d\u0020\u0009\u0070\u0009\u0020\u0005\u0020\u0003\u0040\u0001\u0080\u0001\u0000\u0001\u0000\u0001\u0080\u0003\u00c0\u0007\u00c0\u0007\u0080\u0003";

    private static final String ICIRoberta =
        "\u0000\u0038\u000c\u0040\u0002\u0000\u0078\u001f\u0084\u0020\u0042\u0040\u0031\u008c\u0019\u0096\u0039\u009e\u0031\u008c\u0042\u0040\u0084\u0020\u00f8\u001f\u00c0\u0001\u00e0\u0000\u00e0\u0000";
    private static final String ICICLEAR =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";

    private static final Image wifiImage = new Image(16, 16, Utils.stringToBytes8(ICIWifi));
    private static final Image usbImage = new Image(16, 16, Utils.stringToBytes8(ICIUSB));
    private static final Image robertaImage = new Image(16, 16, Utils.stringToBytes8(ICIRoberta));
    private static final Image clearImage = new Image(16, 16, Utils.stringToBytes8(ICICLEAR));

    private static final int ICON_X = 160;

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

    private final boolean rechargeable = false;
    private boolean wifi = false;
    private boolean usb = false;

    private final TextLCD lcd = LocalEV3.get().getTextLCD();
    private final GraphicsLCD g = LocalEV3.get().getGraphicsLCD();

    public BatteryIndicator() {
        if ( this.rechargeable ) {
            this.levelMin = RECHARGE_MIN;
            this.levelOk = RECHARGE_OK;
            this.levelHigh = RECHARGE_MAX;
        } else {
            this.levelMin = STD_MIN;
            this.levelOk = STD_OK;
            this.levelHigh = STD_MAX;
        }

        int val = LocalEV3.ev3.getPower().getVoltageMilliVolt();
        this.windowcenter = val;
        this.historysum = val * HISTORY_SIZE;
        for ( int i = 0; i < HISTORY_SIZE; i++ ) {
            this.history[i] = val;
        }
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean getWifi() {
        return this.wifi;
    }

    public void setUsb(boolean usb) {
        this.usb = usb;
    }

    public synchronized void setDefaultTitle(String title) {
        this.titleString = "";
        byte[] o = this.default_title;
        byte[] b = Utils.textToBytes(title);
        this.default_title = b;
        if ( this.title == o ) {
            this.title = b;
        }
    }

    public synchronized void setTitle(String title) {
        this.titleString = title;
        this.title = (title == null) ? this.default_title : Utils.textToBytes(title);
    }

    private int getLevel() {
        int val = LocalEV3.ev3.getPower().getVoltageMilliVolt();

        this.historysum += val - this.history[this.historyindex];
        this.history[this.historyindex] = val;
        this.historyindex = (this.historyindex + 1) % HISTORY_SIZE;
        int average = (this.historysum + HISTORY_SIZE / 2) / HISTORY_SIZE;

        int diff = average - this.windowcenter;
        if ( diff < -WINDOW_TOLERANCE || diff > WINDOW_TOLERANCE ) {
            this.windowcenter += diff / 2;
        }

        return this.windowcenter;
    }

    /**
     * Display the battery icon.
     */
    public synchronized void draw(long time) {
        int level = getLevel();

        if ( level <= this.levelMin ) {
            this.isOk = false;
            level = this.levelMin;
        }
        if ( level >= this.levelOk ) {
            this.isOk = true;
        }
        if ( level > this.levelHigh ) {
            level = this.levelHigh;
        }

        if ( this.titleString == null ) {
            this.titleString = "";
        }

        this.lcd.drawString(this.titleString, 8 - (this.titleString.length() / 2), 0);

        if ( this.isOk || (time % (2 * Config.ICON_BATTERY_BLINK)) < Config.ICON_BATTERY_BLINK ) {
            this.lcd.drawInt((level - level % 1000) / 1000, 0, 0);
            this.lcd.drawString(".", 1, 0);
            this.lcd.drawInt((level % 1000) / 100, 2, 0);
        } else {
            this.lcd.drawString("   ", 0, 0);
        }

        if ( this.wifi ) {
            this.g.drawRegion(wifiImage, 0, 0, 16, 16, 0, ICON_X, 0, 0);
        } else if ( this.usb && !this.wifi ) {
            this.g.drawRegion(usbImage, 0, 0, 16, 16, 0, ICON_X, 0, 0);
        } else {
            this.g.drawRegionRop(null, 0, 0, 16, 16, 0, ICON_X, 0, 0, CommonLCD.ROP_CLEAR);
        }

        if ( ORAhandler.isRegistered() ) {
            this.g.drawRegion(robertaImage, 0, 0, 16, 16, 0, ICON_X - 18, 0, 0);
        } else {
            this.g.drawRegion(clearImage, 0, 0, 16, 16, 0, ICON_X - 18, 0, 0);
        }
    }
}
