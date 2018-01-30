package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class ListsSubListTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        String a =
            "ArrayList<Float>Element=BlocklyMethods.createListWithNumber(0,0,0);publicvoidrun()throwsException{Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FROM_START,0,IndexLocation.FROM_START,0);Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FROM_END,0,IndexLocation.FROM_END,0);Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FROM_START,0,IndexLocation.LAST);Element=BlocklyMethods.listsGetSubList(BlocklyMethods.createListWithNumber(0,0,0),IndexLocation.FIRST,IndexLocation.FROM_START,0);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_sub_list.xml");
    }

}
