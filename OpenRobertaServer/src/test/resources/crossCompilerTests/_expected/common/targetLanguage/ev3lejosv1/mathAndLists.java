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

    ArrayList<Float> ___l1 = new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0, (float) 0));
    float ___x = 0;
    float ___item3 = 0;
    ArrayList<Float> ___l2 = new ArrayList<>();
    boolean ___b = true;

    public void run() throws Exception {
        ___x = _sum(___l1);
        ___x = Collections.min(___l1);
        ___x = Collections.max(___l1);
        ___x = _average(___l1);
        ___x = _median(___l1);
        ___x = _standardDeviation(___l1);
        ___l2 = ___l1;
        ___b = ___l1.isEmpty();
        ___x = ___l1.size();
        ___x = ___l1.indexOf( (float) 0);
        ___x = ___l1.lastIndexOf( (float) 0);
        ___x = ___l1.get( (int) (0));
        ___x = ___l1.get( (int) ((___l1.size() - 1) - 0));
        ___x = ___l1.get( (int) (0));
        ___x = ___l1.get( (int) (___l1.size() - 1));
        ___x = ___l1.remove( (int) (0));
        ___x = ___l1.remove( (int) ((___l1.size() - 1) - 0));
        ___x = ___l1.remove( (int) (0));
        ___x = ___l1.remove( (int) (___l1.size() - 1));
        ___l1.remove( (int) (0));
        ___l1.remove( (int) ((___l1.size() - 1) - 0));
        ___l1.remove( (int) (0));
        ___l1.remove( (int) (___l1.size() - 1));
        ___l1.set((int) (0), (float) 0);
        ___l1.set((int) ((___l1.size() - 1) - 0), (float) 0);
        ___l1.set(0, (float) 0);
        ___l1.set((int) (___l1.size() - 1), (float) 0);
        ___l1.add((int) (0), (float) 0);
        ___l1.add((int) ((___l1.size() - 1) - 0), (float) 0);
        ___l1.add(0, (float) 0);
        ___l1.add((float) 0);
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