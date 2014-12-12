package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ListsIsEmptyTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.isEmpty(BlocklyMethods.createListWith(0, 0, 0))";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_is_empty.xml");
    }
}
