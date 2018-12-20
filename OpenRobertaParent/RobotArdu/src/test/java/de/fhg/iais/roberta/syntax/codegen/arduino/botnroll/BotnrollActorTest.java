package de.fhg.iais.roberta.syntax.codegen.arduino.botnroll;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class BotnrollActorTest {
    HelperBotNrollForXmlTest botnrollHelper = new HelperBotNrollForXmlTest();

    Configuration configuration = HelperBotNrollForXmlTest.makeConfiguration();

    @Test
    public void botnrollLcdTest() throws Exception {
        this.botnrollHelper
            .compareExistingAndGeneratedSource("/ast/actions/botnroll_indication_test.ino", "/ast/actions/botnroll_indication_test.xml", this.configuration);
    }

    @Test
    public void botnrollMovementTest() throws Exception {
        this.botnrollHelper
            .compareExistingAndGeneratedSource("/ast/actions/botnroll_movement_test.ino", "/ast/actions/botnroll_movement_test.xml", this.configuration);
    }
}
