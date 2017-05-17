package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.EV3Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.ev3.ActorPort;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.RobotProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.Ev3CodePreprocessVisitor;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ProgramConfigurationCompatabilityTest {
    Helper h = new Helper();

    public void test() throws Exception {
        EV3Configuration.Builder builder = new EV3Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH)).addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC));

        EV3Configuration brickConfiguration = (EV3Configuration) builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/syntax/code_generator/java_code_generator2.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, brickConfiguration);
        Set<UsedSensor> usedSensors = checkVisitor.getUsedSensors();
        RobotProgramCheckVisitor programChecker = new RobotProgramCheckVisitor(brickConfiguration);
        programChecker.check(phrases);
        int countErrors = programChecker.getErrorCount();
        ArrayList<ArrayList<Phrase<Void>>> checkedProgram = programChecker.getCheckedProgram();
        System.out.println(countErrors);
        System.out.println(checkedProgram);
        Assert.assertEquals("[HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_colour, SENSOR]]", usedSensors.toString());
    }
}
