package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.ev3.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class EV3ProgramUsedHardwareCheckTest {

    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    private Configuration makeConfiguration() {
        return new EV3Configuration.Builder()
            .setTrackWidth(17)
            .setWheelDiameter(5.6)
            .addActor(new ActorPort("A", "MA"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(new ActorPort("B", "MB"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
            .addActor(new ActorPort("D", "MD"), new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.NONE))
            .addSensor(new SensorPort("1", "S1"), new Sensor(SensorType.TOUCH))
            .addSensor(new SensorPort("2", "S2"), new Sensor(SensorType.ULTRASONIC))
            .build();
    }

    @Test
    public void test0ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test1ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check1.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test2ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check2.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, TOUCH, TOUCH], UsedSensor [3, COLOR, COLOUR]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test3ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check3.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, TOUCH, TOUCH], UsedSensor [4, ULTRASONIC, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test4ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check4.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals(
            "[UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE], UsedSensor [1, TOUCH, TOUCH]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE], UsedActor [A, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test5ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test6ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt1.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, TOUCH, TOUCH]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test7ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt2.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test8ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt3.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [1, INFRARED, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test9ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check5.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test10ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check6.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals(
            "[UsedSensor [3, COLOR, COLOUR], UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test11ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/methods/method_return_3.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test12ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check7.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals(
            "[UsedSensor [3, COLOR, COLOUR], UsedSensor [3, COLOR, AMBIENTLIGHT], UsedSensor [4, COLOR, RED]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test13ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check8.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [D, MEDIUM]]", checkVisitor.getUsedActors().toString());
    }
}
