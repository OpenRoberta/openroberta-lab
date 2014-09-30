package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.dbc.DbcException;

public class BrickConfigurationTest {
    private final String expectedBrickConfigurationGenerator = //
        "privateBrickConfigurationbrickConfiguration=newBrickConfiguration.Builder()"
            + ".setWheelDiameter(0.0)"
            + ".setTrackWidth(0.0)"
            + ".addActor(ActorPort.A,newHardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor,DriveDirection.FOREWARD,MotorSide.LEFT))"
            + ".addActor(ActorPort.B,newHardwareComponent(HardwareComponentType.EV3MediumRegulatedMotor,DriveDirection.FOREWARD,MotorSide.RIGHT))"
            + ".addSensor(SensorPort.S1,newHardwareComponent(HardwareComponentType.EV3UltrasonicSensor))"
            + ".addSensor(SensorPort.S4,newHardwareComponent(HardwareComponentType.EV3ColorSensor))"
            + ".build();";

    @Test
    public void testBuilder() {
        BrickConfiguration.Builder builder = new BrickConfiguration.Builder();
        builder.addActor(ActorPort.A, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT));
        builder.addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3MediumRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3UltrasonicSensor));
        builder.addSensor(SensorPort.S4, new HardwareComponent(HardwareComponentType.EV3ColorSensor));
        BrickConfiguration conf = builder.build();

        assertEquals(HardwareComponentType.EV3LargeRegulatedMotor, conf.getActorA().getComponentType());
        assertEquals(HardwareComponentType.EV3MediumRegulatedMotor, conf.getActorB().getComponentType());
        assertNull(conf.getActorC());
        assertEquals(HardwareComponentType.EV3UltrasonicSensor, conf.getSensor1().getComponentType());
        assertEquals(HardwareComponentType.EV3ColorSensor, conf.getSensor4().getComponentType());
        assertNull(conf.getSensor2());

        assertEquals(this.expectedBrickConfigurationGenerator, conf.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void testBuilderFluent() {
        BrickConfiguration conf =
            new BrickConfiguration.Builder()
                .addActor(ActorPort.A, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3MediumRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3UltrasonicSensor))
                .addSensor(SensorPort.S4, new HardwareComponent(HardwareComponentType.EV3ColorSensor))
                .build();

        assertEquals(HardwareComponentType.EV3LargeRegulatedMotor, conf.getActorA().getComponentType());
        assertEquals(HardwareComponentType.EV3MediumRegulatedMotor, conf.getActorB().getComponentType());
        assertNull(conf.getActorC());
        assertEquals(HardwareComponentType.EV3UltrasonicSensor, conf.getSensor1().getComponentType());
        assertEquals(HardwareComponentType.EV3ColorSensor, conf.getSensor4().getComponentType());
        assertNull(conf.getSensor2());

        assertEquals(this.expectedBrickConfigurationGenerator, conf.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void testVisitorBuilder() {
        BrickConfiguration.Builder builder = new BrickConfiguration.Builder();
        builder.visiting("regulated", "large", "left", "off");
        builder.visitingActorPort("A");
        builder.visiting("regulated", "middle", "right", "off");
        builder.visitingActorPort("B");
        builder.visiting("ultrasonic");
        builder.visitingSensorPort("1");
        builder.visiting("Farbe");
        builder.visitingSensorPort("4");
        BrickConfiguration conf = builder.build();

        assertEquals(HardwareComponentType.EV3LargeRegulatedMotor, conf.getActorA().getComponentType());
        assertEquals(HardwareComponentType.EV3MediumRegulatedMotor, conf.getActorB().getComponentType());
        assertEquals(HardwareComponentType.EV3UltrasonicSensor, conf.getSensor1().getComponentType());
        assertEquals(HardwareComponentType.EV3ColorSensor, conf.getSensor4().getComponentType());

        assertEquals(this.expectedBrickConfigurationGenerator, conf.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test(expected = DbcException.class)
    public void testVisitorExc1() {
        BrickConfiguration.Builder builder = new BrickConfiguration.Builder();
        builder.visiting("regulated", "latsch");
        builder.visitingActorPort("A");
    }

    @Test(expected = DbcException.class)
    public void testVisitorExc2() {
        BrickConfiguration.Builder builder = new BrickConfiguration.Builder();
        builder.visiting("regulated", "large");
        builder.visitingActorPort("X");
    }
}
