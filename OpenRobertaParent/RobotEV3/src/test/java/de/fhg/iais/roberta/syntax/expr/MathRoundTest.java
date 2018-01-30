package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class MathRoundTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        String a =
            "floatElement=BlocklyMethods.round(0);floatElement2=BlocklyMethods.floor(0);floatElement3=BlocklyMethods.ceil(0);publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
