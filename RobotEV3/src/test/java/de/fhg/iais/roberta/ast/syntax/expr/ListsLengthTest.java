package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.ev3.Helper;

public class ListsLengthTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.length(BlocklyMethods.createListWithNumber(((float)0.1),((float)0.0),0))";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_length.xml");
    }
}
