package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.ev3.BrickCheckVisitor;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ProgramConfigurationCompatabilityTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void ev3program_configuration_compatibility_4_errors() throws Exception {
        EV3Configuration.Builder builder = new EV3Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder
            .addActor(new ActorPort("A", "MA"), new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(new ActorPort("B", "MB"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(new SensorPort("1", "S1"), new Sensor(SensorType.TOUCH)).addSensor(new SensorPort("2", "S2"), new Sensor(SensorType.ULTRASONIC));

        EV3Configuration brickConfiguration = (EV3Configuration) builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/program_config_compatibility.xml");

        RobotBrickCheckVisitor programChecker = new BrickCheckVisitor(brickConfiguration);
        programChecker.check(phrases);

        Assert.assertEquals(4, programChecker.getErrorCount());

    }

    @Test
    public void ev3program_configuration_compatibility_0_errors() throws Exception {
        EV3Configuration.Builder builder = new EV3Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder
            .addActor(new ActorPort("A", "MA"), new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(new ActorPort("B", "MB"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder
            .addSensor(new SensorPort("1", "S1"), new Sensor(SensorType.TOUCH))
            .addSensor(new SensorPort("2", "S2"), new Sensor(SensorType.COLOR))
            .addSensor(new SensorPort("3", "S3"), new Sensor(SensorType.GYRO))
            .addSensor(new SensorPort("4", "S4"), new Sensor(SensorType.ULTRASONIC));

        EV3Configuration brickConfiguration = (EV3Configuration) builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/program_config_compatibility_gyro_touch_ultra_color.xml");

        RobotBrickCheckVisitor programChecker = new BrickCheckVisitor(brickConfiguration);
        programChecker.check(phrases);

        Assert.assertEquals(0, programChecker.getErrorCount());

    }
}
