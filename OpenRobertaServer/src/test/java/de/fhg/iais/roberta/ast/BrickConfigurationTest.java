package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.transformer.JaxbBrickConfigTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.jaxb.JaxbHelper;

public class BrickConfigurationTest {

    @Test
    public void test0() throws Exception {
        String a =
            "private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()"
                + ".setWheelDiameter(5.6)"
                + ".setTrackWidth(17.0)"
                + ".addActor(ActorPort.A, new EV3Actor(HardwareComponentEV3Actor.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))"
                + ".addActor(ActorPort.B, new EV3Actor(HardwareComponentEV3Actor.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.NONE))"
                + ".addSensor(SensorPort.S1, new EV3Sensor(HardwareComponentEV3Sensor.EV3_TOUCH_SENSOR))"
                + ".addSensor(SensorPort.S2, new EV3Sensor(HardwareComponentEV3Sensor.EV3_COLOR_SENSOR))"
                + ".addSensor(SensorPort.S3, new EV3Sensor(HardwareComponentEV3Sensor.EV3_ULTRASONIC_SENSOR))"
                + ".build();";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration0.xml");
        JaxbBrickConfigTransformer<Void> transformer = new JaxbBrickConfigTransformer<>();
        BrickConfiguration b = transformer.transform(project);
        System.out.println(b.generateRegenerate());
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void test1() throws Exception {
        String a =
            "private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()"
                + ".setWheelDiameter(5.0)"
                + ".setTrackWidth(17.0)"
                + ".addActor(ActorPort.A, new EV3Actor(HardwareComponentEV3Actor.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))"
                + ".addSensor(SensorPort.S3, new EV3Sensor(HardwareComponentEV3Sensor.EV3_IR_SENSOR))"
                + ".build();";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration1.xml");
        JaxbBrickConfigTransformer<Void> transformer = new JaxbBrickConfigTransformer<>();
        BrickConfiguration b = transformer.transform(project);
        System.out.println(b.generateRegenerate());
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void test2() throws Exception {
        String a =
            "private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()"
                + ".setWheelDiameter(5.6)."
                + "setTrackWidth(17.0)"
                + ".addActor(ActorPort.B, new EV3Actor(HardwareComponentEV3Actor.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))"
                + ".addActor(ActorPort.C, new EV3Actor(HardwareComponentEV3Actor.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))"
                + ".addSensor(SensorPort.S1, new EV3Sensor(HardwareComponentEV3Sensor.EV3_TOUCH_SENSOR))"
                + ".addSensor(SensorPort.S4, new EV3Sensor(HardwareComponentEV3Sensor.EV3_ULTRASONIC_SENSOR))"
                + ".build();";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration2.xml");
        JaxbBrickConfigTransformer<Void> transformer = new JaxbBrickConfigTransformer<>();
        BrickConfiguration b = transformer.transform(project);
        System.out.println(b.generateRegenerate());
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void test3() throws Exception {
        String a =
            "private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()"
                + ".setWheelDiameter(5.6)."
                + "setTrackWidth(17.0)"
                + ".addActor(ActorPort.B, new EV3Actor(HardwareComponentEV3Actor.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))"
                + ".addActor(ActorPort.C, new EV3Actor(HardwareComponentEV3Actor.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))"
                + ".addSensor(SensorPort.S1, new EV3Sensor(HardwareComponentEV3Sensor.EV3_TOUCH_SENSOR))"
                + ".addSensor(SensorPort.S4, new EV3Sensor(HardwareComponentEV3Sensor.EV3_ULTRASONIC_SENSOR))"
                + ".build();";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration3.xml");
        JaxbBrickConfigTransformer<Void> transformer = new JaxbBrickConfigTransformer<>();
        BrickConfiguration b = transformer.transform(project);
        System.out.println(b.generateRegenerate());
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }
}
