package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3SensorsTest extends Bob3AstTest {

    @Test
    @Ignore
    public void listsTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/ast/sensors/bob3_sensors_test.ino", "/ast/sensors/bob3_sensors_test.xml");
    }

    @Test
    public void waitBlockTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/ast/sensors/bob3_wait_test.ino", "/ast/sensors/bob3_wait_test.xml");
    }

}
