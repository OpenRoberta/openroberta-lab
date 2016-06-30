package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.hardwarecheck.generic.UsedSensorsCheckVisitor;
import de.fhg.iais.roberta.testutil.Helper;

public class EV3ProgramUsedHardwareCheckTest {
    @Test
    public void test0ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test1ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator1.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test2ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator2.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH], UsedSensor [S3, COLOR, COLOUR]]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test3ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator3.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH], UsedSensor [S4, ULTRASONIC, DISTANCE]]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test4ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator4.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[UsedSensor [S4, INFRARED, DISTANCE], UsedSensor [S4, ULTRASONIC, DISTANCE], UsedSensor [S2, GYRO, RESET], UsedSensor [S1, TOUCH, TOUCH]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test5ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test6ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt1.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH]]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test7ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt2.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test8ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt3.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[UsedSensor [S4, INFRARED, DISTANCE]]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test9ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/methods/method_void_1.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test10ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/methods/method_void_4.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[UsedSensor [S3, COLOR, COLOUR], UsedSensor [S4, INFRARED, DISTANCE], UsedSensor [S4, ULTRASONIC, DISTANCE]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test11ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/methods/method_return_3.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[UsedSensor [S3, COLOR, COLOUR], UsedSensor [S1, TOUCH, TOUCH], UsedSensor [S4, INFRARED, DISTANCE], UsedSensor [S2, GYRO, ANGLE], UsedSensor [S4, ULTRASONIC, DISTANCE]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test12ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator10.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[UsedSensor [S3, COLOR, COLOUR], UsedSensor [S3, COLOR, AMBIENTLIGHT], UsedSensor [S4, COLOR, RED]]",
            hardwareCheckVisitor.toString());
    }
}
