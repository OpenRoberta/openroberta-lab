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
        // Math basics START
        ___ergebnis = ___ergebnis + 1;
        ___ergebnis = ___ergebnis - 3;
        ___ergebnis = ___ergebnis * -1;
        ___ergebnis = ___ergebnis / ((float) 2);
        if (!(1 == ___ergebnis)) {
            System.out.println("Assertion failed: " + "pos-1" + 1 + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis + ( ((float) 0.1) - ((float) 0.1) );
        ___ergebnis = ___ergebnis + ( 5 * 2 );
        ___ergebnis = ___ergebnis + ( 3 / ((float) 2) );
        if (!(((float) 12.5) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "pos-2" + ((float) 12.5) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis * ( 1 + 2 );
        ___ergebnis = ___ergebnis * ( 1 - 2 );
        ___ergebnis = ___ergebnis * ( 1 / ((float) 2) );
        if (!(((float) -18.75) == ___ergebnis)) {
            System.out.println("Assertion failed: " + "pos-3" + ((float) -18.75) + "EQ" + ___ergebnis);
        }
        ___ergebnis = ___ergebnis / ((float) ( ((float) 0.1) + ((float) 0.1) ));
        ___ergebnis = ___ergebnis / ((float) ( ((float) 0.1) - ((float) 0.2) ));
        ___ergebnis = ___ergebnis / ((float) ( ((float) 0.1) * ((float) 0.1) ));
        if (!(((float) 1e-7) > (float) Math.abs(93750 - ___ergebnis))) {
            System.out.println("Assertion failed: " + "pos-4" + ((float) 1e-7) + "GT" + (float) Math.abs(93750 - ___ergebnis));
        }
        ___ergebnis = ___ergebnis - ( ((float) 1.535345) + ((float) 0.999999999999999) );
        ___ergebnis = ___ergebnis - ( ((float) 0.1111111111111111) + ((float) 0.9999999999999999) );
        ___ergebnis = ___ergebnis - ( 435 + ((float) 0.14543) );
        if (!(((float) 1e-7) > (float) Math.abs(((float) 93311.208113889) - ___ergebnis))) {
            System.out.println("Assertion failed: " + "pos-5" + ((float) 1e-7) + "GT" + (float) Math.abs(((float) 93311.208113889) - ___ergebnis));
        }
        // Math basics END
    }

}