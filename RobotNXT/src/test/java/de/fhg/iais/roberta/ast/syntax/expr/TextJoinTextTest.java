package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class TextJoinTextTest {
    Helper h = new Helper();

    //ignore
    public void Test() throws Exception {
        String a = "BlocklyMethods.textJoin(0, 0, \"a\", \"b\", true, hal.isPressed(SensorPort.S1))";

        this.h.assertCodeIsOk(a, "/syntax/text/text_join.xml");
    }

}
