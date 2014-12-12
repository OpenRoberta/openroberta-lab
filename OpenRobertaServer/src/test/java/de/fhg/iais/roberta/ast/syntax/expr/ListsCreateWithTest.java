package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ListsCreateWithTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.createListWith(1, 3, 2)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "BlocklyMethods.createListWith(\"a\", \"b\", \"c\")";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "BlocklyMethods.createListWith(true, true, false)";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with2.xml");
    }

    @Test
    public void Test3() throws Exception {
        String a = "BlocklyMethods.createListWith(true, 0, \"553\")";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with3.xml");
    }
}
