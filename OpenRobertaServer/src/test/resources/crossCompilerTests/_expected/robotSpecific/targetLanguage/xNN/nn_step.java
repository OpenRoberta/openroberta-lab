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
    
    private float ____out1;
    private float ____out2;
    private float ____out3;
    private float ____b_h1n1 = 1;
    private float ____w_in1_h1n1 = 1;
    private float ____w_in2_h1n1 = 1;
    private float ____w_in3_h1n1 = 1;
    private float ____b_h1n2 = 1;
    private float ____w_in1_h1n2 = 1;
    private float ____w_in2_h1n2 = 1;
    private float ____w_in3_h1n2 = 1;
    private float ____b_out1 = -1;
    private float ____w_h1n1_out1 = -1;
    private float ____w_h1n2_out1 = -1;
    private float ____b_out2 = -1;
    private float ____w_h1n1_out2 = -1;
    private float ____w_h1n2_out2 = -1;
    private float ____b_out3 = -1;
    private float ____w_h1n1_out3 = -1;
    private float ____w_h1n2_out3 = -1;
    
    private void ____nnStep( float _in1, float _in2, float _in3) {
        float ____h1n1 = ____b_h1n1 + _in1 * ____w_in1_h1n1 + _in2 * ____w_in2_h1n1 + _in3 * ____w_in3_h1n1;
        float ____h1n2 = ____b_h1n2 + _in1 * ____w_in1_h1n2 + _in2 * ____w_in2_h1n2 + _in3 * ____w_in3_h1n2;
        ____out1 = ____b_out1 + ____h1n1 * ____w_h1n1_out1 + ____h1n2 * ____w_h1n2_out1;
        ____out2 = ____b_out2 + ____h1n1 * ____w_h1n1_out2 + ____h1n2 * ____w_h1n2_out2;
        ____out3 = ____b_out3 + ____h1n1 * ____w_h1n1_out3 + ____h1n2 * ____w_h1n2_out3;
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
    
    float ___o1 = 0;
    float ___o2 = 0;
    float ___o3 = 0;
    
    public void run() throws Exception {
        ____w_in1_in1 += 1;
        ____b_h1n1 = 2;
        ___o3 = ____w_in1_in1;
        ___o3 = ____b_h1n1;
        ____nnStep(1,2,1 + 2);
        ___o1 = ____out1;
        ___o2 = ____out2;
        
        ___o3 = ____out1;
        
    }
}