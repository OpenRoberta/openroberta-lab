package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class ListsGetIndexTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.listsIndex(BlocklyMethods.createListWith(55,66,11), ListElementOperations.GET, IndexLocation.FROM_START, 1)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_get_index.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "BlocklyMethods.listsIndex(BlocklyMethods.createListWith(55,66,11), ListElementOperations.REMOVE, IndexLocation.FROM_START, 1);";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_get_index1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "BlocklyMethods.listsIndex(BlocklyMethods.createListWith(55,66,11), ListElementOperations.GET_REMOVE, IndexLocation.LAST)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_get_index2.xml");
    }

}
