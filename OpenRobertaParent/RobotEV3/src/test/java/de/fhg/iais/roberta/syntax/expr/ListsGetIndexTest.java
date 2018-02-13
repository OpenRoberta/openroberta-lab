package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsGetIndexTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.regulatedDrive(DriveDirection.FOREWARD,BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0,0,0),ListElementOperations.GET,IndexLocation.FROM_START,0));hal.regulatedDrive(DriveDirection.FOREWARD,BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0,0,0),ListElementOperations.GET_REMOVE,IndexLocation.FIRST));BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0,0,0),ListElementOperations.REMOVE,IndexLocation.LAST);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_get_index.xml");
    }
}
