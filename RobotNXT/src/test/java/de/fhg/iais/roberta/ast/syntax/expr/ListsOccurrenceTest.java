package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsOccurrenceTest extends NxtAstTest {

    //ignore
    public void Test() throws Exception {
        final String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence.xml", false);
    }

    //ignore
    public void Test1() throws Exception {
        final String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence1.xml", false);
    }
}
