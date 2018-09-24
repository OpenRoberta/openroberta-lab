package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class MathSingleTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "sqrt(0)abs(0)-(0)log(0)log10(0)exp(0)pow(10.0,0)";
        this.h.assertCodeIsOk(a, "/syntax/math/math_single.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        final String a = "";
        this.h.assertCodeIsOk(a, "/syntax/math/math_single1.xml", false);
    }

    @Test
    public void Test2() throws Exception {
        final String a = "item=sqrt(0);";
        this.h.assertCodeIsOk(a, "/syntax/math/math_single2.xml", false);
    }
}
