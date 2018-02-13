package de.fhg.iais.roberta.ast.syntax.actors.bob3;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

@Ignore
public class Bob3BodyLEDActionTest {

    private final HelperBob3ForXmlTest h = new HelperBob3ForXmlTest();

    @Test
    public void turnOnLeftLED() throws Exception {
        //TODO: Figure out hot to cross-test blocks
        final String a = "how do I use blocks from makeblock? JUint says that blockly block is not found";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Bob3LED.xml", false);
    }
}
