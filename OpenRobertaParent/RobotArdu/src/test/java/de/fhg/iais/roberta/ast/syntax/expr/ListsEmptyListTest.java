package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class ListsEmptyListTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void Test() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_empty_list.xml", false);
    }

}
