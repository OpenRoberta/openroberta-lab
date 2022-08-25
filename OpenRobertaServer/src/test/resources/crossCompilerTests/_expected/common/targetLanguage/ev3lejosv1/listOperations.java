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

    ArrayList<Float> ___input = new ArrayList<>(Arrays.asList((float) 1, (float) 2, (float) 3, (float) 4, (float) 3));
    ArrayList<Float> ___input2 = new ArrayList<>();
    float ___result = 0;

    public void run() throws Exception {
        ___result = ___input.size();
        ___result = ___result + ___input2.size();
        if ( ___input.isEmpty() ) {
            ___result = ___result + 1;
        } else {
            ___result = ___result + 2;
        }
        if ( ___input2.isEmpty() ) {
            ___result = ___result + 1;
        } else {
            ___result = ___result + 2;
        }
        // 8
        ___result = ___result + ___input.indexOf( (float) 3);
        ___result = ___result + ___input.lastIndexOf( (float) 3);
        // 14
        ___result = ___result + ___input.get( (int) (1));
        ___result = ___result + ___input.get( (int) ((___input.size() - 1) - 1));
        ___result = ___result + ___input.get( (int) (0));
        ___result = ___result + ___input.get( (int) (___input.size() - 1));
        // 24
        ___result = ___result + ___input.remove( (int) (1));
        ___result = ___result + ___input.remove( (int) ((___input.size() - 1) - 1));
        ___result = ___result + ___input.remove( (int) (0));
        ___result = ___result + ___input.remove( (int) (___input.size() - 1));
        ___result = ___result + ___input.size();
        // 35
        ___input.add((int) (0), (float) 1);
        ___input.add((int) ((___input.size() - 1) - 1), (float) 2);
        ___input.add(0, (float) 0);
        ___input.add((float) 4);
        ___result = ___result + ___input.size();
        // 40
        ___input.remove( (int) (1));
        ___input.remove( (int) ((___input.size() - 1) - 1));
        ___input.remove( (int) (0));
        ___input.remove( (int) (___input.size() - 1));
        ___result = ___result + ___input.size();
        ___result = ___result + ___input.get( (int) (___input.size() - 1));
        // 42
        ___input.add((int) (0), (float) 1);
        ___input.add((int) ((___input.size() - 1) - 1), (float) 2);
        ___input.add(0, (float) 0);
        ___input.add((float) 4);
        ___input.set((int) (2), (float) 3);
        ___input.set((int) (1), (float) 2);
        ___input.set((int) ((___input.size() - 1) - 1), (float) 4);
        ___input.set(0, (float) 1);
        ___input.set((int) (___input.size() - 1), (float) 5);
        ___result = ___result + _sum(___input);
        // 57
        ___result = ___result + _sum(new ArrayList<>(___input.subList((int) 1, (int) 3)));
        ___result = ___result + _sum(new ArrayList<>(___input.subList((int) 1, (___input.size() - 1) - (int) 1)));
        ___result = ___result + _sum(new ArrayList<>(___input.subList((int) 1, ___input.size())));
        // 89
        ___result = ___result + _sum(new ArrayList<>(___input.subList((___input.size() - 1) - (int) 3, (int) 4)));
        ___result = ___result + _sum(new ArrayList<>(___input.subList((___input.size() - 1) - (int) 4, (___input.size() - 1) - (int) 3)));
        ___result = ___result + _sum(new ArrayList<>(___input.subList((___input.size() - 1) - (int) 3, ___input.size())));
        // 120
        ___result = ___result + _sum(new ArrayList<>(___input.subList(0, (int) 3)));
        ___result = ___result + _sum(new ArrayList<>(___input.subList(0, (___input.size() - 1) - (int) 3)));
        ___result = ___result + _sum(new ArrayList<>(___input.subList(0, ___input.size())));
        // 148
        ___result = ___result + Collections.min(___input);
        ___result = ___result + Collections.max(___input);
        // 154
        ___result = ___result + _average(___input);
        ___result = ___result + _median(___input);
        ___result = ___result + _standardDeviation(___input);
        // 161.414...
        // 161.414 - sim, 161.5 - board, OK
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