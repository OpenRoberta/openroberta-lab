package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ShowTextActionTest extends Ev3LejosAstTest {

    @Test
    public void clearDisplay() throws Exception {
        String a = "\nhal.drawText(\"Hallo\", 0, 0);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_ShowText.xml", false);

    }
}
