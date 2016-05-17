package generated.main;

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

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();

    private Hal hal = new Hal(brickConfiguration, usedSensors);

    public static void main(String[] args) {
        try {
             brickConfiguration = new Ev3Configuration.Builder()
                .setWheelDiameter(5.6)
                .setTrackWidth(18.0)
                .addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))
                .addSensor(SensorPort.S2, new EV3Sensor(EV3Sensors.EV3_GYRO_SENSOR))
                .addSensor(SensorPort.S3, new EV3Sensor(EV3Sensors.EV3_COLOR_SENSOR))
                .addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))
                .build();
            new NEPOprog().run();
        } catch ( Exception e ) {
            Hal.displayExceptionWaitForKeyPress(e);
        }
    }


    public void run() throws Exception {
        hal.startLogging();
        hal.driveDistance(DriveDirection.FOREWARD, 30, 20);
        hal.closeResources();
    }
}
