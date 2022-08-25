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

    public void run() throws Exception {
        // Logic Boolean Operators -- Start
        if ( true && true ) {
            ___x += 1;
        }
        if ( true && false ) {
            ___x += 1000;
        }
        if ( false && true ) {
            ___x += 1000;
        }
        if ( false && false ) {
            ___x += 1000;
        }
        if (!(1 == ___x)) {
            System.out.println("Assertion failed: " + "pos-1" + 1 + "EQ" + ___x);
        }
        if ( ! (true && true) ) {
            ___x += 1000;
        }
        if ( ! (true && false) ) {
            ___x += 1;
        }
        if ( ! (false && true) ) {
            ___x += 1;
        }
        if ( ! (false && false) ) {
            ___x += 1;
        }
        if (!(4 == ___x)) {
            System.out.println("Assertion failed: " + "pos-2" + 4 + "EQ" + ___x);
        }
        if ( true || true ) {
            ___x += 1;
        }
        if ( true || false ) {
            ___x += 1;
        }
        if ( false || true ) {
            ___x += 1;
        }
        if ( false || false ) {
            ___x += 1000;
        }
        if (!(7 == ___x)) {
            System.out.println("Assertion failed: " + "pos-3" + 7 + "EQ" + ___x);
        }
        if ( ! (true || true) ) {
            ___x += 1000;
        }
        if ( ! (true || false) ) {
            ___x += 1000;
        }
        if ( ! (false || true) ) {
            ___x += 1000;
        }
        if ( ! (false || false) ) {
            ___x += 1;
        }
        if (!(8 == ___x)) {
            System.out.println("Assertion failed: " + "pos-4" + 8 + "EQ" + ___x);
        }
        if ( ( true && true ) && ( true && true ) ) {
            ___x += 1;
        }
        if ( ( true && false ) || ( false && true ) ) {
            ___x += 1000;
        }
        if ( ! (true || true) && ! (true || true) ) {
            ___x += 1000;
        }
        if ( ! (true && false) || ! (true && false) ) {
            ___x += 1;
        }
        System.out.println(( ( 10 == ___x ) ? "Logic Boolean operators Test: success" : "Logic Boolean operators Test: FAIL" ));
        // Logic Boolean Operators -- End
    }
}