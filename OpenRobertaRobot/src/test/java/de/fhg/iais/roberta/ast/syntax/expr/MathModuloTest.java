package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

@Ignore
public class MathModuloTest {
    @Test
    public void Test() throws Exception {
        String a = "floatvariablenName=1%0;publicvoidrun(){}";

        Helper.assertCodeIsOk(a, "/syntax/math/math_modulo.xml");
    }

}
