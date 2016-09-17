package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ToneActionTest {
    @Test
    public void playTone() throws Exception {
        final String a = "PlayToneEx(300, 100, volume, false);Wait(100);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_PlaySound.xml");
    }
}
