package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class InfraredSensorTest {
    @Test
    public void setInfrared() throws Exception {
        String a = "\nhal.setInfraredSensorMode(SensorPort.S4, InfraredSensorMode.DISTANCE);";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setInfrared.xml");
    }

    @Test
    public void getInfraredModeName() throws Exception {
        String a = "\nhal.getInfraredSensorModeName(SensorPort.S4)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_getModeInfrared.xml");
    }

    @Test
    public void getSampleInfrared() throws Exception {
        String a = "\nhal.getInfraredSensorValue(SensorPort.S4)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_getSampleInfrared.xml");
    }
}
