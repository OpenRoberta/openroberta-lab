package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightActionStatusTest extends Ev3LejosAstTest {

    @Test
    public void ledOff() throws Exception {
        String a = "\nhal.ledOff();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_BrickLightStatus.xml", false);
    }

    @Test
    public void resetLED() throws Exception {
        String a = "\nhal.resetLED();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_BrickLightStatus1.xml", false);
    }
}
