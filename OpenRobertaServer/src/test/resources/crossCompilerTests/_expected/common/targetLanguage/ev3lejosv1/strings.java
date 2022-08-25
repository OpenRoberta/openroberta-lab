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

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    public static void main(String[] args) {
        try {
             brickConfiguration = new EV3Configuration.Builder()
                .setWheelDiameter(5.6)
                .setTrackWidth(18.0)
                .build();

            new NEPOprog().run();
        } catch ( Exception e ) {
            Hal.displayExceptionWaitForKeyPress(e);
        }
    }

    String ___text = "start:";
    String ___eight = " eight";
    float ___number = 0;

    public void run() throws Exception {
        // String Concat -- Start --
        ___text += String.valueOf(" one");
        if (!("start: one".equals(___text))) {
            System.out.println("Assertion failed: " + "POS-1" + "start: one" + "EQ" + ___text);
        }
        ___text += String.valueOf(String.valueOf(" two") + String.valueOf(" three"));
        if (!("start: one two three".equals(___text))) {
            System.out.println("Assertion failed: " + "POS-2" + "start: one two three" + "EQ" + ___text);
        }
        ___text += String.valueOf(String.valueOf(4) + String.valueOf(5));
        if (!("start: one two three45".equals(___text))) {
            System.out.println("Assertion failed: " + "POS-3" + "start: one two three45" + "EQ" + ___text);
        }
        ___text += String.valueOf(String.valueOf(6) + String.valueOf(" seven"));
        if (!("start: one two three456 seven".equals(___text))) {
            System.out.println("Assertion failed: " + "POS-4" + "start: one two three456 seven" + "EQ" + ___text);
        }
        ___text = String.valueOf(String.valueOf(___text) + String.valueOf(___eight)) + String.valueOf(" nine");
        if (!("start: one two three456 seven eight nine".equals(___text))) {
            System.out.println("Assertion failed: " + "POS-5" + "start: one two three456 seven eight nine" + "EQ" + ___text);
        }
        ___text = String.valueOf(String.valueOf(___text) + String.valueOf("ten")) + String.valueOf(String.valueOf(" eleven") + String.valueOf(" twelve"));
        System.out.println(( ( "start: one two three456 seven eight nine ten eleven twelve".equals(___text) ) ? "String Concat SUCCESS" : "String Concat FAIL" ));
        ___number = Float.parseFloat(___text);
        ___number = (int)(___text.charAt(0));
        ___text = (String.valueOf(10));
        ___text = String.valueOf((char)(int)(30));
        // String Concat -- End --
    }

}