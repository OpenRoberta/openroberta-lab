package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ListsEmptyListTest {
    @Test
    public void Test() throws Exception {
        String a = "";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_empty_list.xml");
    }

}
