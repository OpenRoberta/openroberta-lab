package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsSetIndexTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 55, (float) 66, (float) 11)).set((int) (1), (float) 99);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_set_index.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 55, (float) 66, (float) 11)).add( (int) (1), (float) 99);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_set_index1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 55, (float) 66, (float) 11)).add((float) 99);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_set_index2.xml");
    }
}
