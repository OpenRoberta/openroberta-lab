package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class MathSingleTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "floatElement=BlocklyMethods.sqrt(0);floatElement2=BlocklyMethods.abs(0);floatElement3=-(0);floatElement4=BlocklyMethods.log(0);floatElement5=BlocklyMethods.log10(0);floatElement6=BlocklyMethods.exp(0);floatElement7=BlocklyMethods.pow(10,0);publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_single.xml");
    }
}
