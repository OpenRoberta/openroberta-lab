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

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S4, SensorType.INFRARED, InfraredSensorMode.DISTANCE), new UsedSensor(SensorPort.S4, SensorType.INFRARED, InfraredSensorMode.SEEK), new UsedSensor(SensorPort.S2, SensorType.SOUND, SoundSensorMode.SOUND)));
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    private void ____sensors() {
        hal.drawText(String.valueOf(hal.getInfraredSensorDistance(SensorPort.S4)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getInfraredSensorSeek(SensorPort.S4)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.getSoundLevel(SensorPort.S2)), ___numberVar, ___numberVar);
    }

    private void ____waitUntil() {
        while ( true ) {
            if ( hal.getInfraredSensorDistance(SensorPort.S4) < 30 ) {
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
                .addSensor(SensorPort.S4, new Sensor(SensorType.INFRARED))
                .addSensor(SensorPort.S2, new Sensor(SensorType.SOUND))
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