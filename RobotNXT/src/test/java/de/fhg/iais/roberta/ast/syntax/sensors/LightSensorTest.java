package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Ignore;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightSensorTest extends NxtAstTest {

    @Ignore
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_3,\"RED\")SensorColor(IN_4,\"AMBIENTLIGHT\")";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_setLight.xml", false);
    }
}
