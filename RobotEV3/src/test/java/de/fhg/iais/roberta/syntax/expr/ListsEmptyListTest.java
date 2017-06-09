package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ListsEmptyListTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<String>()" + "newArrayList<Pickcolor>()" + "newArrayList<Boolean>()" + "newArrayList<Float>()}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_empty_list.xml");
    }

}
