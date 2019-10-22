package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ClearDisplayActionTest extends Ev3LejosAstTest {

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun()throwsException{hal.clearDisplay();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_ClearDisplay.xml", false);
    }
}
