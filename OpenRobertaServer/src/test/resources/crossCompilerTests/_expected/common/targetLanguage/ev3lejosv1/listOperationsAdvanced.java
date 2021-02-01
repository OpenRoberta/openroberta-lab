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
    
    ArrayList<Float> ___nl = new ArrayList<>();
    ArrayList<Boolean> ___bl = new ArrayList<>();
    ArrayList<String> ___sl = new ArrayList<>();
    ArrayList<Float> ___nl3 = new ArrayList<>(Arrays.asList((float) 1, (float) 2, (float) 9));
    ArrayList<Boolean> ___bl3 = new ArrayList<>(Arrays.<Boolean>asList(true, true, false));
    ArrayList<String> ___sl3 = new ArrayList<>(Arrays.<String>asList("a", "b", "c"));
    float ___n = 0;
    boolean ___b = true;
    String ___s = "";
    
    public void run() throws Exception {
        // Basis List Operations START
        if ( ___nl.isEmpty() ) {
            ___nl = new ArrayList<>(Arrays.asList((float) 3, (float) 4, (float) 5, (float) 6, (float) 7, (float) 8));
            ___nl3.add((int) ((___nl3.size() - 1) - 1), ___nl.remove( (int) (0)));;
        }
        if ( ___bl.isEmpty() ) {
            ___bl = new ArrayList<>(Arrays.<Boolean>asList(true, false, true));
            ___bl = new ArrayList<>(Arrays.<Boolean>asList(___bl.get( (int) (0)).equals(___bl.get( (int) (___bl.size() - 1))), ___bl.get( (int) (1)).equals(___bl.get( (int) ((___bl.size() - 1) - 1))), ___bl.get( (int) (___bl.size() - 1)).equals(___bl.get( (int) (0)))));
        }
        if ( ___sl.isEmpty() ) {
            ___sl = new ArrayList<>(Arrays.<String>asList("d", "e", "f"));
        }
        ___n = ___nl.size();
        ___n = new ArrayList<>(___nl.subList(0, ___nl.size())).size();
        ___n = new ArrayList<>(___nl.subList(0, ___nl.size())).size() + new ArrayList<>(___nl.subList((int) 1, (int) 3)).size();
        ___n = ___sl.indexOf("b");
        ___n = new ArrayList<>(Collections.nCopies( (int) 5,  (float) 5)).get( (int) (new ArrayList<>(Collections.nCopies( (int) 5,  (float) 5)).size() - 1));
        ___s = new ArrayList<>(Collections.nCopies( (int) 5, "copy")).get( (int) ((new ArrayList<>(Collections.nCopies( (int) 5, "copy")).size() - 1) - 5));
        while ( ! ! ___sl.isEmpty() ) {
            ___sl3.set((int) (___sl3.size() - 1), ___sl.remove( (int) (0)));;
        }
        while ( ! (___nl3.size() <= 9) ) {
            ___nl3.add((int) ((___nl3.size() - 1) - 1), ___nl.remove( (int) (0)));;
        }
        new ArrayList<>(___nl3.subList((int) 2, (___nl3.size() - 1) - (int) 5)).set(0, ___nl3.indexOf( (float) ___n));
        // Basis List Operations END
    }

}