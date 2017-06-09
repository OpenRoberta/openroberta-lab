package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ListsSubListTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a =
            "ArrayList<Float>Element=BlocklyMethods.createListWithNumber(0,0,0);publicvoidrun()throwsException{Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FROM_START,0,IndexLocation.FROM_START,0);Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FROM_END,0,IndexLocation.FROM_END,0);Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FROM_START,0,IndexLocation.LAST);Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FIRST,IndexLocation.FROM_START,0);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_sub_list.xml");
    }

}
