package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class TextAppendTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Ignore
    public void Test() throws Exception {
        final String a = "item+String(SENSOR_1)item+String(0)item+String(\"aaa\")";

        this.h.assertCodeIsOk(a, "/syntax/text/text_append.xml", false);
    }
}
