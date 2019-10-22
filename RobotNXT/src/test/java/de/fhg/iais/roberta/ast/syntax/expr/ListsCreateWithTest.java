package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsCreateWithTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        String a = "{1.0,3.1,2}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        String a = "{\"a\",\"b\",\"c\"}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with1.xml", false);
    }

    @Test
    public void Test2() throws Exception {
        String a = "{true,true,false}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with2.xml", false);
    }

    @Test
    public void Test3() throws Exception {
        String a = "{true,true,true}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with3.xml", false);
    }

    @Test
    public void Test4() throws Exception {
        String a = "{NULL,INPUT_REDCOLOR}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with4.xml", false);
    }
}
