package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class UltrasonicSensorTest extends BotnrollAstTest {

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nbnr.ultrasonicDistance(4)bnr.ultrasonicDistance(2)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_setUltrasonic.xml", false);
    }
}
