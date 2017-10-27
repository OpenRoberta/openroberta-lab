package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ListsCreateWithItemTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.createListWithItem(1, 5)}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with_item.xml");
    }

}
