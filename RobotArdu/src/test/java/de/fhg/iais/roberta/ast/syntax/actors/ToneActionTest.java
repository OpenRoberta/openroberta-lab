package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class ToneActionTest {

    Helper h = new Helper();

    @Test
    public void playTone() throws Exception {
        final String a = "tone(9,300, 100);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlaySound.xml", false);
    }
}
