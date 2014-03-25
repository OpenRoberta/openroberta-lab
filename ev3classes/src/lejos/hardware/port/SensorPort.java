package lejos.hardware.port;

import lejos.hardware.BrickFinder;

/**
 * Basic interface for EV3 sensor ports.
 * @author andy
 *
 */
public interface SensorPort {
    public static final Port S1 = BrickFinder.getDefault().getPort("S1");
    public static final Port S2 = BrickFinder.getDefault().getPort("S2");
    public static final Port S3 = BrickFinder.getDefault().getPort("S3");
    public static final Port S4 = BrickFinder.getDefault().getPort("S4");

}
