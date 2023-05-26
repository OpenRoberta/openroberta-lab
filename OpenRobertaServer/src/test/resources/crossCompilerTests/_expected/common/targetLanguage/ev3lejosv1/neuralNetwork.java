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

    private void ____runNN() {
        ____n1 = 2;
        ____n3 = 4;
        ____w_n1_h1n1 = ____w_h1n2_n2;
        ____w_n3_h1n1 = ____w_h1n2_n4;
        ____b_n2 = ____b_h1n1;
        ____b_n4 = ____b_h1n2;
        ____nnStep();
        ___n = ____n2;
    }

    private float ____n1;
    private float ____n3;
    private float ____h1n1;
    private float ____h1n2;
    private float ____n2;
    private float ____n4;
    private float ____b_h1n1 = 0.5f;
    private float ____w_n1_h1n1 = 1f;
    private float ____w_n3_h1n1 = 2f;
    private float ____b_h1n2 = 1.5f;
    private float ____w_n1_h1n2 = 3f;
    private float ____w_n3_h1n2 = 4f;
    private float ____b_n2 = 0f;
    private float ____w_h1n1_n2 = 7f;
    private float ____w_h1n2_n2 = 6f;
    private float ____b_n4 = 0f;
    private float ____w_h1n1_n4 = 8f;
    private float ____w_h1n2_n4 = 5f;

    private void ____nnStep() {
        ____h1n1 = ____b_h1n1 + ____n1 * ____w_n1_h1n1 + ____n3 * ____w_n3_h1n1;
        ____h1n2 = ____b_h1n2 + ____n1 * ____w_n1_h1n2 + ____n3 * ____w_n3_h1n2;
        ____n2 = ____b_n2 + ____h1n1 * ____w_h1n1_n2 + ____h1n2 * ____w_h1n2_n2;
        ____n4 = ____b_n4 + ____h1n1 * ____w_h1n1_n4 + ____h1n2 * ____w_h1n2_n4;
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

    float ___n = 0;

    public void run() throws Exception {
        ____runNN();
    }
}
