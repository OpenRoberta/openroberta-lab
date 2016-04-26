package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ListsSetIndexTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(55,66,11), ListElementOperations.SET, 99, IndexLocation.FROM_START, 1);";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_set_index.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(55,66,11), ListElementOperations.INSERT, 99, IndexLocation.FROM_START, 1);";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_set_index1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(55,66,11), ListElementOperations.INSERT, 99, IndexLocation.RANDOM);";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_set_index2.xml");
    }
}
