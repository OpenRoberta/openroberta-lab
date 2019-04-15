package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class MathConstrainTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "rob.clamp(bnr.ultrasonicDistance(4),1,100)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constrain.xml", false);
    }
}
