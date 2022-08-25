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

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S1, SensorType.TOUCH, TouchSensorMode.TOUCH), new UsedSensor(SensorPort.S4, SensorType.ULTRASONIC, UltrasonicSensorMode.DISTANCE), new UsedSensor(SensorPort.S4, SensorType.ULTRASONIC, UltrasonicSensorMode.PRESENCE), new UsedSensor(SensorPort.S3, SensorType.COLOR, ColorSensorMode.COLOUR), new UsedSensor(SensorPort.S3, SensorType.COLOR, ColorSensorMode.RED), new UsedSensor(SensorPort.S3, SensorType.COLOR, ColorSensorMode.AMBIENTLIGHT), new UsedSensor(SensorPort.S3, SensorType.COLOR, ColorSensorMode.RGB), new UsedSensor(SensorPort.S2, SensorType.GYRO, GyroSensorMode.ANGLE), new UsedSensor(SensorPort.S2, SensorType.GYRO, GyroSensorMode.RATE)));
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    private void ____sensors() {
        hal.drawText(String.valueOf(hal.isPressed(SensorPort.S1)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getUltraSonicSensorDistance(SensorPort.S4)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getUltraSonicSensorPresence(SensorPort.S4)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getColorSensorColour(SensorPort.S3)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getColorSensorRed(SensorPort.S3)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getColorSensorAmbient(SensorPort.S3)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getColorSensorRgb(SensorPort.S3)), ___numberVar, ___numberVar);
        hal.resetRegulatedMotorTacho(ActorPort.B);
        hal.drawText(String.valueOf(hal.getRegulatedMotorTachoValue(ActorPort.B, MotorTachoMode.DEGREE)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getRegulatedMotorTachoValue(ActorPort.B, MotorTachoMode.ROTATION)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getRegulatedMotorTachoValue(ActorPort.B, MotorTachoMode.DISTANCE)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.isPressed(BrickKey.ENTER)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.isPressed(BrickKey.UP)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.isPressed(BrickKey.DOWN)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.isPressed(BrickKey.LEFT)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.isPressed(BrickKey.RIGHT)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.isPressed(BrickKey.ESCAPE)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.isPressed(BrickKey.ANY)), ___numberVar, ___numberVar);
        hal.resetGyroSensor(SensorPort.S2);
        hal.drawText(String.valueOf(hal.getGyroSensorAngle(SensorPort.S2)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getGyroSensorRate(SensorPort.S2)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getTimerValue(1)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getTimerValue(2)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getTimerValue(3)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getTimerValue(4)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getTimerValue(5)), ___numberVar, ___numberVar);
        hal.resetTimer(1);
        hal.resetTimer(2);
        hal.resetTimer(3);
        hal.resetTimer(4);
        hal.resetTimer(5);
    }

    private void ____waitUntil() {
        while ( true ) {
            if ( hal.isPressed(SensorPort.S1) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getUltraSonicSensorDistance(SensorPort.S4) < 30 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getUltraSonicSensorPresence(SensorPort.S4) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getColorSensorColour(SensorPort.S3) == PickColor.RED ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getColorSensorRed(SensorPort.S3) < 50 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getColorSensorAmbient(SensorPort.S3) < 50 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getRegulatedMotorTachoValue(ActorPort.B, MotorTachoMode.DEGREE) > 180 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getRegulatedMotorTachoValue(ActorPort.B, MotorTachoMode.ROTATION) > 2 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getRegulatedMotorTachoValue(ActorPort.B, MotorTachoMode.DISTANCE) < 30 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.isPressed(BrickKey.ENTER) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.isPressed(BrickKey.UP) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.isPressed(BrickKey.DOWN) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.isPressed(BrickKey.LEFT) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.isPressed(BrickKey.RIGHT) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.isPressed(BrickKey.ESCAPE) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.isPressed(BrickKey.ANY) == true ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getTimerValue(1) > 500 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getTimerValue(2) > 500 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getTimerValue(3) > 500 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getTimerValue(4) > 500 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getTimerValue(5) > 500 ) {
                break;
            }
            hal.waitFor(15);
        }
    }

    public static void main(String[] args) {
        try {
             brickConfiguration = new EV3Configuration.Builder()
                .setWheelDiameter(5.6)
                .setTrackWidth(18.0)
                .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
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

    float ___numberVar = 0;
    boolean ___booleanVar = true;
    String ___stringVar = "";
    PickColor ___colourVar = PickColor.WHITE;
    NXTConnection ___connectionVar = null;
    ArrayList<Float> ___numberList = new ArrayList<>(Arrays.asList((float) 0, (float) 0));
    ArrayList<Boolean> ___booleanList = new ArrayList<>(Arrays.<Boolean>asList(true, true));
    ArrayList<String> ___stringList = new ArrayList<>(Arrays.<String>asList("", ""));
    ArrayList<PickColor> ___colourList = new ArrayList<>(Arrays.<PickColor>asList(PickColor.WHITE, PickColor.WHITE));
    ArrayList<NXTConnection> ___connectionList = new ArrayList<>(Arrays.<NXTConnection>asList(___connectionVar, ___connectionVar));

    public void run() throws Exception {
        hal.startLogging();
        ____sensors();
        hal.closeResources();

    }
}