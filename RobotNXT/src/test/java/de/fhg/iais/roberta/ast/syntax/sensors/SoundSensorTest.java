package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Ignore;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SoundSensorTest extends NxtAstTest {

    @Ignore
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_2)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_setLight.xml", false);
    }
}
