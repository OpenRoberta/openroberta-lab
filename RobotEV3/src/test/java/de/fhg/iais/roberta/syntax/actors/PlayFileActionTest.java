package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class PlayFileActionTest {
    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(1);}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_PlayFile.xml");
    }
}
