package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsLengthTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float)((float) 0.1), (float) ((float)0.0), (float) 0)).size()}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_length.xml");
    }
}
