package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class TextAppendTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "item+=String.valueOf(hal.isPressed(SensorPort.S1));item+=String.valueOf(0);item+=String.valueOf(\"aaa\");}";

        this.h.assertCodeIsOk(a, "/syntax/text/text_append.xml");
    }
}
