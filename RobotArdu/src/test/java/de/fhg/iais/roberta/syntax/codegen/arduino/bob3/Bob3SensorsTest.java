package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

public class Bob3SensorsTest {
    private final HelperBob3ForXmlTest bob3Helper = new HelperBob3ForXmlTest();

    @Test
    public void listsTest() throws Exception {
        this.bob3Helper.compareExistingAndGeneratedSource("/ast/sensors/bob3_sensors_test.ino", "/ast/sensors/bob3_sensors_test.xml");
    }

    @Test
    public void waitBlockTest() throws Exception {
        this.bob3Helper.compareExistingAndGeneratedSource("/ast/sensors/bob3_wait_test.ino", "/ast/sensors/bob3_wait_test.xml");
    }

}
