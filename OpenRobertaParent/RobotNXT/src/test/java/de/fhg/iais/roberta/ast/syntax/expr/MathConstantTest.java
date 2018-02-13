package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MathConstantTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {

        String a = "PIEGOLDEN_RATIOSQRT2SQRT1_2INFINITY";
        //"Float.POSITIVE_INFINITY";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constant.xml");
    }

    //ignore
    public void Test1() throws Exception {

        final String a = "RotateMotor(B,PI,360.0*((1.0+sqrt(5.0))/2.0)))";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constant1.xml");
    }

}
