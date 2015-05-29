package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.hardwarecheck.ev3.UsedSensorsCheckVisitor;
import de.fhg.iais.roberta.testutil.Helper;

public class EV3ProgramUsedHardwareCheckTest {
    @Test
    public void test0ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test1ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator1.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test2ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator2.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_colour, SENSOR]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test3ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator3.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_ultrasonic, SENSOR]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test4ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator4.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert
            .assertEquals(
                "[HardwareComponentType [robBrick_infrared, SENSOR], HardwareComponentType [robBrick_ultrasonic, SENSOR], HardwareComponentType [robBrick_gyro, SENSOR], HardwareComponentType [robBrick_touch, SENSOR]]",
                hardwareCheckVisitor.toString());
    }

    @Test
    public void test5ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test6ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt1.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[HardwareComponentType [robBrick_touch, SENSOR]]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test7ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt2.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test8ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/ast/control/wait_stmt3.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[HardwareComponentType [robBrick_infrared, SENSOR]]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test9ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/methods/method_void_1.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test10ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/methods/method_void_4.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert
            .assertEquals(
                "[HardwareComponentType [robBrick_colour, SENSOR], HardwareComponentType [robBrick_infrared, SENSOR], HardwareComponentType [robBrick_ultrasonic, SENSOR]]",
                hardwareCheckVisitor.toString());
    }

    @Test
    public void test11ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/syntax/methods/method_return_3.xml");

        Set<EV3Sensors> hardwareCheckVisitor = UsedSensorsCheckVisitor.check(phrases);
        Assert
            .assertEquals(
                "[HardwareComponentType [robBrick_colour, SENSOR], HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_infrared, SENSOR], HardwareComponentType [robBrick_gyro, SENSOR], HardwareComponentType [robBrick_ultrasonic, SENSOR]]",
                hardwareCheckVisitor.toString());
    }
}
