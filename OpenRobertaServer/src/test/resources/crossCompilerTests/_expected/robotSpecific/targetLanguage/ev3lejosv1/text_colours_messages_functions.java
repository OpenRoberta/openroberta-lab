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

    private void ____text() {
        hal.drawText("", ___numberVar, ___numberVar);
        //
        hal.drawText(String.valueOf(String.valueOf(___stringVar) + String.valueOf(___stringVar)), ___numberVar, ___numberVar);
        ___stringVar += String.valueOf(___stringVar);
    }

    private void ____colours() {
        hal.drawText(String.valueOf(PickColor.BLACK), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(PickColor.BLUE), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(PickColor.GREEN), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(PickColor.BROWN), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(PickColor.NONE), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(PickColor.RED), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(PickColor.YELLOW), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(PickColor.WHITE), ___numberVar, ___numberVar);
    }

    private void ____messages() {
        hal.drawText(String.valueOf(hal.establishConnectionTo(String.valueOf(___stringVar))), ___numberVar, ___numberVar);
        hal.sendMessage(String.valueOf(___stringVar), ___connectionVar);
        hal.drawText(String.valueOf(hal.readMessage(___connectionVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(hal.waitForConnection()), ___numberVar, ___numberVar);
    }

    private void ____function_parameter(float ___x, boolean ___x2, String ___x3, PickColor ___x4, NXTConnection ___x5, ArrayList<Float> ___x6, ArrayList<Boolean> ___x7, ArrayList<String> ___x8, ArrayList<PickColor> ___x9, ArrayList<NXTConnection> ___x10) {
        if (___booleanVar) return ;
    }

    private float ____function_return_numberVar() {
        return ___numberVar;
    }

    private boolean ____function_return_booleanVar() {
        return ___booleanVar;
    }

    private String ____function_return_stringVar() {
        return ___stringVar;
    }

    private PickColor ____function_return_colourVar() {
        return ___colourVar;
    }

    private NXTConnection ____function_return_connectionVar() {
        return ___connectionVar;
    }

    private ArrayList<Float> ____function_return_numberList() {
        return ___numberList;
    }

    private ArrayList<Boolean> ____function_return_booleanList() {
        return ___booleanList;
    }

    private ArrayList<String> ____function_return_stringList() {
        return ___stringList;
    }

    private ArrayList<PickColor> ____function_return_colourList() {
        return ___colourList;
    }

    private ArrayList<NXTConnection> ____function_return_connectionList() {
        return ___connectionList;
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
        ____text();
        ____colours();
        ____messages();
        ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList);
        hal.drawText(String.valueOf(____function_return_numberVar()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_booleanVar()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_stringVar()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_colourVar()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_connectionVar()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_numberList()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_booleanList()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_stringList()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_colourList()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(____function_return_connectionList()), ___numberVar, ___numberVar);
        hal.closeResources();

    }
}