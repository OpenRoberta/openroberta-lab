package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MathNumberPropertyTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "(0%2==0)(0%2!=0)MathPrime(0)MathIsWhole(0)(0>0)(0<0)(0%0==0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_number_property.xml");
    }

    @Test
    public void Test1() throws Exception {
        final String a = "item=(0%2==0);";

        this.h.assertCodeIsOk(a, "/syntax/math/math_number_property1.xml");
    }

}
