package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MathModuloTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "floatvariablenName;taskmain(){variablenName=1%0;";

        this.h.assertCodeIsOk(a, "/syntax/math/math_modulo.xml");
    }

}
