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

    float ___ergebnis = 0;

    public void run() throws Exception {
        // Grundrechenarten Basics  --START--
        ___ergebnis = 2 + ( ( 3 * 4 ) / ((float) 5) );
        if (!(((float) 4.4) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "POS-1" + ((float) 4.4) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( 2 * ( ( 3 + 4 ) * 5 ) );
        if (!(((float) 74.4) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "POS-2" + ((float) 74.4) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( 2 * ( 3 * ( 4 + 5 ) ) );
        if (!(((float) 128.4) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "POS-3" + ((float) 128.4) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 * 4 ) - 5 ) * 6 ) );
        if (!(((float) 172.4) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "POS-4" + ((float) 172.4) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) * 5 ) * 6 ) );
        if (!(((float) 592.4) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "POS-5" + ((float) 592.4) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( 2 * ( 6 * ( ( 3 + 4 ) * 5 ) ) );
        if (!(((float) 1012.4) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "POS-7" + ((float) 1012.4) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 + 4 ) / ((float) ( 5 - 6 )) ) - ( ( 7 * 8 ) + ( 9 + 10 ) ) ) );
        if (!(((float) 932.4) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "POS-13" + ((float) 932.4) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) + ( 5 * 6 ) ) * ( ( 7 * 8 ) + ( 9 - 10 ) ) ) );
        System.out.println(( ( ___ergebnis == ((float) 5002.4) ) ? "SUCCESS" : "FAIL" ));
        // Grundrechenarten Basics  --END--
    }
}