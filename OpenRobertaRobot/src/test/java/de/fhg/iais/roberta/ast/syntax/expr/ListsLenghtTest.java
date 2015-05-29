package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ListsLenghtTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.lenght(BlocklyMethods.createListWith(((float)0.1),((float)0.0),0))";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_lenght.xml");
    }
}
