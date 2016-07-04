package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.generic.factory.action.ActorPort;
import de.fhg.iais.roberta.generic.factory.action.DriveDirection;
import de.fhg.iais.roberta.generic.factory.action.MotorSide;
import de.fhg.iais.roberta.generic.factory.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.hardwarecheck.generic.RobotProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.hardwarecheck.generic.UsedSensorsCheckVisitor;
import de.fhg.iais.roberta.testutil.Helper;

public class ProgramConfigurationCompatabilityTest {

    public void test() throws Exception {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH)).addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC));

        Configuration brickConfiguration = builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator2.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        RobotProgramCheckVisitor programChecker = new RobotProgramCheckVisitor(brickConfiguration);
        int countErrors = programChecker.check(phrases);
        ArrayList<ArrayList<Phrase<Void>>> checkedProgram = programChecker.getCheckedProgram();
        System.out.println(countErrors);
        System.out.println(checkedProgram);
        Assert
            .assertEquals("[HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_colour, SENSOR]]", hardwareCheckVisitor.toString());
    }
}
