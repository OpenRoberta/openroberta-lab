package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class MathModuloTest {
    @Test
    public void Test() throws Exception {
        String a = "1%0";

        Helper.assertCodeIsOk(a, "/syntax/math/math_modulo.xml");
    }

}
