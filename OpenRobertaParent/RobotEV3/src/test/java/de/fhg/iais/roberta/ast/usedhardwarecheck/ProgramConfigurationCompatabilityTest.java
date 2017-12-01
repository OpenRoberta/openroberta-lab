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
import de.fhg.iais.roberta.mode.action.MoveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.ev3.BrickCheckVisitor;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ProgramConfigurationCompatabilityTest {
    Helper h = new Helper();

    @Test
    public void ev3program_configuration_compatibility_4_errors() throws Exception {
        EV3Configuration.Builder builder = new EV3Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder
            .addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, MoveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, MoveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH)).addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC));

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
            .addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, MoveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, MoveDirection.FOREWARD, MotorSide.RIGHT));
        builder
            .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))
            .addSensor(SensorPort.S2, new Sensor(SensorType.COLOR))
            .addSensor(SensorPort.S3, new Sensor(SensorType.GYRO))
            .addSensor(SensorPort.S4, new Sensor(SensorType.ULTRASONIC));

        EV3Configuration brickConfiguration = (EV3Configuration) builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/program_config_compatibility_gyro_touch_ultra_color.xml");

        RobotBrickCheckVisitor programChecker = new BrickCheckVisitor(brickConfiguration);
        programChecker.check(phrases);

        Assert.assertEquals(0, programChecker.getErrorCount());

    }
}
