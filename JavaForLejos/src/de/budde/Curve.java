package de.budde;

import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.EV3Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.ev3.ActorPort;
import de.fhg.iais.roberta.mode.action.ev3.DriveDirection;
import de.fhg.iais.roberta.mode.action.ev3.MotorSide;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.runtime.ev3.Hal;

public class Curve {
    private static Configuration brickConfiguration;

    private Set<UsedSensor> usedSensors = new LinkedHashSet<>();

    private Hal hal = new Hal(brickConfiguration, this.usedSensors);

    public static void main(String[] args) {
        try {
            brickConfiguration =
                new EV3Configuration.Builder()
                    .setWheelDiameter(5.6)
                    .setTrackWidth(18.0)
                    .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                    .addActor(ActorPort.C, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                    .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))
                    .addSensor(SensorPort.S2, new Sensor(SensorType.GYRO))
                    .addSensor(SensorPort.S3, new Sensor(SensorType.COLOR))
                    .addSensor(SensorPort.S4, new Sensor(SensorType.ULTRASONIC))
                    .build();
            new Curve().run();
        } catch ( Exception e ) {
            Hal.displayExceptionWaitForKeyPress(e);
        }
    }

    public void run() throws Exception {
        this.hal.startLogging();
        this.hal.driveInCurve(DriveDirection.FOREWARD, 30, 50);
        this.hal.driveDistance(DriveDirection.FOREWARD, 30, 20);
        this.hal.closeResources();
    }

}