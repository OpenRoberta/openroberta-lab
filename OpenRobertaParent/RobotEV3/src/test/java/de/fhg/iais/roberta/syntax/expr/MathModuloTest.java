package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class MathModuloTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        String a = "floatvariablenName=1%0;publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_modulo.xml");
    }

}
