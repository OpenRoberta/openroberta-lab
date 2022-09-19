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

    private void ____number() {
        ___n1 = ___n2 + ___n3;
    }

    private void ____breakFunct() {
        if (5 == ___n1) return ;
        ___n1 = ___n1 + 1000;
    }

    private boolean ____retBool() {
        ___n1 = ___n1;
        return ___b;
    }

    private float ____retNumber() {
        ___n1 = ___n1;
        return ___n1;
    }

    private float ____retNumber2(float ___x) {
        ___x = ___x / ((float) 2);
        return ___x;
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

    float ___n1 = 0;
    boolean ___b = false;
    float ___n2 = 1;
    float ___n3 = 4;

    public void run() throws Exception {
        // Basic Functions START
        ____number();
        ____breakFunct();
        if (!(5 == ___n1)) {
            System.out.println("Assertion failed: " + "pos-1" + 5 + "EQ" + ___n1);
        }
        ___n1 = ____retNumber();
        ___b = ____retBool();
        ___n1 = ____retNumber2(10);
        // Basic Functions END
    }
}