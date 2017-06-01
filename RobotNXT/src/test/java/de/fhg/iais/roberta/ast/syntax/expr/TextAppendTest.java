package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class TextAppendTest {
    Helper h = new Helper();

    @Ignore
    public void Test() throws Exception {
        final String a = "item+=String(S1);item+=NumToStr(0);item+=\"aaa\";";

        this.h.assertCodeIsOk(a, "/syntax/text/text_append.xml");
    }
}
