package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class PlayFileActionTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void playFile() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlayFile.xml", false);
    }
}
