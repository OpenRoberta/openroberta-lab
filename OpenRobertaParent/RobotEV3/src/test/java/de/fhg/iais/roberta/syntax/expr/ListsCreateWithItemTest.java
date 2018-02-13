package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsCreateWithItemTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.createListWithItem((float) 1, 5)}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with_item.xml");
    }

}
