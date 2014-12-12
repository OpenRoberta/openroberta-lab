package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ListsLenghtTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.lenght(BlocklyMethods.createListWith(0, 0, 0))";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_lenght.xml");
    }
}
