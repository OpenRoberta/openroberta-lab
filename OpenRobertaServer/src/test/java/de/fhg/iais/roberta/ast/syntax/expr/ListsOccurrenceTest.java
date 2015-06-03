package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class ListsOccurrenceTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.findFirst(BlocklyMethods.createListWith(5, 1, 2),2)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "BlocklyMethods.findLast(BlocklyMethods.createListWith(5, 1, 2),2)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_occurrence1.xml");
    }
}
