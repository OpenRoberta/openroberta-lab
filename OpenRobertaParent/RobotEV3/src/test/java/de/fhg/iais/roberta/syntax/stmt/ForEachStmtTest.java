package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ForEachStmtTest {
    Helper h = new Helper();

    @Test
    public void forEachStmt() throws Exception {
        String a =
            "\nArrayList<PickColor>variablenName=BlocklyMethods.createListWithColour(PickColor.NONE,PickColor.RED,PickColor.BLUE);publicvoidrun()throwsException{for(PickColorvariablenName2:variablenName){hal.drawText(String.valueOf(variablenName2),0,0);}}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

}