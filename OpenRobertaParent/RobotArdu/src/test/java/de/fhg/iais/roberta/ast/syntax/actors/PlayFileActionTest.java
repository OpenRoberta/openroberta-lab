package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class PlayFileActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void playFile() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlayFile.xml", false);
    }
}
