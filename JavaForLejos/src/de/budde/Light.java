package de.budde;

import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.runtime.ev3.Hal;
import de.fhg.iais.roberta.shared.action.ev3.BlinkMode;
import de.fhg.iais.roberta.shared.action.ev3.BrickLedColor;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;

public class Light {
    private static Ev3Configuration brickConfiguration;

    private Set usedSensors = new LinkedHashSet();
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    public static void main(String[] args) {
        try {
            brickConfiguration =
                new Ev3Configuration.Builder()
                    .setWheelDiameter(5.6)
                    .setTrackWidth(18.0)
                    .addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))
                    .build();
            new Light().run();
        } catch ( Exception e ) {
            lejos.hardware.lcd.TextLCD lcd = lejos.hardware.ev3.LocalEV3.get().getTextLCD();
            lcd.clear();
            lcd.drawString("Error in the EV3", 0, 0);
            if ( e.getMessage() != null ) {
                lcd.drawString("Error message:", 0, 2);
                Hal.formatInfoMessage(e.getMessage(), lcd);
            }
            lcd.drawString("Press any key", 0, 7);
            lejos.hardware.Button.waitForAnyPress();
        }
    }

    public void run() throws Exception {
        hal.startLogging();
        lejos.hardware.lcd.TextLCD lcd = lejos.hardware.ev3.LocalEV3.get().getTextLCD();
        lcd.clear();
        lcd.drawString("Hallo, Ihr!", 0, 4);
        hal.ledOn(BrickLedColor.RED, BlinkMode.ON);
        hal.waitFor(1500);
        hal.ledOn(BrickLedColor.ORANGE, BlinkMode.ON);
        hal.waitFor(1500);
    }
}
