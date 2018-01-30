package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class TextAppendTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Ignore
    public void Test() throws Exception {
        final String a = "item+=String(S1);item+=NumToStr(0);item+=\"aaa\";";

        this.h.assertCodeIsOk(a, "/syntax/text/text_append.xml");
    }
}
