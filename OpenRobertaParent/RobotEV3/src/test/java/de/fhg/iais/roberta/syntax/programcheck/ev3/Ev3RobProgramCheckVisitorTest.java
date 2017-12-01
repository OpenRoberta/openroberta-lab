package de.fhg.iais.roberta.syntax.programcheck.ev3;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.MoveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.ev3.BrickCheckVisitor;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class Ev3RobProgramCheckVisitorTest {
    Helper h = new Helper();

    private Configuration makeConfiguration() {
        return new EV3Configuration.Builder()
            .setTrackWidth(17)
            .setWheelDiameter(5.6)
            .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, MoveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, MoveDirection.FOREWARD, MotorSide.RIGHT))
            .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, true, MoveDirection.FOREWARD, MotorSide.NONE))
            .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))
            .addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC))
            .build();
    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/MoveWithZeroSpeed.xml");

        BrickCheckVisitor checkVisitor = new BrickCheckVisitor(makeConfiguration());
        checkVisitor.check(phrases);
        Assert.assertEquals(2, checkVisitor.getWarningCount());

    }
}
