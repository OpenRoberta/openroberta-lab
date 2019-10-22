package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SayTextActionTest extends Ev3LejosAstTest {

    @Test
    public void sayText() throws Exception {
        String a = "\nhal.sayText(\"Hello world\");}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_SayText.xml", makeStandard(), false);
    }
}
