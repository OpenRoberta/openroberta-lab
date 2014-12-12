package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ListsSubListTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.listsGetSubList(BlocklyMethods.createListWith(11,22,33,44), IndexLocation.FROM_START, 1, IndexLocation.FROM_END, 1)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_sub_list.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "BlocklyMethods.listsGetSubList(BlocklyMethods.createListWith(11,22,33,44), IndexLocation.FIRST, IndexLocation.FROM_END, 1)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_sub_list1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "BlocklyMethods.listsGetSubList(BlocklyMethods.createListWith(11,22,33,44), IndexLocation.FROM_START, 1, IndexLocation.LAST)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_sub_list2.xml");
    }

    @Test
    public void Test3() throws Exception {
        String a = "BlocklyMethods.listsGetSubList(BlocklyMethods.createListWith(11,22,33,44), IndexLocation.FIRST, IndexLocation.LAST)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_sub_list3.xml");
    }
}
