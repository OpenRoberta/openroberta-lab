package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.hardwarecomponents.ev3.HardwareComponentEV3Sensor;

public class EV3ProgramUsedHardwareCheckTest {
    @Test
    public void test0ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test1ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator1.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test2ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator2.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert
            .assertEquals(
                "[HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_ultrasonic, SENSOR], HardwareComponentType [robBrick_colour, SENSOR]]",
                hardwareCheckVisitor.toString());
    }

    @Test
    public void test3ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator3.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert.assertEquals(
            "[HardwareComponentType [robBrick_touch, SENSOR], HardwareComponentType [robBrick_ultrasonic, SENSOR]]",
            hardwareCheckVisitor.toString());
    }

    @Test
    public void test4ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/syntax/code_generator/java_code_generator4.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert
            .assertEquals(
                "[HardwareComponentType [robBrick_infrared, SENSOR], HardwareComponentType [robBrick_ultrasonic, SENSOR], HardwareComponentType [robBrick_gyro, SENSOR], HardwareComponentType [robBrick_touch, SENSOR]]",
                hardwareCheckVisitor.toString());
    }

    @Test
    public void test5ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/ast/control/wait_stmt.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test6ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/ast/control/wait_stmt1.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert.assertEquals("[HardwareComponentType [robBrick_touch, SENSOR]]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test7ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/ast/control/wait_stmt2.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert.assertEquals("[]", hardwareCheckVisitor.toString());
    }

    @Test
    public void test8ok() throws Exception {
        List<Phrase<Void>> phrases = Helper.generateASTs("/ast/control/wait_stmt3.xml");

        Set<HardwareComponentEV3Sensor> hardwareCheckVisitor = HardwareCheckVisitor.check(phrases);
        Assert
            .assertEquals(
                "[HardwareComponentType [robBrick_ultrasonic, SENSOR], HardwareComponentType [robBrick_colour, SENSOR], HardwareComponentType [robBrick_infrared, SENSOR], HardwareComponentType [robBrick_gyro, SENSOR]]",
                hardwareCheckVisitor.toString());
    }
}
