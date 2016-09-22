package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathRoundTest {
    @Test
    public void Test() throws Exception {
        final String a = "round(0)ceil(0)floor(0)";

        Helper.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
