package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ToneActionTest extends Ev3LejosAstTest {

    @Test
    public void playTone() throws Exception {
        String a = "\nhal.playTone(300, 100);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_PlaySound.xml", false);
    }
}
