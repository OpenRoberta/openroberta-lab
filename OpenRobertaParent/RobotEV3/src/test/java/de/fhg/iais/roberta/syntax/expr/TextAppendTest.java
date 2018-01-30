package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class TextAppendTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        String a = "item+=String.valueOf(hal.isPressed(SensorPort.S1));item+=String.valueOf(0);item+=String.valueOf(\"aaa\");}";

        this.h.assertCodeIsOk(a, "/syntax/text/text_append.xml");
    }
}
