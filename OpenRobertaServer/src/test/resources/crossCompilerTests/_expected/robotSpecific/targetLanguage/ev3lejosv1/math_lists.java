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

    private void ____math() {
        hal.drawText(String.valueOf(0), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar + ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar - ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar * ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar / ((float) ___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.pow(___numberVar, ___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.sqrt(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.abs(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(- (___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.log(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.log10(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.exp(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.pow(10, ___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.sin(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.cos(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.tan(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.asin(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.acos(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.atan(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.PI), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.E), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) ((1.0 + Math.sqrt(5.0)) / 2.0)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.sqrt(2)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.sqrt(0.5)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(Float.POSITIVE_INFINITY), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((___numberVar % 2 == 0)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((___numberVar % 2 == 1)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(_isPrime( (int) ___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((___numberVar % 1 == 0)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((___numberVar > 0)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((___numberVar < 0)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((___numberVar % ___numberVar == 0)), ___numberVar, ___numberVar);
        ___numberVar += ___numberVar;
        hal.drawText(String.valueOf((float) Math.round(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.ceil(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.floor(___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(_sum(___numberList)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(Collections.min(___numberList)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(Collections.max(___numberList)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(_average(___numberList)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(_median(___numberList)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(_standardDeviation(___numberList)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.get(0)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberVar % ___numberVar), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(Math.min(Math.max(___numberVar, ___numberVar), ___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(( Math.round(Math.random() * (___numberVar - ___numberVar)) + ___numberVar )), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf((float) Math.random()), ___numberVar, ___numberVar);
    }

    private void ____lists() {
        ___numberList = new ArrayList<>();
        ___numberList = new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0));
        ___numberList = new ArrayList<>(Collections.nCopies( (int) ___numberVar,  (float) ___numberVar));
        hal.drawText(String.valueOf(___numberList.size()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.isEmpty()), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.indexOf( (float) ___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.lastIndexOf( (float) ___numberVar)), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.get( (int) (___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.get( (int) ((___numberList.size() - 1) - ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.get( (int) (0))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.get( (int) (___numberList.size() - 1))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.remove( (int) (___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.remove( (int) ((___numberList.size() - 1) - ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.remove( (int) (0))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(___numberList.remove( (int) (___numberList.size() - 1))), ___numberVar, ___numberVar);
        ___numberList.remove( (int) (___numberVar));;
        ___numberList.remove( (int) ((___numberList.size() - 1) - ___numberVar));;
        ___numberList.remove( (int) (0));;
        ___numberList.remove( (int) (___numberList.size() - 1));;
        ___numberList.set((int) (___numberVar), (float) ___numberVar);;
        ___numberList.set((int) ((___numberList.size() - 1) - ___numberVar), (float) ___numberVar);;
        ___numberList.set(0, (float) ___numberVar);;
        ___numberList.set((int) (___numberList.size() - 1), (float) ___numberVar);;
        ___numberList.add((int) (___numberVar), (float) ___numberVar);;
        ___numberList.add((int) ((___numberList.size() - 1) - ___numberVar), (float) ___numberVar);;
        ___numberList.add(0, (float) ___numberVar);;
        ___numberList.add((float) ___numberVar);;
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList((int) ___numberVar, (int) ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList((int) ___numberVar, (___numberList.size() - 1) - (int) ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList((int) ___numberVar, ___numberList.size()))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList(0, (int) ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList(0, (___numberList.size() - 1) - (int) ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList(0, ___numberList.size()))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList((___numberList.size() - 1) - (int) ___numberVar, (int) ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList((___numberList.size() - 1) - (int) ___numberVar, (___numberList.size() - 1) - (int) ___numberVar))), ___numberVar, ___numberVar);
        hal.drawText(String.valueOf(new ArrayList<>(___numberList.subList((___numberList.size() - 1) - (int) ___numberVar, ___numberList.size()))), ___numberVar, ___numberVar);
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
        ____math();
        ____lists();
        hal.closeResources();

    }

    private float _average(List<Float> list) {
        float sum = 0.0f; // TODO reuse _sum?
        for ( Float f : list ) {
            sum += f;
        }
        return sum / list.size();
    }

    private float _median(List<Float> list) {
        List<Float> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);
        int listLen = sortedList.size();
        if (list.isEmpty()) throw new IllegalArgumentException("List cannot be empty!"); // TODO remove?
        if (listLen % 2 == 0) {
            return (sortedList.get((listLen - 1) / 2) + sortedList.get((listLen + 1) / 2)) / 2.0f;
        } else {
            return sortedList.get((listLen - 1) / 2);
        }
    }

    private boolean _isPrime(int n) {
        if (n == 2) return true;
        if (n % 2 == 0 || n == 1) return false;
        for (int i = 2; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private float _standardDeviation(List<Float> list) {
        float sum = 0.0f; // TODO reuse _average?
        for ( Float f : list ) {
            sum += f;
        }
        float mean = sum / list.size();
        float standardDev = 0;
        for ( Float f : list ) {
            standardDev += (f - mean) * (f + mean);
        }
        return (float) Math.sqrt(standardDev / list.size());
    }

    private float _sum(List<Float> list) {
        float sum = 0.0f;
        for ( Float f : list ) {
            sum += f;
        }
        return sum;
    }
}