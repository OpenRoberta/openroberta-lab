package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsLengthTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.length(BlocklyMethods.createListWithNumber(((float)0.1),((float)0.0),0))}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_length.xml");
    }
}
