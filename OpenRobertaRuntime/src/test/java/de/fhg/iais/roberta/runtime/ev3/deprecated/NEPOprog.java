package de.fhg.iais.roberta.runtime.ev3.deprecated;

import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Actors;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.components.ev3.UsedSensor;
import de.fhg.iais.roberta.runtime.ev3.Hal;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.action.ev3.DriveDirection;
import de.fhg.iais.roberta.shared.action.ev3.MotorSide;
import de.fhg.iais.roberta.shared.action.ev3.ShowPicture;
import de.fhg.iais.roberta.shared.sensor.ev3.BrickKey;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;

public class NEPOprog {
    private static final boolean TRUE = true;
    private static Ev3Configuration brickConfiguration;

    private final Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();

    private final Hal hal = new Hal(brickConfiguration, this.usedSensors);

    public static void main(String[] args) {
        try {
            brickConfiguration =
                new Ev3Configuration.Builder()
                    .setWheelDiameter(5.6)
                    .setTrackWidth(12.0)
                    .addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                    .addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                    .addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))
                    .addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))
                    .build();
            new NEPOprog().run();
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

        this.hal.drawPicture(ShowPicture.OLDGLASSES, 0, 0);
        if ( TRUE ) {
            while ( true ) {
                if ( this.hal.isPressed(BrickKey.ENTER) == true ) {
                    break;
                }
                this.hal.waitFor(15);
            }
        }
        this.hal.closeResources();
    }
}
