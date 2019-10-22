package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsCreateWithTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float)((float)1.0),(float)((float)3.1),(float)2))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        String a = "newArrayList<>(Arrays.<String>asList(\"a\", \"b\", \"c\"))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with1.xml", false);
    }

    @Test
    public void Test2() throws Exception {
        String a = "newArrayList<>(Arrays.<Boolean>asList(true, true, false))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with2.xml", false);
    }

    @Test
    public void Test3() throws Exception {
        String a = "newArrayList<>(Arrays.<Boolean>asList(true, true, true))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with3.xml", false);
    }

    @Test
    public void Test4() throws Exception {
        String a = "newArrayList<>(Arrays.<PickColor>asList(PickColor.NONE,PickColor.RED,PickColor.BROWN))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with4.xml", false);
    }
}
