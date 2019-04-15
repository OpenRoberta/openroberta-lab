package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class ListsLengthTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void Test() throws Exception {
        final String a = "NULL";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_length.xml", false);
    }
}
