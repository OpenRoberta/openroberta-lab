package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class GyroSensorTest extends Ev3LejosAstTest {

    @Test
    public void setGyro() throws Exception {
        String a = "\nhal.getGyroSensorAngle(SensorPort.S2)" + "hal.getGyroSensorRate(SensorPort.S4)}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_setGyro.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }

    @Test
    public void resetGyroSensor() throws Exception {
        String a = "\nhal.resetGyroSensor(SensorPort.S2);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_resetGyro.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}
