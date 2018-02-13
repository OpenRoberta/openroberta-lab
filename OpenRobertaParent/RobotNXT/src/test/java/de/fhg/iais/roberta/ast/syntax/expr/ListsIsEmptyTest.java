package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class ListsIsEmptyTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "ArrIsEmpty({0,0,0})";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_is_empty.xml");
    }
}
