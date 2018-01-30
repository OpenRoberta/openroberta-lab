package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class VolumeActionTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void setVolume() throws Exception {
        String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_SetVolume.xml", false);
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_GetVolume.xml", false);
    }
}
