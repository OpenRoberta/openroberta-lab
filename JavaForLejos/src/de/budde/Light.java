package de.budde;

import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.runtime.ev3.Hal;
import de.fhg.iais.roberta.shared.action.BlinkMode;
import de.fhg.iais.roberta.shared.action.BrickLedColor;
import de.fhg.iais.roberta.shared.sensor.SensorPort;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

public class Light {
    private static Configuration brickConfiguration;

    private Set usedSensors = new LinkedHashSet();
    private Hal hal = new Hal(brickConfiguration, this.usedSensors);

    public static void main(String[] args) {
        try {
            brickConfiguration =
                new Configuration.Builder().setWheelDiameter(5.6).setTrackWidth(18.0).addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH)).build();
            new Light().run();
        } catch ( Exception e ) {
            TextLCD lcd = lejos.hardware.ev3.LocalEV3.get().getTextLCD();
            lcd.clear();
            lcd.drawString("Error in the EV3", 0, 0);
            if ( e.getMessage() != null ) {
                lcd.drawString("Error message:", 0, 2);
                Hal.formatInfoMessage(e.getMessage(), lcd);
            }
            lcd.drawString("Press any key", 0, 7);
            Button.waitForAnyPress();
        }
    }

    public void run() throws Exception {
        this.hal.startLogging();
        TextLCD lcd = LocalEV3.get().getTextLCD();
        lcd.clear();
        lcd.drawString("Hallo, Ihr!", 0, 4);
        this.hal.ledOn(BrickLedColor.RED, BlinkMode.ON);
        this.hal.waitFor(1500);
        this.hal.ledOn(BrickLedColor.ORANGE, BlinkMode.ON);
        this.hal.waitFor(1500);
    }
}
