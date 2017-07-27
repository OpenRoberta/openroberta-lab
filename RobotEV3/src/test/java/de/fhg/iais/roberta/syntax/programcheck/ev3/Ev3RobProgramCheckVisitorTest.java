package de.fhg.iais.roberta.syntax.programcheck.ev3;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.EV3Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.ev3.ActorPort;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.Ev3RobProgramCheckVisitor;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class Ev3RobProgramCheckVisitorTest {
    Helper h = new Helper();

    private Configuration makeConfiguration() {
        return new EV3Configuration.Builder()
            .setTrackWidth(17)
            .setWheelDiameter(5.6)
            .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
            .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.NONE))
            .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))
            .addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC))
            .build();
    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/MoveWithZeroSpeed.xml");

        Ev3RobProgramCheckVisitor checkVisitor = new Ev3RobProgramCheckVisitor(makeConfiguration());
        checkVisitor.check(phrases);
        Assert.assertEquals(5, checkVisitor.getWarningCount());

    }
}
