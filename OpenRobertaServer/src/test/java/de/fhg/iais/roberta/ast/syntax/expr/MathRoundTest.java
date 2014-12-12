package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class MathRoundTest {
    @Test
    public void Test() throws Exception {
        String a = "Math.round(0)Math.ceil(0)Math.floor(0)";

        Helper.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
