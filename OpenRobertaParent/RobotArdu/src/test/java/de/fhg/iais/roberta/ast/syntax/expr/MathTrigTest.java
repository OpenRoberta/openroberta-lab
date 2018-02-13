package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class MathTrigTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "sin(PI/180.0*0)cos(PI/180.0*0)tan(PI/180.0*0)180.0/PI*asin(0)180.0/PI*acos(0)180.0/PI*atan(0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_trig.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        final String a = "if(0==sin(PI/180.0*0)){one.movePID(180.0/PI*acos(0),180.0/PI*acos(0));}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_trig1.xml", false);
    }

}
