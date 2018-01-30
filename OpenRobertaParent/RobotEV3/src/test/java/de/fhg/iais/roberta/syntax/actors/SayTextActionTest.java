package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class SayTextActionTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void sayText() throws Exception {
        String a = "\nhal.sayText(\"Hello world\");}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_SayText.xml");
    }
}
