package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.hardwarecheck.ev3.UsedHardwareVisitor;
import de.fhg.iais.roberta.testutil.Helper;

public class EV3ProgramUsedHardwareCheckTest {
    @Test
    public void test0ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test1ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check1.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test2ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check2.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH], UsedSensor [S3, COLOR, COLOUR]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test3ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check3.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH], UsedSensor [S4, ULTRASONIC, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test4ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check4.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals(
            "[UsedSensor [S4, INFRARED, DISTANCE], UsedSensor [S4, ULTRASONIC, DISTANCE], UsedSensor [S2, GYRO, RESET], UsedSensor [S1, TOUCH, TOUCH]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE], UsedActor [A, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test5ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test6ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt1.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test7ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt2.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test8ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt3.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[UsedSensor [S4, INFRARED, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test9ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check5.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test10ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check6.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals(
            "[UsedSensor [S3, COLOR, COLOUR], UsedSensor [S4, INFRARED, DISTANCE], UsedSensor [S4, ULTRASONIC, DISTANCE]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test11ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/methods/method_return_3.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test12ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/hardware_check7.xml");

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrases);
        Assert.assertEquals(
            "[UsedSensor [S3, COLOR, COLOUR], UsedSensor [S3, COLOR, AMBIENTLIGHT], UsedSensor [S4, COLOR, RED]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }
}
