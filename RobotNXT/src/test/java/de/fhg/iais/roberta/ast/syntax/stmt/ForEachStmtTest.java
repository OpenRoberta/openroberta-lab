package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

@Ignore
public class ForEachStmtTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void forEachStmt() throws Exception {
        String a =
            "\nArrayList<Pickcolor>variablenName=BlocklyMethods.createListWithColour(Pickcolor.NONE,Pickcolor.RED,Pickcolor.BLUE);publicvoidrun(){for(PickcolorvariablenName2:variablenName){hal.drawText(String.valueOf(variablenName2),0,0);}}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

}