package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class SoundSensorTest extends Ev3LejosAstTest {

    @Test
    public void getSampleSound() throws Exception {
        String a = "\nhal.getSoundLevel(SensorPort.S1)}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_getSampleSound.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}
