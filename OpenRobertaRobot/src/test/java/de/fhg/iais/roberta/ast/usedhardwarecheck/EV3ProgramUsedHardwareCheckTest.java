package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.ev3.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.hardwarecheck.ev3.UsedSensorsCheckVisitor;
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
        Assert.assertEquals(
            "[[S1, HardwareComponentType [robBrick_touch, SENSOR], TOUCH], [S3, HardwareComponentType [robBrick_colour, SENSOR], COLOUR]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test3ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator3.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[[S1, HardwareComponentType [robBrick_touch, SENSOR], TOUCH], [S4, HardwareComponentType [robBrick_ultrasonic, SENSOR], DISTANCE]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test4ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator4.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[[S4, HardwareComponentType [robBrick_infrared, SENSOR], DISTANCE], [S4, HardwareComponentType [robBrick_ultrasonic, SENSOR], DISTANCE], [S2, HardwareComponentType [robBrick_gyro, SENSOR], RESET], [S1, HardwareComponentType [robBrick_touch, SENSOR], TOUCH]]",
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
        Assert.assertEquals("[[S1, HardwareComponentType [robBrick_touch, SENSOR], TOUCH]]", hardwareCheckVisitor.toString());
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
        Assert.assertEquals("[[S4, HardwareComponentType [robBrick_infrared, SENSOR], DISTANCE]]", hardwareCheckVisitor.toString());
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
            "[[S3, HardwareComponentType [robBrick_colour, SENSOR], COLOUR], [S4, HardwareComponentType [robBrick_infrared, SENSOR], DISTANCE], [S4, HardwareComponentType [robBrick_ultrasonic, SENSOR], DISTANCE]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test11ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/methods/method_return_3.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[[S3, HardwareComponentType [robBrick_colour, SENSOR], COLOUR], [S1, HardwareComponentType [robBrick_touch, SENSOR], TOUCH], [S4, HardwareComponentType [robBrick_infrared, SENSOR], DISTANCE], [S2, HardwareComponentType [robBrick_gyro, SENSOR], ANGLE], [S4, HardwareComponentType [robBrick_ultrasonic, SENSOR], DISTANCE]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test12ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java/java_code_generator10.xml");

        Set<UsedSensor> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[[S3, HardwareComponentType [robBrick_colour, SENSOR], COLOUR], [S3, HardwareComponentType [robBrick_colour, SENSOR], AMBIENTLIGHT], [S4, HardwareComponentType [robBrick_colour, SENSOR], RED]]",
            hardwareCheckVisitor.toString());
    }
}
