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

    private void ____control() {
        if ( ___booleanVar ) {
        } else if ( ___booleanVar ) {
        }
        if ( ___booleanVar ) {
        } else if ( ___booleanVar ) {
        }
        if ( true ) {
            while ( true ) {
            }
        }
        for ( float ___k0 = 0; ___k0< ___numberVar; ___k0+= 1 ) {
        }
        for ( float ___i = ___numberVar; ___i< ___numberVar; ___i+= ___numberVar ) {
        }
        if ( true ) {
            while ( true ) {
                break;
            }
        }
        if ( true ) {
            while ( true ) {
                continue;
            }
        }
        hal.waitFor(___numberVar);
        while ( ___booleanVar ) {
        }
        while ( ! ___booleanVar ) {
        }
        for ( float ___item : ___numberList ) {
        }
        for ( boolean ___item2 : ___booleanList ) {
        }
        for ( String ___item3 : ___stringList ) {
        }
        for ( PickColor ___item4 : ___colourList ) {
        }
        for ( NXTConnection ___item5 : ___connectionList ) {
        }
        while ( true ) {
            if ( ___booleanVar ) {
                break;
            }
            if ( ___booleanVar ) {
                break;
            }
            hal.waitFor(15);
        }
        while ( true ) {
            if ( ___booleanVar ) {
                break;
            }
            hal.waitFor(15);
        }
    }

    private void ____logic() {
        hal.drawText(String.valueOf(___numberVar == ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar != ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar < ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar <= ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar > ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar >= ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___booleanVar && ___booleanVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___booleanVar || ___booleanVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(! ___booleanVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(true), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(false), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(null), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(( ( ___booleanVar ) ? ___numberVar : ___numberVar )), ___numberVar, ___numberVar);
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

    float ___numberVar = 0;
    boolean ___booleanVar = true;
    String ___stringVar = "";
    PickColor ___colourVar = PickColor.WHITE;
    NXTConnection ___connectionVar = null;
    ArrayList<Float> ___numberList = new ArrayList<>(Arrays.asList((float) 0, (float) 0));
    ArrayList<Boolean> ___booleanList = new ArrayList<>(Arrays.<Boolean>asList(true, true));
    ArrayList<String> ___stringList = new ArrayList<>(Arrays.<String>asList("", ""));
    ArrayList<PickColor> ___colourList = new ArrayList<>(Arrays.<PickColor>asList(PickColor.WHITE, PickColor.WHITE));
    ArrayList<NXTConnection> ___connectionList = new ArrayList<>(Arrays.<NXTConnection>asList(___connectionVar, ___connectionVar));

    public void run() throws Exception {
        hal.startLogging();
        ____control();
        ____logic();
        hal.closeResources();

    }
}