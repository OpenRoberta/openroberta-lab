package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class ListsEmptyListTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_empty_list.xml", false);
    }

}
