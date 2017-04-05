package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ListsGetIndexTest {
    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.regulatedDrive(DriveDirection.FOREWARD,BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0,0,0),ListElementOperations.GET,IndexLocation.FROM_START,0));hal.regulatedDrive(DriveDirection.FOREWARD,BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0,0,0),ListElementOperations.GET_REMOVE,IndexLocation.FIRST));BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0,0,0),ListElementOperations.REMOVE,IndexLocation.LAST);}";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_get_index.xml");
    }
}
