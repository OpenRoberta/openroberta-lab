package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ForEachStmtTest extends Ev3LejosAstTest {

    @Test
    public void forEachStmt() throws Exception {
        String a =
            "\nArrayList<PickColor>variablenName=newArrayList<>(Arrays.<PickColor>asList(PickColor.NONE,PickColor.RED,PickColor.BLUE));publicvoidrun()throwsException{for(PickColorvariablenName2:variablenName){hal.drawText(String.valueOf(variablenName2),0,0);}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/forEach_stmt.xml", false);
    }

}