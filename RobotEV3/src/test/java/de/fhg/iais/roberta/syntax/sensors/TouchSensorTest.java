package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class TouchSensorTest extends Ev3LejosAstTest {

    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(SensorPort.S1)}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_Touch.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}
