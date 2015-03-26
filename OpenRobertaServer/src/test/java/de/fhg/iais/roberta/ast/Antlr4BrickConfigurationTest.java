package de.fhg.iais.roberta.ast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationLexer;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.ConfContext;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.conf.transformer.ConfigurationParseTree2ConfigurationVisitor;
import de.fhg.iais.roberta.ev3.EV3Actors;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration.Builder;
import de.fhg.iais.roberta.ev3.EV3Sensors;
import de.fhg.iais.roberta.ev3.components.EV3Actor;
import de.fhg.iais.roberta.ev3.components.EV3Sensor;

public class Antlr4BrickConfigurationTest {
    private static final boolean DO_ASSERT = true;
    private static final boolean DO_PRINT = true;

    @Test
    public void testParsing() throws Exception {
        String actual =
            expr2String("ev3 Craesy-PID-2014 {"
                + "wheel diameter 5 cm track width 2.5 cm "
                + "sensor port 1 touch "
                + "actor port B middle motor { regulated forward }"
                + " }");
        String expected =
            "(conf ev3 Craesy-PID-2014 { "
                + "(sizes wheel diameter 5 cm track width 2.5 cm) "
                + "(connector sensor port 1 touch) "
                + "(connector actor port B (actor middle motor { (motorSpec regulated forward) }))"
                + " })";
        assertEquals(expected, actual);
    }

    @Test
    public void testParseError() throws Exception {
        String withSyntaxErrors =
            "ev3 Craesy-PID-2014 {"
                + "wheel diameter 5 cm track width 2.5 cm "
                + "sensor here in line 3, column 8,  there is a syntax error "
                + "actor port B middle motor { regulated forward }"
                + " }";
        BrickConfigurationParser parser = mkParser(withSyntaxErrors);
        ConfContext tree = parser.conf();
        RecognitionException recognitionException = tree.exception;
    }

    @Test
    public void testParseTree2ConfigurationEmpty() throws Exception {
        Builder expected = new EV3BrickConfiguration.Builder();
        expected.setWheelDiameter(5.0).setTrackWidth(2.5);
        BrickConfiguration actual =
            ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting("ev3 Craesy-PID-2014 {wheel diameter 5 cm track width 2.5 cm}");
        assertEquals(expected.build(), actual);
    }

    @Test
    public void testParseTree2Configuration1Sensor() throws Exception {
        Builder expectedBuilder = new EV3BrickConfiguration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR));
        EV3BrickConfiguration expected = expectedBuilder.build();

        BrickConfiguration actual1 = ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting( //
            "ev3 Craesy-PID-2014 {wheel diameter 5 cm track width 2.5 cm sensor 1 touch}");
        assertEquals(expected, actual1);

        String expectedAsString = expected.generateText("Craesy-PID-2014");
        BrickConfiguration actual2 = ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting(expectedAsString);
        assertEquals(expected, actual2);
    }

    @Test
    public void testParseTree2Configuration2Sensors() throws Exception {
        Builder expectedBuilder = new EV3BrickConfiguration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR));
        expectedBuilder.addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_IR_SENSOR));
        EV3BrickConfiguration expected = expectedBuilder.build();

        assertAllEquals("Craesy-PID-2014", expected, "ev3 Craesy-PID-2014 {wheel diameter 5 cm track width 2.5 cm sensor port 1 touch sensor port 4 infrared}");
    }

    @Test
    public void testParseTree2Configuration1Actor() throws Exception {
        Builder expectedBuilder = new EV3BrickConfiguration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.NONE));
        EV3BrickConfiguration expected = expectedBuilder.build();

        assertAllEquals("Craesy-PID-2014", expected, "ev3 Craesy-PID-2014 {"
            + "wheel diameter 5 cm\n"
            + "track width 2.5 cm\n"
            + "actor port A\n"
            + "middle motor { regulated foreward }\n"
            + " }");
    }

    @Test
    public void testParseTree2Configuration3Actors() throws Exception {
        Builder expectedBuilder = new EV3BrickConfiguration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.NONE));
        expectedBuilder.addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, false, DriveDirection.BACKWARD, MotorSide.LEFT));
        expectedBuilder.addActor(ActorPort.D, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, false, DriveDirection.BACKWARD, MotorSide.RIGHT));
        EV3BrickConfiguration expected = expectedBuilder.build();

        assertAllEquals("Craesy-PID-2015", expected, "ev3 Craesy-PID-2015 {"
            + "wheel diameter 5 cm track width 2.5 cm "
            + "actor port A middle motor { regulated foreward }"
            + "actor port C large motor { unregulated backward left }"
            + "actor port D large motor { unregulated backward right }"
            + " }");
    }

    @Test
    public void testParseTree2ConfigurationStandardConfiguration() throws Exception {
        Builder expectedBuilder = new EV3BrickConfiguration.Builder();
        expectedBuilder
            .setWheelDiameter(5.6)
            .setTrackWidth(13.5)
            .addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))
            .addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))
            .addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
            .addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT));
        EV3BrickConfiguration expected = expectedBuilder.build();

        assertAllEquals("EV3basis", expected, "ev3 EV3basis {"
            + "wheel diameter 5.6 cm track width 13.5 cm "
            + "sensor port 1 touch sensor port 4 ultrasonic "
            + "actor port B large motor { regulated foreward right }"
            + "actor port C large motor { regulated foreward left }"
            + " }");
    }

    private String expr2String(String expr) throws Exception {
        BrickConfigurationParser parser = mkParser(expr);
        ConfContext tree = parser.conf();
        return tree.toStringTree(parser);
    }

    private BrickConfigurationParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        BrickConfigurationLexer lex = new BrickConfigurationLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        BrickConfigurationParser parser = new BrickConfigurationParser(tokens);
        return parser;
    }

    private void assertAllEquals(String configurationName, EV3BrickConfiguration expected, String actualAsString) throws Exception {
        BrickConfiguration actual = ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting(actualAsString);
        assertEquals(expected, actual);

        String expectedAsString = expected.generateText(configurationName);
        BrickConfiguration actual2 = ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting(expectedAsString);
        assertEquals(expected, actual2);
    }

    private void assertEquals(Object expected, Object actual) {
        if ( DO_PRINT ) {
            System.out.println(expected);
            System.out.println(actual);
        }
        if ( DO_ASSERT ) {
            Assert.assertEquals(expected, actual);
        }
    }
}