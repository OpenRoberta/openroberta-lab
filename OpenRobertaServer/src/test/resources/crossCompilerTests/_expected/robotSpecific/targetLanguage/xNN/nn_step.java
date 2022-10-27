package generated.main;

import de.fhg.iais.roberta.runtime.*;
import de.fhg.iais.roberta.runtime.ev3.*;

import de.fhg.iais.roberta.mode.general.*;
import de.fhg.iais.roberta.mode.action.*;
import de.fhg.iais.roberta.mode.sensor.*;
import de.fhg.iais.roberta.mode.action.ev3.*;
import de.fhg.iais.roberta.mode.sensor.ev3.*;

import de.fhg.iais.roberta.components.*;

import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import lejos.remote.nxt.NXTConnection;

public class NEPOprog {
    private static Configuration brickConfiguration;

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S1, SensorType.TOUCH, TouchSensorMode.TOUCH), new UsedSensor(SensorPort.S4, SensorType.ULTRASONIC, UltrasonicSensorMode.DISTANCE), new UsedSensor(SensorPort.S3, SensorType.COLOR, ColorSensorMode.RED), new UsedSensor(SensorPort.S2, SensorType.GYRO, GyroSensorMode.ANGLE)));
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    private float ____i1;
    private float ____i2;
    private float ____o1;
    private float ____o2;
    private float ____b_h1n1 = 1.0f;
    private float ____w_i1_h1n1 = 1f;
    private float ____w_i2_h1n1 = 1f;
    private float ____b_h1n2 = 1.0f;
    private float ____w_i1_h1n2 = -1f;
    private float ____w_i2_h1n2 = 1/1f;
    private float ____b_h1n3 = 0f;
    private float ____w_i1_h1n3 = 0f;
    private float ____w_i2_h1n3 = 1/1f;
    private float ____b_o1 = 0f;
    private float ____w_h1n1_o1 = -1.0f;
    private float ____w_h1n2_o1 = -1/15f;
    private float ____w_h1n3_o1 = -1/15f;
    private float ____b_o2 = 0f;
    private float ____w_h1n1_o2 = -1.0f;
    private float ____w_h1n2_o2 = 1.0f;
    private float ____w_h1n3_o2 = 1.0f;

    private void ____nnStep() {
        float ____h1n1 = ____b_h1n1 + ____i1 * ____w_i1_h1n1 + ____i2 * ____w_i2_h1n1;
        float ____h1n2 = ____b_h1n2 + ____i1 * ____w_i1_h1n2 + ____i2 * ____w_i2_h1n2;
        float ____h1n3 = ____b_h1n3 + ____i1 * ____w_i1_h1n3 + ____i2 * ____w_i2_h1n3;
        ____o1 = ____b_o1 + ____h1n1 * ____w_h1n1_o1 + ____h1n2 * ____w_h1n2_o1 + ____h1n3 * ____w_h1n3_o1;
        ____o2 = ____b_o2 + ____h1n1 * ____w_h1n1_o2 + ____h1n2 * ____w_h1n2_o2 + ____h1n3 * ____w_h1n3_o2;
    }

    public static void main(String[] args) {
        try {
             brickConfiguration = new EV3Configuration.Builder()
                .setWheelDiameter(5.6)
                .setTrackWidth(18.0)
                .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))
                .addSensor(SensorPort.S2, new Sensor(SensorType.GYRO))
                .addSensor(SensorPort.S3, new Sensor(SensorType.COLOR))
                .addSensor(SensorPort.S4, new Sensor(SensorType.ULTRASONIC))
                .build();

            new NEPOprog().run();
        } catch ( Exception e ) {
            Hal.displayExceptionWaitForKeyPress(e);
        }
    }

    float ___x = 0;

    public void run() throws Exception {
        ____i1 = 5;
        ____i2 = ( ( hal.isPressed(SensorPort.S1) ) ? 1 : 2 );
        ____nnStep();
        ___x = ____o1;
        ___x = ____o2;
        ____w_i1_h1n1 = ((float) 555.5);
        ____w_h1n1_o1 = hal.getUltraSonicSensorDistance(SensorPort.S4);
        ____b_h1n1 = hal.getColorSensorRed(SensorPort.S3);
        ____b_o1 = hal.getGyroSensorAngle(SensorPort.S2);
        ___x = ____w_i1_h1n3;
        ___x = ____w_h1n1_o1;
        ___x = ____b_h1n2;
        ___x = ____b_o1;
    }
}