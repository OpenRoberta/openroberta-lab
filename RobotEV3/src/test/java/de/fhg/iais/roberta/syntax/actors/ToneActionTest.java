package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ToneActionTest {
    Helper h = new Helper();

    @Test
    public void playTone() throws Exception {
        String a = "\nhal.playTone(300, 100);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_PlaySound.xml");
    }
}
