package de.fhg.iais.roberta.ast;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Configuration.Builder;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.ev3Configuration.generated.Ev3ConfigurationLexer;
import de.fhg.iais.roberta.ev3Configuration.generated.Ev3ConfigurationParser;
import de.fhg.iais.roberta.ev3Configuration.generated.Ev3ConfigurationParser.ConfContext;
import de.fhg.iais.roberta.factory.ev3.lejos.v0.Factory;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.MoveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.transformer.ev3.Ev3ConfigurationParseTree2Ev3ConfigurationVisitor;
import de.fhg.iais.roberta.util.Option;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Antlr4Ev3ConfigurationTest {
    Factory robotModeFactory = new Factory();
    private static final boolean DO_ASSERT = true;
    private static final boolean DO_PRINT = false;

    @BeforeClass
    public static void loadPropertiesForTests() {
        Properties properties = Util1.loadProperties(null);
        RobertaProperties.setRobertaProperties(properties);
    }

    @Test
    public void testParsing() throws Exception {
        String actual =
            expr2String(
                "robot ev3 Craesy-PID-2014 {"
                    + "size { wheel diameter 5 cm; track width 2.5 cm; } "
                    + "sensor port { 1: touch; } "
                    + "actor port { B: middle motor, regulated, forward; }"
                    + " }");
        String expected =
            "" //
                + "(conf robot ev3 Craesy-PID-2014 { "
                + "(sizes size { wheel diameter 5 cm ; track width 2.5 cm ; }) "
                + "(sensors sensor port { (sdecl 1 : touch ;) }) "
                + "(actors actor port { (adecl B : (actor middle motor , (motorSpec regulated , forward)) ;) })"
                + " })";
        assertEquals(expected, actual);
    }

    @Test
    public void testParseError() throws Exception {
        String withSyntaxErrors =
            "robot ev3 Craesy-PID-2014 {\n"
                + "size { wheel diameter 5 cm; track width 2.5 cm; }\n"
                + "sensor here in line 3, column 8,  there is a syntax error\n"
                + "actor port B middle motor { regulated forward }\n"
                + " }";
        final boolean[] errorFound = new boolean[1];
        errorFound[0] = false;
        Ev3ConfigurationParser parser = mkParser(withSyntaxErrors);
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                if ( line == 3 && charPositionInLine == 7 ) {
                    errorFound[0] = true;
                }
            }
        });
        parser.conf();
        Assert.assertTrue("parse error not found", errorFound[0]);
    }

    @Test
    public void testParseTree2ConfigurationEmpty() throws Exception {
        Builder<EV3Configuration.Builder> expected = new EV3Configuration.Builder();
        expected.setWheelDiameter(5.0).setTrackWidth(2.5);
        Configuration actual =
            Ev3ConfigurationParseTree2Ev3ConfigurationVisitor
                .startWalkForVisiting("robot ev3 Craesy-PID-2014 { size {wheel diameter 5 cm;track width 2.5 cm;} }", this.robotModeFactory)
                .getVal();
        assertEquals(expected.build(), actual);

    }

    @Test
    public void testParseTree2Configuration1Sensor() throws Exception {
        Builder<EV3Configuration.Builder> expectedBuilder = new EV3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH));
        EV3Configuration expected = (EV3Configuration) expectedBuilder.build();

        Configuration actual1 =
            Ev3ConfigurationParseTree2Ev3ConfigurationVisitor
                .startWalkForVisiting( //
                    "robot ev3 Craesy-PID-2014 { size { wheel diameter 5 cm; track width 2.5 cm; } sensor port {1: touch; } }",
                    this.robotModeFactory)
                .getVal();
        assertEquals(expected, actual1);

        String expectedAsString = expected.generateText("Craesy-PID-2014");
        Configuration actual2 = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(expectedAsString, this.robotModeFactory).getVal();
        assertEquals(expected, actual2);
    }

    @Test
    public void testParseTree2Configuration2Sensors() throws Exception {
        Builder<EV3Configuration.Builder> expectedBuilder = new EV3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH));
        expectedBuilder.addSensor(SensorPort.S4, new Sensor(SensorType.INFRARED));
        EV3Configuration expected = (EV3Configuration) expectedBuilder.build();

        assertAllEquals(
            "Craesy-PID-2014",
            expected,
            "robot ev3 Craesy-PID-2014 { " + "size { wheel diameter 5 cm; track width 2.5 cm; } " + "sensor port { 1: touch; 4: infrared } }");
    }

    @Test
    public void testParseTree2Configuration1Actor() throws Exception {
        Builder<EV3Configuration.Builder> expectedBuilder = new EV3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, MoveDirection.FOREWARD, MotorSide.NONE));
        EV3Configuration expected = (EV3Configuration) expectedBuilder.build();

        assertAllEquals(
            "Craesy-PID-2014",
            expected,
            "robot ev3 Craesy-PID-2014 { size {"
                + "wheel diameter 5 cm;\n"
                + "track width 2.5 cm;\n}\n"
                + "actor port { A:\n"
                + "middle motor, regulated, forward;\n"
                + "}}");
    }

    @Test
    public void testParseTree2Configuration3Actors() throws Exception {
        Builder<EV3Configuration.Builder> expectedBuilder = new EV3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, MoveDirection.FOREWARD, MotorSide.NONE));
        expectedBuilder.addActor(ActorPort.C, new Actor(ActorType.LARGE, false, MoveDirection.BACKWARD, MotorSide.LEFT));
        expectedBuilder.addActor(ActorPort.D, new Actor(ActorType.LARGE, false, MoveDirection.BACKWARD, MotorSide.RIGHT));
        EV3Configuration expected = (EV3Configuration) expectedBuilder.build();

        assertAllEquals(
            "Craesy-PID-2015",
            expected,
            "robot ev3 Craesy-PID-2015 {"
                + "size { wheel diameter 5 cm; track width 2.5 cm; } "
                + "actor port {"
                + "A: middle motor, regulated, forward;\n"
                + "C: large motor, unregulated, backward, left;\n"
                + "D: large motor, unregulated, backward, right;\n"
                + "} }");
    }

    @Test
    public void testParseTree2ConfigurationStandardConfiguration() throws Exception {
        Builder<EV3Configuration.Builder> expectedBuilder = new EV3Configuration.Builder();
        expectedBuilder
            .setWheelDiameter(5.6)
            .setTrackWidth(13.5)
            .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))
            .addSensor(SensorPort.S4, new Sensor(SensorType.ULTRASONIC))
            .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, MoveDirection.FOREWARD, MotorSide.RIGHT))
            .addActor(ActorPort.C, new Actor(ActorType.LARGE, true, MoveDirection.FOREWARD, MotorSide.LEFT));
        EV3Configuration expected = (EV3Configuration) expectedBuilder.build();

        assertAllEquals(
            "EV3basis",
            expected,
            "robot ev3 EV3basis {\n"
                + "size { wheel diameter 5.6 cm; track width 13.5 cm; }\n"
                + "sensor port { 1: touch; 4: ultrasonic; }\n"
                + "actor port { B: large motor, regulated, forward, right;\n"
                + "             C: large motor, regulated, forward, left;"
                + "}}");
    }

    @Test
    public void testParseTree2ConfigurationParseError() {
        String conf =
            "robot ev3 EV3basis {\n"
                + "size { wheel diameter 5.6 cm; track width 13.5 cm; }\n"
                + "sensor port { 1: touch; 4: ultrasonic; }\n"
                + "actor port { B: large mOtOr, regulated, forward, right;\n"
                + "             C: large motor, regulated, forward, left;"
                + "}}";
        Option<Configuration> confResult = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(conf, this.robotModeFactory);
        assertTrue(!confResult.isSet());
        assertTrue(confResult.getMessage().matches(".*mOtOr.*"));
    }

    private String expr2String(String expr) throws Exception {
        Ev3ConfigurationParser parser = mkParser(expr);
        ConfContext tree = parser.conf();
        return tree.toStringTree(parser);
    }

    private Ev3ConfigurationParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        Ev3ConfigurationLexer lex = new Ev3ConfigurationLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        Ev3ConfigurationParser parser = new Ev3ConfigurationParser(tokens);
        return parser;
    }

    private void assertAllEquals(String configurationName, Configuration expected, String actualAsString) throws Exception {
        Configuration actual = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(actualAsString, this.robotModeFactory).getVal();
        assertEquals(expected, actual);

        String expectedAsString = expected.generateText(configurationName);
        Configuration actual2 = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(expectedAsString, this.robotModeFactory).getVal();
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