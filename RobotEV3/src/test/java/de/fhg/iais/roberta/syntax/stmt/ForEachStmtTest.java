package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ForEachStmtTest {

    @Test
    public void forEachStmt() throws Exception {
        String a =
            "\nArrayList<PickColor>variablenName=BlocklyMethods.createListWithColour(PickColor.NONE,PickColor.RED,PickColor.BLUE);publicvoidrun()throwsException{for(PickColorvariablenName2:variablenName){hal.drawText(String.valueOf(variablenName2),0,0);}}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

}