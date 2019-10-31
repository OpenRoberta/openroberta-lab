package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class IRSeekerSensorTest extends Ev3LejosAstTest {

    @Test
    public void getIRSeeker() throws Exception {
        String a =
            "hal.drawText(String.valueOf(hal.getHiTecIRSeekerModulated(SensorPort.S1)), 0, 0);"
                + "hal.drawText(String.valueOf(hal.getHiTecIRSeekerUnmodulated(SensorPort.S1)), 0, 0);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_getIRSeeker.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}
