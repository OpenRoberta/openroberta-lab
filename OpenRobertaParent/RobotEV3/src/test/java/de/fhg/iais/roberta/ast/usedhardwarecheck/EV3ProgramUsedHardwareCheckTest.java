package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedHardwareCollectorVisitor;

public class EV3ProgramUsedHardwareCheckTest {

    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    private static Configuration makeConfiguration() {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", BlocklyConstants.NO_SLOT, "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", BlocklyConstants.NO_SLOT, "B", motorBproperties);

        Map<String, String> motorDproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorD = new ConfigurationComponent("MEDIUM", true, "D", BlocklyConstants.NO_SLOT, "D", motorDproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", BlocklyConstants.NO_SLOT, "1", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S2", BlocklyConstants.NO_SLOT, "2", Collections.emptyMap());

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorD, touchSensor, ultrasonicSensor));
        return builder.build();
    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }

    @Test
    public void test0ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test1ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check1.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test2ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check2.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [3, COLOR, COLOUR]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test3ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check3.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [4, ULTRASONIC, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test4ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check4.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert
            .assertEquals(
                "[UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE], UsedSensor [1, TOUCH, DEFAULT]]",
                checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE], UsedActor [A, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test5ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test6ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt1.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, TOUCH, PRESSED]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test7ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt2.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test8ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt3.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, INFRARED, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test9ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check5.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test10ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check6.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert
            .assertEquals(
                "[UsedSensor [3, COLOR, COLOUR], UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE]]",
                checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test11ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/methods/method_return_3.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test12ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check7.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert
            .assertEquals(
                "[UsedSensor [3, COLOR, COLOUR], UsedSensor [3, COLOR, AMBIENTLIGHT], UsedSensor [4, COLOR, LIGHT]]",
                checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test13ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check8.xml");

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [D, MEDIUM]]", checkVisitor.getUsedActors().toString());
    }
}
