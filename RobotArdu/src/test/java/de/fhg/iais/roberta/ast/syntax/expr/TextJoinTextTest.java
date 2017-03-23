package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class TextJoinTextTest {
    Helper h = new Helper();

    @Ignore
    public void Test() throws Exception {
        String a = "BlocklyMethods.textJoin(0, 0, \"a\", \"b\", true, hal.isPressed(SensorPort.S1))";

        this.h.assertCodeIsOk(a, "/syntax/text/text_join.xml", false);
    }

}
