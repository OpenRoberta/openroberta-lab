package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Actors;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.action.ev3.DriveDirection;
import de.fhg.iais.roberta.shared.action.ev3.MotorSide;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;

public class EV3BrickConfigurationTest {
    private static final String expectedBrickConfigurationGenerator = //
        "robotev3test{"//
            + "sensorport{1:ultrasonic;4:color;}"//
            + "actorport{A:largemotor,regulated,forward,left;B:middlemotor,regulated,forward,right;}}";

    @Test
    public void testBuilder() {
        Ev3Configuration.Builder builder = new Ev3Configuration.Builder();
        builder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT));
        builder.addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR));
        builder.addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_COLOR_SENSOR));
        Ev3Configuration conf = builder.build();

        assertEquals(EV3Actors.EV3_LARGE_MOTOR, conf.getActorOnPort(ActorPort.A).getComponentType());
        assertEquals(EV3Actors.EV3_MEDIUM_MOTOR, conf.getActorOnPort(ActorPort.B).getComponentType());
        assertNull(conf.getActorOnPort(ActorPort.C));
        assertEquals(EV3Sensors.EV3_ULTRASONIC_SENSOR, conf.getSensorOnPort(SensorPort.S1).getComponentType());
        assertEquals(EV3Sensors.EV3_COLOR_SENSOR, conf.getSensorOnPort(SensorPort.S4).getComponentType());
        assertNull(conf.getSensorOnPort(SensorPort.S2));

        assertEquals(EV3BrickConfigurationTest.expectedBrickConfigurationGenerator, conf.generateText("test").replaceAll("\\s+", ""));
    }

    @Test
    public void testBuilderFluent() {
        Ev3Configuration conf =
            new Ev3Configuration.Builder()
                .addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))
                .addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_COLOR_SENSOR))
                .build();

        assertEquals(EV3Actors.EV3_LARGE_MOTOR, conf.getActorOnPort(ActorPort.A).getComponentType());
        assertEquals(EV3Actors.EV3_MEDIUM_MOTOR, conf.getActorOnPort(ActorPort.B).getComponentType());
        assertNull(conf.getActorOnPort(ActorPort.C));
        assertEquals(EV3Sensors.EV3_ULTRASONIC_SENSOR, conf.getSensorOnPort(SensorPort.S1).getComponentType());
        assertEquals(EV3Sensors.EV3_COLOR_SENSOR, conf.getSensorOnPort(SensorPort.S4).getComponentType());
        assertNull(conf.getSensorOnPort(SensorPort.S2));

        assertEquals(EV3BrickConfigurationTest.expectedBrickConfigurationGenerator, conf.generateText("test").replaceAll("\\s+", ""));
    }

    @Test
    public void testText() {
        Ev3Configuration.Builder builder = new Ev3Configuration.Builder();
        builder.setTrackWidth(4.0).setWheelDiameter(-3.1428);
        builder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT));
        builder.addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, false, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR));
        builder.addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_COLOR_SENSOR));
        Ev3Configuration conf = builder.build();

        String expected = "" //
            + "robot ev3 test {\n"
            + "  size {\n"
            + "    wheel diameter -3.1 cm;\n"
            + "    track width    4.0 cm;\n"
            + "  }\n"
            + "  sensor port {\n"
            + "    1: ultrasonic;\n"
            + "    4: color;\n"
            + "  }\n"
            + "  actor port {\n"
            + "    A: large motor, regulated, forward, left;\n"
            + "    B: middle motor, unregulated, forward, right;\n"
            + "  }\n"
            + "}";

        assertEquals(expected.replaceAll("\\s+", ""), conf.generateText("test").replaceAll("\\s+", ""));
    }

    //    @Test
    //    public void testVisitorBuilder() {
    //        EV3BrickConfiguration.Builder builder = new EV3BrickConfiguration.Builder();
    //        builder.visiting("regulated", "large", "left", "off");
    //        builder.visitingActorPort("A");
    //        builder.visiting("regulated", "middle", "right", "off");
    //        builder.visitingActorPort("B");
    //        builder.visiting("ultrasonic");
    //        builder.visitingSensorPort("1");
    //        builder.visiting("Farbe");
    //        builder.visitingSensorPort("4");
    //        EV3BrickConfiguration conf = builder.build();
    //
    //        assertEquals(HardwareComponentType.EV3LargeRegulatedMotor, conf.getActorOnPort(ActorPort.A).getComponentType());
    //        assertEquals(HardwareComponentType.EV3MediumRegulatedMotor, conf.getActorOnPort(ActorPort.B).getComponentType());
    //        assertEquals(HardwareComponentType.EV3UltrasonicSensor, conf.getSensorOnPort(SensorPort.S1).getComponentType());
    //        assertEquals(HardwareComponentType.EV3ColorSensor, conf.getSensorOnPort(SensorPort.S4).getComponentType());
    //
    //        assertEquals(this.expectedBrickConfigurationGenerator, conf.generateRegenerate().replaceAll("\\s+", ""));
    //    }
    //
    //    @Test(expected = DbcException.class)
    //    public void testVisitorExc1() {
    //        EV3BrickConfiguration.Builder builder = new EV3BrickConfiguration.Builder();
    //        builder.visiting("regulated", "latsch");
    //        builder.visitingActorPort("A");
    //    }
    //
    //    @Test(expected = DbcException.class)
    //    public void testVisitorExc2() {
    //        EV3BrickConfiguration.Builder builder = new EV3BrickConfiguration.Builder();
    //        builder.visiting("regulated", "large");
    //        builder.visitingActorPort("X");
    //    }
}
