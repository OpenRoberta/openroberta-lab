package de.fhg.iais.roberta.runtime.ev3.deprecated;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.utility.Delay;

public class SensorTest {
    static Brick brick;
    static Port[] p = new Port[4];
    private static boolean uDetected = false;

    public static void main(String[] args) {

        brick = BrickFinder.getDefault();
        GraphicsLCD g = brick.getGraphicsLCD();
        Key enter = brick.getKey("Escape");
        String[] sensorName = new String[4];
        p[0] = brick.getPort("S1");
        p[1] = brick.getPort("S2");
        p[2] = brick.getPort("S3");
        p[3] = brick.getPort("S4");

        while ( enter.isUp() ) {
            uDetected = false;
            for ( int i = 0; i < sensorName.length; i++ ) {
                sensorName[i] = getSensorName(i);
            }
            g.clear();
            g.drawString("ESCAPE to quit", 0, 100, 0, true);
            g.setFont(Font.getSmallFont());
            g.drawString("B.", 165, 105, 0);
            g.setFont(Font.getDefaultFont());
            for ( int i = 0; i < sensorName.length; i++ ) {
                g.drawString("Port " + (i + 1) + ": " + sensorName[i], 0, i * 20 + 20, 0);
            }
            if ( !uDetected ) {
                Delay.msDelay(100);
            }
        }
    }

    private static String getSensorName(int i) {
        try {
            AnalogPort aP = p[i].open(AnalogPort.class);
            float v1 = aP.getPin1();
            aP.close();
            if ( v1 > 4.5 ) {
                return "NO";
            } else if ( v1 > 0.3 && v1 < 0.5 ) {
                return "TOUCH";
            } else {
                // TODO: test Pin6 for NXT sensors
            }
        } catch ( Exception e ) {
            // TODO: nothing
        }
        try {
            UARTPort uP = p[i].open(UARTPort.class);
            uP.resetSensor();
            uP.initialiseSensor(0);
            String name = uP.getModeName(0);
            uP.close();
            uDetected = true;
            return name;
        } catch ( Exception e ) {
            return "I2C?";
            // TODO: test I2c port for NXT Sensors
        }
    }
}
