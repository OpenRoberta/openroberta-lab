package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MathRoundTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void Test() throws Exception {
        final String a = "round(0)ceil(0)floor(0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_round.xml", false);
    }
}
