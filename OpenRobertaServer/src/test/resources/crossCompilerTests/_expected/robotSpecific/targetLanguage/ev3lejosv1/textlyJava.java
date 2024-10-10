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

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S4, SensorType.ULTRASONIC, UltrasonicSensorMode.PRESENCE), new UsedSensor(SensorPort.S3, SensorType.COLOR, ColorSensorMode.RGB), new UsedSensor(SensorPort.S2, SensorType.GYRO, GyroSensorMode.ANGLE)));
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    public static void main(String[] args) {
        try {
            brickConfiguration = new EV3Configuration.Builder()
                .setWheelDiameter(5.6)
                .setTrackWidth(18.0)
                .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addSensor(SensorPort.S2, new Sensor(SensorType.GYRO))
                .addSensor(SensorPort.S3, new Sensor(SensorType.COLOR))
                .addSensor(SensorPort.S4, new Sensor(SensorType.ULTRASONIC))
                .build();

            new NEPOprog().run();
        } catch ( Exception e ) {
            Hal.displayExceptionWaitForKeyPress(e);
        }
    }

    float ___num = 0;
    boolean ___boolT = true;
    String ___str = "";
    PickColor ___color = PickColor.WHITE;
    boolean ___boolF = false;
    NXTConnection ___conn = null;
    ArrayList<Float> ___listN = new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0));
    ArrayList<Float> ___listN2 = new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0));
    ArrayList<NXTConnection> ___listConn = new ArrayList<>(Arrays.<NXTConnection>asList(null, null, null));
    ArrayList<PickColor> ___listColor = new ArrayList<>(Arrays.<PickColor>asList(PickColor.WHITE, PickColor.WHITE, PickColor.WHITE));

    public void run() throws Exception {
        ___num = ( (float) Math.exp(2) + (float) Math.sin(90) ) - ( ( Math.round(Math.random() * ((10) - (1))) + (1) ) * (float) Math.ceil(((float) 2.3)) );
        ___num = ( ( ( _sum(___listN) + ___listN.get(0) ) + ___listN.indexOf( (float) 0) ) + ___listN.get( (int) (0)) ) - ___listN2.remove( (int) (1));
        ___boolT = ( ( ( (10 % 2 == 0) && (7 % 2 == 1) ) || ( _isPrime( (int) 11) && (8 % 1 == 0) ) ) || ( ___listN.isEmpty() && (5 > 0) ) ) || ( (- (3) < 0) && (10 % 5 == 0) );
        ___str = String.valueOf(5) + String.valueOf("Hello") + String.valueOf(String.valueOf((char)(int)(65))) + String.valueOf(true);
        ___listN2 = new ArrayList<>(___listN.subList((int) 0, (int) 3));
        ___color = PickColor.BLUE;
        ___color = PickColor.GREEN;
        ___num = hal.getRegulatedMotorSpeed(ActorPort.B);
        ___boolT = hal.getUltraSonicSensorPresence(SensorPort.S4);
        ___listN2 = hal.getColorSensorRgb(SensorPort.S3);
        ___boolT = hal.isPressed(BrickKey.UP);
        ___num = hal.getGyroSensorAngle(SensorPort.S2);
        ___conn = hal.establishConnectionTo("hola");
        ___str = hal.readMessage(___conn);
        ___conn = hal.waitForConnection();
    }

    private boolean _isPrime(int n) {
        if (n == 2) return true;
        if (n % 2 == 0 || n == 1) return false;
        for (int i = 2; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private float _sum(List<Float> list) {
        float sum = 0.0f;
        for ( Float f : list ) {
            sum += f;
        }
        return sum;
    }
}