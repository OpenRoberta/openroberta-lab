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
        // Control Flow Nested Loop --Start
        if ( true ) {
            while ( true ) {
                while ( ! (___X >= 20) ) {
                    for ( float ___i = 1; ___i< 11; ___i+= 1 ) {
                        for ( float ___k0 = 0; ___k0< 2; ___k0+= 1 ) {
                            if ( (___i % 2 == 0) ) {
                                continue;
                            }
                            ___X += 1;
                        }
                    }
                }
                break;
            }
        }
        if (!(20 == ___X)) {
            System.out.println("Assertion failed: " + "pos-1" + 20 + "EQ" + ___X);
        }
        for ( float ___j = 1; ___j< 4; ___j+= 3 ) {
            ___X += 1;
            if (!(21 == ___X)) {
                System.out.println("Assertion failed: " + " = X" + 21 + "EQ" + ___X);
            }
            for ( float ___k = 1; ___k< 5; ___k+= 3 ) {
                ___X += 1;
            }
        }
        if (!(23 == ___X)) {
            System.out.println("Assertion failed: " + "pos-2" + 23 + "EQ" + ___X);
        }
        if ( true ) {
            while ( true ) {
                if ( true ) {
                    while ( true ) {
                        if ( 23 == ___X ) {
                            if ( true ) {
                                while ( true ) {
                                    if ( (___X % 2 == 0) ) {
                                        continue;
                                    }
                                    ___X += 1;
                                    break;
                                }
                            }
                            if ( (___X % 2 == 0) ) {
                                break;
                            }
                            ___X += 1000;
                        }
                        break;
                    }
                }
                break;
            }
        }
        System.out.println(( ( 24 == ___X ) ? "Control Flow Nested Loops Test:success" : "ntrol Flow Nested Loops Test: FAIL" ));
        if ( true ) {
            loop10:
            while ( true ) {
                while ( true ) {
                    if ( true ) {
                        if (true) break loop10;
                        break;
                    }
                    if ( true ) {
                        break;
                    }
                    hal.waitFor(15);
                }
            }
        }
        // Control Flow Nested Loop -- End
    }

}