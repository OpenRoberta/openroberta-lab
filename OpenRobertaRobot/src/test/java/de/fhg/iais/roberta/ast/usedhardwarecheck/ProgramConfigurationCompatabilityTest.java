package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;

import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Actors;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.action.ev3.DriveDirection;
import de.fhg.iais.roberta.shared.action.ev3.MotorSide;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.hardwarecheck.ev3.UsedPortsCheckVisitor;
import de.fhg.iais.roberta.syntax.hardwarecheck.ev3.UsedSensorsCheckVisitor;
import de.fhg.iais.roberta.testutil.Helper;

public class ProgramConfigurationCompatabilityTest {

    @Ignore
    public void test() throws Exception {
        Ev3Configuration.Builder builder = new Ev3Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR)).addSensor(SensorPort.S2, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR));

        Ev3Configuration brickConfiguration = builder.build();
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
