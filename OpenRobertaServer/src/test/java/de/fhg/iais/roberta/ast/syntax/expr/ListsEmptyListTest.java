package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ListsEmptyListTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.createEmptyList()";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_empty_list.xml");
    }

}
