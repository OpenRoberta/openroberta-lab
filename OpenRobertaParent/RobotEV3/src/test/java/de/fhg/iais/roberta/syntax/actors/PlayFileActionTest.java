package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class PlayFileActionTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(1);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_PlayFile.xml");
    }
}
