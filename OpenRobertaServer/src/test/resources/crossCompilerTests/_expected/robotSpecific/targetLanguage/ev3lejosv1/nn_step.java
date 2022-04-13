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
    
    class NNStep {
        public final double out1,out2,out3;
        public NNStep( double in1, double in2, double in3) {
            double h1n1 = 1 + in1*1 + in2*1 + in3*1;
            double h1n2 = 1 + in1*1 + in2*1 + in3*1;
            out1 = -1 + h1n1*-1 + h1n2*-1;
            out2 = -1 + h1n1*-1 + h1n2*-1;
            out3 = -1 + h1n1*-1 + h1n2*-1;
        }
    }
    NNStep nnStep = null;
    
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
        
        
        nnStep = new NNStep(1,2,1 + 2);
        ___o1 = nnStep.out1;
        ___o2 = nnStep.out2;
        
        ___o3 = nnStep.o3;
    }
}