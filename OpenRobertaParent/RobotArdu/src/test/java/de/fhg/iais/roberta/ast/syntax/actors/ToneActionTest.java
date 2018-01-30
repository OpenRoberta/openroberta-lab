package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class ToneActionTest {

    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void playTone() throws Exception {
        final String a = "tone(9,300, 100);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlaySound.xml", false);
    }
}
