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

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();
    private Hal hal = new Hal(brickConfiguration, usedSensors);

    public static void main(String[] args) {
        try {
             brickConfiguration = new EV3Configuration.Builder()
                .setWheelDiameter(5.6)
                .setTrackWidth(18.0)
                .addActor(ActorPort.A, new Actor(ActorType.OTHER, false, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(ActorPort.B, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .build();

            new Test().run();
        } catch ( Exception e ) {
            Hal.displayExceptionWaitForKeyPress(e);
        }
    }
    
    float ___item = hal.getUnregulatedMotorSpeed(ActorPort.A);
    float ___item2 = hal.getRegulatedMotorSpeed(ActorPort.B);
    
    public void run() throws Exception {

        hal.turnOnUnregulatedMotor(ActorPort.A, 30);
        hal.turnOnRegulatedMotor(ActorPort.B, 30);
        hal.rotateRegulatedMotor(ActorPort.B, 30, MotorMoveMode.ROTATIONS, 1);
        hal.setUnregulatedMotorSpeed(ActorPort.A, 30);
        hal.setRegulatedMotorSpeed(ActorPort.B, 30);
        hal.stopUnregulatedMotor(ActorPort.A, MotorStopMode.FLOAT);
        hal.stopUnregulatedMotor(ActorPort.A, MotorStopMode.NONFLOAT);
        hal.stopRegulatedMotor(ActorPort.B, MotorStopMode.FLOAT);
        hal.stopRegulatedMotor(ActorPort.B, MotorStopMode.NONFLOAT);
    }
}
