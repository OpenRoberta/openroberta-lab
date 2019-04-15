package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class PlayFileActionTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(1);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_PlayFile.xml");
    }
}
