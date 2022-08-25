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
        ___r3 = ( ( ___sim ) ? (float) Math.PI / ((float) 2) : 90 );
        ___b1 = ___b1 && ( (float) Math.sin(___r3) == 1 );
        ___b1 = ___b1 && ( (float) Math.cos(0) == 1 );
        ___b1 = ___b1 && ( (float) Math.tan(0) == 0 );
        ___b1 = ___b1 && ( (float) Math.asin(1) == ___r3 );
        ___b1 = ___b1 && ( (float) Math.acos(1) == 0 );
        ___b1 = ___b1 && ( (float) Math.atan(0) == 0 );
        ___b1 = ___b1 && ( ( (float) Math.E > ((float) 2.6) ) && ( (float) Math.E < ((float) 2.8) ) );
        ___b1 = ___b1 && ( ( ( (float) Math.sqrt(2) * (float) Math.sqrt(0.5) ) >= ((float) 0.999) ) && ( ( (float) Math.sqrt(2) * (float) Math.sqrt(0.5) ) <= ((float) 1.001) ) );
        // if b1 is true, the test succeeded, otherwise it failed
    }

}