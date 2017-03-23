package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class MathConstantTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {

        String a = "PIM_EGOLDEN_RATIOM_SQRT2M_SQRT1_2INFINITY";
        //"Float.POSITIVE_INFINITY";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constant.xml", false);
    }

    @Ignore
    public void Test1() throws Exception {

        final String a = "RotateMotor(B,PI,360.0*((1.0+sqrt(5.0))/2.0)))";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constant1.xml", false);
    }

}
