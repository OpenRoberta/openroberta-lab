package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsIsEmptyTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).isEmpty()}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_is_empty.xml");
    }
}
