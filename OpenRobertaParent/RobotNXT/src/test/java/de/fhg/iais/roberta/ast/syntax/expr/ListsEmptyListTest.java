package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class ListsEmptyListTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_empty_list.xml");
    }

}
