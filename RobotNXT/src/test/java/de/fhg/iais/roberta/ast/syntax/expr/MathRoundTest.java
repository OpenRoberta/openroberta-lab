package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MathRoundTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "MathRound(0)MathRoundUp(0)MathFloor(0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
