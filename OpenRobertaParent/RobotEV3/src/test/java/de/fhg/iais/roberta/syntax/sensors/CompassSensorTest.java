package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class CompassSensorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void getCompass() throws Exception {
        String a =
            "hal.drawText(String.valueOf(hal.getHiTecCompassAngle(SensorPort.S1)), 0, 0);"
                + "hal.drawText(String.valueOf(hal.getHiTecCompassCompass(SensorPort.S1)), 0, 0);}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_getCompass.xml");
    }

    @Test
    public void calibrateCompass() throws Exception {
        String a = "hal.hiTecCompassStartCalibration(SensorPort.S1);" + "hal.waitFor(40000);" + "hal.hiTecCompassStopCalibration(SensorPort.S1);}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_calibrateCompass.xml");
    }
}
