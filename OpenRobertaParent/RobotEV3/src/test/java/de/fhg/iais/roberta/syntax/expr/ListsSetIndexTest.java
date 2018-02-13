package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsSetIndexTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(55,66,11), ListElementOperations.SET, (float) 99, IndexLocation.FROM_START, 1);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_set_index.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a =
            "BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(55,66,11), ListElementOperations.INSERT, (float) 99, IndexLocation.FROM_START, 1);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_set_index1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(55,66,11), ListElementOperations.INSERT, (float) 99, IndexLocation.RANDOM);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_set_index2.xml");
    }
}
