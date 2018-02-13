package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class ListsLengthTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "ArrayLen({0.1,0.0,0})";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_length.xml");
    }
}
