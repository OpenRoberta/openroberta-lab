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

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S2, SensorType.COMPASS, CompassSensorMode.CALIBRATE), new UsedSensor(SensorPort.S2, SensorType.COMPASS, CompassSensorMode.ANGLE), new UsedSensor(SensorPort.S2, SensorType.COMPASS, CompassSensorMode.COMPASS), new UsedSensor(SensorPort.S4, SensorType.IRSEEKER, IRSeekerSensorMode.MODULATED), new UsedSensor(SensorPort.S4, SensorType.IRSEEKER, IRSeekerSensorMode.UNMODULATED), new UsedSensor(SensorPort.S3, SensorType.HT_COLOR, HiTecColorSensorV2Mode.COLOUR), new UsedSensor(SensorPort.S3, SensorType.HT_COLOR, HiTecColorSensorV2Mode.LIGHT), new UsedSensor(SensorPort.S3, SensorType.HT_COLOR, HiTecColorSensorV2Mode.AMBIENTLIGHT), new UsedSensor(SensorPort.S3, SensorType.HT_COLOR, HiTecColorSensorV2Mode.RGB)));
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    private void ____sensors() {
        hal.hiTecCompassStartCalibration(SensorPort.S2);
        hal.waitFor(40000);
        hal.hiTecCompassStopCalibration(SensorPort.S2);
        hal.drawText(String.valueOf(hal.getHiTecCompassAngle(SensorPort.S2)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getHiTecCompassCompass(SensorPort.S2)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getHiTecIRSeekerModulated(SensorPort.S4)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getHiTecIRSeekerUnmodulated(SensorPort.S4)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getHiTecColorSensorV2Colour(SensorPort.S3)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getHiTecColorSensorV2Light(SensorPort.S3)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getHiTecColorSensorV2Ambient(SensorPort.S3)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getHiTecColorSensorV2Rgb(SensorPort.S3)), ___numberVar, ___numberVar);
    }

    private void ____waitUntil() {
        while ( true ) {
            if ( hal.getHiTecCompassAngle(SensorPort.S2) < 30 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getHiTecCompassCompass(SensorPort.S2) < 30 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getHiTecIRSeekerModulated(SensorPort.S4) < 30 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getHiTecIRSeekerUnmodulated(SensorPort.S4) < 30 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getHiTecColorSensorV2Colour(SensorPort.S3) == PickColor.RED ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getHiTecColorSensorV2Light(SensorPort.S3) < 50 ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( hal.getHiTecColorSensorV2Ambient(SensorPort.S3) < 50 ) {
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
                .addSensor(SensorPort.S2, new Sensor(SensorType.COMPASS))
                .addSensor(SensorPort.S3, new Sensor(SensorType.HT_COLOR))
                .addSensor(SensorPort.S4, new Sensor(SensorType.IRSEEKER))
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