package lejos.hardware;

public interface Sounds
{
    // Instruments (yes I know they don't sound anything like the names!)
    public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5};
    public final static int[] FLUTE = new int[]{10, 25, 2000, 1000, 25};
    public final static int[] XYLOPHONE = new int[]{1, 8, 3000, 5000, 5};

    public final static int BEEP = 0;
    public final static int DOUBLE_BEEP = 1;
    public final static int ASCENDING = 2;
    public final static int DESCENDING = 3;
    public final static int BUZZ = 4;

    public static final int VOL_MAX = 100;


}
