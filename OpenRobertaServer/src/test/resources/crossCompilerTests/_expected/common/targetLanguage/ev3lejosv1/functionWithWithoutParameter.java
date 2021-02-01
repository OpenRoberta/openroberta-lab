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
    
    private float get3() {
        return 3;
    }
    
    private ArrayList<Float> getList() {
        return new ArrayList<>(Arrays.asList((float) 1, (float) 2, (float) 3));
    }
    
    private float getParmPlus6(float ___x1) {
        return ___x1 + 6;
    }
    
    private ArrayList<Float> getListUpd1To8(ArrayList<Float> ___x2) {
        ___x2.set((int) (1), (float) 8);;
        return ___x2;
    }
    
    private String getString() {
        return String.valueOf("++") + String.valueOf("--");
    }
    
    private String getStringAppPP(String ___x3) {
        return String.valueOf(___x3) + String.valueOf("++");
    }
    
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
    
    float ___two = 3;
    float ___summe = 3;
    ArrayList<Float> ___liste00 = new ArrayList<>(Arrays.asList((float) 0, (float) 0));
    
    public void run() throws Exception {
        ___two = getList().get( (int) (1));
        ___summe = ___two + get3();
        // 5
        if ( getString().equals("++--") ) {
            ___summe += 4;
        }
        // 9
        ___summe += getListUpd1To8(___liste00).get( (int) (1));
        // 17
        ___summe += getParmPlus6(1);
        // 24
        if ( getStringAppPP("--").equals("--++") ) {
            ___summe += 11;
        }
        // 35
    }

}