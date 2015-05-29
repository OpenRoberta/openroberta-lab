package de.fhg.iais.roberta.ast;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Actors;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration.Builder;
import de.fhg.iais.roberta.ev3Configuration.generated.Ev3ConfigurationLexer;
import de.fhg.iais.roberta.ev3Configuration.generated.Ev3ConfigurationParser;
import de.fhg.iais.roberta.ev3Configuration.generated.Ev3ConfigurationParser.ConfContext;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.action.ev3.DriveDirection;
import de.fhg.iais.roberta.shared.action.ev3.MotorSide;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.transformer.ev3.Ev3ConfigurationParseTree2Ev3ConfigurationVisitor;
import de.fhg.iais.roberta.util.Option;

public class Antlr4Ev3ConfigurationTest {
    private static final boolean DO_ASSERT = true;
    private static final boolean DO_PRINT = true;

    @Test
    public void testParsing() throws Exception {
        String actual =
            expr2String("robot ev3 Craesy-PID-2014 {"
                + "size { wheel diameter 5 cm; track width 2.5 cm; } "
                + "sensor port { 1: touch; } "
                + "actor port { B: middle motor, regulated, forward; }"
                + " }");
        String expected = "" //
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
        Builder expected = new Ev3Configuration.Builder();
        expected.setWheelDiameter(5.0).setTrackWidth(2.5);
        Ev3Configuration actual =
            Ev3ConfigurationParseTree2Ev3ConfigurationVisitor
            .startWalkForVisiting("robot ev3 Craesy-PID-2014 { size {wheel diameter 5 cm;track width 2.5 cm;} }")
                .getVal();
        assertEquals(expected.build(), actual);
    }

    @Test
    public void testParseTree2Configuration1Sensor() throws Exception {
        Builder expectedBuilder = new Ev3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR));
        Ev3Configuration expected = expectedBuilder.build();

        Ev3Configuration actual1 = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting( //
            "robot ev3 Craesy-PID-2014 { size { wheel diameter 5 cm; track width 2.5 cm; } sensor port {1: touch; } }").getVal();
        assertEquals(expected, actual1);

        String expectedAsString = expected.generateText("Craesy-PID-2014");
        Ev3Configuration actual2 = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(expectedAsString).getVal();
        assertEquals(expected, actual2);
    }

    @Test
    public void testParseTree2Configuration2Sensors() throws Exception {
        Builder expectedBuilder = new Ev3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR));
        expectedBuilder.addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_IR_SENSOR));
        Ev3Configuration expected = expectedBuilder.build();

        assertAllEquals("Craesy-PID-2014", expected, "robot ev3 Craesy-PID-2014 { "
            + "size { wheel diameter 5 cm; track width 2.5 cm; } "
            + "sensor port { 1: touch; 4: infrared } }");
    }

    @Test
    public void testParseTree2Configuration1Actor() throws Exception {
        Builder expectedBuilder = new Ev3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.NONE));
        Ev3Configuration expected = expectedBuilder.build();

        assertAllEquals("Craesy-PID-2014", expected, "robot ev3 Craesy-PID-2014 { size {"
            + "wheel diameter 5 cm;\n"
            + "track width 2.5 cm;\n}\n"
            + "actor port { A:\n"
            + "middle motor, regulated, forward;\n"
            + "}}");
    }

    @Test
    public void testParseTree2Configuration3Actors() throws Exception {
        Builder expectedBuilder = new Ev3Configuration.Builder();
        expectedBuilder.setWheelDiameter(5.0).setTrackWidth(2.5);
        expectedBuilder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.NONE));
        expectedBuilder.addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, false, DriveDirection.BACKWARD, MotorSide.LEFT));
        expectedBuilder.addActor(ActorPort.D, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, false, DriveDirection.BACKWARD, MotorSide.RIGHT));
        Ev3Configuration expected = expectedBuilder.build();

        assertAllEquals("Craesy-PID-2015", expected, "robot ev3 Craesy-PID-2015 {"
            + "size { wheel diameter 5 cm; track width 2.5 cm; } "
            + "actor port {"
            + "A: middle motor, regulated, forward;\n"
            + "C: large motor, unregulated, backward, left;\n"
            + "D: large motor, unregulated, backward, right;\n"
            + "} }");
    }

    @Test
    public void testParseTree2ConfigurationStandardConfiguration() throws Exception {
        Builder expectedBuilder = new Ev3Configuration.Builder();
        expectedBuilder
            .setWheelDiameter(5.6)
            .setTrackWidth(13.5)
            .addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))
            .addSensor(SensorPort.S4, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))
            .addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
            .addActor(ActorPort.C, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT));
        Ev3Configuration expected = expectedBuilder.build();

        assertAllEquals("EV3basis", expected, "robot ev3 EV3basis {\n"
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
        Option<Ev3Configuration> confResult = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(conf);
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

    private void assertAllEquals(String configurationName, Ev3Configuration expected, String actualAsString) throws Exception {
        Ev3Configuration actual = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(actualAsString).getVal();
        assertEquals(expected, actual);

        String expectedAsString = expected.generateText(configurationName);
        Ev3Configuration actual2 = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(expectedAsString).getVal();
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