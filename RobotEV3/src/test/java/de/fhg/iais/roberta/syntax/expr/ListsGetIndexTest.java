package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsGetIndexTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.regulatedDrive(DriveDirection.FOREWARD,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).get((int)(0)));hal.regulatedDrive(DriveDirection.FOREWARD,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).remove((int)(0)));newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).remove((int)(newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).size()-1));}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_get_index.xml", makeStandard(), false);
    }
}
