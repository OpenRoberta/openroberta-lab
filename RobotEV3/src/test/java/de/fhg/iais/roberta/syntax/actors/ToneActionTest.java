package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ToneActionTest {
    @Test
    public void playTone() throws Exception {
        String a = "\nhal.playTone(300, 100);}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_PlaySound.xml");
    }
}
