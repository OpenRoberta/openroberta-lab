package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class ListsEmptyListTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_empty_list.xml");
    }

}
