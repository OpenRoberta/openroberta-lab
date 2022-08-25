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
        ___x = ___x + (float) Math.sqrt(4);
        ___x = ___x + (float) Math.abs(-2);
        ___x = ___x + ( - (-4) );
        ___x = ___x + (float) Math.log((float) Math.exp(2));
        ___x = ___x + (float) Math.log10(100);
        ___x = ___x + (float) Math.pow(10, 2);
        ___x = ___x + ( 5 % 3 );
        ___x = ___x + (float) Math.sin((float) Math.PI / ((float) 2));
        ___x = ___x + (float) Math.cos(0);
        ___x = ___x + (float) Math.tan(0);
        ___x = ___x + (float) Math.asin(0);
        ___x = ___x + (float) Math.acos(1);
        ___x = ___x + (float) Math.atan(0);
        ___x = ___x + (float) Math.floor(((float) 42.8));
        ___x = ___x + (float) Math.sin(Math.min(Math.max(2, 1), 100));
        // expected: 170
    }

}