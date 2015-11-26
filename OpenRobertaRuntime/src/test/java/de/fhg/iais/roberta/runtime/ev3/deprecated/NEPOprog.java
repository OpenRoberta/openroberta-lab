package de.fhg.iais.roberta.runtime.ev3.deprecated;

import de.fhg.iais.roberta.runtime.*;
import de.fhg.iais.roberta.runtime.ev3.*;

import de.fhg.iais.roberta.shared.*;
import de.fhg.iais.roberta.shared.action.ev3.*;
import de.fhg.iais.roberta.shared.sensor.ev3.*;

import de.fhg.iais.roberta.components.*;
import de.fhg.iais.roberta.components.ev3.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import lejos.remote.nxt.NXTConnection;

public class NEPOprog {
    private static final boolean TRUE = true;
    private static Ev3Configuration brickConfiguration;

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S4, EV3Sensors.EV3_ULTRASONIC_SENSOR, UltrasonicSensorMode.DISTANCE), new UsedSensor(SensorPort.S3, EV3Sensors.EV3_COLOR_SENSOR, ColorSensorMode.COLOUR)));

    private Hal hal = new Hal(brickConfiguration, usedSensors);

    public static void main(String[] args) {
        try {
             brickConfiguration = new Ev3Configuration.Builder()
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
            if (e.getMessage() != null) {
                lcd.drawString("Error message:", 0, 2);
                Hal.formatInfoMessage(e.getMessage(), lcd);
            }
            lcd.drawString("Press any key", 0, 7);
            lejos.hardware.Button.waitForAnyPress();
        }
    }


    public void run() {
        hal.startServerLoggingThread();
        hal.drawText(String.valueOf(hal.getUltraSonicSensorDistance(SensorPort.S4)), 0, 0);
        hal.drawText(String.valueOf(hal.getColorSensorColour(SensorPort.S3)), 0, 0);
    }
}
