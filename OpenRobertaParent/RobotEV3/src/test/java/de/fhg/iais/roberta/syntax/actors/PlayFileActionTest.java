package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class PlayFileActionTest {
    Helper h = new Helper();

    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(1);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_PlayFile.xml");
    }
}
