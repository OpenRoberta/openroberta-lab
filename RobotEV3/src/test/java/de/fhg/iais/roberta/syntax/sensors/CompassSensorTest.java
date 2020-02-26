package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class CompassSensorTest extends Ev3LejosAstTest {

    @Test
    public void getCompass() throws Exception {
        String a =
            "hal.drawText(String.valueOf(hal.getHiTecCompassAngle(SensorPort.S1)), 0, 0);"
                + "hal.drawText(String.valueOf(hal.getHiTecCompassCompass(SensorPort.S1)), 0, 0);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_getCompass.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }

    @Test
    public void calibrateCompass() throws Exception {
        String a = "hal.hiTecCompassStartCalibration(SensorPort.S1);" + "hal.waitFor(40000);" + "hal.hiTecCompassStopCalibration(SensorPort.S1);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_calibrateCompass.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}
