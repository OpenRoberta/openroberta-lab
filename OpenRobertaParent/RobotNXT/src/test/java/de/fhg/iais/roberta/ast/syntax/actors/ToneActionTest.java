package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class ToneActionTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void playTone() throws Exception {
        final String a = "PlayToneEx(300, 100, volume, false);Wait(100);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlaySound.xml");
    }
}
