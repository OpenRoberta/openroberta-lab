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
    
    private float ____i1;
    private float ____i2;
    private float ____o1;
    private float ____o2;
    private float ____b_h1n1 = -1f;
    private float ____w_i1_h1n1 = 1f;
    private float ____w_i2_h1n1 = 1f;
    private float ____b_h1n2 = -1f;
    private float ____w_i1_h1n2 = 1f;
    private float ____w_i2_h1n2 = 1f;
    private float ____b_h1n3 = -1f;
    private float ____w_i1_h1n3 = 1f;
    private float ____w_i2_h1n3 = 1f;
    private float ____b_o1 = -1f;
    private float ____w_h1n1_o1 = 1f;
    private float ____w_h1n2_o1 = 1f;
    private float ____w_h1n3_o1 = 1f;
    private float ____b_o2 = -1f;
    private float ____w_h1n1_o2 = 1f;
    private float ____w_h1n2_o2 = 1f;
    private float ____w_h1n3_o2 = 1f;
    
    private void ____nnStep() {
        float ____h1n1 = ____b_h1n1 + ____i1 * ____w_i1_h1n1 + ____i2 * ____w_i2_h1n1;
        float ____h1n2 = ____b_h1n2 + ____i1 * ____w_i1_h1n2 + ____i2 * ____w_i2_h1n2;
        float ____h1n3 = ____b_h1n3 + ____i1 * ____w_i1_h1n3 + ____i2 * ____w_i2_h1n3;
        ____o1 = ____b_o1 + ____h1n1 * ____w_h1n1_o1 + ____h1n2 * ____w_h1n2_o1 + ____h1n3 * ____w_h1n3_o1;
        ____o2 = ____b_o2 + ____h1n1 * ____w_h1n1_o2 + ____h1n2 * ____w_h1n2_o2 + ____h1n3 * ____w_h1n3_o2;
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
    
    float ___x = 0;
    
    public void run() throws Exception {
        ____i1 = 5;
        ____i2 = 5;
        ____nnStep();
        ___x = ____o1;
        ___x = ____o2;
        ____w_i1_h1n1 = 5;
        ____w_h1n1_o1 = 5;
        ____b_h1n1 = 5;
        ____b_o1 = 5;
        ___x = ____w_i1_h1n3;
        ___x = ____w_h1n1_o1;
        ___x = ____b_h1n2;
        ___x = ____b_o1;
    }
}