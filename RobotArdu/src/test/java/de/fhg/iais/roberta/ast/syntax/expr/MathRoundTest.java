package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class MathRoundTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        final String a = "round(0)ceil(0)floor(0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_round.xml", false);
    }
}
