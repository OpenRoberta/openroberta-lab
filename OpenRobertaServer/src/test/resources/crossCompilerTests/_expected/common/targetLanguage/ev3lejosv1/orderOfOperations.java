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

    private void ____plusOperations(float ___item2) {
        ___item2 = ( 1 * 2 ) + ( 3 + 4 );
        ___item2 = Math.min(Math.max(( ( 6 + 5 ) % ( 5 ) ), 1), 100);
        ___item2 = ( ( (float) Math.sqrt(6) + (float) Math.sin(5) ) % ( 5 ) );
        ___item2 = ( ( 6 + (float) Math.PI ) % ( (float) Math.round(((float) 7.8)) ) );
        ___item2 = ( ( 6 + ( Math.round(Math.random() * ((100 - 1) - (10 - 1))) + (10 - 1) ) ) % ( 5 ) );
        ___item2 = ( ( (float) Math.random() + 5 ) % ( 5 ) );
    }

    private void ____multiplicationOperations(float ___item4) {
        ___item4 = ( 1 * 2 ) * ( 3 + 4 );
        ___item4 = Math.min(Math.max(( ( 6 * 5 ) % ( 5 ) ), 1), 100);
        ___item4 = ( ( (float) Math.sqrt(6) * (float) Math.sin(5) ) % ( 5 ) );
        ___item4 = ( ( 6 * (float) Math.PI ) % ( (float) Math.round(((float) 7.8)) ) );
        ___item4 = ( ( 6 * ( Math.round(Math.random() * ((100 - 1) - (10 - 1))) + (10 - 1) ) ) % ( 5 ) );
        ___item4 = ( ( (float) Math.random() * 5 ) % ( 5 ) );
    }

    private void ____exponentOperations(float ___item6) {
        ___item6 = (float) Math.pow(1 * 2, 3 + 4);
        ___item6 = Math.min(Math.max(( ( (float) Math.pow(6, 5) ) % ( 5 ) ), 1), 100);
        ___item6 = ( ( (float) Math.pow((float) Math.sqrt(6), (float) Math.sin(5)) ) % ( 5 ) );
        ___item6 = ( ( (float) Math.pow(6, (float) Math.PI) ) % ( (float) Math.round(((float) 7.8)) ) );
        ___item6 = ( ( (float) Math.pow(6, ( Math.round(Math.random() * ((100 - 1) - (10 - 1))) + (10 - 1) )) ) % ( 5 ) );
        ___item6 = ( ( (float) Math.pow((float) Math.random(), 5) ) % ( 5 ) );
    }

    private void ____minusOperations(float ___item3) {
        ___item3 = ( 1 * 2 ) - ( 3 + 4 );
        ___item3 = Math.min(Math.max(( ( 6 - 5 ) % ( 5 ) ), 1), 100);
        ___item3 = ( ( (float) Math.sqrt(6) - (float) Math.sin(5) ) % ( 5 ) );
        ___item3 = ( ( 6 - (float) Math.PI ) % ( (float) Math.round(((float) 7.8)) ) );
        ___item3 = ( ( 6 - ( Math.round(Math.random() * ((100 - 1) - (10 - 1))) + (10 - 1) ) ) % ( 5 ) );
        ___item3 = ( ( (float) Math.random() - 5 ) % ( 5 ) );
    }

    private void ____divisionOperations(float ___item5) {
        ___item5 = ( 1 * 2 ) / ((float) ( 3 + 4 ));
        ___item5 = Math.min(Math.max(( ( 6 / ((float) 5) ) % ( 5 ) ), 1), 100);
        ___item5 = ( ( (float) Math.sqrt(6) / ((float) (float) Math.sin(5)) ) % ( 5 ) );
        ___item5 = ( ( 6 / ((float) (float) Math.PI) ) % ( (float) Math.round(((float) 7.8)) ) );
        ___item5 = ( ( 6 / ((float) ( Math.round(Math.random() * ((100 - 1) - (10 - 1))) + (10 - 1) )) ) % ( 5 ) );
        ___item5 = ( ( (float) Math.random() / ((float) 5) ) % ( 5 ) );
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

    float ___item = 0;

    public void run() throws Exception {
        ____plusOperations(___item);
        ____minusOperations(___item);
        ____multiplicationOperations(___item);
        ____divisionOperations(___item);
        ____exponentOperations(___item);
    }

}
