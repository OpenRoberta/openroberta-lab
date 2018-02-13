package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class MathRandomIntTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.randInt(1,100)}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_random_int.xml");
    }
}
