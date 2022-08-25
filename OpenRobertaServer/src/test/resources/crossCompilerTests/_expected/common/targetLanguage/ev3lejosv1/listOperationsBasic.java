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

    ArrayList<Float> ___initialEmptyNumbers = new ArrayList<>();
    ArrayList<Boolean> ___initialEmptyBoolean = new ArrayList<>();
    ArrayList<String> ___initialEmptyStrings = new ArrayList<>();
    float ___number = 3;
    boolean ___bool = true;
    String ___string = "c";
    float ___item = 0;

    public void run() throws Exception {
        // Basis List Operations START
        if ( ___initialEmptyNumbers.isEmpty() ) {
            ___initialEmptyNumbers = new ArrayList<>(Arrays.asList((float) 1, (float) 2));
            ___item = ___initialEmptyNumbers.size();
            ___item = ___initialEmptyNumbers.indexOf( (float) 1);
            ___item = ___initialEmptyNumbers.indexOf( (float) 5);
            ___initialEmptyNumbers.set(0, (float) 2);;
        }
        if ( ___initialEmptyBoolean.isEmpty() ) {
            ___initialEmptyBoolean = new ArrayList<>(Arrays.<Boolean>asList(true, false));
            ___item = ___initialEmptyBoolean.size();
            ___item = ___initialEmptyBoolean.indexOf(___bool);
            ___item = ___initialEmptyBoolean.indexOf(null);
            ___initialEmptyBoolean.add(true);;
        }
        if ( ___initialEmptyStrings.isEmpty() ) {
            ___initialEmptyStrings = new ArrayList<>(Arrays.<String>asList("a", "b"));
            ___item = ___initialEmptyStrings.size();
            ___item = ___initialEmptyStrings.indexOf("a");
            ___initialEmptyStrings.set((int) ((___initialEmptyStrings.size() - 1) - 2), "c");;
            ___initialEmptyStrings.add((int) ((___initialEmptyStrings.size() - 1) - 1), "d");;
        }
        // Basis List Operations END
    }

}