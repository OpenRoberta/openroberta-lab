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

public class Test {
    private static Configuration brickConfiguration;

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S2, SensorType.IRSEEKER, IRSeekerSensorMode.MODULATED), new UsedSensor(SensorPort.S2, SensorType.IRSEEKER, IRSeekerSensorMode.UNMODULATED), new UsedSensor(SensorPort.S1, SensorType.COMPASS, CompassSensorMode.ANGLE), new UsedSensor(SensorPort.S1, SensorType.COMPASS, CompassSensorMode.COMPASS)));
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    public static void main(String[] args) {
        try {
             brickConfiguration = new EV3Configuration.Builder()
                .setWheelDiameter(5.6)
                .setTrackWidth(18.0)
                .addSensor(SensorPort.S1, new Sensor(SensorType.COMPASS))
                .addSensor(SensorPort.S2, new Sensor(SensorType.IRSEEKER))
                .build();

            new Test().run();
        } catch ( Exception e ) {
            Hal.displayExceptionWaitForKeyPress(e);
        }
    }
    
    float ___item2 = hal.getHiTecIRSeekerModulated(SensorPort.S2);
    float ___item = hal.getHiTecIRSeekerUnmodulated(SensorPort.S2);
    float ___item3 = hal.getHiTecCompassAngle(SensorPort.S1);
    float ___item4 = hal.getHiTecCompassCompass(SensorPort.S1);
    
    public void run() throws Exception {

    }
}
