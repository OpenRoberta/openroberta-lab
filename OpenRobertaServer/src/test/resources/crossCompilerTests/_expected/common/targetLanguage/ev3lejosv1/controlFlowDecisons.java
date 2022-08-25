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

    float ___X = 0;

    public void run() throws Exception {
        if (!(0 == ___X)) {
            System.out.println("Assertion failed: " + "pos-0" + 0 + "EQ" + ___X);
        }
        if ( true ) {
            ___X += 1;
        }
        if (!(1 == ___X)) {
            System.out.println("Assertion failed: " + "pos-1" + 1 + "EQ" + ___X);
        }
        if ( false ) {
            ___X += 1000;
        }
        if (!(1 == ___X)) {
            System.out.println("Assertion failed: " + "pos-2" + 1 + "EQ" + ___X);
        }
        if ( true ) {
            if ( true ) {
                ___X += 1;
            }
            ___X += 1;
        }
        if (!(3 == ___X)) {
            System.out.println("Assertion failed: " + "pos-3" + 3 + "EQ" + ___X);
        }
        if ( true ) {
            if ( false ) {
                ___X += 1000;
            }
            ___X += 1;
        }
        if (!(4 == ___X)) {
            System.out.println("Assertion failed: " + "pos-4" + 4 + "EQ" + ___X);
        }
        if ( false ) {
            if ( false ) {
                ___X += 1000;
            }
            ___X += 1000;
        }
        if (!(4 == ___X)) {
            System.out.println("Assertion failed: " + "pos-5" + 4 + "EQ" + ___X);
        }
        if ( false ) {
            if ( true ) {
                ___X += 1000;
            }
            ___X += 1000;
        }
        if (!(4 == ___X)) {
            System.out.println("Assertion failed: " + "pos-6" + 4 + "EQ" + ___X);
        }
        if ( true ) {
            if ( true ) {
                if ( false ) {
                    ___X += 1000;
                }
                ___X += 1;
            }
            ___X += 1;
        }
        if (!(6 == ___X)) {
            System.out.println("Assertion failed: " + "pos-7" + 6 + "EQ" + ___X);
        }
        if ( true ) {
            ___X += 1;
        } else if ( false ) {
            ___X += 1000;
        }
        if (!(7 == ___X)) {
            System.out.println("Assertion failed: " + "pos-8" + 7 + "EQ" + ___X);
        }
        if ( false ) {
            ___X += 1000;
        } else if ( true ) {
            ___X += 1;
        }
        if (!(8 == ___X)) {
            System.out.println("Assertion failed: " + "pos-9" + 8 + "EQ" + ___X);
        }
        if ( true ) {
            ___X += 1;
        } else {
            ___X += 1000;
        }
        if (!(9 == ___X)) {
            System.out.println("Assertion failed: " + "pos-10" + 9 + "EQ" + ___X);
        }
        if ( false ) {
            ___X += 1000;
        } else {
            ___X += 1;
        }
        if (!(10 == ___X)) {
            System.out.println("Assertion failed: " + "pos-11" + 10 + "EQ" + ___X);
        }
        if ( true ) {
            ___X += 1;
        } else if ( true ) {
            ___X += 1000;
        } else {
            ___X += 1000;
        }
        if (!(11 == ___X)) {
            System.out.println("Assertion failed: " + "pos-12" + 11 + "EQ" + ___X);
        }
        if ( false ) {
            ___X += 1000;
        } else if ( false ) {
            ___X += 1000;
        } else {
            ___X += 1;
        }
        if (!(12 == ___X)) {
            System.out.println("Assertion failed: " + "pos-13" + 12 + "EQ" + ___X);
        }
        if ( true ) {
            ___X += 1;
        } else if ( false ) {
            ___X += 1000;
        } else {
            ___X += 1000;
        }
        if (!(14 == ___X)) {
            System.out.println("Assertion failed: " + "pos-14" + 14 + "EQ" + ___X);
        }
        if ( false ) {
            ___X += 1000;
        } else if ( true ) {
            ___X += 1;
        } else {
            ___X += 1000;
        }
        if (!(14 == ___X)) {
            System.out.println("Assertion failed: " + "pos-15" + 14 + "EQ" + ___X);
        }
        System.out.println(( ( 14 == ___X ) ? "Control Flow Test: success" : "Control Flow Test: FAIL" ));
    }
}