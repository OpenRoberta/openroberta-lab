package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class ToneActionTest {

    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void playTone() throws Exception {
        final String a = "tone(9,300, 100);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlaySound.xml", false);
    }
}
