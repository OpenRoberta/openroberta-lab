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

    boolean ___result = false;

    public void run() throws Exception {
        // Math power -- Start --
        ___result = 1 == (float) Math.pow(2, 0);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-1" + true + "EQ" + ___result);
        }
        ___result = 2 == (float) Math.pow(2, 1);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-2" + true + "EQ" + ___result);
        }
        ___result = 4 == (float) Math.pow(2, 2);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-3" + true + "EQ" + ___result);
        }
        ___result = 8 == (float) Math.pow(2, 3);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-4" + true + "EQ" + ___result);
        }
        ___result = -4 == ( - ((float) Math.pow(2, 2)) );
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-5" + true + "EQ" + ___result);
        }
        ___result = 4 == (float) Math.pow(-2, 2);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-6" + true + "EQ" + ___result);
        }
        ___result = ( (float) Math.pow(2, 2) * (float) Math.pow(2, 3) ) == (float) Math.pow(2, 2 + 3);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-7" + true + "EQ" + ___result);
        }
        ___result = ( (float) Math.pow(2, 2) * (float) Math.pow(3, 2) ) == (float) Math.pow(2 * 3, 2);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-8" + true + "EQ" + ___result);
        }
        ___result = (float) Math.pow((float) Math.pow(2, 2), 3) == (float) Math.pow(2, 2 * 3);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-9" + true + "EQ" + ___result);
        }
        ___result = ( (float) Math.pow(2, 2) / ((float) (float) Math.pow(3, 2)) ) == (float) Math.pow(2 / ((float) 3), 2);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-10" + true + "EQ" + ___result);
        }
        ___result = ( (float) Math.pow(2, 2) / ((float) (float) Math.pow(2, 3)) ) == (float) Math.pow(2, 2 - 3);
        if (!(true == ___result)) {
            System.out.println("Assertion failed: " + "pos-11" + true + "EQ" + ___result);
        }
        System.out.println(( ( true == ___result ) ? "Math Power Test: success" : "Basic Math Test: FAIL" ));
        // Math power -- End --
    }

}