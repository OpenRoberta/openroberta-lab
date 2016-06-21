package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.ev3.Helper;

public class ForEachStmtTest {

    @Test
    public void forEachStmt() throws Exception {
        String a =
            "\nArrayList<Pickcolor>variablenName=BlocklyMethods.createListWithColour(Pickcolor.NONE,Pickcolor.RED,Pickcolor.BLUE);publicvoidrun()throwsException{for(PickcolorvariablenName2:variablenName){hal.drawText(String.valueOf(variablenName2),0,0);}}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

}