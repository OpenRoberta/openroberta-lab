package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.dbc.DbcException;

public class BrickConfigurationTest {
    private final String expectedBrickConfigurationGenerator = //
        "privateBrickConfigurationbrickConfiguration=newBrickConfiguration.Builder()"
            + ".addActor(ActorPort.A,HardwareComponent.EV3LargeRegulatedMotor)"
            + ".addActor(ActorPort.B,HardwareComponent.EV3MediumRegulatedMotor)"
            + ".addSensor(SensorPort.S1,HardwareComponent.EV3UltrasonicSensor)"
            + ".addSensor(SensorPort.S4,HardwareComponent.EV3ColorSensor)"
            + ".build();";

    @Test
    public void testBuilder() {
        BrickConfigurationOld.Builder builder = new BrickConfigurationOld.Builder();
        builder.addActor(ActorPort.A, HardwareComponentOld.EV3LargeRegulatedMotor);
        builder.addActor(ActorPort.B, HardwareComponentOld.EV3MediumRegulatedMotor);
        builder.addSensor(SensorPort.S1, HardwareComponentOld.EV3UltrasonicSensor);
        builder.addSensor(SensorPort.S4, HardwareComponentOld.EV3ColorSensor);
        BrickConfigurationOld conf = builder.build();

        assertEquals(HardwareComponentOld.EV3LargeRegulatedMotor, conf.getActorA());
        assertEquals(HardwareComponentOld.EV3MediumRegulatedMotor, conf.getActorB());
        assertNull(conf.getActorC());
        assertEquals(HardwareComponentOld.EV3UltrasonicSensor, conf.getSensor1());
        assertEquals(HardwareComponentOld.EV3ColorSensor, conf.getSensor4());
        assertNull(conf.getSensor2());

        assertEquals(this.expectedBrickConfigurationGenerator, conf.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void testBuilderFluent() {
        BrickConfigurationOld conf =
            new BrickConfigurationOld.Builder()
                .addActor(ActorPort.A, HardwareComponentOld.EV3LargeRegulatedMotor)
                .addActor(ActorPort.B, HardwareComponentOld.EV3MediumRegulatedMotor)
                .addSensor(SensorPort.S1, HardwareComponentOld.EV3UltrasonicSensor)
                .addSensor(SensorPort.S4, HardwareComponentOld.EV3ColorSensor)
                .build();

        assertEquals(HardwareComponentOld.EV3LargeRegulatedMotor, conf.getActorA());
        assertEquals(HardwareComponentOld.EV3MediumRegulatedMotor, conf.getActorB());
        assertNull(conf.getActorC());
        assertEquals(HardwareComponentOld.EV3UltrasonicSensor, conf.getSensor1());
        assertEquals(HardwareComponentOld.EV3ColorSensor, conf.getSensor4());
        assertNull(conf.getSensor2());

        assertEquals(this.expectedBrickConfigurationGenerator, conf.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void testVisitorBuilder() {
        BrickConfigurationOld.Builder builder = new BrickConfigurationOld.Builder();
        builder.visiting("regulated", "large");
        builder.visitingActorPort("A");
        builder.visiting("regulated", "middle");
        builder.visitingActorPort("B");
        builder.visiting("ultrasonic");
        builder.visitingSensorPort("1");
        builder.visiting("Farbe");
        builder.visitingSensorPort("4");
        BrickConfigurationOld conf = builder.build();

        assertEquals(HardwareComponentOld.EV3LargeRegulatedMotor, conf.getActorA());
        assertEquals(HardwareComponentOld.EV3MediumRegulatedMotor, conf.getActorB());
        assertEquals(HardwareComponentOld.EV3UltrasonicSensor, conf.getSensor1());
        assertEquals(HardwareComponentOld.EV3ColorSensor, conf.getSensor4());

        assertEquals(this.expectedBrickConfigurationGenerator, conf.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test(expected = DbcException.class)
    public void testVisitorExc1() {
        BrickConfigurationOld.Builder builder = new BrickConfigurationOld.Builder();
        builder.visiting("regulated", "latsch");
        builder.visitingActorPort("A");
    }

    @Test(expected = DbcException.class)
    public void testVisitorExc2() {
        BrickConfigurationOld.Builder builder = new BrickConfigurationOld.Builder();
        builder.visiting("regulated", "large");
        builder.visitingActorPort("X");
    }
}
