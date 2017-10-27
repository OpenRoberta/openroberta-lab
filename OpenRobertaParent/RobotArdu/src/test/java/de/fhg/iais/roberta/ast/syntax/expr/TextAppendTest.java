package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class TextAppendTest {
    HelperBotNroll h = new HelperBotNroll();

    @Ignore
    public void Test() throws Exception {
        final String a = "item+String(SENSOR_1)item+String(0)item+String(\"aaa\")";

        this.h.assertCodeIsOk(a, "/syntax/text/text_append.xml", false);
    }
}
