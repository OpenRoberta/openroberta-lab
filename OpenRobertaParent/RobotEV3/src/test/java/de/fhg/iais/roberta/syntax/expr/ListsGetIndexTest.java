package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsGetIndexTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.regulatedDrive(DriveDirection.FOREWARD,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).get((int)(0)));hal.regulatedDrive(DriveDirection.FOREWARD,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).remove((int)0)));newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).remove((int)newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).size()-1));}";
        this.h.assertCodeIsOk(a, "/syntax/lists/lists_get_index.xml");
    }
}
