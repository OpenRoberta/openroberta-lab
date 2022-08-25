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

    float ___r1 = 0;
    float ___r2 = 0;
    boolean ___b1 = true;
    float ___r3 = 0;
    boolean ___sim = true;

    public void run() throws Exception {
        ___r1 = (float) Math.sqrt(( 20 - ( 2 * ( 4 / ((float) 2) ) ) ) + (float) Math.pow(3, 2));
        ___b1 = ___b1 && ! (___r1 % 2 == 0);
        ___b1 = ___b1 && (___r1 % 2 == 1);
        ___b1 = ___b1 && _isPrime( (int) ___r1);
        ___b1 = ___b1 && (___r1 % 1 == 0);
        ___b1 = ___b1 && (___r1 > 0);
        ___b1 = ___b1 && ! (___r1 < 0);
        ___b1 = ___b1 && (___r1 % 5 == 0);
        ___b1 = ___b1 && ! (___r1 % 3 == 0);
        ___r1 += 1;
        ___b1 = ___b1 && (___r1 % 2 == 0);
        ___r2 = (float) Math.sqrt(20);
        ___b1 = ___b1 && ! (___r2 % 1 == 0);
        ___b1 = ___b1 && ( (float) Math.round(___r2) == 4 );
        ___b1 = ___b1 && ( (float) Math.ceil(___r2) == 5 );
        ___b1 = ___b1 && ( (float) Math.floor(___r2) == 4 );
        ___b1 = ___b1 && ( ___r1 > ___r2 );
        ___b1 = ___b1 && ( ___r1 >= ___r2 );
        ___b1 = ___b1;
        ___b1 = ( ___b1 && ( ___r2 < ___r1 ) ) && ( ___r1 <= ___r1 );
        ___b1 = ___b1 && ( ( ___r1 % 4 ) == 2 );
        ___b1 = ___b1 && ( 29 == ( Math.min(Math.max((float) Math.pow(3, 2), 1), 20) + ( Math.min(Math.max(9, 3 * 4), 18) + Math.min(Math.max(3 * 3, 5), 8) ) ) );
        ___b1 = ___b1 && ( 11 > ( (float) Math.random() * ( Math.round(Math.random() * (10 - 1)) + 1 ) ) );
        // if b1 is true, the test succeeded, otherwise it failed :-)
    }

    private boolean _isPrime(int n) {
        if (n == 2) return true;
        if (n % 2 == 0 || n == 1) return false;
        for (int i = 2; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}