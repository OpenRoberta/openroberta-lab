package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class LightSensorTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Ignore
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_3,\"RED\")SensorColor(IN_4,\"AMBIENTLIGHT\")";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setLight.xml");
    }
}
