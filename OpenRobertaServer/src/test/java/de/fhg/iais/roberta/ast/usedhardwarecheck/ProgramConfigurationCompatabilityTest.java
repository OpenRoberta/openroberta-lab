package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;

import de.fhg.iais.roberta.ast.hardwarecheck.UsedPortsCheckVisitor;
import de.fhg.iais.roberta.ast.hardwarecheck.UsedSensorsCheckVisitor;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.codegen.lejos.Helper;
import de.fhg.iais.roberta.ev3.EV3Actors;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Sensors;
import de.fhg.iais.roberta.ev3.components.EV3Actor;
import de.fhg.iais.roberta.ev3.components.EV3Sensor;

public class ProgramConfigurationCompatabilityTest {

    @Ignore
    public void test() throws Exception {
        EV3BrickConfiguration.Builder builder = new EV3BrickConfiguration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR)).addSensor(SensorPort.S2, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR));

        EV3BrickConfiguration brickConfiguration = builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator2.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        UsedPortsCheckVisitor programChecker = new UsedPortsCheckVisitor(brickConfiguration);
        int countErrors = programChecker.check(phrases);
        ArrayList<ArrayList<Phrase<Void>>> checkedProgram = programChecker.getCheckedProgram();
        System.out.println(countErrors);
        System.out.println(checkedProgram);
        Assert.assertEquals(
            "[HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_colour, SENSOR]]",
            hardwareCheckVisitor.toString());
    }
}
