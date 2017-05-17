package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class TextJoinTextTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.textJoin(0, 0, \"a\", \"b\", true, hal.isPressed(SensorPort.S1))}";

        this.h.assertCodeIsOk(a, "/syntax/text/text_join.xml");
    }

}
