package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ToneActionTest extends NxtAstTest {

    @Test
    public void playTone() throws Exception {
        final String a = "PlayToneEx(300, 100, volume, false);Wait(100);";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_PlaySound.xml", false);
    }
}
