package de.fhg.iais.roberta.ast;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.transformer.JaxbBrickConfigTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.conf.transformer.ConfigurationParseTree2ConfigurationVisitor;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.jaxb.JaxbHelper;

public class BrickConfigurationTest {

    @Test
    public void testRoundtrip() throws Exception {
        testRoundtrip("brick_configuration0");
    }

    @Test
    public void testText2Text() throws Exception {
        testText2Text("brick_configuration0", "craesy");
        testText2Text("brick_configuration4", "craesy");
        testText2Text("standard_ev3_configuration", "EV3basis");
    }

    @Test
    public void test1() throws Exception {
        String a =
            "private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()"
                + ".setWheelDiameter(5.0)"
                + ".setTrackWidth(17.0)"
                + ".addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.NONE))"
                + ".addSensor(SensorPort.S3, new EV3Sensor(EV3Sensors.EV3_IR_SENSOR))"
                + ".build();";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration1.xml");
        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        BrickConfiguration b = transformer.transform(project);
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void test2() throws Exception {
        String a =
            "private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()"
                + ".setWheelDiameter(5.6)."
                + "setTrackWidth(17.0)"
                + ".addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))"
                + ".addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))"
                + ".addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))"
                + ".addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))"
                + ".build();";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration2.xml");
        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        BrickConfiguration b = transformer.transform(project);
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void test3() throws Exception {
        String a =
            "private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()"
                + ".setWheelDiameter(5.6)."
                + "setTrackWidth(17.0)"
                + ".addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))"
                + ".addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))"
                + ".addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))"
                + ".addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))"
                + ".build();";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration3.xml");
        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        BrickConfiguration b = transformer.transform(project);
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }

    /**
     * ROUND TRIP FOR CONFIGURATION TESTS. Expects three different representations of a configuration as text files.<br>
     * <br>
     * 1. make a String from a XML file, call it xmlExpected, and generate a BrickConfiguration bc1<br>
     * 2. from the BrickConfiguration bc1 generate the generateRegenerate snippet cg and check it<br>
     * 3. from the BrickConfiguration bc1 generate text and check it against textExpected<br>
     * 4. from the text generate a BrickConfiguration bc2 and check it against bc1<br>
     * 5. from the BrickConfiguration bc2 generate XML xmlActual and check it against xmlExpected<br>
     * 6. from xmlActual generate a BrickConfiguration bc3 and check it against bc1 and bc2.<br>
     */
    private void testRoundtrip(String baseName) throws Exception {
        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        // 1.
        String xmlExpected = resourceAsString(baseName + ".xml");
        BlockSet bs1 = JaxbHelper.xml2BlockSet(xmlExpected);
        BrickConfiguration bc1 = transformer.transform(bs1);
        // 2.
        String cgExpected = resourceAsString(baseName + ".gGs");
        String cg = bc1.generateRegenerate();
        assertEq(cgExpected, cg);
        // 3.
        String textExpected = resourceAsString(baseName + ".conf");
        String text = bc1.generateText("craesy");
        assertEq(textExpected, text);
        // 4.
        EV3BrickConfiguration bc2 = (EV3BrickConfiguration) ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting(text);
        Assert.assertEquals(bc1, bc2);
        // 5.
        BlockSet bs2 = transformer.transformInverse(bc2);
        String xmlActual = JaxbHelper.blockSet2xml(bs2);
        assertEq(xmlExpected, xmlActual);
        // 6.
        BlockSet bs3 = JaxbHelper.xml2BlockSet(xmlActual);
        BrickConfiguration bc3 = transformer.transform(bs3);
        Assert.assertEquals(bc1, bc3);
        Assert.assertEquals(bc2, bc3);
    }

    /**
     * TEST TEXT to TEXT TRANSFORMATION. Expects a textual representation of a configuration as text file.<br>
     * <br>
     * 1. make a String from a textual configuration file, call it text1<br>
     * 2. generate a BrickConfiguration bc1 from text1<br>
     * 3. from the BrickConfiguration bc1 generate text2<br>
     * 4. check text1 against text2.<br>
     * 5. generate a BrickConfiguration bc2 from text2<br>
     * 6. check bc1 against bc2.<br>
     */
    private void testText2Text(String baseName, String name) throws Exception {
        String text1 = resourceAsString(baseName + ".conf"); // 1.
        EV3BrickConfiguration bc1 = (EV3BrickConfiguration) ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting(text1); // 2.
        String text2 = bc1.generateText(name); // 3.
        assertEq(text1, text2); // 4.
        EV3BrickConfiguration bc2 = (EV3BrickConfiguration) ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting(text1);
        Assert.assertEquals(bc1, bc2);
    }

    private String resourceAsString(String name) throws Exception {
        return IOUtils.toString(BrickConfigurationTest.class.getResourceAsStream("/ast/brickConfiguration/" + name), "UTF-8");
    }

    private void assertEq(String expected, String actual) {
        Assert.assertEquals(expected.replaceAll("\\s+", ""), actual.replaceAll("\\s+", ""));
    }
}
