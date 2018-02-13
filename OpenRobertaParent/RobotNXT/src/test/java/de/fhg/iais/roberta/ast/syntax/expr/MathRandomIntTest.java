package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MathRandomIntTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "RandomIntegerInRange(1,100)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_random_int.xml");
    }
}
