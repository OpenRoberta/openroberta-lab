package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ListsCreateWithTest {
    @Test
    public void Test() throws Exception {
        String a = "{1.0,3.1,2}";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "{\"a\",\"b\",\"c\"}";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "{true,true,false}";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with2.xml");
    }

    @Test
    public void Test3() throws Exception {
        String a = "{true,true,true}";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with3.xml");
    }

    @Test
    public void Test4() throws Exception {
        String a = "{(String)\"NONE\",(String)\"RED\",(String)\"BROWN\"}";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_create_with4.xml");
    }
}
