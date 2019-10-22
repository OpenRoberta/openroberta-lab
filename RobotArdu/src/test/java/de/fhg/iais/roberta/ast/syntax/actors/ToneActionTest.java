package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ToneActionTest extends BotnrollAstTest {

    @Test
    public void playTone() throws Exception {
        final String a = "tone(9,300, 100);";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_PlaySound.xml", false);
    }
}
