package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class UltrasonicSensorTest extends Ev3LejosAstTest {

    @Test
    public void setUltrasonic() throws Exception {
        String a = "\nhal.getUltraSonicSensorDistance(SensorPort.S4)" + "hal.getUltraSonicSensorPresence(SensorPort.S2)}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_setUltrasonic.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}
