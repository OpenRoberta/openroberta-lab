package de.fhg.iais.roberta.ast.syntax.actors.bob3;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForTest;

@Ignore
public class Bob3BodyLEDActionTest {

    HelperBob3ForTest h = new HelperBob3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void turnOnLeftLED() throws Exception {
        //TODO: Figure out hot to cross-test blocks
        final String a = "how do I use blocks from makeblock? JUint says that blockly block is not found";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Bob3LED.xml", false);
    }
}
