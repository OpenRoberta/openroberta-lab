package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightActionTest extends Ev3LejosAstTest {

    @Test
    public void ledOn() throws Exception {
        String a = "\nhal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_BrickLight.xml", false);
    }
}
