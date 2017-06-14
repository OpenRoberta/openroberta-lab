package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class ListsIsEmptyTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void Test() throws Exception {
        final String a = "NULL";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_is_empty.xml", false);
    }
}
