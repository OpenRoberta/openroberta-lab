package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class ToneActionTest {
    Helper h = new Helper();

    @Test
    public void playTone() throws Exception {
        final String a = "PlayToneEx(300, 100, volume, false);Wait(100);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlaySound.xml");
    }
}
