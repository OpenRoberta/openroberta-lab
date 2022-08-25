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

    float ___x = 0;
    float ___y = 1;

    public void run() throws Exception {
        // Control Flow Loop -- Start
        if (!(0 == ___x)) {
            System.out.println("Assertion failed: " + "pos-0" + 0 + "EQ" + ___x);
        }
        for ( float ___k0 = 0; ___k0< 5; ___k0+= 1 ) {
            ___x = ___x + 1;
        }
        if (!(5 == ___x)) {
            System.out.println("Assertion failed: " + "pos-1" + 5 + "EQ" + ___x);
        }
        while ( ! (___x == 10) ) {
            ___x = ___x + 1;
        }
        if (!(10 == ___x)) {
            System.out.println("Assertion failed: " + "pos-2" + 10 + "EQ" + ___x);
        }
        while ( ___x < 15 ) {
            ___x = ___x + 1;
        }
        if (!(15 == ___x)) {
            System.out.println("Assertion failed: " + "pos-3" + 15 + "EQ" + ___x);
        }
        for ( float ___i = 1; ___i< 6; ___i+= 1 ) {
            ___x = ___x + 1;
        }
        if (!(20 == ___x)) {
            System.out.println("Assertion failed: " + "pos-4" + 20 + "EQ" + ___x);
        }
        for ( float ___j = 2; ___j< 5; ___j+= 3 ) {
            ___x = ___x + 1;
        }
        if (!(21 == ___x)) {
            System.out.println("Assertion failed: " + "pos-5" + 21 + "EQ" + ___x);
        }
        for ( float ___k = 2; ___k< 6; ___k+= 3 ) {
            ___x = ___x + 1;
        }
        if (!(23 == ___x)) {
            System.out.println("Assertion failed: " + "pos-6" + 23 + "EQ" + ___x);
        }
        for ( float ___o = 2; ___o< 7; ___o+= 3 ) {
            ___x = ___x + 1;
        }
        if (!(25 == ___x)) {
            System.out.println("Assertion failed: " + "pos-7" + 25 + "EQ" + ___x);
        }
        for ( float ___p = 10; ___p> 9; ___p+= -1 ) {
            ___x = ___x + 1;
        }
        if (!(25 == ___x)) {
            System.out.println("Assertion failed: " + "pos-8" + 25 + "EQ" + ___x);
        }
        for ( float ___m = 1; ___m< 5; ___m+= ___y ) {
            ___y = ___y + 1;
            ___x = ___x + 1;
        }
        if (!(27 == ___x)) {
            System.out.println("Assertion failed: " + "pos-9" + 27 + "EQ" + ___x);
        }
        if ( true ) {
            while ( true ) {
                if ( ___x < 30 ) {
                    ___x = ___x + 1;
                    if ( true ) {
                        continue;
                    }
                    ___x = ___x + 1000;
                } else if ( ___x >= 30 ) {
                    break;
                }
            }
        }
        if (!(30 == ___x)) {
            System.out.println("Assertion failed: " + "pos-10" + 30 + "EQ" + ___x);
        }
        System.out.println(( ( ___x == 30 ) ? "Control Flow Loops: success" : "Control Flow Loops: FAIL" ));
        // Control Flow Loop -- End
    }
}