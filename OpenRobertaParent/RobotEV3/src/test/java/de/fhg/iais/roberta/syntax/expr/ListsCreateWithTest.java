package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsCreateWithTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.createListWithNumber(((float)1.0), ((float)3.1), 2)}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "BlocklyMethods.createListWithString(\"a\", \"b\", \"c\")}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "BlocklyMethods.createListWithBoolean(true, true, false)}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with2.xml");
    }

    @Test
    public void Test3() throws Exception {
        String a = "BlocklyMethods.createListWithBoolean(true, true, true)}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with3.xml");
    }

    @Test
    public void Test4() throws Exception {
        String a = "BlocklyMethods.createListWithColour(PickColor.NONE,PickColor.RED,PickColor.BROWN)}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with4.xml");
    }
}
