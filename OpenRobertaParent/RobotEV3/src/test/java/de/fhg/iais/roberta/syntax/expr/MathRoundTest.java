package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class MathRoundTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "floatElement=BlocklyMethods.round(0);floatElement2=BlocklyMethods.floor(0);floatElement3=BlocklyMethods.ceil(0);publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
